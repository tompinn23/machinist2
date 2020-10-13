package one.tlph.machinist.inventory;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

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
        DEFAULT_VALIDATOR.addGroup(new ResourceLocation("forge", BLOCK));
        DEFAULT_VALIDATOR.addGroup(new ResourceLocation("forge", ORE));
        DEFAULT_VALIDATOR.addGroup(new ResourceLocation("forge", DUST));
        DEFAULT_VALIDATOR.addGroup(new ResourceLocation("forge", INGOT));
        DEFAULT_VALIDATOR.addGroup(new ResourceLocation("forge", NUGGET));
        DEFAULT_VALIDATOR.addGroup(new ResourceLocation("forge", GEM));
        DEFAULT_VALIDATOR.addGroup(new ResourceLocation("forge", PLATE));
    }


    public Item item = Items.AIR;
    public int metadata = -1;
    public int stackSize = -1;

    public ResourceLocation oreID = null;
    public String oreName = "Unknown";


    public ComparableItemStack(ItemStack stack) {
        this.item = stack.getItem();
        this.metadata = stack.getDamage();
        if(!stack.isEmpty()) {
            this.stackSize = stack.getCount();

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
        List<ResourceLocation> tags = (List<ResourceLocation>)ItemTags.getCollection().getOwningTags(item);
        if(tags.size() != 0)
            this.oreID = tags.get(0);
        return other != null && (oreID != null && oreID == other.oreID || isEqual(other));
    }

    public boolean isStackEqual(ComparableItemStack other) {
        return isItemEqual(other) && stackSize == other.stackSize;
    }


    public ItemStack toItemStack() {
        return item != Items.AIR ? new ItemStack(item, stackSize) : ItemStack.EMPTY;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ComparableItemStack && isItemEqual((ComparableItemStack)obj);
    }

    @Override
    public int hashCode() {
        List<ResourceLocation> tags = (List<ResourceLocation>)ItemTags.getCollection().getOwningTags(item);
        if(tags.size() != 0)
            this.oreID = tags.get(0);
        return oreID != null ? (oreID.toString() + "/" + item.getName().getUnformattedComponentText()).hashCode() : (metadata & 65535) | getId() << 16;
    }

    public int getId() {
        return Item.getIdFromItem(item);
    }
}
