package one.tlph.machinist.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public class BlastFurnaceRecipe extends MachinistRecipe implements BiPredicate<ItemStack, ItemStack> {

    private final ItemStackIngredient mainInput;
    private final ItemStackIngredient extraInput;
    private final ItemStack output;

    public BlastFurnaceRecipe(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient extraInput, ItemStack output) {
        super(id);
        this.mainInput = mainInput;
        this.extraInput = extraInput;
        this.output = output.copy();
    }

    @Override
    public boolean test(ItemStack input, ItemStack extra) {
        return mainInput.test(input) && extraInput.test(extra);
    }

    public ItemStackIngredient getMainInput() {
        return mainInput;
    }

    public ItemStackIngredient getExtraInput() {
        return extraInput;
    }

    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack extra) {
        return output.copy();
    }


    /**
     * For JEI, gets a display stack
     *
     * @return Representation of output, MUST NOT be modified
     */
    public List<ItemStack> getOutputDefinition() {
        return output.isEmpty() ? Collections.emptyList() : Collections.singletonList(output);
    }

    @Override
    public void write(PacketBuffer buffer) {
        mainInput.write(buffer);
        extraInput.write(buffer);
        buffer.writeItemStack(output);
    }

    @Nonnull
    @Override
    public IRecipeType<BlastFurnaceRecipe> getType() {
        return MachinistRecipeType.BLAST_FURNACE;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

}
