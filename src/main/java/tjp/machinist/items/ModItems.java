package tjp.machinist.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems {

	public static final CreativeTabs tabMachinistMod = (new CreativeTabs("tabMachinistMod") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(coupler);
		}
	});


	@GameRegistry.ObjectHolder("machinist:coupler")
	public static Coupler coupler;

	@GameRegistry.ObjectHolder("machinist:steel_ingot")
	public static BasicItem steelIngot;

	@GameRegistry.ObjectHolder("machinist:gold_dust")
	public static BasicItem goldDust;

	@GameRegistry.ObjectHolder("machinist:iron_dust")
	public static BasicItem ironDust;

	@GameRegistry.ObjectHolder("machinist:coal_dust")
	public static BasicItem coalDust;

	@GameRegistry.ObjectHolder("machinist:steel_dust")
	public static BasicItem steelDust;

	@GameRegistry.ObjectHolder("machinist:copper_dust")
	public  static BasicItem copperDust;

	@GameRegistry.ObjectHolder("machinist:copper_ingot")
	public static BasicItem copperIngot;

	public static void init() {
		OreDictionary.registerOre("dustCopper", copperDust);
		OreDictionary.registerOre("dustSteel", steelDust);
		OreDictionary.registerOre("dustCoal", coalDust);
		OreDictionary.registerOre("dustIron", ironDust);
		OreDictionary.registerOre("dustGold", goldDust);

		OreDictionary.registerOre("ingotSteel", steelIngot);
		OreDictionary.registerOre("ingotCopper", copperIngot);

	}


	@SideOnly(Side.CLIENT)
	public static void initModels() {
		coupler.initModel();
		steelIngot.initModel();
		goldDust.initModel();
		ironDust.initModel();
		coalDust.initModel();
		steelDust.initModel();
		copperDust.initModel();
		copperIngot.initModel();
	}



	
}
