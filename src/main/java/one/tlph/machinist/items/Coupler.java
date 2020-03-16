package one.tlph.machinist.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import one.tlph.machinist.Machinist;

public class Coupler extends Item {
	
	public Coupler() {
		setRegistryName("coupler");
		setUnlocalizedName(Machinist.MODID + ".coupler");
		setCreativeTab(ModItems.tabMachinistMod);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
