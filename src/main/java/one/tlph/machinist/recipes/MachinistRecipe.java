package one.tlph.machinist.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import one.tlph.machinist.inventory.IgnoredIInventory;

public abstract class MachinistRecipe implements IRecipe<IgnoredIInventory> {
    private final ResourceLocation id;

    protected MachinistRecipe(ResourceLocation id) {
        this.id = id;
    }

    public abstract void write(PacketBuffer buffer);

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean matches(IgnoredIInventory inv, World worldIn) {
        return true;
    }

    @Override
    public ItemStack getCraftingResult(IgnoredIInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
