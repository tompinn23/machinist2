package one.tlph.machinist.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.items.BasicItem;
import one.tlph.machinist.items.Coupler;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Machinist.MODID);

	public static final RegistryObject<Item> COUPLER = ITEMS.register("coupler", () -> new Coupler(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));
	public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));
	public static final RegistryObject<Item> GOLD_DUST = ITEMS.register("gold_dust", () -> new Item(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));
	public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", () -> new Item(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));
	public static final RegistryObject<Item> COAL_DUST = ITEMS.register("coal_dust", () -> new Item(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));
	public static final RegistryObject<Item> STEEL_DUST = ITEMS.register("steel_dust", () -> new Item(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));
	public static final RegistryObject<Item> COPPER_DUST = ITEMS.register("copper_dust", () -> new Item(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));
	public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)));

	public static void init() {
//		OreDictionary.registerOre("dustCopper", copperDust);
//		OreDictionary.registerOre("dustSteel", steelDust);
//		OreDictionary.registerOre("dustCoal", coalDust);
//		OreDictionary.registerOre("dustIron", ironDust);
//		OreDictionary.registerOre("dustGold", goldDust);
//
//		OreDictionary.registerOre("ingotSteel", steelIngot);
//		OreDictionary.registerOre("ingotCopper", copperIngot);
//
//		Item
		
	}



	
}
