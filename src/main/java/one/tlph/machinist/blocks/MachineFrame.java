package one.tlph.machinist.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.items.ModItems;

public class MachineFrame extends Block {
    public MachineFrame() {
	super(Properties.create(Material.ROCK));
        setUnlocalizedName(Machinist.MODID + ".machineframe");
        setRegistryName("machineframe");
        setCreativeTab(ModItems.tabMachinistMod);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return false;
    }
}
