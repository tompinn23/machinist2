package one.tlph.machinist.container;

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
import one.tlph.machinist.tileentity.CrusherTileEntity;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CrusherContainer extends ContainerBase {
    public CrusherTileEntity tileEntity;
    private final IWorldPosCallable canInteractWithCallable;

    public CrusherContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    public CrusherContainer(final int windowId, final PlayerInventory playerInventory, final CrusherTileEntity tileEntity) {
        super(ModContainerTypes.CRUSHER, windowId);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());
        this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntity.cookTime, v -> tileEntity.cookTime = (short) v));

        this.addPlayerSlots(playerInventory);
    }

    private static CrusherTileEntity getTileEntity(PlayerInventory playerInventory, PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileEntity = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if(tileEntity instanceof CrusherTileEntity)
            return (CrusherTileEntity)tileEntity;
        throw new IllegalStateException("Tile entity is not correct! " + tileEntity);
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, ModBlocks.CRUSHER);
    }

    private class CrushableSlotHandler extends SlotItemHandler {


        public CrushableSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return CrusherTileEntity.isItemValidInput(stack);
        }
    }




}
