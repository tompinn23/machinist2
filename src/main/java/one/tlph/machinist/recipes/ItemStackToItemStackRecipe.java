package one.tlph.machinist.recipes;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ItemStackToItemStackRecipe extends MachinistRecipe implements Predicate<ItemStack> {

    private final ItemStackIngredient input;
    private final ItemStack output;

    public ItemStackToItemStackRecipe(ResourceLocation id, ItemStackIngredient input, ItemStack output) {
        super(id);
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return this.input.test(itemStack);
    }

    public ItemStackIngredient getInput() {
        return input;
    }

    public ItemStack getOutput(ItemStack input) {
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
        input.write(buffer);
        buffer.writeItemStack(output);
    }
}
