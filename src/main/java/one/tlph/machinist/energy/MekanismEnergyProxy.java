package one.tlph.machinist.energy;

import mekanism.api.energy.IEnergyContainer;

public class MekanismEnergyProxy extends AbstractEnergyProxy {
    private final IEnergyContainer energy;

    public MekanismEnergyProxy(IEnergyContainer energy) {
        this.energy = energy;
    }
}
