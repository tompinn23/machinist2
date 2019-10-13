package tjp.engineering.energy;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;


public class TileEntityPowerable extends TileEntity implements IEnergyStorage, IEnergySink, IEnergySource {

	protected EnergyMachine energyStorage;
	
	public TileEntityPowerable(int capacity) {
		this(capacity, 200, 200);
	}
	
	public TileEntityPowerable(int capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer);
	}
	
	
	public TileEntityPowerable(int capacity, int maxReceive, int maxExtract) {
		energyStorage = new EnergyMachine(capacity, maxReceive, maxExtract);
	}
	
	
	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getOfferedEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void drawEnergy(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSourceTier() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDemandedEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSinkTier() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEnergyStored() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxEnergyStored() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canExtract() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canReceive() {
		// TODO Auto-generated method stub
		return false;
	}

}
