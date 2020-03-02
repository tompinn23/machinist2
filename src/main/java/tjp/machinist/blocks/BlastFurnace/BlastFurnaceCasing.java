package tjp.machinist.blocks.BlastFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tjp.machinist.Machinist;
import tjp.machinist.api.multiblock.IMultiblockPart;
import tjp.machinist.api.multiblock.MultiblockControllerBase;
import tjp.machinist.api.multiblock.validation.ValidationError;
import tjp.machinist.items.ModItems;

import javax.crypto.Mac;

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
        super(Material.ROCK);
        setUnlocalizedName(Machinist.MODID + ".blastfurnacecasing");
        setRegistryName("blastfurnacecasing");
        setCreativeTab(ModItems.tabMachinistMod);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new BlastFurnaceCasingTE();
    }


    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
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
        ItemStack st = playerIn.getHeldItemMainhand();
        if (st.isEmpty()) {
            if (controller != null) {
                ValidationError status = controller.getLastError();
                if (null != status) {
                    playerIn.sendStatusMessage(status.getChatMessage(), false);
                    return true;
                }
            }

        }
       if(controller == null || !controller.isAssembled()) { return false; }

        if(!worldIn.isRemote) {
            playerIn.openGui(Machinist.instance, BlastFurnaceMultiController.GUI_ID, worldIn, pos.getX(), pos.getY() , pos.getZ());
        }
        return true;
    }





    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
