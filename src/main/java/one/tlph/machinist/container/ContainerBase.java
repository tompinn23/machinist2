package one.tlph.machinist.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class ContainerBase extends Container {

    public final PlayerEntity player;
    public final World world;
	
    protected ContainerBase(@Nullable ContainerType<?> type, int id, PlayerInventory inventory) {
        super(type, id);
        this.player = inventory.player;
        this.world = this.player.world;
    }

    protected void addPlayerSlots(IInventory playerInventory) {
        //Slots for main inventory
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                this.addSlot(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        for(int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 58 + 84;
            this.addSlot(new Slot(playerInventory, row, x ,y));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }





}
