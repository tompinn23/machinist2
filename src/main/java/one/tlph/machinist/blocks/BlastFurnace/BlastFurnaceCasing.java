package one.tlph.machinist.blocks.BlastFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import one.tlph.machinist.api.multiblock.IMultiblockPart;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;
import one.tlph.machinist.api.multiblock.validation.ValidationError;
import one.tlph.machinist.tileentity.BlastFurnaceCasingTileEntity;
import one.tlph.machinist.tileentity.BlastFurnaceMultiBlockTileEntity;

import javax.annotation.Nullable;

public class BlastFurnaceCasing extends Block {
/*
      public enum CasingBorder implements IStringSerializable {
        BL,
        ML,
        TL,
        BR,
        MR,
        MT,
        M,
        MB,
        ;

        @Override
        public String getName() {
            switch(this) {
                case BL:
                    return "bl";
                case ML:
                    return "ml";
                case TL:
                    return "tl";
                case BR:
                    return "br";
                case MR:
                    return "mr";
                case MT:
                    return "mt";
                case MB:
                    return "mb";
                default:
                case M:
                    return "m";
            }
        }
    }

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyEnum<CasingBorder> BORDER = PropertyEnum.create("border", CasingBorder.class);
*/
    public BlastFurnaceCasing() {
    	super(Block.Properties.create(Material.ROCK));
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlastFurnaceCasingTileEntity();
    }


    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
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
            }

        }
        if(controller == null || !controller.isAssembled()) { return false; }

        if(!worldIn.isRemote) {
            //TODO: Fix gui handling
            NetworkHooks.openGui((ServerPlayerEntity)player, (BlastFurnaceMultiBlockTileEntity)controller, pos);
        }
        return true;
    }
}
