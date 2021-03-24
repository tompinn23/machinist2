package one.tlph.machinist.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.init.registries.ModRecipeSerializers;

import java.util.function.Predicate;

public class CrusherRecipe extends MachinistRecipe implements Predicate<ItemStack> {


    private final Ingredient input;
    private final ItemStack output;
    private final ItemStack extraOutput;
    private final float chance;

    public CrusherRecipe(ResourceLocation id, Ingredient input, ItemStack output, ItemStack extraOutput, float chance) {
        super(id);
        this.input = input;
        this.output = output.copy();
        this.extraOutput = extraOutput.copy();
        this.chance = chance;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return input.test(itemStack);
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() { return output.copy(); }
    public ItemStack getExtraOutput() { return extraOutput.copy(); }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(getInput());
        return list;
    }

    @Override
    public void write(PacketBuffer buffer) {
        input.write(buffer);
        buffer.writeItemStack(output);
        buffer.writeItemStack(extraOutput);
        buffer.writeFloat(chance);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRUSHER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return MachinistRecipeType.CRUSHER;
    }
}
