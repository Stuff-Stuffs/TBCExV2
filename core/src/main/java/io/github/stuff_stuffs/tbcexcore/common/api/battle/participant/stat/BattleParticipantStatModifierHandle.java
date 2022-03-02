package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

public final class BattleParticipantStatModifierHandle {
    private final BattleParticipantStatContainer.Container parent;
    private final BattleParticipantStatModifier modifier;
    private boolean alive;

    BattleParticipantStatModifierHandle(final BattleParticipantStatContainer.Container parent, final BattleParticipantStatModifier modifier) {
        this.parent = parent;
        this.modifier = modifier;
    }

    public boolean isAlive() {
        return alive;
    }

    public void destroy() {
        parent.destroy(this);
        alive = false;
    }
}
