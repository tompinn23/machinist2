package tjp.machinist.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import tjp.machinist.api.multiblock.MultiblockTileEntityBase;
import tjp.machinist.api.multiblock.rectangular.RectangularMultiblockControllerBase;
import tjp.machinist.api.multiblock.validation.IMultiblockValidator;
import tjp.machinist.recipes.BlastFurnaceRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlastFurnaceMultiControllerTileEntity extends RectangularMultiblockControllerBase implements ICapabilityProvider {

    public static final int GUI_ID = 3;
    public static final int SIZE = 4;

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
        readFromNBT(data);
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

        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        if(saveDelegatePart.isMultiblockSaveDelegate()) {
            ((MultiblockTileEntityBase)saveDelegatePart).readFromNBT(data);
        }
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
        return BlastFurnaceRecipes.instance().hasRecipe(stack);
    }

    public static boolean isValidFuel(ItemStack stack) {
        return stack.getItem() == Items.COAL || stack.getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK);
    }
}
