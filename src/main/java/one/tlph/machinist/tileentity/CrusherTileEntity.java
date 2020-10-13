package one.tlph.machinist.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import one.tlph.machinist.init.ModBlocks;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.energy.TileEntityPowerable;
import one.tlph.machinist.init.ModTileEntityTypes;
import one.tlph.machinist.recipes.CrusherManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrusherTileEntity extends TileEntityPowerable implements ITickable, INamedContainerProvider {

    public static final int SIZE = 3;

    private static int TRANSFER_BASE = 40;


    private static final int COOK_TIME_FOR_COMPLETION = 150;
    public int cookTime = 0;


    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int OUTPUT_SLOT2 = 2;

    private final ItemStackHandler input = new ItemStackHandler(1);
    private final ItemStackHandler outputInv = new ItemStackHandler(2);


    public CrusherTileEntity() {
        super(ModTileEntityTypes.CRUSHER.get(), 10000, TRANSFER_BASE * 2, 0);
    }

    public IItemHandler getInput() {
        return input;
    }

    public IItemHandler getOutput() {
        return outputInv;
    }


    @Override
    public void tick() {
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

    private ItemStack getInputItem() {
        return input.getStackInSlot(0);
    }


    private void doAction() {
        CrusherManager.CrusherRecipe result = null;
        if (!getInputItem().isEmpty()) {
            result = CrusherManager.getRecipe(getInputItem());
            ItemStack left = result.getOutput().copy();

            for (int i = 0; i < outputInv.getSlots(); i++) {
                ItemStack left2 = outputInv.insertItem(i, left, true);
                if (left2.isEmpty()) {
                    outputInv.insertItem(i, left, false);
                    input.extractItem(0, 1, false);
                } else if (left2 != ItemStack.EMPTY && (i + 1) == outputInv.getSlots()) {
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
        for(int i = 0; i < outputInv.getSlots(); i++) {
            if(outputInv.getStackInSlot(i).getCount() >= outputInv.getStackInSlot(i).getMaxStackSize())
                continue;
            else {
                if(energyStorage.getEnergyStored() > TRANSFER_BASE) {
                    return CrusherManager.recipeExists(outputInv.getStackInSlot(INPUT_SLOT));
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
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("input", input.serializeNBT());
        compound.put("outputInv", outputInv.serializeNBT());
        compound.putInt("cookTime", cookTime);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        input.deserializeNBT(compound.getCompound("input"));
        outputInv.deserializeNBT(compound.getCompound("outputInv"));
        cookTime = compound.getInt("cookTime");
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


    public LazyOptional<CombinedInvWrapper> itemCap = LazyOptional.of(() -> new CombinedInvWrapper(input, outputInv));


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nonnull Direction direction) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemCap.cast();
        }
        return super.getCapability(cap, direction);
    }


    public static boolean isItemValidInput(ItemStack stack) {
        return CrusherManager.recipeExists(stack);
    }

    public double getCookProgress() {
        double fraction = cookTime / (double)COOK_TIME_FOR_COMPLETION;
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new CrusherContainer(windowId, inventory, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(ModBlocks.CRUSHER.get().getTranslationKey());
	}


}
