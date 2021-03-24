package one.tlph.machinist.init.registries;

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

import javax.swing.*;


public class ModContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Machinist.MODID);

    public static final RegistryObject<ContainerType<BlastFurnaceMultiContainer>> BLAST_FURNACE = CONTAINER_TYPES.register("blast_furnace", () -> IForgeContainerType.create(BlastFurnaceMultiContainer::new));

    public static final RegistryObject<ContainerType<CrusherContainer>> CRUSHER = CONTAINER_TYPES.register("crusher", () -> IForgeContainerType.create(CrusherContainer::new));
    
    public static final RegistryObject<ContainerType<SmelterContainer>> SMELTER = CONTAINER_TYPES.register("smelter", () -> IForgeContainerType.create(SmelterContainer::new));
}
