package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.race.BattleParticipantRace;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.race.BattleParticipantRaces;

public interface BattleCharacter {
    BattleParticipantRace getRace();

    void setRace(BattleParticipantRace race);

    CharacterLevelInfo getLevelInfo();

    void setLevelInfo(CharacterLevelInfo info);

    class Impl implements BattleCharacter {
        public static final Codec<Impl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BattleParticipantRaces.REGISTRY.getCodec().fieldOf("race").forGetter(Impl::getRace),
                CharacterLevelInfo.CODEC.fieldOf("levelInfo").forGetter(Impl::getLevelInfo)
        ).apply(instance, Impl::new));
        private BattleParticipantRace race;
        private CharacterLevelInfo levelInfo;

        public Impl(final BattleParticipantRace race, final CharacterLevelInfo info) {
            this.race = race;
            levelInfo = info;
        }

        @Override
        public BattleParticipantRace getRace() {
            return race;
        }

        @Override
        public void setRace(final BattleParticipantRace race) {
            this.race = race;
        }

        @Override
        public CharacterLevelInfo getLevelInfo() {
            return levelInfo;
        }

        @Override
        public void setLevelInfo(final CharacterLevelInfo info) {
            levelInfo = info;
        }
    }

    class SyncImpl extends Impl {
        public static final Codec<SyncImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BattleParticipantRaces.REGISTRY.getCodec().fieldOf("race").forGetter(Impl::getRace),
                CharacterLevelInfo.CODEC.fieldOf("levelInfo").forGetter(Impl::getLevelInfo)
        ).apply(instance, SyncImpl::new));
        private boolean dirty;

        public SyncImpl(final BattleParticipantRace race, final CharacterLevelInfo info) {
            super(race, info);
            dirty = true;
        }

        @Override
        public void setLevelInfo(final CharacterLevelInfo info) {
            super.setLevelInfo(info);
            dirty = true;
        }

        @Override
        public void setRace(final BattleParticipantRace race) {
            super.setRace(race);
            dirty = true;
        }

        public void setDirty(final boolean dirty) {
            this.dirty = dirty;
        }

        public boolean isDirty() {
            return dirty;
        }
    }
}
