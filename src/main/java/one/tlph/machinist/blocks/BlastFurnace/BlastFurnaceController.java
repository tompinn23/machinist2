package one.tlph.machinist.blocks.BlastFurnace;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.validation.ValidationError;
import one.tlph.machinist.tileentity.BlastFurnaceControllerTileEntity;
import one.tlph.machinist.tileentity.BlastFurnaceMultiBlockTileEntity;

import javax.annotation.Nullable;

public class BlastFurnaceController extends HorizontalBlock {


    public BlastFurnaceController(final Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlastFurnaceControllerTileEntity();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            if (player.isSneaking()) {
                return false;
            }
            TileEntity te = worldIn.getTileEntity(pos);
            IMultiblockPart part = null;
            MultiblockControllerBase controller = null;
            if (te instanceof IMultiblockPart) {
                part = (IMultiblockPart) te;
                controller = part.getMultiblockController();
            }
            if(controller != null) controller.onBlockActivated(pos);
            ItemStack st = player.getHeldItemMainhand();
            if (st.isEmpty()) {
                if (controller != null) {
                    ValidationError status = controller.getLastError();
                    if (null != status) {
                        player.sendStatusMessage(status.getChatMessage(), false);
                        return true;
                    }
                    //TODO: Fix guis.
                    NetworkHooks.openGui(Machinist.instance, BlastFurnaceMultiBlockTileEntity.GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
