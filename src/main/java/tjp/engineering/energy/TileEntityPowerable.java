package tjp.engineering.energy;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import tjp.engineering.Engineering;
import tjp.engineering.energy.EnergyUtils.Units;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"),
								  @Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2")})
public class TileEntityPowerable extends TileEntity implements IEnergyStorage, IEnergySink, IEnergyTile {

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
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("energyStorage")) {
			energyStorage.deserializeNBT((NBTTagCompound) compound.getTag("energyStorage"));
		}
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("energyStorage", energyStorage.serializeNBT());
		return super.writeToNBT(compound);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return energyStorage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return energyStorage.canExtract();
	}

	@Override
	public boolean canReceive() {
		return energyStorage.canReceive();
	}
	

	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		
		return super.getCapability(capability, facing);
	}
	
	private boolean addedToEnet;
	
	@Override
	@Optional.Method(modid = "ic2")
	public void onLoad( ) {
		if(!this.addedToEnet && !FMLCommonHandler.instance().getEffectiveSide().isClient() && Engineering.ic2Loaded) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnet = true;
		}
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void invalidate() {
		super.invalidate();
		onChunkUnload();
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public void onChunkUnload() {
		super.onChunkUnload();
		if(this.addedToEnet && Engineering.ic2Loaded) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			this.addedToEnet = false;
		}
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return true;
	}


	@Override
	@Optional.Method(modid = "ic2")
	public double getDemandedEnergy() {
		return EnergyUtils.convertTo(Units.EU, energyStorage.getReceive());
	}

	@Override
	@Optional.Method(modid = "ic2")
	public int getSinkTier() {
		return Integer.MAX_VALUE;
	}

	@Override
	@Optional.Method(modid = "ic2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		long rf = (long)Math.floor(EnergyUtils.convertFrom(Units.EU, amount));
		return rf - (receiveEnergy((int)rf, false));
	}


}
