package tjp.machinist.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import tjp.machinist.blocks.crusher.Crusher;
import tjp.machinist.energy.TileEntityPowerable;

import javax.annotation.Nullable;

public class CrusherTileEntity extends TileEntityPowerable implements ITickable {

    public static final int SIZE = 3;

    private static int TRANSFER_BASE = 40;


    private static final int COOK_TIME_FOR_COMPLETION = 150;
    private int cookTime = 0;



    private ItemStackHandler inputStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) { CrusherTileEntity.this.markDirty();}
    };

    private ItemStackHandler outputStackHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) { CrusherTileEntity.this.markDirty();}
    };

    public CrusherTileEntity() {
        super(10000, TRANSFER_BASE * 2, 0);
        recipeHandler = CrusherRecipes.instance();
    }

    @Override
    public void update() {
        if(!this.world.isRemote) {
            if(canDoAction()) {
                if(useEnergy()) {
                    cookTime++;
                }


                if (cookTime >= COOK_TIME_FOR_COMPLETION) {
                    doAction();
                    cookTime = 0;
                }

            } else {
                cookTime = 0;
            }
            sendUpdates();
        }

    }

    private void doAction() {
        ItemStack result = ItemStack.EMPTY;
        if (!inputStackHandler.getStackInSlot(0).isEmpty()) {
            result = recipeHandler.getResult(inputStackHandler.getStackInSlot(0))[0];
            ItemStack left = result.copy();

            for (int i = 0; i < outputStackHandler.getSlots(); i++) {
                ItemStack left2 = outputStackHandler.insertItem(i, left, true);
                if (left2.isEmpty()) {
                    outputStackHandler.insertItem(i, left, false);
                    inputStackHandler.extractItem(0, 1, false);
                } else if (left2 != ItemStack.EMPTY && (i + 1) == outputStackHandler.getSlots()) {
                    return;
                } else {
                    left = left2;
                    continue;
                }

                markDirty();
                return;
            }
        }
    }

    private boolean canDoAction() {
        for(int i = 0; i < outputStackHandler.getSlots(); i++) {
            if(outputStackHandler.getStackInSlot(i).getCount() >= outputStackHandler.getStackInSlot(0).getMaxStackSize())
                continue;
            else {
                if(energyStorage.getEnergyStored() > TRANSFER_BASE) {
                    return recipeHandler.hasRecipe(inputStackHandler.getStackInSlot(0));
                }
            }
        }
        return false;
    }

    private boolean useEnergy() {
        if(energyStorage.getEnergyStored() >= TRANSFER_BASE) {
            energyStorage.useEnergy(TRANSFER_BASE);
        }
        return true;
    }



    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inputStack", inputStackHandler.serializeNBT());
        compound.setTag("outputStack", outputStackHandler.serializeNBT());
        compound.setInteger("cookTime", cookTime);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inputStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("inputStack"));
        outputStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("outputStack"));
        cookTime = compound.getInteger("cookTime");
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
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(inputStackHandler, outputStackHandler));
            }

            IBlockState bs = world.getBlockState(pos);
            EnumFacing face = bs.getValue(Crusher.FACING);

            if(facing == EnumFacing.UP) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputStackHandler);
            }
            else {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputStackHandler);
            }
        }
        return super.getCapability(capability,facing);
    }

    public static boolean isItemValidInput(ItemStack stack) {
        return CrusherRecipes.instance().hasRecipe(stack);
    }

    public double getCookProgress() {
        double fraction = cookTime / (double)COOK_TIME_FOR_COMPLETION;
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }
}
