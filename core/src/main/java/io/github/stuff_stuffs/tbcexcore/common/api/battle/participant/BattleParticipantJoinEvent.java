package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;

import java.util.function.Consumer;

public interface BattleParticipantJoinEvent {
    Event<BattleParticipantJoinEvent> EVENT = EventFactory.createArrayBacked(BattleParticipantJoinEvent.class, events -> new BattleParticipantJoinEvent() {
        @Override
        public <E extends Entity & BattleParticipant> void onJoinBattle(E entity, Consumer<BattleParticipantEffect> effectConsumer) {
            for (BattleParticipantJoinEvent event : events) {
                event.onJoinBattle(entity, effectConsumer);
            }
        }
    });

    <E extends Entity & BattleParticipant> void onJoinBattle(E entity, Consumer<BattleParticipantEffect> effectConsumer);
}
