package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Collections;
import java.util.Set;

public final class BattleParticipantDamageType {
    private final Set<BattleParticipantDamageType> parents;
    private final Set<BattleParticipantDamageType> ancestors;
    private final RegistryEntry.Reference<BattleParticipantDamageType> reference;

    public BattleParticipantDamageType(final Set<BattleParticipantDamageType> parents) {
        this.parents = parents;
        final Set<BattleParticipantDamageType> ancestors = new ReferenceOpenHashSet<>();
        for (final BattleParticipantDamageType parent : parents) {
            collectAncestors(ancestors, parent);
        }
        this.ancestors = Collections.unmodifiableSet(ancestors);
        reference = BattleParticipantDamageTypes.REGISTRY.createEntry(this);
    }

    private void collectAncestors(final Set<BattleParticipantDamageType> ancestors, final BattleParticipantDamageType ancestor) {
        ancestors.add(ancestor);
        for (final BattleParticipantDamageType parent : ancestor.parents) {
            collectAncestors(ancestors, parent);
        }
    }

    public Set<BattleParticipantDamageType> getParents() {
        return parents;
    }

    public Set<BattleParticipantDamageType> getAncestors() {
        return ancestors;
    }

    public RegistryEntry.Reference<BattleParticipantDamageType> getReference() {
        return reference;
    }

    public boolean hasAncestor(final BattleParticipantDamageType type) {
        return ancestors.contains(type);
    }

    public boolean hasDescendant(final BattleParticipantDamageType type) {
        return type.hasAncestor(this);
    }

    @Override
    public String toString() {
        final Identifier id = BattleParticipantDamageTypes.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered BattleParticipantDamageType";
        }
        return "BattleParticipantDamageType{" + id + "}";
    }
}
