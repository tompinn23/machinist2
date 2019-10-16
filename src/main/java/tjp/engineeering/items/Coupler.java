package tjp.engineeering.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tjp.engineering.Engineering;

public class Coupler extends Item {
	
	public Coupler() {
		setRegistryName("coupler");
		setUnlocalizedName(Engineering.MODID + ".coupler");
		setCreativeTab(ModItems.tabEngineeringMod);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
