package one.tlph.machinist.energy;

import net.minecraftforge.energy.EnergyStorage;

public class ResizableEnergyStore extends EnergyStorage {
    public ResizableEnergyStore(int capacity) {
        super(capacity);
    }

    public ResizableEnergyStore(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public ResizableEnergyStore(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public ResizableEnergyStore(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
