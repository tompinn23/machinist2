package one.tlph.machinist.blocks.crusher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import one.tlph.machinist.blocks.AbstractHorizontalBlock;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.inventory.Inventory;
import one.tlph.machinist.tileentity.AbstractTileEntity;

public class Crusher extends AbstractHorizontalBlock {
	
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public Crusher() {
    	super(Block.Properties.create(Material.ROCK));
    	this.setDefaultState(this.getDefaultState().with(ACTIVE, false));
    }

    
    @Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
    	builder.add(ACTIVE);
	}


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrusherTileEntity();
    }



    @Nullable
    @Override
    public Container getContainer(int id, PlayerInventory playerInventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof CrusherTileEntity) {
            return new CrusherContainer(id, playerInventory, (CrusherTileEntity) te);
        }
        return null;
    }
}
