package one.tlph.machinist.inventory;


import java.util.Set;

import gnu.trove.set.hash.THashSet;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;



public class OreValidator {

	public Set<ResourceLocation> grps = new THashSet<>();

    public boolean validate(Item item) {
    	for(ResourceLocation grp : grps) {
    		if(ItemTags.getCollection().get(grp).contains(item))
    				return true;
    	}
    	return false;
    }

    public boolean addGroup(ResourceLocation id) {
    	if(!grps.contains(id)) {
    		grps.add(id);
    		return true;
    	}
    	else
    		return false;
    		
    }

}
