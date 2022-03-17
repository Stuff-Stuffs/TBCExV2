package io.github.stuff_stuffs.tbcexcore.common.impl.battle.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.BattleBounds;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public final class BattleResizeAction implements BattleAction {
    public static final Codec<BattleResizeAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleBounds.CODEC.fieldOf("bounds").forGetter(action -> action.bounds)).apply(instance, BattleResizeAction::new));
    private final BattleBounds bounds;

    public BattleResizeAction(final BattleBounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public BattleActionType<?> getType() {
        return BattleActionTypes.BATTLE_RESIZE_ACTION_TYPE;
    }

    @Override
    public void apply(final BattleState state, final Tracer<ActionTrace> tracer) {
        state.setBounds(bounds, tracer);//TODO error handling
    }
}
