package one.tlph.machinist.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ComparableItemStackValidated extends ComparableItemStack {
    private final OreValidator validator;

    public ComparableItemStackValidated(ItemStack stack, OreValidator validator) {
        super(stack);
        this.validator = validator;
        this.oreID = getOreID(stack);
    }

    public ComparableItemStackValidated(ItemStack stack) {
        super(stack);
        this.validator = DEFAULT_VALIDATOR;
        this.oreID = getOreID(stack);
        //this.oreName = OreDictionaryHelper.getOreName(oreID);
    }

    public ResourceLocation getOreID(ItemStack stack) {
        if(stack.isEmpty())
            return null;
        List<ResourceLocation> ids = ((List<ResourceLocation>)ItemTags.getCollection().getOwningTags(stack.getItem()));
        if(!ids.isEmpty()) {
            for(ResourceLocation id : ids) {
                if(id != null && validator.validate(stack.getItem()));
                    return id;
            }
        }
        return null;
    }
}
