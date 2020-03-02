package tjp.machinist.blocks.BlastFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tjp.machinist.Machinist;
import tjp.machinist.api.multiblock.IMultiblockPart;
import tjp.machinist.api.multiblock.MultiblockControllerBase;
import tjp.machinist.api.multiblock.validation.ValidationError;
import tjp.machinist.items.ModItems;
import tjp.machinist.tileentity.BlastFurnaceControllerTileEntity;
import tjp.machinist.tileentity.BlastFurnaceMultiControllerTileEntity;

import javax.annotation.Nullable;

public class BlastFurnaceController extends Block {


    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlastFurnaceController() {
        super(Material.ROCK);
        setUnlocalizedName(Machinist.MODID + ".blastfurnacecontroller");
        setRegistryName("blastfurnacecontroller");
        setCreativeTab(ModItems.tabMachinistMod);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        EnumFacing facing = EnumFacing.NORTH;
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if(tileentity instanceof BlastFurnaceControllerTileEntity) {
            facing = ((BlastFurnaceControllerTileEntity) tileentity).getFacing();
        }
        return state.withProperty(FACING, facing);
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
        return EnumFacing.getFacingFromVector(
                (float) (entity.posX - clickedBlock.getX()),
                0f,
                (float) (entity.posZ - clickedBlock.getZ()));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof BlastFurnaceControllerTileEntity) {
            ((BlastFurnaceControllerTileEntity)te).setFacing(placer.getHorizontalFacing().getOpposite());
        }
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));

    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState state) {
        return new BlastFurnaceControllerTileEntity(state.getValue(FACING));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
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
                    playerIn.openGui(Machinist.instance, BlastFurnaceMultiControllerTileEntity.GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
