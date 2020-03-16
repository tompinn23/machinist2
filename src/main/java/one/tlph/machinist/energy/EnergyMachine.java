package one.tlph.machinist.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyMachine extends EnergyStorage implements INBTSerializable<CompoundNBT> {

	public EnergyMachine(int capacity) {
		super(capacity);
	}
	
    public EnergyMachine(int capacity, int maxTransfer)
    {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public EnergyMachine(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public EnergyMachine(int capacity, int maxReceive, int maxExtract, int energy)
    {
        super(capacity, maxReceive, maxExtract, energy);
    }

    
    public int useEnergy(int amount) {
        int energyExtracted = Math.min(energy, amount);
        energy -= energyExtracted;
        return energyExtracted;    	
    }
    
    @Override
    public void deserializeNBT(CompoundNBT tag) {
    	energy = tag.getInt("Energy");
    	capacity = tag.getInt("Capacity");
    	maxReceive = tag.getInt("MaxReceive");
    	maxExtract = tag.getInt("MaxExtract");
    }

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("Energy", energy);
		tag.putInt("Capacity", capacity);
		tag.putInt("MaxReceive", maxReceive);
		tag.putInt("MaxExtract", maxExtract);
		return tag;
	}
	
	public int getReceive() {
		return this.maxReceive;
	}
	
	public int getExtract() {
		return this.maxExtract;
	}
    
}
