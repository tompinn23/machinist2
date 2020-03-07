package tjp.machinist.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tjp.machinist.util.OreDictionaryHelper;

public class ComparableItemStack {

    public static final OreValidator DEFAULT_VALIDATOR = new OreValidator();

    public static final String BLOCK = "block";
    public static final String ORE = "ore";
    public static final String DUST = "dust";
    public static final String INGOT = "ingot";
    public static final String NUGGET = "nugget";
    public static final String GEM = "gem";
    public static final String PLATE = "plate";

    static {
        DEFAULT_VALIDATOR.addPrefix(BLOCK);
        DEFAULT_VALIDATOR.addPrefix(ORE);
        DEFAULT_VALIDATOR.addPrefix(DUST);
        DEFAULT_VALIDATOR.addPrefix(INGOT);
        DEFAULT_VALIDATOR.addPrefix(NUGGET);
        DEFAULT_VALIDATOR.addPrefix(GEM);
        DEFAULT_VALIDATOR.addPrefix(PLATE);
    }


    public Item item = Items.AIR;
    public int metadata = -1;
    public int stackSize = -1;

    public int oreID = -1;
    public String oreName = "Unknown";


    public ComparableItemStack(ItemStack stack) {
        this.item = stack.getItem();
        this.metadata = stack.getItemDamage();
        if(!stack.isEmpty()) {
            this.stackSize = stack.getCount();
            this.oreID = OreDictionaryHelper.getOreID(OreDictionaryHelper.getOreName(stack));
            this.oreName = OreDictionaryHelper.getOreName(stack);
        }
    }

    public boolean isEqual(ComparableItemStack other) {
        if(other == null)
            return false;
        if(metadata == other.metadata) {
            if(item == other.item)
                return true;
            if(item != null && other.item != null) {
                return item.delegate.get() == other.item.delegate.get();
            }
        }
        return false;
    }

    public boolean isItemEqual(ComparableItemStack other) {
        return other != null && (oreID != -1 && oreID == other.oreID || isEqual(other));
    }

    public boolean isStackEqual(ComparableItemStack other) {
        return isItemEqual(other) && stackSize == other.stackSize;
    }


    public ItemStack toItemStack() {
        return item != Items.AIR ? new ItemStack(item, stackSize, metadata) : ItemStack.EMPTY;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ComparableItemStack && isItemEqual((ComparableItemStack)obj);
    }

    @Override
    public int hashCode() {
        return oreID != -1 ? oreName.hashCode() : (metadata & 65535) | getId() << 16;
    }

    public int getId() {
        return Item.getIdFromItem(item);
    }
}
