package one.tlph.machinist.proxy;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.container.CrusherContainer;

public class ModContainerTypes {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Machinist.MODID);

    public static final RegistryObject<ContainerType<CrusherContainer>> CRUSHER = CONTAINER_TYPES.register("crusher", () -> IForgeContainerType.create(CrusherContainer::new));
}
