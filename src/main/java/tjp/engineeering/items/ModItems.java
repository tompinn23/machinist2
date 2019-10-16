package tjp.engineeering.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	public static final CreativeTabs tabEngineeringMod = (new CreativeTabs("tabTpEngineeringMod") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(coupler);
		}
	});


	@GameRegistry.ObjectHolder("engineering:coupler")
	public static Coupler coupler;

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		coupler.initModel();
	}
	
}
