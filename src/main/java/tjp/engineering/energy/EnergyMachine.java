package tjp.engineering.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyMachine extends EnergyStorage implements INBTSerializable<NBTTagCompound> {

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
    public void deserializeNBT(NBTTagCompound tag) {
    	energy = tag.getInteger("Energy");
    	capacity = tag.getInteger("Capacity");
    	maxReceive = tag.getInteger("MaxReceive");
    	maxExtract = tag.getInteger("MaxExtract");
    }

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Energy", energy);
		tag.setInteger("Capacity", capacity);
		tag.setInteger("MaxReceive", maxReceive);
		tag.setInteger("MaxExtract", maxExtract);
		return tag;
	}
	
	public int getReceive() {
		return this.maxReceive;
	}
	
	public int getExtract() {
		return this.maxExtract;
	}
    
}
