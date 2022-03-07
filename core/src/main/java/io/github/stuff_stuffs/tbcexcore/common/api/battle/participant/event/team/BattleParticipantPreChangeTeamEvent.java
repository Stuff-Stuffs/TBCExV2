package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.team;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantTeam;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattleParticipantPreChangeTeamEvent {
    void beforeTeamChange(BattleParticipantStateView state, BattleParticipantTeam currentTeam, BattleParticipantTeam newTeam, Tracer<ActionTrace> tracer);

    static Mut convert(final BattleParticipantPreChangeTeamEvent event) {
        return (state, currentTeam, newTeam, tracer) -> {
            event.beforeTeamChange(state, currentTeam, newTeam, tracer);
            return true;
        };
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, currentTeam, newTeam, tracer) -> {
            boolean b = true;
            enter.run();
            for (final Mut listener : listeners) {
                b &= listener.beforeTeamChange(state, currentTeam, newTeam, tracer);
            }
            exit.run();
            return b;
        };
    }

    interface Mut {
        boolean beforeTeamChange(BattleParticipantStateView state, BattleParticipantTeam currentTeam, BattleParticipantTeam newTeam, Tracer<ActionTrace> tracer);
    }
}
