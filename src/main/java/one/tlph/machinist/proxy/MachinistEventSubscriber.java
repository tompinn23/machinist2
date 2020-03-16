package one.tlph.machinist.proxy;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.registry.GameRegistry;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.ModBlocks;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceCasing;
import one.tlph.machinist.blocks.BlastFurnace.BlastFurnaceController;
import one.tlph.machinist.blocks.MachineFrame;
import one.tlph.machinist.blocks.crusher.Crusher;
import one.tlph.machinist.blocks.smelter.Smelter;
import one.tlph.machinist.items.BasicItem;
import one.tlph.machinist.items.Coupler;
import one.tlph.machinist.items.ModItems;
import one.tlph.machinist.tileentity.BlastFurnaceCasingTileEntity;
import one.tlph.machinist.tileentity.BlastFurnaceControllerTileEntity;
import one.tlph.machinist.tileentity.CrusherTileEntity;
import one.tlph.machinist.tileentity.SmelterTileEntity;

import javax.rmi.CORBA.Tie;
import java.util.Objects;

@EventBusSubscriber(modid = Machinist.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MachinistEventSubscriber {

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        Item.Properties properties = new Item.Properties().group(Machinist.setup.itemGroup);
        event.getRegistry().registerAll(new BlockItem(ModBlocks.machineFrame, properties).setRegistryName(ModBlocks.machineFrame.getRegistryName()),
                new BlockItem(ModBlocks.smelter, properties).setRegistryName(ModBlocks.smelter.getRegistryName()),
                new BlockItem(ModBlocks.crusher, properties).setRegistryName(ModBlocks.crusher.getRegistryName()),
                new BlockItem(ModBlocks.blastCasing, properties).setRegistryName(ModBlocks.blastCasing.getRegistryName()),
                new BlockItem(ModBlocks.blastController, properties).setRegistryName(ModBlocks.blastController.getRegistryName()));


        event.getRegistry().register(new Coupler());
        BasicItem.InitBasicItems(event);
    }

    @SubscribeEvent
    public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(new MachineFrame(),
                new Smelter(),
                new Crusher(),
                new BlastFurnaceCasing(),
                new BlastFurnaceController()
        );
    }

    @SubscribeEvent
    public static void onRegisterTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(SmelterTileEntity::new, ModBlocks.smelter).build(null).setRegistryName(Machinist.MODID, "_smelter"));
        event.getRegistry().register(TileEntityType.Builder.create(CrusherTileEntity::new, ModBlocks.crusher).build(null).setRegistryName(Machinist.MODID, "_crusher"));
        event.getRegistry().register(TileEntityType.Builder.create(BlastFurnaceCasingTileEntity::new, ModBlocks.blastCasing).build(null).setRegistryName(Machinist.MODID, "_blastfurnacecasing"));
        event.getRegistry().register(TileEntityType.Builder.create(BlastFurnaceControllerTileEntity::new, ModBlocks.blastController).build(null).setRegistryName(Machinist.MODID, "_blastfurnacecontroller"));
    }



    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
        ModBlocks.initModels();
        ModItems.initModels();
    }


}
