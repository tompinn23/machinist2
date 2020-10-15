package one.tlph.machinist.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.recipes.BlastFurnaceRecipe;
import one.tlph.machinist.recipes.BlastFurnaceRecipeSerializer;

public class ModRecipeSerializers {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Machinist.MODID);

    public static final RegistryObject<IRecipeSerializer<BlastFurnaceRecipe>> BLAST_FURNACE = RECIPE_SERIALIZERS.register("blast_furnace", () -> new BlastFurnaceRecipeSerializer<>(BlastFurnaceRecipe::new));
}
