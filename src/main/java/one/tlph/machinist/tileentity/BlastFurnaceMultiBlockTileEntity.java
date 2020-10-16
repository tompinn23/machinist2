package one.tlph.machinist.tileentity;


import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.rectangular.RectangularMultiblockControllerBase;
import one.tlph.machinist.api.multiblock.validation.IMultiblockValidator;
import one.tlph.machinist.container.BlastFurnaceMultiContainer;
import one.tlph.machinist.inventory.IgnoredIInventory;
import one.tlph.machinist.recipes.BlastFurnaceManager;
import one.tlph.machinist.recipes.BlastFurnaceRecipe;
import one.tlph.machinist.recipes.MachinistRecipe;
import one.tlph.machinist.recipes.MachinistRecipeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlastFurnaceMultiBlockTileEntity extends RectangularMultiblockControllerBase implements ICapabilityProvider, INamedContainerProvider {

    public static final int GUI_ID = 3;
    public static final int SIZE = 4;

    public int cookTime = 0;
    public int burnTime = 0;

    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int FUEL_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;



    private ItemStackHandler inventory = new ItemStackHandler(4) {

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch (slot) {
                case INPUT_SLOT_1:
                case INPUT_SLOT_2:
                    return BlastFurnaceMultiBlockTileEntity.this.isValidInput(stack);
                case FUEL_SLOT:
                    return BlastFurnaceMultiBlockTileEntity.this.isValidFuel(stack);
                default:
                    return false;
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            BlastFurnaceMultiBlockTileEntity.this.markReferenceCoordDirty();
            super.onContentsChanged(slot);
        }
    };

    private LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> this.inventory);
    private LazyOptional<IItemHandlerModifiable> inventoryInputCap = LazyOptional.of(() -> new RangedWrapper(this.inventory, INPUT_SLOT_1, INPUT_SLOT_2 + 1));
    private LazyOptional<IItemHandlerModifiable> inventoryFuelCap = LazyOptional.of(() -> new RangedWrapper(this.inventory, FUEL_SLOT, FUEL_SLOT + 1));
    private LazyOptional<IItemHandlerModifiable> inventoryOutputCap = LazyOptional.of(() -> new RangedWrapper(this.inventory, OUTPUT_SLOT, OUTPUT_SLOT + 1));


    private IMultiblockPart saveDelegatePart = null;


    private static final int MACHINE_SZ = 3;
    private Set<BlastFurnaceControllerTileEntity> connectedControllers = null;
    private BlockPos lastClickedPos;

    public BlastFurnaceMultiBlockTileEntity(World world) {
        super(world);

    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart part, CompoundNBT data) {
        if(part != saveDelegatePart)
            saveDelegatePart = part;
        read(data);
    }

    @Override
    protected void onBlockAdded(IMultiblockPart newPart) {
            if(newPart instanceof BlastFurnaceControllerTileEntity) {
            if (connectedControllers == null) {
                connectedControllers = new HashSet<>();
            }
            connectedControllers.add((BlastFurnaceControllerTileEntity) newPart);
        }
    }

    @Override
    protected void onBlockRemoved(IMultiblockPart oldPart) {
        if(oldPart instanceof BlastFurnaceControllerTileEntity) {
            connectedControllers.remove(oldPart);
        }
    }

    @Override
    protected void onMachineAssembled() {
    }

    @Override
    protected void onMachineRestored() {

    }

    @Override
    protected void onMachinePaused() {

    }

    @Override
    protected void onMachineDisassembled() {

    }

    @Override
    protected int getMinimumNumberOfBlocksForAssembledMachine() {
        return MACHINE_SZ;
    }

    @Override
    protected int getMaximumXSize() {
        return MACHINE_SZ;
    }

    @Override
    protected int getMaximumZSize() {
        return MACHINE_SZ;
    }

    @Override
    protected int getMaximumYSize() {
        return MACHINE_SZ;
    }

    @Override
    protected void onAssimilate(MultiblockControllerBase assimilated) {

    }

    @Override
    protected void onAssimilated(MultiblockControllerBase assimilator) {

    }

    @Override
    protected boolean updateServer() {
        if(canSmelt()) {
            if(burnFuel()) {
                cookTime++;
            }


            if (cookTime >= getRecipe().getCookTime()) {
                doSmelt();
                cookTime = 0;
            }
            sendUpdates();
            return true;
        } else if(burnTime > 0) {
                burnFuel();
                cookTime = 0;
                sendUpdates();
                return true;
        }
        return false;
    }


    private boolean burnFuel() {
        if(burnTime > 0) {
            burnTime--;
            return true;
        }
        if(getBurnTime(inventory.getStackInSlot(FUEL_SLOT)) > 0) {
            burnTime = getBurnTime(inventory.getStackInSlot(FUEL_SLOT));
            inventory.extractItem(FUEL_SLOT, 1, false);
            return true;
        }
        return false;
    }




    @Override
    protected void updateClient() {

    }

    private boolean canSmelt() {
        return smeltItem(false);
    }

    private boolean doSmelt() {
        return smeltItem(true);
    }

    private BlastFurnaceRecipe getRecipe() {
        BlastFurnaceRecipe re = MachinistRecipeType.BLAST_FURNACE.findFirst(this.WORLD, recipe -> recipe.test(inventory.getStackInSlot(INPUT_SLOT_1), inventory.getStackInSlot(INPUT_SLOT_2)));
        return re;
    }

    private boolean smeltItem(boolean doSmelt) {
        BlastFurnaceRecipe recipe = null;
        boolean canSmelt = false;
        if(!inventory.getStackInSlot(INPUT_SLOT_1).isEmpty() || !inventory.getStackInSlot(INPUT_SLOT_2).isEmpty()) {
            recipe = getRecipe();
        }
        if(recipe != null) {
            //if(recipe.checkIngredients(inventory.getStackInSlot(INPUT_SLOT_1), inventory.getStackInSlot(INPUT_SLOT_2))) {
                ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
                if(!outputStack.isEmpty()) {
                    if(outputStack.getItem() == recipe.getOutput(inventory.getStackInSlot(INPUT_SLOT_1), inventory.getStackInSlot(INPUT_SLOT_2)).getItem()) {
                        int combinedSize = outputStack.getCount() + recipe.getOutput(inventory.getStackInSlot(INPUT_SLOT_1), inventory.getStackInSlot(INPUT_SLOT_2)).getCount();
                        canSmelt = combinedSize <= outputStack.getMaxStackSize();
                    }
                } else {
                    canSmelt = true;
                }
            //}
        }

        if(canSmelt) {
            if(!doSmelt) return true;
            ItemStack in1 = inventory.getStackInSlot(INPUT_SLOT_1);
            ItemStack in2 = inventory.getStackInSlot(INPUT_SLOT_2);
                inventory.extractItem(INPUT_SLOT_1, recipe.getMainInput().getMatchingInstance(inventory.getStackInSlot(INPUT_SLOT_1)).getCount(), false);
                inventory.extractItem(INPUT_SLOT_2, recipe.getMainInput().getMatchingInstance(inventory.getStackInSlot(INPUT_SLOT_2)).getCount(), false);
                inventory.insertItem(OUTPUT_SLOT, recipe.getOutput(inventory.getStackInSlot(INPUT_SLOT_1), inventory.getStackInSlot(INPUT_SLOT_2)).copy(), false);
            return true;
        }
        return false;
    }


    public double getCookProgress() {
        BlastFurnaceRecipe recipe = getRecipe();
        if(recipe == null) {
            return 0.0D;
        }
        return MathHelper.clamp(cookTime / (double)getRecipe().getCookTime(), 0.0D, 1.0D);
    }

    public double getFuelLeft() {
        return MathHelper.clamp(burnTime / (double)getBurnTime(inventory.getStackInSlot(FUEL_SLOT)), 0.0D, 1.0D);
    }



    public void onBlockActivated(BlockPos pos) {
        this.lastClickedPos = pos;
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        if(!super.isMachineWhole(validatorCallback))
            return false;
        if(connectedControllers != null) {
            if (connectedControllers.size() != 1) {
                validatorCallback.setLastError("message.machinist.blastfurnace.multiplecontrollers");
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean isBlockGoodForFrame(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForTop(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForBottom(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForSides(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
        return world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR;
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        data.put("inventory", inventory.serializeNBT());
        data.putInt("cookTime", cookTime);
        data.putInt("burnTime", burnTime);
        return data;
    }

    @Override
    public void read(CompoundNBT data) {
        if(data.contains("cookTime"))
            cookTime = data.getInt("cookTime");
        if(data.contains("burnTime"))
            burnTime = data.getInt("burnTime");
        if(data.contains("inventory"))
            inventory.deserializeNBT(data.getCompound("inputStack"));
    }

    @Override
    public void formatDescriptionPacket(CompoundNBT data) {
        write(data);
    }

    @Override
    public void decodeDescriptionPacket(CompoundNBT data) {
        read(data);
    }

    protected void sendUpdates() {
        markMultiblockForRenderUpdate();
        markReferenceCoordDirty();
        markReferenceCoordForUpdate();
    }




    public boolean canInteractWith(PlayerEntity playerIn) {
            return playerIn.getDistanceSq(lastClickedPos.getX() + 0.5D, lastClickedPos.getY() + 0.5D, lastClickedPos.getZ() + 0.5D) <= 64D;
    }


    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(facing == null) {
                return inventoryCap.cast();
            }
            //TOOD: Directions
        }
        return null;
    }

    public static boolean isValidInput(ItemStack stack) {
        return true;
//        return BlastFurnaceRecipes.instance().GetRecipe(stack) != null;
    }

    public static boolean isValidFuel(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }


    private static int getBurnTime(ItemStack stack) {
        Map<Item, Integer> burnTime = FurnaceTileEntity.getBurnTimes();
        return 	burnTime.getOrDefault(stack.getItem(), 0);
    }

	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity entity) {
		return new BlastFurnaceMultiContainer(windowId, inventory, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
}
