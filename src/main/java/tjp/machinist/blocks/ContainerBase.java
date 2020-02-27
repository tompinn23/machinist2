package tjp.machinist.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;


public abstract class ContainerBase extends Container {

    protected abstract void addOwnSlots();

    protected void addPlayerSlots(IInventory playerInventory) {
        //Slots for main inventory
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        for(int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 58 + 84;
            this.addSlotToContainer(new Slot(playerInventory, row, x ,y));
        }
    }

    @Override
    public abstract boolean canInteractWith(EntityPlayer playerIn);


}
