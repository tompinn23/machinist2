package one.tlph.machinist.container;

import java.util.Objects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import one.tlph.machinist.init.ModBlocks;
import one.tlph.machinist.init.ModContainerTypes;
import one.tlph.machinist.blocks.smelter.SmelterTileEntity;

public class SmelterContainer extends ContainerTileBase<SmelterTileEntity> {
	
	public class SmeltableSlotHandler extends SlotItemHandler {
		public SmeltableSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return te.isItemValidInput(stack);
		}
	}
	
	public class FuelSlotHandler extends SlotItemHandler {
		public FuelSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return te.isItemValidFuel(stack);
		}
	}


    public SmelterContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        super(ModContainerTypes.SMELTER.get(), windowId, playerInventory, data);
        this.addPlayerSlots(playerInventory);
    }

    public SmelterContainer(final int windowId, final PlayerInventory playerInventory, SmelterTileEntity tileEntity) {
		super(ModContainerTypes.SMELTER.get(), windowId, playerInventory, tileEntity);
		this.addPlayerSlots(playerInventory);
	}

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.world.getBlockState(te.getPos()).getBlock() == ModBlocks.SMELTER.get().getBlock()
                && playerIn.getDistanceSq(te.getPos().getX() + 0.5, te.getPos().getY() + 0.5, te.getPos().getZ() + 0.5) <= 64.0;
    }
}
