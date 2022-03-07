package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.team;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantTeam;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattleParticipantPostChangeTeamEvent {
    void afterTeamChange(BattleParticipantStateView state, BattleParticipantTeam oldTeam, BattleParticipantTeam currentTeam, Tracer<ActionTrace> tracer);

    static Mut convert(final BattleParticipantPostChangeTeamEvent event) {
        return event::afterTeamChange;
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, oldTeam, currentTeam, tracer) -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.afterTeamChange(state, oldTeam, currentTeam, tracer);
            }
            exit.run();
        };
    }

    interface Mut {
        void afterTeamChange(BattleParticipantStateView state, BattleParticipantTeam oldTeam, BattleParticipantTeam currentTeam, Tracer<ActionTrace> tracer);
    }
}
