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

import javax.annotation.Nullable;

public class CrusherRecipeSerializer<T extends CrusherRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private final IFactory<T> factory;

    public CrusherRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        JsonElement mainInput = JSONUtils.getJsonObject(json, Constants.JSON.INPUT);
        Ingredient input = Ingredient.deserialize(mainInput.getAsJsonObject());

        ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);
        if(output.isEmpty()) {
            throw new JsonSyntaxException("Crusher output must be valid.");
        }
        float chance = 1.0f;
        ItemStack extraOutput = ItemStack.EMPTY;
        if(json.has(Constants.JSON.CHANCE)) {
            chance = json.get(Constants.JSON.CHANCE).getAsFloat();
        }
        if(json.has(Constants.JSON.EXTRA_OUTPUT)) {
            extraOutput = SerializerHelper.getItemStack(json, Constants.JSON.EXTRA_OUTPUT);
        }
        return this.factory.create(recipeId, input, output, extraOutput, chance);
    }

    @Nullable
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        try {
            Ingredient input = Ingredient.read(buffer);
            ItemStack output = buffer.readItemStack();
            ItemStack extraOutput = buffer.readItemStack();
            float chance = buffer.readFloat();
            return this.factory.create(recipeId, input, output, extraOutput, chance);
        } catch (Exception e) {
            Machinist.logger.error("Error reading crusher recipe from packet.");
            throw e;
        }
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Machinist.logger.error("Error writing crusher recipe to packet.");
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<T extends CrusherRecipe> {
        T create(ResourceLocation id, Ingredient input, ItemStack output, ItemStack extraOutput, float chance);
    }

}
