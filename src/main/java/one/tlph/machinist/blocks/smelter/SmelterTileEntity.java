package one.tlph.machinist.blocks.smelter;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;

import one.tlph.machinist.init.registries.ModItems;
import one.tlph.machinist.init.registries.ModTileEntityTypes;
import one.tlph.machinist.inventory.IInventoryHolder;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;
import one.tlph.machinist.util.Ticker;

import java.util.Optional;

public class SmelterTileEntity extends AbstractPoweredTileEntity<Smelter> implements IInventoryHolder {

	private Ticker cookTime;
	private Ticker burnTime;
	
	private static final short COOK_TIME_FOR_COMPLETION = 200;


	
	//private boolean useEnergy = false;
	
	public static final int INPUT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int OUTPUT_SLOT = 2;


	private static final int CAPACITY_BASE = 10000;
	private static final int TRANSFER_BASE = 100;
	
	public SmelterTileEntity() {
		super(ModTileEntityTypes.SMELTER.get());
		this.inv.set(3);
		this.cookTime = new Ticker(COOK_TIME_FOR_COMPLETION);
		this.burnTime = new Ticker(0, true);
		this.energyStorage.setCapacity(10000);
		this.energyStorage.setRecieve(TRANSFER_BASE * 2);
	}

	@Override
	public void readStorable(CompoundNBT nbt) {
		super.readStorable(nbt);
		this.cookTime.read(nbt, "cookTime");
		this.burnTime.read(nbt, "burnTime", true);
	}

	@Override
	public CompoundNBT writeStorable(CompoundNBT nbt) {
		this.cookTime.write(nbt, "cookTime");
		this.burnTime.write(nbt, "burnTime", true);
		return super.writeStorable(nbt);
	}

	@Override
    public int postTick() {
    	if(!this.world.isRemote) {
	    	if(canSmelt()) {
	    		if(useEnergy()) {
	    			this.cookTime.tick();
	    		}


	    		if (this.cookTime.check()) {
	    			smeltItem();
	    		}

	    	} else {
	    		if(!burnTime.check()) {
    				if(!useEnergy()) {
    					useFuel();
					}
	    		}
	    	}
    	}
    	return 4;
    }

	private void useFuel() {
	}

	private boolean useEnergy() {
		if(energyStorage.getEnergyStored() >= TRANSFER_BASE) {
			energyStorage.consume(TRANSFER_BASE);
			return true;
		}
		return false;
	}

	public double fractionOfCookTimeComplete() {
		double fraction = cookTime.get() / (double)COOK_TIME_FOR_COMPLETION;
		return MathHelper.clamp(fraction, 0.0, 1.0);
    }

	
	public double fractionOfEnergyRemaining() {
		double fraction = energyStorage.getEnergyStored() / (double)energyStorage.getMaxEnergyStored();
		return MathHelper.clamp(fraction, 0.0, 1.0);
	}


	private ItemStack getRecipe(final ItemStack stack) {
		return this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), world).get().getRecipeOutput();
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
		
		if(!this.inv.getStackInSlot(INPUT_SLOT).isEmpty()) {
			result = getRecipe(this.inv.getStackInSlot(INPUT_SLOT));
		}
		if(!result.isEmpty()) {
			ItemStack outputStack = this.inv.getStackInSlot(OUTPUT_SLOT);
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
			this.inv.extractItem(INPUT_SLOT, 1, false);
			this.inv.insertItem(OUTPUT_SLOT, result.copy(), false);
			sync(4);
			return true;
		}

		return false;
	}

	private Optional<FurnaceRecipe> getRecipe(final IInventory inventory) {
		return world.getRecipeManager().getRecipe(IRecipeType.SMELTING, inventory, world);
	}


	private Optional<ItemStack> getResult(final ItemStack input) {
		final Inventory inv = new Inventory(input);
		return getRecipe(inv).map(recipe -> recipe.getCraftingResult(inv));
	}


	public boolean isItemValidInput(ItemStack stack) {
		return getResult(stack).isPresent();
	}

	public boolean isItemValidFuel(ItemStack stack) {
			return getBurnTime(stack) > 0;
	}

	private static int getBurnTime(ItemStack stack) {
		/*
		 * if item is capacitor / connector etc.
		 * USE Energy.
		 * 
		 */
		if(ModItems.COUPLER.get() == stack.getItem()) {
			return 1;
		}
		
		return ForgeHooks.getBurnTime(stack);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack) {
		return (slot == 0 && isItemValidFuel(stack)) || (slot == 1 && isItemValidInput(stack)) || slot == 2;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public one.tlph.machinist.inventory.Inventory getInventory() {
		return this.inv;
	}
}
