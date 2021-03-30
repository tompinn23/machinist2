package one.tlph.machinist.init.registries;

import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PointOfInterest {

    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, "machinist");

    public static final RegistryObject<PointOfInterestType> UNDER_NETHER_PORTAL = POI.register("under_nether_portal", () -> new PointOfInterestType("under_nether_portal", PointOfInterestType.getAllStates(ModBlocks.PORTAL_BLOCK.get()), 0, 1));

}
