package tjp.machinist.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import tjp.machinist.ModBlocks;
import tjp.machinist.blocks.smelter.Smelter;
import tjp.machinist.energy.TileEntityPowerable;
import tjp.machinist.items.ModItems;

import javax.annotation.Nullable;

public class SmelterTileEntity extends TileEntityPowerable implements ITickable {

	public static final int SIZE = 3;
	
	private int burnTimeRemaining = 0;
	private int burnTimeInitialValue = 0;
	private short cookTime;

	private EnumFacing facing;
	
	private static final short COOK_TIME_FOR_COMPLETION = 200;
	

	
	//private boolean useEnergy = false;
	
	
	private ItemStackHandler fuelStackHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			SmelterTileEntity.this.markDirty();
		}
		
	};
	
	private ItemStackHandler inputStackHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			SmelterTileEntity.this.markDirty();
		}
		
	};
	
	private ItemStackHandler outputStackHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			SmelterTileEntity.this.markDirty();
		}
	
	};
	
	private static final int CAPACITY_BASE = 10000;
	private static final int TRANSFER_BASE = 100;
	
	public SmelterTileEntity() {
		super(CAPACITY_BASE, TRANSFER_BASE, 0);
		facing = EnumFacing.NORTH;
	}

	public SmelterTileEntity(EnumFacing facing) {
		super(CAPACITY_BASE, TRANSFER_BASE, 0);
		this.facing = facing;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("fuelStack")) {
			fuelStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("fuelStack"));
		}
		if(compound.hasKey("inputStack")) {
			inputStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("inputStack"));
		}
		if(compound.hasKey("outputStack")) {
			outputStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("outputStack"));
		}
		if(compound.hasKey("energyStorage")) {
			energyStorage.deserializeNBT((NBTTagCompound) compound.getTag("energyStorage")); 
		}
		facing = EnumFacing.values()[compound.getInteger("facing")];
		cookTime = compound.getShort("CookTime");
		burnTimeRemaining = compound.getInteger("burnTimeRemaining");
		burnTimeInitialValue = compound.getInteger("burnTimeInitialValue");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("facing", facing.getIndex());
		compound.setTag("fuelStack", fuelStackHandler.serializeNBT());
		compound.setTag("inputStack", inputStackHandler.serializeNBT());
		compound.setTag("outputStack", outputStackHandler.serializeNBT());
		compound.setTag("energyStorage", energyStorage.serializeNBT());
		compound.setShort("CookTime", cookTime);
		compound.setInteger("burnTimeRemaining", burnTimeRemaining);
		compound.setInteger("burnTimeInitialValue", burnTimeInitialValue);
		return compound;
	}
	


    @Override
    public void update() {
    	if(!this.world.isRemote) {
	    	if(canSmelt()) {
	    		if(useEnergy()) {
	    			cookTime++;
	    		}


	    		if (cookTime >= COOK_TIME_FOR_COMPLETION) {
	    			smeltItem();
	    			cookTime = 0;
	    		}

	    	} else {
	    		if(burnTimeRemaining > 0) {
    				useEnergy();
	    		}
	    		cookTime = 0;
	    	}
			sendUpdates();
    	}
    }

	private boolean useEnergy() {
		if(energyStorage.getEnergyStored() >= TRANSFER_BASE) {
			energyStorage.useEnergy(TRANSFER_BASE);
			return true;
		}
		return false;
	}

	public double fractionOfCookTimeComplete() {
		double fraction = cookTime / (double)COOK_TIME_FOR_COMPLETION;
		return MathHelper.clamp(fraction, 0.0, 1.0);
    }

	
	public double fractionOfEnergyRemaining() {
		double fraction = energyStorage.getEnergyStored() / (double)energyStorage.getMaxEnergyStored();
		return MathHelper.clamp(fraction, 0.0, 1.0);
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return newState.getBlock() != ModBlocks.smelter;
	}


	private boolean canSmelt() {
		return smeltItem(false);
	}
	
	private boolean smeltItem() {
		return smeltItem(true);
	}

	private boolean smeltItem(boolean performSmelt) {
		
		ItemStack result = ItemStack.EMPTY;
		boolean canSmelt = false;
		
		if(!inputStackHandler.getStackInSlot(0).isEmpty()) {
			result = FurnaceRecipes.instance().getSmeltingResult(inputStackHandler.getStackInSlot(0));
		}
		if(!result.isEmpty()) {
			ItemStack outputStack = outputStackHandler.getStackInSlot(0);
			if(!outputStack.isEmpty())
			{
				if(outputStack.getItem() == result.getItem()) {
					int combinedSize = outputStack.getCount() + result.getCount();
					//Success
					canSmelt = combinedSize <= outputStack.getMaxStackSize();
				} 
			} else {
				canSmelt = true;
			}
		}
		
		if(canSmelt) {
			if(!performSmelt) return true;
			inputStackHandler.extractItem(0, 1, false);
			outputStackHandler.insertItem(0, result.copy(), false);
			markDirty();
			return true;
		}

		return false;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(facing == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(fuelStackHandler, inputStackHandler, outputStackHandler));
			}

			// Will Error if block broken.
			IBlockState bs = world.getBlockState(pos);
			EnumFacing face = bs.getValue(Smelter.FACING);
			if(facing == EnumFacing.DOWN)
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(fuelStackHandler);
			else if(facing == EnumFacing.UP) 
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputStackHandler);
			else if(facing == face) {
				
			}
			else
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputStackHandler);
		}
		return super.getCapability(capability, facing);
	}

	public static boolean isItemValidInput(ItemStack stack) {
		return FurnaceRecipes.instance().getSmeltingResult(stack) != ItemStack.EMPTY;
	}

	public static boolean isItemValidFuel(ItemStack stack) {
			return getBurnTime(stack) > 0;
	}

	private static int getBurnTime(ItemStack stack) {
		/*
		 * if item is capacitor / connector etc.
		 * USE Energy.
		 * 
		 */
		if(ModItems.coupler == stack.getItem()) {
			return 1;
		}
		
		int burnTime = TileEntityFurnace.getItemBurnTime(stack);
		return burnTime;
	}

	public EnumFacing getFacing() {
		return facing;
	}


	public void setFacing(EnumFacing opposite) {
		this.facing = facing;
	}
}
