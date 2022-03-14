package io.github.stuff_stuffs.tbcexcore.common.impl.battle.world;

import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.BattleImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.BattleTimelineImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.action.ParticipantJoinBattleAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state.BattleParticipantStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.CacheEvictionMap;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.netty.buffer.ByteBufOutputStream;
import it.unimi.dsi.fastutil.longs.*;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class ServerBattleWorldImpl implements BattleWorld {
    private static final Logger LOGGER = LogManager.getLogger();
    //TODO config option
    //5 minutes
    private static final long TIME_OUT = 20 * 60 * 5;
    private static final String VERSION = "0.0";
    private final Long2ReferenceMap<BattleImpl> battles;
    private final Path directory;
    private final RegistryKey<World> worldKey;
    private final Path metaFile;
    private final CacheEvictionMap<BattleImpl> evictionMap;
    private long nextId;

    public ServerBattleWorldImpl(final Path directory, final RegistryKey<World> worldKey) {
        battles = new Long2ReferenceOpenHashMap<>();
        this.directory = directory;
        this.worldKey = worldKey;
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (final IOException e) {
                throw new TBCExException("Cannot create tbcex_battle_world directory");
            }
        }
        if (!Files.isDirectory(directory)) {
            throw new TBCExException("Cannot create tbcex_battle_world directory or already exists as file");
        }
        metaFile = directory.resolve("central.tbcex_battle_meta");
        if (Files.isRegularFile(metaFile)) {
            try (final InputStream metaStream = Files.newInputStream(metaFile, StandardOpenOption.READ)) {
                final ByteBuffer buffer = ByteBuffer.allocate(256);
                buffer.put(metaStream.readAllBytes());
                buffer.position(0);
                nextId = buffer.getLong();
            } catch (final IOException e) {
                throw new TBCExException("Error while trying to read battle meta file", e);
            }
        } else {
            nextId = 0;
        }
        evictionMap = new CacheEvictionMap<>(TIME_OUT);
    }

    public void writeBattle(final BattleHandle handle, final PacketByteBuf buf, final int size) {
        if (!handle.getWorld().equals(worldKey)) {
            throw new TBCExException("Tried to get battle from wrong world!");
        }
        final BattleImpl battle = getBattle(handle);
        try (final ByteBufOutputStream stream = new ByteBufOutputStream(buf)) {
            final NbtCompound wrapper = new NbtCompound();
            wrapper.put("handle", BattleHandle.CODEC.encodeStart(NbtOps.INSTANCE, handle).result().orElseThrow(() -> new TBCExException("Error while serializing BattleHandle!")));
            if (size == 0) {
                final Optional<NbtElement> result = BattleImpl.CODEC.encodeStart(NbtOps.INSTANCE, battle).result();
                if (result.isPresent()) {
                    final NbtElement nbt = result.get();
                    wrapper.put("data", nbt);
                } else {
                    throw new TBCExException("Error while serializing battle!");
                }
            } else {
                final NbtList list = new NbtList();
                final BattleTimelineImpl timeline = (BattleTimelineImpl) battle.getTimeline();
                for (int i = size; i < timeline.getSize(); i++) {
                    final BattleAction action = timeline.getAction(i);
                    final NbtCompound actionWrapper = new NbtCompound();
                    final Optional<NbtElement> result = BattleAction.CODEC.encodeStart(NbtOps.INSTANCE, action).result();
                    if (result.isPresent()) {
                        actionWrapper.put("data", result.get());
                    } else {
                        throw new TBCExException("Error while serializing battle action of type: " + action.getType());
                    }
                    list.add(actionWrapper);
                }
                wrapper.put("data", list);
            }
            NbtIo.writeCompressed(wrapper, stream);
            stream.flush();
        } catch (final IOException e) {
            throw new TBCExException("Error while writing battle to buffer!", e);
        }
    }

    @Override
    public BattleImpl getBattle(final BattleHandle handle) {
        if (!handle.getWorld().equals(worldKey)) {
            LOGGER.error("Tried to get battle from wrong world!");
            return null;
        }
        final BattleImpl battle = battles.get(handle.getId());
        if (battle == null) {
            return tryLoad(handle);
        }
        evictionMap.access(handle.getId());
        return battle;
    }

    public void tryJoin(final BattleParticipant participant, final BattleHandle handle) {
        final BattleImpl battle = getBattle(handle);
        if (battle != null) {
            ((BattleTimelineImpl) battle.getTimeline()).push(new ParticipantJoinBattleAction(new BattleParticipantHandle(handle, ((Entity) participant).getUuid()), new BattleParticipantStateImpl(participant, participant.tbcex$getRestoreData(handle))), new Tracer<>(i -> false, debugPredicate));
            if (participant instanceof ServerPlayerEntity entity) {
                TBCExCore.setupPlayerToJoinBattle(entity, handle);
            }
        }
    }

    public BattleHandle create() {
        final BattleHandle handle = new BattleHandle(worldKey, nextId++);
        final BattleImpl battle = new BattleImpl();
        battle.init(handle);
        battles.put(handle.getId(), battle);
        evictionMap.put(handle.getId(), battle);
        return handle;
    }

    public void tick() {
        evictionMap.tick();
        final LongList toRemove = new LongArrayList();
        final LongIterator iterator = battles.keySet().longIterator();
        while (iterator.hasNext()) {
            final long key = iterator.nextLong();
            if (evictionMap.shouldEvict(key)) {
                toRemove.add(key);
            }
        }
        final LongIterator removeIterator = toRemove.iterator();
        while (removeIterator.hasNext()) {
            final long key = removeIterator.nextLong();
            save(key);
            battles.remove(key);
            evictionMap.evict(key);
        }
    }

    public void save() {
        final LongIterator iterator = battles.keySet().iterator();
        while (iterator.hasNext()) {
            save(iterator.nextLong());
        }
        try (final OutputStream metaStream = Files.newOutputStream(metaFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            final ByteBuffer buffer = ByteBuffer.allocate(256);
            buffer.putLong(nextId);
            metaStream.write(buffer.array());
        } catch (final IOException e) {
            throw new TBCExException("Error during saving battle", e);
        }
    }

    private static String handleToFile(final BattleHandle handle) {
        return handleToFile(handle.getId());
    }

    private static String handleToFile(final long handleId) {
        return "Battle" + Long.toString(handleId, 16) + ".tbcex_battle";
    }

    private static String handleToFileMeta(final BattleHandle handle) {
        return handleToFileMeta(handle.getId());
    }

    private static String handleToFileMeta(final long handleId) {
        return "Battle" + Long.toString(handleId, 16) + ".tbcex_battle_meta";
    }

    private @Nullable BattleImpl tryLoad(final BattleHandle handle) {
        final Path battlePath = directory.resolve(handleToFile(handle));
        final Path metaPath = directory.resolve(handleToFileMeta(handle));
        if (Files.exists(battlePath) && !Files.isDirectory(battlePath) && Files.exists(metaPath) && !Files.isDirectory(metaPath)) {
            try (final InputStream battleStream = Files.newInputStream(battlePath, StandardOpenOption.READ); final InputStream metaStream = Files.newInputStream(metaPath, StandardOpenOption.READ)) {
                final int versionHeaderLength = metaStream.read();
                final String version = new String(metaStream.readNBytes(versionHeaderLength));
                if (!version.equals(VERSION)) {
                    LOGGER.error("Error loading battle: " + handleToFile(handle) + ", version mismatch");
                    return null;
                }
                try (final DataInputStream s = new DataInputStream(battleStream)) {
                    final Optional<BattleImpl> result = BattleImpl.CODEC.parse(NbtOps.INSTANCE, NbtIo.readCompressed(s).get("data")).result();
                    if (result.isPresent()) {
                        final BattleImpl battle = result.get();
                        battle.init(handle);
                        battles.put(handle.getId(), battle);
                        evictionMap.put(handle.getId(), battle);
                        return battle;
                    } else {
                        LOGGER.error("Error loading battle: " + handleToFile(handle) + ", decoding error");
                        return null;
                    }
                } catch (final IOException e) {
                    LOGGER.error("Error loading battle: " + handleToFile(handle) + ", IOException {}", e);
                }
            } catch (final IOException e) {
                LOGGER.error("Error loading battle: " + handleToFile(handle) + ", IOException {}", e);
            }
        }
        LOGGER.error("Error loading battle: " + handleToFile(handle) + ", non-existent battle");
        return null;
    }

    private void save(final long handle) {
        final BattleImpl battle = battles.get(handle);
        if (battle != null) {
            try (
                    final OutputStream battleStream = Files.newOutputStream(directory.resolve(handleToFile(handle)), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                    final OutputStream metaStream = Files.newOutputStream(directory.resolve(handleToFileMeta(handle)), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
            ) {
                metaStream.write(VERSION.length());
                metaStream.write(VERSION.getBytes(StandardCharsets.UTF_8));
                BattleImpl.CODEC.encodeStart(NbtOps.INSTANCE, battle).result().ifPresentOrElse(element -> {
                    try {
                        final NbtCompound nbtCompound = new NbtCompound();
                        nbtCompound.put("data", element);
                        NbtIo.writeCompressed(nbtCompound, battleStream);
                    } catch (final IOException e) {
                        throw new TBCExException("Cannot write battle file");
                    }
                }, () -> LOGGER.error("Error saving battle: " + handleToFile(handle)));
                battleStream.flush();
            } catch (final IOException e) {
                LOGGER.error("Error saving battle: " + handleToFile(handle));
            }
        } else {
            LOGGER.error("Error saving battle: " + handleToFile(handle));
        }
    }
}
