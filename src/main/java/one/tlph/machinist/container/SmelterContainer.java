package one.tlph.machinist.container;

import java.util.Objects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import one.tlph.machinist.blocks.ModBlocks;
import one.tlph.machinist.proxy.ModContainerTypes;
import one.tlph.machinist.tileentity.SmelterTileEntity;

public class SmelterContainer extends ContainerBase {
	
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
	
	
	public SmelterTileEntity te;
    private final IWorldPosCallable canInteractWithCallable;

    public SmelterContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    public SmelterContainer(final int windowId, final PlayerInventory playerInventory, final SmelterTileEntity tileEntity) {
        super(ModContainerTypes.SMELTER, windowId);
        this.te = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());
        this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntity.cookTime, v -> tileEntity.cookTime = (short) v));

        this.addPlayerSlots(playerInventory);
    }

    private static SmelterTileEntity getTileEntity(PlayerInventory playerInventory, PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileEntity = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if(tileEntity instanceof SmelterTileEntity)
            return (SmelterTileEntity)tileEntity;
        throw new IllegalStateException("Tile entity is not correct! " + tileEntity);
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.world.getBlockState(te.getPos()).getBlock() == ModBlocks.SMELTER.getBlock()
                && playerIn.getDistanceSq(te.getPos().getX() + 0.5, te.getPos().getY() + 0.5, te.getPos().getZ() + 0.5) <= 64.0;
    }
}
