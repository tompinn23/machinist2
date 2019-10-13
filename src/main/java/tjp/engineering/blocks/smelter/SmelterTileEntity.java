package tjp.engineering.blocks.smelter;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import tjp.engineeering.items.ModItems;
import tjp.engineering.energy.EnergyMachine;

public class SmelterTileEntity extends TileEntity implements ITickable {

	public static final int SIZE = 3;
	
	private int burnTimeRemaining = 0;
	private int burnTimeInitialValue = 0;
	private short cookTime;
	
	private static final short COOK_TIME_FOR_COMPLETION = 200;
	
	private static final short FUEL_SLOT = 0;
	private static final short INPUT_SLOT = 1;
	private static final short OUTPUT_SLOT = 2;
	
	
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
	private static final int TRANSFER_BASE = 20;
	
	private EnergyMachine energyStorage = new EnergyMachine(CAPACITY_BASE, 0, TRANSFER_BASE);
	
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
		cookTime = compound.getShort("CookTime");
		burnTimeRemaining = compound.getInteger("burnTimeRemaining");
		burnTimeInitialValue = compound.getInteger("burnTimeInitialValue");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("fuelStack", fuelStackHandler.serializeNBT());
		compound.setTag("inputStack", inputStackHandler.serializeNBT());
		compound.setTag("outputStack", outputStackHandler.serializeNBT());
		compound.setTag("energyStorage", energyStorage.serializeNBT());
		compound.setShort("CookTime", cookTime);
		compound.setInteger("burnTimeRemaining", burnTimeRemaining);
		compound.setInteger("burnTimeInitialValue", burnTimeInitialValue);
		return compound;
	}
	
    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public void update() {
    	if(!this.world.isRemote) {
	    	if(canSmelt()) {
	    		if(burnFuel()) {
	    			cookTime++;
	    		}
	    		
	    		
	    		if (cookTime >= COOK_TIME_FOR_COMPLETION) {
	    			smeltItem();
	    			cookTime = 0;
	    		}
	    		sendUpdates();
	    	} else {
	    		if(burnTimeRemaining > 0) {
    				burnFuel();
    				sendUpdates();
	    		}
	    		cookTime = 0;
	    	
	    	}
    	}
    }
    
    public double fractionOfCookTimeComplete() {
		double fraction = cookTime / (double)COOK_TIME_FOR_COMPLETION;
		return MathHelper.clamp(fraction, 0.0, 1.0);
    }
    
	public double fractionOfFuelRemaining()
	{
		if(fuelStackHandler.getStackInSlot(0).getItem() == ModItems.coupler) {
			return 1.0;
		}
		if (burnTimeRemaining <= 0 ) return 0;
		double fraction = burnTimeRemaining / (double)burnTimeInitialValue;
		return MathHelper.clamp(fraction, 0.0, 1.0);
	}
	
	public double fractionOfEnergyRemaining() {
		if(fuelStackHandler.getStackInSlot(0).getItem() == ModItems.coupler) {
			double fraction = energyStorage.getEnergyStored() / (double)energyStorage.getMaxEnergyStored();
			return MathHelper.clamp(fraction, 0.0, 1.0);
		} else {
			return 0;
		}
	}
	
	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}
	
	public int getMaxEnergy() {
		return energyStorage.getMaxEnergyStored();
	}
	
	private boolean burnFuel() {
		ItemStack fuel = fuelStackHandler.getStackInSlot(0);
		boolean inventoryChanged = false;
		if(fuel.getItem() == ModItems.coupler) {
			if(TRANSFER_BASE == energyStorage.useEnergy(TRANSFER_BASE)) {
				return true;
			}
			return false;
		}
		
		
		if(burnTimeRemaining > 0) {
			burnTimeRemaining--;
			return true;
		}
		if(burnTimeRemaining == 0) {
			if(!fuel.isEmpty() && getBurnTime(fuel) > 0) {
				burnTimeRemaining = burnTimeInitialValue =  getBurnTime(fuel);
				fuelStackHandler.extractItem(0, 1, false);
				inventoryChanged = true;
			}
		}
		if(inventoryChanged) {
			markDirty();
			return true;
		}
		return false;
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
				if(outputStack.getItem() == result.getItem() && (!outputStack.getHasSubtypes() && ItemStack.areItemStackTagsEqual(outputStack, result))) {
					int combinedSize = outputStack.getCount() + result.getCount();
					if(combinedSize <= outputStack.getMaxStackSize()) {
						//Success
						canSmelt = true;
					}
					else {
						canSmelt = false;
					}
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
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			IBlockState bs = world.getBlockState(pos);
			EnumFacing face = bs.getValue(Smelter.FACING);
			if(facing == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(fuelStackHandler, inputStackHandler, outputStackHandler));
			}
			
			if(facing == EnumFacing.DOWN)
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(fuelStackHandler);
			else if(facing == EnumFacing.UP) 
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputStackHandler);
			else if(facing == face) {
				
			}
			else
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputStackHandler);
		}
		if(capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
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
	
	private void sendUpdates() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(),0,0);
		markDirty();
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
}
