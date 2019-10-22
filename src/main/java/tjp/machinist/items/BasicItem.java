package tjp.machinist.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import tjp.machinist.Machinist;

public class BasicItem extends Item {

    /*
        public GoldDust() {
        setRegistryName("gold_dust");
        setUnlocalizedName(Machinist.MODID + ".goldDust");
        setCreativeTab(ModItems.tabMachinistMod);
    }
     */



    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public static void InitBasicItems(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> registry = evt.getRegistry();
        registry.register(new BasicItem().setRegistryName("steel_dust").setUnlocalizedName(Machinist.MODID + ".steelDust").setCreativeTab(ModItems.tabMachinistMod));
        registry.register(new BasicItem().setRegistryName("coal_dust").setUnlocalizedName(Machinist.MODID + ".coalDust").setCreativeTab(ModItems.tabMachinistMod));
        registry.register(new BasicItem().setRegistryName("copper_dust").setUnlocalizedName(Machinist.MODID + ".copperDust").setCreativeTab(ModItems.tabMachinistMod));
        registry.register(new BasicItem().setRegistryName("copper_ingot").setUnlocalizedName(Machinist.MODID + ".copperIngot").setCreativeTab(ModItems.tabMachinistMod));
    }
}
