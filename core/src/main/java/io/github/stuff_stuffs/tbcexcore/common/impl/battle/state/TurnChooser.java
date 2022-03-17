package io.github.stuff_stuffs.tbcexcore.common.impl.battle.state;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

//TODO configurable turn choosing algorithms
public final class TurnChooser {
    public static final Codec<TurnChooser> CODEC = CodecUtil.createOptional(BattleParticipantHandle.CODEC).xmap(TurnChooser::new, chooser -> chooser.last);
    private Optional<BattleParticipantHandle> last;

    public TurnChooser() {
        last = Optional.empty();
    }

    private TurnChooser(final Optional<BattleParticipantHandle> last) {
        this.last = last;
    }

    public @Nullable BattleParticipantHandle getCurrent(final ObjectSortedSet<BattleParticipantHandle> handles, final Function<BattleParticipantHandle, BattleParticipantStateView> stateGetter) {
        return last.orElse(null);
    }

    public BattleParticipantHandle advance(final ObjectSortedSet<BattleParticipantHandle> handles, final Function<BattleParticipantHandle, BattleParticipantStateView> stateGetter) {
        if (handles.size() < 1) {
            throw new TBCExException("Can not choose turn from zero participants!");
        }
        if (last.isEmpty()) {
            return chooseFirst(handles);
        } else {
            final ObjectSortedSet<BattleParticipantHandle> set = handles.tailSet(last.get());
            if (set.isEmpty()) {
                return chooseFirst(handles);
            } else {
                return chooseFirst(set);
            }
        }
    }

    private BattleParticipantHandle chooseFirst(final ObjectSortedSet<BattleParticipantHandle> handles) {
        final BattleParticipantHandle first = handles.first();
        last = Optional.of(first);
        return first;
    }
}
