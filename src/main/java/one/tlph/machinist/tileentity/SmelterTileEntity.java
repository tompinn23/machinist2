package one.tlph.machinist.tileentity;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import net.minecraftforge.items.wrapper.RangedWrapper;
import one.tlph.machinist.blocks.ModBlocks;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.energy.TileEntityPowerable;
import one.tlph.machinist.items.ModItems;
import one.tlph.machinist.proxy.ModTileEntityTypes;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.omg.CORBA.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class SmelterTileEntity extends TileEntityPowerable implements ITickable, INamedContainerProvider {

	public static final int SIZE = 3;
	
	private int burnTimeRemaining = 0;
	private int burnTimeInitialValue = 0;
	public short cookTime;
	
	private static final short COOK_TIME_FOR_COMPLETION = 200;


	
	//private boolean useEnergy = false;
	
	public static final int INPUT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int OUTPUT_SLOT = 2;

	public ItemStackHandler inventory = new ItemStackHandler(3) {

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			switch (slot) {
				case INPUT_SLOT:
					return isItemValidInput(stack);
				case FUEL_SLOT:
					return isItemValidFuel(stack);
				default:
					return false;
			}
		}

		@Override
		protected void onContentsChanged(int slot) {
			SmelterTileEntity.this.markDirty();
		}
	};

	private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> this.inventory);
	private final LazyOptional<IItemHandlerModifiable> inventoryUpCap = LazyOptional.of(() -> new RangedWrapper(this.inventory, INPUT_SLOT, INPUT_SLOT  + 1));
	private final LazyOptional<IItemHandlerModifiable> inventoryDownCap = LazyOptional.of(() -> new RangedWrapper(this.inventory, FUEL_SLOT, FUEL_SLOT + 1));
	private final LazyOptional<IItemHandlerModifiable> inventorySidesCap = LazyOptional.of(() -> new RangedWrapper(this.inventory, OUTPUT_SLOT, OUTPUT_SLOT + 1));





	private static final int CAPACITY_BASE = 10000;
	private static final int TRANSFER_BASE = 100;
	
	public SmelterTileEntity() {
		super(ModTileEntityTypes.SMELTER, CAPACITY_BASE, TRANSFER_BASE, 0);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if(compound.contains("inventory")) {
			inventory.deserializeNBT(compound.getCompound("inventory"));
		}
		if(compound.contains("energyStorage")) {
			energyStorage.deserializeNBT(compound.getCompound("energyStorage"));
		}
		cookTime = compound.getShort("CookTime");
		burnTimeRemaining = compound.getInt("burnTimeRemaining");
		burnTimeInitialValue = compound.getInt("burnTimeInitialValue");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("inventory", inventory.serializeNBT());
		compound.put("energyStorage", energyStorage.serializeNBT());
		compound.putShort("CookTime", cookTime);
		compound.putInt("burnTimeRemaining", burnTimeRemaining);
		compound.putInt("burnTimeInitialValue", burnTimeInitialValue);
		return compound;
	}
	


    @Override
    public void tick() {
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
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
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
		
		if(!inventory.getStackInSlot(INPUT_SLOT).isEmpty()) {
			result = getRecipe(inventory.getStackInSlot(INPUT_SLOT));
		}
		if(!result.isEmpty()) {
			ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
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
			inventory.extractItem(INPUT_SLOT, 1, false);
			inventory.insertItem(OUTPUT_SLOT, result.copy(), false);
			markDirty();
			return true;
		}

		return false;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == null)
				return inventoryCap.cast();
			switch(side) {
				case UP:
					return inventoryUpCap.cast();
				case DOWN:
					return inventoryDownCap.cast();
				case EAST:
				case WEST:
				case NORTH:
				case SOUTH:
					return inventorySidesCap.cast();
			}
		}
		return super.getCapability(cap, side);
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
		if(ModItems.coupler == stack.getItem()) {
			return 1;
		}
		
		Map<Item, Integer> burnTime = FurnaceTileEntity.getBurnTimes();
		return 	burnTime.getOrDefault(stack.getItem(), 0);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new SmelterContainer(windowId, inventory, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(ModBlocks.SMELTER.getTranslationKey());
	}

}
