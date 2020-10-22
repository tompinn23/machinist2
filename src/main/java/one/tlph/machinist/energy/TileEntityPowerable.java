package one.tlph.machinist.energy;


import com.google.common.base.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.energy.EnergyUtils.Units;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;



//@Optional.InterfaceList(value = { @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"),
//								  @Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2")})
public class TileEntityPowerable extends TileEntity implements IEnergyStorage /*, IEnergySink, IEnergyTile */ {

	protected EnergyMachine energyStorage;


	public TileEntityPowerable(TileEntityType<?> ty, int capacity) {
		this(ty, capacity, 200, 200);
	}
	
	public TileEntityPowerable(TileEntityType<?> ty, int capacity, int maxTransfer) {
		this(ty, capacity, maxTransfer, maxTransfer);
	}
	


    public TileEntityPowerable(TileEntityType<?> ty, int capacityBase, int transferBase, int i) {
        super(ty);
		energyStorage = new EnergyMachine(capacityBase, transferBase, i);
    }


    @Override
	public void read(BlockState state, CompoundNBT compound) {
		if(compound.contains("energyStorage")) {
			energyStorage.deserializeNBT(compound.getCompound("energyStorage"));
		}
		super.read(state, compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("energyStorage", energyStorage.serializeNBT());
		return super.write(compound);
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


	public boolean canInteractWith(PlayerEntity playerIn) {
		// If we are too far away from this tile entity you cannot use it
		return !isRemoved() && playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)<= 64D;
	}


	LazyOptional<EnergyStorage> energyStorageCapability = LazyOptional.of(() -> this.energyStorage);


	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityEnergy.ENERGY) {
			return energyStorageCapability.cast();
		}
		return super.getCapability(cap, side);
	}

	protected void sendUpdates() {
		world.markAndNotifyBlock(pos, world.getChunkAt(pos), world.getBlockState(pos), world.getBlockState(pos), 3, 0);
		markDirty();
	}


	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}


	public double fractionOfEnergyRemaining() {
		double fraction = energyStorage.getEnergyStored() / (double)energyStorage.getMaxEnergyStored();
		return MathHelper.clamp(fraction, 0.0, 1.0);
	}


	private boolean addedToEnet;
	
//	@Override
//	@Optional.Method(modid = "ic2")
//	public void onLoad( ) {
//		if(!this.addedToEnet && !FMLCommonHandler.instance().getEffectiveSide().isClient() && Machinist.ic2Loaded) {
//			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
//			this.addedToEnet = true;
//		}
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public void invalidate() {
//		super.invalidate();
//		onChunkUnload();
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public void onChunkUnload() {
//		super.onChunkUnload();
//		if(this.addedToEnet && Machinist.ic2Loaded) {
//			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
//			this.addedToEnet = false;
//		}
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
//		return true;
//	}
//
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public double getDemandedEnergy() {
//		return EnergyUtils.convertTo(Units.EU, energyStorage.getReceive());
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public int getSinkTier() {
//		return Integer.MAX_VALUE;
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
//		long rf = (long)Math.floor(EnergyUtils.convertFrom(Units.EU, amount));
//		return rf - (receiveEnergy((int)rf, false));
//	}


}
