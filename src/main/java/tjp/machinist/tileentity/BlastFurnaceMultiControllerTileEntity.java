package tjp.machinist.tileentity;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import tjp.machinist.api.multiblock.IMultiblockPart;
import tjp.machinist.api.multiblock.MultiblockControllerBase;
import tjp.machinist.api.multiblock.rectangular.RectangularMultiblockControllerBase;
import tjp.machinist.api.multiblock.validation.IMultiblockValidator;
import tjp.machinist.recipes.BlastFurnaceRecipes;
import tjp.machinist.recipes.MachineRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlastFurnaceMultiControllerTileEntity extends RectangularMultiblockControllerBase implements ICapabilityProvider {

    public static final int GUI_ID = 3;
    public static final int SIZE = 4;

    private int cookTime = 0;
    private int burnTime = 0;

    private MachineRecipe curRecipe = null;

    private ItemStackHandler inputStackHandler = new ItemStackHandler(2);
    private ItemStackHandler fuelStackHandler = new ItemStackHandler(1);
    private ItemStackHandler outputStackHandler = new ItemStackHandler(1);


    private IMultiblockPart saveDelegatePart = null;


    private static final int MACHINE_SZ = 3;
    private Set<BlastFurnaceControllerTileEntity> connectedControllers = null;
    private BlockPos lastClickedPos;

    public BlastFurnaceMultiControllerTileEntity(World world) {
        super(world);

    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
        if(part != saveDelegatePart)
            saveDelegatePart = part;
        readFromNBT(data.getCompoundTag("multiblockData"));
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


            if (cookTime >= curRecipe.getProcessingTime()) {
                doSmelt();
                cookTime = 0;
            }
            return true;
        } else if(burnTime > 0) {
                burnFuel();
                cookTime = 0;
                return true;
        }
        return false;
    }

    private boolean burnFuel() {
        if(burnTime > 0) {
            burnTime--;
            return true;
        }
        if(TileEntityFurnace.getItemBurnTime(fuelStackHandler.getStackInSlot(0)) > 0) {
            burnTime = TileEntityFurnace.getItemBurnTime(fuelStackHandler.getStackInSlot(0));
            fuelStackHandler.extractItem(0, 1, false);
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

    private boolean smeltItem(boolean doSmelt) {
        MachineRecipe recipe = null;
        boolean canSmelt = false;
        if(!inputStackHandler.getStackInSlot(0).isEmpty() || !inputStackHandler.getStackInSlot(1).isEmpty()) {
            recipe = BlastFurnaceRecipes.instance().GetRecipe(new ItemStack[] {inputStackHandler.getStackInSlot(0), inputStackHandler.getStackInSlot(1)});
        }
        if(recipe != null) {
            if(recipe.getIngredientAmt(inputStackHandler.getStackInSlot(0).getItem()) <= inputStackHandler.getStackInSlot(0).getCount() ||
               recipe.getIngredientAmt(inputStackHandler.getStackInSlot(1).getItem()) <= inputStackHandler.getStackInSlot(1).getCount()) {
                ItemStack outputStack = outputStackHandler.getStackInSlot(0);
                if(!outputStack.isEmpty()) {
                    if(outputStack.getItem() == recipe.getResult().getItem()) {
                        int combinedSize = outputStack.getCount() + recipe.getResult().getCount();
                        canSmelt = combinedSize <= outputStack.getMaxStackSize();
                    }
                } else {
                    canSmelt = true;
                }
            }
        }

        if(canSmelt) {
            curRecipe = recipe;
            if(!doSmelt) return true;
            ItemStack in1 = inputStackHandler.getStackInSlot(0);
            ItemStack in2 = inputStackHandler.getStackInSlot(1);
            inputStackHandler.extractItem(0, recipe.getIngredientAmt(in1.getItem()), false);
            inputStackHandler.extractItem(1, recipe.getIngredientAmt(in2.getItem()), false);
            outputStackHandler.insertItem(0, recipe.getResult().copy(), false);
            return true;
        }
        return false;
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
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        data.setTag("inputStack", inputStackHandler.serializeNBT());
        data.setTag("fuelStack", fuelStackHandler.serializeNBT());
        data.setTag("outputStack", outputStackHandler.serializeNBT());
        data.setInteger("cookTime", cookTime);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        if(data.hasKey("cookTime"))
            cookTime = data.getInteger("cookTime");
        if(data.hasKey("inputStack"))
            inputStackHandler.deserializeNBT((NBTTagCompound)data.getTag("inputStack"));
        if(data.hasKey("fuelStack"))
            fuelStackHandler.deserializeNBT((NBTTagCompound)data.getTag("fuelStack"));
        if(data.hasKey("outputStack"))
            fuelStackHandler.deserializeNBT((NBTTagCompound)data.getTag("outputStack"));
    }

    @Override
    public void formatDescriptionPacket(NBTTagCompound data) {
        writeToNBT(data);
    }

    @Override
    public void decodeDescriptionPacket(NBTTagCompound data) {
        readFromNBT(data);
    }

    protected void sendUpdates() {
        markMultiblockForRenderUpdate();
        markReferenceCoordDirty();
        markReferenceCoordForUpdate();
    }




    public boolean canInteractWith(EntityPlayer playerIn) {
            return playerIn.getDistanceSq(lastClickedPos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if(facing == null)
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(facing == null) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(inputStackHandler,fuelStackHandler, outputStackHandler));
            }
        }
        return null;
    }

    public static boolean isValidInput(ItemStack stack) {
        return BlastFurnaceRecipes.instance().GetRecipe(stack) != null;
    }

    public static boolean isValidFuel(ItemStack stack) {
        return TileEntityFurnace.getItemBurnTime(stack) > 0;
    }
}
