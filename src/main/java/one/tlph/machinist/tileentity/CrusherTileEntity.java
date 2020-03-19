package one.tlph.machinist.tileentity;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
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
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import one.tlph.machinist.blocks.ModBlocks;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.energy.TileEntityPowerable;
import one.tlph.machinist.proxy.ModTileEntityTypes;
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


    public ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) { CrusherTileEntity.this.markDirty();}
    };

    public CrusherTileEntity() {
        super(ModTileEntityTypes.CRUSHER, 10000, TRANSFER_BASE * 2, 0);
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

    private void doAction() {
        CrusherManager.CrusherRecipe result = null;
        if (!inventory.getStackInSlot(INPUT_SLOT).isEmpty()) {
            result = CrusherManager.getRecipe(inventory.getStackInSlot(INPUT_SLOT));
            ItemStack left = result.getOutput().copy();

            for (int i = OUTPUT_SLOT; i < inventory.getSlots(); i++) {
                ItemStack left2 = inventory.insertItem(i, left, true);
                if (left2.isEmpty()) {
                    inventory.insertItem(i, left, false);
                    inventory.extractItem(INPUT_SLOT, 1, false);
                } else if (left2 != ItemStack.EMPTY && (i + 1) == inventory.getSlots()) {
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
        for(int i = 1; i < inventory.getSlots(); i++) {
            if(inventory.getStackInSlot(i).getCount() >= inventory.getStackInSlot(i).getMaxStackSize())
                continue;
            else {
                if(energyStorage.getEnergyStored() > TRANSFER_BASE) {
                    return CrusherManager.recipeExists(inventory.getStackInSlot(INPUT_SLOT));
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
        compound.put("inventory", inventory.serializeNBT());
        compound.putInt("cookTime", cookTime);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inventory.deserializeNBT(compound.getCompound("inventory"));
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

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }


    public LazyOptional<CombinedInvWrapper> itemCap = LazyOptional.of(() -> new CombinedInvWrapper(inventory));


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
		return new TranslationTextComponent(ModBlocks.CRUSHER.getTranslationKey());
	}
}
