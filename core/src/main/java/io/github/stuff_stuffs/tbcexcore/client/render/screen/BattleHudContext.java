package io.github.stuff_stuffs.tbcexcore.client.render.screen;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;

public interface BattleHudContext {
    double getTotalEnergy();

    double getEnergy();

    void setPotentialEnergyCost(double cost);

    double getPotentialEnergyCost();

    BattleParticipantHandle getHandle();

    final class Impl implements BattleHudContext {
        private final BattleParticipantHandle participantHandle;
        private double totalEnergy;
        private double energy;
        private double potentialCost;

        public Impl(final BattleParticipantHandle participantHandle, final double totalEnergy, final double energy) {
            this.participantHandle = participantHandle;
            this.totalEnergy = totalEnergy;
            this.energy = energy;
        }

        @Override
        public double getTotalEnergy() {
            return totalEnergy;
        }

        @Override
        public double getEnergy() {
            return energy;
        }

        @Override
        public void setPotentialEnergyCost(final double cost) {
            potentialCost = cost;
        }

        @Override
        public double getPotentialEnergyCost() {
            return potentialCost;
        }

        @Override
        public BattleParticipantHandle getHandle() {
            return participantHandle;
        }

        public void setEnergy(final double energy) {
            this.energy = energy;
        }

        public void setTotalEnergy(final double totalEnergy) {
            this.totalEnergy = totalEnergy;
        }
    }
}
