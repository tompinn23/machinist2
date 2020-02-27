package tjp.machinist.blocks.blastFurnace;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import tjp.machinist.blocks.ContainerBase;

public class BlastFurnaceMultiContainer extends ContainerBase {

    private BlastFurnaceMultiController te;

    public BlastFurnaceMultiContainer(IInventory playerInventory, BlastFurnaceMultiController te) {
        this.te = te;

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    @Override
    protected void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
