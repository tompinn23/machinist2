package one.tlph.machinist.blocks.crusher;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import one.tlph.machinist.energy.TransferType;
import one.tlph.machinist.init.registries.ModTileEntityTypes;
import one.tlph.machinist.inventory.IInventoryHolder;
import one.tlph.machinist.inventory.Inventory;
import one.tlph.machinist.recipes.CrusherRecipe;
import one.tlph.machinist.recipes.MachinistRecipeType;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;

public class CrusherTileEntity extends AbstractPoweredTileEntity<Crusher> implements IInventoryHolder {

    private static int TRANSFER_BASE = 40;


    public static final int COOK_TIME_FOR_COMPLETION = 150;
    public int cookTime = 0;


    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int OUTPUT_SLOT2 = 2;




    public CrusherTileEntity() {
        super(ModTileEntityTypes.CRUSHER.get());
        energyStorage.setCapacity(10000);
        energyStorage.setRecieve(TRANSFER_BASE * 2);
        this.inv.set(3);
        this.getSidedConfig().init(TransferType.IN);
    }


    @Override
    public int postTick() {
        if(!isRemote()) {
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
        }
        return 4;
    }

    private ItemStack getInputItem() {
        return this.inv.getStackInSlot(INPUT_SLOT);
    }


    private void doAction() {
        CrusherRecipe result = null;
        if (!getInputItem().isEmpty()) {
            result = MachinistRecipeType.CRUSHER.findFirst(this.world, crusherRecipe -> crusherRecipe.test(getInputItem()));
            ItemStack left = result.getOutput().copy();

            for (int i = 1; i < inv.getSlots(); i++) {
                ItemStack left2 = inv.insertItem(i, left, true);
                if (left2.isEmpty()) {
                    inv.insertItem(i, left, false);
                    inv.extractItem(INPUT_SLOT, 1, false);
                } else if (left2 != ItemStack.EMPTY && (i + 1) == inv.getSlots()) {
                    return;
                } else {
                    left = left2;
                    continue;
                }
                return;
            }
        }
    }

    private boolean canDoAction() {
        for(int i = 1; i < inv.getSlots(); i++) {
            if(inv.getStackInSlot(i).getCount() >= inv.getStackInSlot(i).getMaxStackSize())
                continue;
            else {
                if(energyStorage.getEnergyStored() > TRANSFER_BASE) {
                    return MachinistRecipeType.CRUSHER.contains(this.world, crusherRecipe -> crusherRecipe.test(getInputItem()));
                }
            }
        }
        return false;
    }

    private boolean useEnergy() {
        if(energyStorage.getEnergyStored() >= TRANSFER_BASE) {
            energyStorage.consume(TRANSFER_BASE);
        }
        return true;
    }

    public boolean isItemValidInput(ItemStack stack) {
        return MachinistRecipeType.CRUSHER.contains(this.world, crusherRecipe -> crusherRecipe.test(stack));
    }

    public double getCookProgress() {
        double fraction = cookTime / (double)COOK_TIME_FOR_COMPLETION;
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    @Override
    protected void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        super.readStorable(nbt);
        this.cookTime = nbt.getInt("cookTime");
    }

    @Override
    protected CompoundNBT writeSync(CompoundNBT nbt) {
        return super.writeSync(nbt);
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        nbt.putInt("cookTime", this.cookTime);
        return super.writeStorable(nbt);
    }



    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return (slot == 0 && isItemValidInput(stack)) || (slot == 1 || slot == 2) ;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
