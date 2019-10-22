package tjp.machinist.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tjp.machinist.Machinist;

public class GoldDust extends Item {

    public GoldDust() {
        setRegistryName("gold_dust");
        setUnlocalizedName(Machinist.MODID + ".goldDust");
        setCreativeTab(ModItems.tabMachinistMod);
    }


    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
