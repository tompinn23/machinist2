package tjp.machinist.blocks.smelter;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import tjp.machinist.Machinist;
import tjp.machinist.items.ModItems;
import tjp.machinist.tileentity.SmelterTileEntity;

import javax.annotation.Nullable;

public class Smelter extends Block {
    
	public static final int GUI_ID = 1;
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public Smelter() {
        super(Material.ROCK);
        setUnlocalizedName(Machinist.MODID + ".smelter");
        setRegistryName("smelter");
        setCreativeTab(ModItems.tabMachinistMod);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));

    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));

    }

    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
        return EnumFacing.getFacingFromVector(
                (float) (entity.posX - clickedBlock.getX()),
                0f,
                (float) (entity.posZ - clickedBlock.getZ()));
    }


    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, getFacingFromEntity(pos, placer));
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState  state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof SmelterTileEntity) {
            ((SmelterTileEntity)te).setFacing(placer.getHorizontalFacing().getOpposite());
        }
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        EnumFacing facing = EnumFacing.NORTH;
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if(tileentity instanceof SmelterTileEntity) {
            facing = ((SmelterTileEntity) tileentity).getFacing();
        }
        return state.withProperty(FACING, facing);
    }



    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }

    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState state) {
    	return new SmelterTileEntity(state.getValue(FACING));
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        //Only execute on server.
    	if(worldIn.isRemote) {
    		return true;
    	}
    	
    	TileEntity te = worldIn.getTileEntity(pos);
    	if(!(te instanceof SmelterTileEntity)) {
    		return false;
    	}
    	
    	playerIn.openGui(Machinist.instance, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
    	return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	TileEntity te = worldIn.getTileEntity(pos);
    	if(te instanceof SmelterTileEntity) {
    		IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    		for(int i = 0; i < inventory.getSlots(); i++) {
    			ItemStack stack = inventory.getStackInSlot(i);
    			EntityItem entityIn;
    			if(stack != null) {
    				entityIn = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
    				entityIn.setDefaultPickupDelay();
    				worldIn.spawnEntity(entityIn);
    			}
    		}
    	}
    	super.breakBlock(worldIn, pos, state);
    }

    
}

