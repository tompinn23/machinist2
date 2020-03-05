package tjp.machinist.inventory;

import net.minecraft.item.ItemStack;
import tjp.machinist.util.OreDictionaryHelper;

import java.util.List;

public class ComparableItemStackValidated extends ComparableItemStack {
    private final OreValidator validator;

    public ComparableItemStackValidated(ItemStack stack, OreValidator validator) {
        super(stack);
        this.validator = validator;
        this.oreID = getOreID(stack);
        this.oreName = OreDictionaryHelper.getOreName(oreID);
    }

    public ComparableItemStackValidated(ItemStack stack) {
        super(stack);
        this.validator = DEFAULT_VALIDATOR;
        this.oreID = getOreID(stack);
        this.oreName = OreDictionaryHelper.getOreName(oreID);
    }

    public int getOreID(ItemStack stack) {
        List<Integer> ids = OreDictionaryHelper.getAllOreIDs(stack);
        if(!ids.isEmpty()) {
            for(Integer id : ids) {
                if(id != -1 && validator.validate(OreDictionaryHelper.getOreName(id)))
                    return id;
            }
        }
        return -1;
    }

    public int getOreID(String oreName) {
        if(!validator.validate(oreName))
            return -1;
        return OreDictionaryHelper.getOreID(oreName);
    }
}
