package one.tlph.machinist.data.datagen;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.util.Constants;
import one.tlph.machinist.util.SerializerHelper;

public class CrusherRecipeBuilder extends MachinistRecipeBuilder<CrusherRecipeBuilder> {
    private final Ingredient input;
    private final ItemStack output;
    private final ItemStack extraOutput;

    protected CrusherRecipeBuilder(Ingredient input, ItemStack output, ItemStack extraOutput) {
        super(serializer("crusher"));
        this.input = input;
        this.output = output;
        this.extraOutput = extraOutput;
    }

    public static CrusherRecipeBuilder crusher(Ingredient input, ItemStack output) {
        return new CrusherRecipeBuilder(input, output, ItemStack.EMPTY);
    }

    public static CrusherRecipeBuilder crusher(Ingredient input, ItemStack output, ItemStack extraOutput) {
        return new CrusherRecipeBuilder(input, output, extraOutput);
    }

    protected CrusherRecipeResult getResult(ResourceLocation id) {
        return new CrusherRecipeResult(id);
    }

    public class CrusherRecipeResult extends RecipeResult {

        public CrusherRecipeResult(ResourceLocation id) {
            super(id);
        }

        @Override
        public void serialize(JsonObject json) {
            json.add(Constants.JSON.INPUT, input.serialize());
            json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
            if(!extraOutput.isEmpty())
                json.add(Constants.JSON.EXTRA_OUTPUT, SerializerHelper.serializeItemStack(extraOutput));
        }
    }

}
