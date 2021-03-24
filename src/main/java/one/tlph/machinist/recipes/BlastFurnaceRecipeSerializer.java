package one.tlph.machinist.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.util.Constants;
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
        JsonElement mainInput = JSONUtils.isJsonArray(json, Constants.JSON.MAIN_INPUT) ? JSONUtils.getJsonArray(json, Constants.JSON.MAIN_INPUT) :
                JSONUtils.getJsonObject(json, Constants.JSON.MAIN_INPUT);
        Ingredient mainIngredient = Ingredient.deserialize(mainInput);
        Ingredient extraIngredient = Ingredient.fromStacks(ItemStack.EMPTY);
        if(json.has(Constants.JSON.EXTRA_INPUT)) {
            JsonElement extraInput = JSONUtils.isJsonArray(json, Constants.JSON.EXTRA_INPUT) ? JSONUtils.getJsonArray(json, Constants.JSON.EXTRA_INPUT) :
                    JSONUtils.getJsonObject(json, Constants.JSON.EXTRA_INPUT);
           extraIngredient = Ingredient.deserialize(extraInput);
        }
        ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);
        if(output.isEmpty()) {
            throw new JsonSyntaxException("Blast furnace recipe output must not be empty.");
        }
        int time = BlastFurnaceRecipe.DEFAULT_TIME;
        if(json.has(Constants.JSON.TIME)) {
            time = json.get(Constants.JSON.TIME).getAsInt();
        }
        Machinist.logger.info("Found blast furnace recipe");
        return this.factory.create(recipeId, mainIngredient, extraIngredient, time, output);
    }

    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        try {
            Ingredient mainInput = Ingredient.read(buffer);
            Ingredient extraInput = Ingredient.read(buffer);
            ItemStack output = buffer.readItemStack();
            int time = buffer.readVarInt();
            return this.factory.create(recipeId, mainInput, extraInput, time, output);
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
        T create(ResourceLocation id, Ingredient mainInput, Ingredient extraInput, int time, ItemStack output);
    }
}
