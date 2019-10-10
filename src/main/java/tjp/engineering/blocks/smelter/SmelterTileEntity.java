package tjp.engineering.blocks.smelter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SmelterTileEntity extends TileEntity implements ITickable {

	public static final int SIZE = 3;
	
	private int burnTimeRemaining = 0;
	private int burnTimeInitialValue = 0;
	private short cookTime;
	
	private static final short COOK_TIME_FOR_COMPLETION = 200;
	
	private static final short FUEL_SLOT = 0;
	private static final short INPUT_SLOT = 1;
	private static final short OUTPUT_SLOT = 2;
	
	private EnergyStorage energyStore = new EnergyStorage(10000, 200);
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			SmelterTileEntity.this.markDirty();
		}
	};
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
		cookTime = compound.getShort("CookTime");
		burnTimeRemaining = compound.getInteger("burnTimeRemaining");
		burnTimeInitialValue = compound.getInteger("burnTimeInitialValue");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
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
    	if(canSmelt()) {
    		if(burnFuel()) {
    			cookTime++;
    		}
    		
    		
    		if (cookTime >= COOK_TIME_FOR_COMPLETION) {
    			smeltItem();
    			cookTime = 0;
    		}
    	} else {
    		if(burnTimeRemaining > 0)
    				burnFuel();
    		cookTime = 0;
    	}
    }
    
    public double fractionOfCookTimeComplete() {
		double fraction = cookTime / (double)COOK_TIME_FOR_COMPLETION;
		return MathHelper.clamp(fraction, 0.0, 1.0);
    }
    
	public double fractionOfFuelRemaining()
	{
		if (burnTimeRemaining <= 0 ) return 0;
		double fraction = burnTimeRemaining / (double)burnTimeInitialValue;
		return MathHelper.clamp(fraction, 0.0, 1.0);
	}
	private boolean burnFuel() {
		ItemStack fuel = itemStackHandler.getStackInSlot(FUEL_SLOT);
		boolean inventoryChanged = false;
		if(burnTimeRemaining > 0) {
			burnTimeRemaining--;
			return true;
		}
		if(burnTimeRemaining == 0) {
			if(!fuel.isEmpty() && getBurnTime(fuel) > 0) {
				burnTimeRemaining = burnTimeInitialValue =  getBurnTime(fuel);
				itemStackHandler.extractItem(FUEL_SLOT, 1, false);
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
		
		if(!itemStackHandler.getStackInSlot(INPUT_SLOT).isEmpty()) {
			result = FurnaceRecipes.instance().getSmeltingResult(itemStackHandler.getStackInSlot(INPUT_SLOT));
		}
		if(!result.isEmpty()) {
			ItemStack outputStack = itemStackHandler.getStackInSlot(OUTPUT_SLOT);
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
			itemStackHandler.extractItem(INPUT_SLOT, 1, false);
			itemStackHandler.insertItem(OUTPUT_SLOT, result.copy(), false);
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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		if(capability == CapabilityEnergy.ENERGY.cast(energyStore));
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
		int burnTime = TileEntityFurnace.getItemBurnTime(stack);
		return burnTime;
	}
	
}
