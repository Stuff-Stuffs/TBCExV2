package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public final class BattleParticipantStatModifierHandle {
    private final BattleParticipantStatContainer.Container parent;
    private boolean alive;

    BattleParticipantStatModifierHandle(final BattleParticipantStatContainer.Container parent) {
        this.parent = parent;
    }

    public boolean isAlive() {
        return alive;
    }

    public void destroy(final Tracer<ActionTrace> tracer) {
        parent.destroy(this, tracer);
        alive = false;
    }
}
