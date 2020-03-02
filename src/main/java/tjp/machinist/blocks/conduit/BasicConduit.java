package tjp.machinist.blocks.conduit;


import javafx.scene.chart.Axis;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tjp.machinist.Machinist;
import tjp.machinist.ModBlocks;
import tjp.machinist.items.ModItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

public class BasicConduit extends Block {

    public static final PropertyBool CONNECTION_NORTH = PropertyBool.create("connection_n");
    public static final PropertyBool CONNECTION_EAST = PropertyBool.create("connection_e");
    public static final PropertyBool CONNECTION_SOUTH = PropertyBool.create("connection_s");
    public static final PropertyBool CONNECTION_WEST = PropertyBool.create("connection_w");
    public static final PropertyBool CONNECTION_UP = PropertyBool.create("connection_u");
    public static final PropertyBool CONNECTION_DOWN = PropertyBool.create("connection_d");






    public BasicConduit() {
        super(Material.CIRCUITS);
        setUnlocalizedName(Machinist.MODID + ".basicconduit");
        setRegistryName("basicconduit");
        setCreativeTab(ModItems.tabMachinistMod);
        setDefaultState(blockState.getBaseState().withProperty(CONNECTION_NORTH, false)
                                                 .withProperty(CONNECTION_EAST, false)
                                                 .withProperty(CONNECTION_SOUTH, false)
                                                 .withProperty(CONNECTION_WEST,false)
                                                 .withProperty(CONNECTION_UP, false)
                                                 .withProperty(CONNECTION_DOWN, false));
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return  false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }



    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state , IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);
        double minX = 0.3125D;
        double minY = 0.3125D;
        double minZ = 0.3125D;
        double maxX = 0.6875D;
        double maxY = 0.6875D;
        double maxZ = 0.6875D;
        if(state.getValue(CONNECTION_DOWN))
            minY = Math.min(minY, 0D);
        if(state.getValue(CONNECTION_UP))
            maxY = Math.max(maxY, 1D);
        if(state.getValue(CONNECTION_SOUTH))
            maxZ = Math.max(maxZ, 1D);
        if(state.getValue(CONNECTION_NORTH))
            minZ = Math.min(minZ, 0D);
        if(state.getValue(CONNECTION_EAST))
            maxX = Math.max(maxX, 1D);
        if(state.getValue(CONNECTION_WEST))
            minX = Math.min(minX, 0D);

        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean north = false;
        boolean east = false;
        boolean south = false;
        boolean west = false;
        boolean up = false;
        boolean down = false;

        if(worldIn.getBlockState(pos.north()).getBlock() == ModBlocks.basicConduit) {
            north = true;
        }
        if(worldIn.getBlockState(pos.east()).getBlock() == ModBlocks.basicConduit) {
            east = true;
        }
        if(worldIn.getBlockState(pos.south()).getBlock() == ModBlocks.basicConduit) {
            south = true;
        }
        if(worldIn.getBlockState(pos.west()).getBlock() == ModBlocks.basicConduit) {
            west = true;
        }
        if(worldIn.getBlockState(pos.up()).getBlock() == ModBlocks.basicConduit) {
            up = true;
        }
        if(worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.basicConduit) {
            down = true;
        }
        return state.withProperty(CONNECTION_NORTH, north).withProperty(CONNECTION_EAST, east).withProperty(CONNECTION_SOUTH, south).withProperty(CONNECTION_WEST, west).withProperty(CONNECTION_UP, up).withProperty(CONNECTION_DOWN, down);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return  new BlockStateContainer(this, CONNECTION_NORTH, CONNECTION_EAST, CONNECTION_SOUTH, CONNECTION_WEST, CONNECTION_UP, CONNECTION_DOWN);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
