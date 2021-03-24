package one.tlph.machinist.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.init.registries.ModItems;

import java.util.function.Supplier;

public class ModItemGroup extends ItemGroup {

    public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(Machinist.MODID, () -> new ItemStack(ModItems.COUPLER.get()));

    private final Supplier<ItemStack> iconSupplier;

    public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier) {
        super(name);
        this.iconSupplier = iconSupplier;
    }

    @Override
    public ItemStack createIcon() {
        return iconSupplier.get();
    }
}
