package io.github.stuff_stuffs.tbcexcore.client.render.screen;

public interface BattleHudContext {
    double getTotalEnergy();

    double getEnergy();

    void setPotentialEnergyCost(double cost);

    double getPotentialEnergyCost();

    final class Impl implements BattleHudContext {
        private double totalEnergy;
        private double energy;
        private double potentialCost;

        public Impl(final double totalEnergy, final double energy) {
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

        public void setEnergy(double energy) {
            this.energy = energy;
        }

        public void setTotalEnergy(double totalEnergy) {
            this.totalEnergy = totalEnergy;
        }
    }
}
