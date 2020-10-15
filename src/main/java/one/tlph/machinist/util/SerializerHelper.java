package one.tlph.machinist.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.JSONUtils;

import javax.annotation.Nonnull;

public class SerializerHelper {

    private static void validateKey(@Nonnull JsonObject json, @Nonnull String key) {
        if (!json.has(key)) {
            throw new JsonSyntaxException("Missing '" + key + "', expected to find an object");
        }
        if (!json.get(key).isJsonObject()) {
            throw new JsonSyntaxException("Expected '" + key + "' to be an object");
        }
    }

    public static ItemStack getItemStack(@Nonnull JsonObject json, @Nonnull String key) {
        validateKey(json, key);
        return ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, key));
    }
}
