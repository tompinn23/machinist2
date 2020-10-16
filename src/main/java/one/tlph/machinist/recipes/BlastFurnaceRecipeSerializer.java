package one.tlph.machinist.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.util.SerializerHelper;

import javax.annotation.Nonnull;

public class BlastFurnaceRecipeSerializer<T extends BlastFurnaceRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private final IFactory<T> factory;

    public BlastFurnaceRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }


    @Nonnull
    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        JsonElement mainInput = JSONUtils.isJsonArray(json, JsonConstants.MAIN_INPUT) ? JSONUtils.getJsonArray(json, JsonConstants.MAIN_INPUT) :
                JSONUtils.getJsonObject(json, JsonConstants.MAIN_INPUT);
        ItemStackIngredient mainIngredient = ItemStackIngredient.deserialize(mainInput);
        ItemStackIngredient extraIngredient = ItemStackIngredient.from(ItemStack.EMPTY);
        if(json.has(JsonConstants.EXTRA_INPUT)) {
            JsonElement extraInput = JSONUtils.isJsonArray(json, JsonConstants.EXTRA_INPUT) ? JSONUtils.getJsonArray(json, JsonConstants.EXTRA_INPUT) :
                    JSONUtils.getJsonObject(json, JsonConstants.EXTRA_INPUT);
           extraIngredient = ItemStackIngredient.deserialize(extraInput);
        }
        ItemStack output = SerializerHelper.getItemStack(json, JsonConstants.OUTPUT);
        if(output.isEmpty()) {
            throw new JsonSyntaxException("Combiner recipe output must not be empty.");
        }
        if(J)
        Machinist.logger.info("Found blast furnace recipe");
        return this.factory.create(recipeId, mainIngredient, extraIngredient, output);
    }

    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        try {
            ItemStackIngredient mainInput = ItemStackIngredient.read(buffer);
            ItemStackIngredient extraInput = ItemStackIngredient.read(buffer);
            ItemStack output = buffer.readItemStack();
            return this.factory.create(recipeId, mainInput, extraInput, output);
        } catch (Exception e) {
            Machinist.logger.error("Error reading blast furnace recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Machinist.logger.error("Error writing blast furnace recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<T extends BlastFurnaceRecipe> {
        T create(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient extraInput, ItemStack output);
    }
}
