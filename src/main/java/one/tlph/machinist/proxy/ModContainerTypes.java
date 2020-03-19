package one.tlph.machinist.proxy;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.container.BlastFurnaceMultiContainer;
import one.tlph.machinist.container.CrusherContainer;
import one.tlph.machinist.container.SmelterContainer;
import one.tlph.machinist.util.ModUtils;


@ObjectHolder(Machinist.MODID)
public class ModContainerTypes {
   // public static final DeferredRegister<ContainerType<?> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Machinist.MODID);

	public static final ContainerType<BlastFurnaceMultiContainer> BLAST_FURNACE = ModUtils._null();
	
    public static final ContainerType<CrusherContainer> CRUSHER = ModUtils._null();
    
    public static final ContainerType<SmelterContainer> SMELTER = ModUtils._null();
}
