package tjp.machinist.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.IEnergyStorage;

public class TilePowerable extends TileEntity implements IEnergyStorage {

	private final static int DEFAULT_CAPACITY = 1000;
	private final static int DEFAULT_TRANSFER = 20; 
	
	public int capacity = 1000;
	public int transferOutMax = 20;
	public int transferInMax = 20;
	
	protected int energyLevel = 0;
	
	public TilePowerable() {
		this(DEFAULT_CAPACITY, DEFAULT_TRANSFER, DEFAULT_TRANSFER);
	}
	
	public TilePowerable(int capacity) {
		this(capacity, DEFAULT_TRANSFER, DEFAULT_TRANSFER);
	}
	

	public TilePowerable(int capacity, int transfer) {
		this(capacity, transfer, transfer);
	}
	
	public TilePowerable(int capacity, int transferOut, int transferIn) {
		super();
		this.capacity = capacity;
		this.transferInMax = transferIn;
		this.transferOutMax = transferOut;
		
	}
	
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int transfer = maxReceive;
		if(maxReceive > transferInMax) {
			transfer = transferInMax;
		}
		if((transfer + energyLevel) > capacity) {
			transfer = capacity - energyLevel;
		}
		if(!simulate) {
			energyLevel += capacity;
		}
		return transfer;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int transfer = maxExtract;
		if(maxExtract > transferOutMax) {
			transfer = transferOutMax;
		}
		if((energyLevel - transfer) < 0) {
			transfer = energyLevel;
		}
		if(!simulate) {
			energyLevel -= transfer;
		}
		return transfer;
	}

	@Override
	public int getEnergyStored() {
		return energyLevel;
	}

	@Override
	public int getMaxEnergyStored() {
		return capacity;
	}

	@Override
	public boolean canExtract() {
		return transferOutMax > 0;
	}

	@Override
	public boolean canReceive() {
		return transferInMax > 0;
	}

}
