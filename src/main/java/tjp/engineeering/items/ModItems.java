package tjp.engineeering.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
	@GameRegistry.ObjectHolder("engineering:coupler")
	public static Coupler coupler;

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		coupler.initModel();
	}
	
}
