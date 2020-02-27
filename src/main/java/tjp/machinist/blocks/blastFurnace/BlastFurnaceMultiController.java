package tjp.machinist.blocks.blastFurnace;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import tjp.machinist.api.multiblock.IMultiblockPart;
import tjp.machinist.api.multiblock.MultiblockControllerBase;
import tjp.machinist.api.multiblock.rectangular.RectangularMultiblockControllerBase;
import tjp.machinist.api.multiblock.validation.IMultiblockValidator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlastFurnaceMultiController extends RectangularMultiblockControllerBase implements ICapabilityProvider {

    private static final int MACHINE_SZ = 3;
    private Set<BlastFurnaceControllerTE> connectedControllers = null;

    protected BlastFurnaceMultiController(World world) {
        super(world);
    }

    @Override
    public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {

    }

    @Override
    protected void onBlockAdded(IMultiblockPart newPart) {
        if(newPart instanceof BlastFurnaceControllerTE) {
            if (connectedControllers == null) {
                connectedControllers = new HashSet<>();
            }
            connectedControllers.add((BlastFurnaceControllerTE) newPart);
        }
    }

    @Override
    protected void onBlockRemoved(IMultiblockPart oldPart) {

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

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        if(!super.isMachineWhole(validatorCallback))
            return false;
        if(connectedControllers.size() != 1) {
            validatorCallback.setLastError("message.machinist.blastfurnace.multiplecontrollers");
            return false;
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
        return new NBTTagCompound();
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {

    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }
}
