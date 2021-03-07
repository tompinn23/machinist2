package one.tlph.machinist.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import one.tlph.machinist.inventory.IInventoryHolder;
import one.tlph.machinist.inventory.Inventory;
import one.tlph.machinist.tileentity.AbstractTileEntity;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public class AbstractBlock extends Block {
    public AbstractBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof AbstractTileEntity) {
                INamedContainerProvider provider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new ItemStack(AbstractBlock.this).getDisplayName();
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int i, PlayerInventory inventory, PlayerEntity player) {
                        return getContainer(i, inventory, (AbstractTileEntity) te, hit);
                    }
                };
                Container container = provider.createMenu(0, player.inventory, player);
                if(container != null) {
                    if(player instanceof ServerPlayerEntity) {
                        NetworkHooks.openGui((ServerPlayerEntity)player, provider, packetBuffer -> {
                            packetBuffer.writeBlockPos(pos);
                            additionalGuiData(packetBuffer, state,worldIn,pos,player, handIn, hit);
                        });
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    protected void additionalGuiData(PacketBuffer buffer, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    }

    @Nullable
    public Container getContainer(int id, PlayerInventory playerInventory, AbstractTileEntity tileEntity, BlockRayTraceResult result) {
        return null;
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof IInventoryHolder) {
                Inventory inventory = ((IInventoryHolder)te).getInventory();
                for(int i = 0; i < inventory.getSlots(); i++) {
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
