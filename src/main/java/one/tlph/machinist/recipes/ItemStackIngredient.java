package one.tlph.machinist.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.NBTIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class ItemStackIngredient implements InputIngredient<ItemStack> {

    public static ItemStackIngredient from(@Nonnull ItemStack stack) {
        return from(stack, stack.getCount());
    }

    public static ItemStackIngredient from(@Nonnull ItemStack stack, int count) {
        Ingredient ingredient = stack.hasTag() ? new NBTIngredient(stack) {} : Ingredient.fromStacks(stack);
        return from(ingredient, count);
    }
    public static ItemStackIngredient from(@Nonnull IItemProvider item) {
        return from(item, 1);
    }

    public static ItemStackIngredient from(@Nonnull IItemProvider item, int amount) {
        return from(new ItemStack(item), amount);
    }

    public static ItemStackIngredient from(@Nonnull ITag<Item> itemTag) {
        return from(itemTag, 1);
    }

    public static ItemStackIngredient from(@Nonnull ITag<Item> itemTag, int amount) {
        return from(Ingredient.fromTag(itemTag), amount);
    }

    public static ItemStackIngredient from(@Nonnull Ingredient ingredient) {
        return from(ingredient, 1);
    }

    public static ItemStackIngredient from(@Nonnull Ingredient ingredient, int amount) {
        return new Single(ingredient, amount);
    }

    public static ItemStackIngredient read(PacketBuffer buffer) {
        IngredientType type = buffer.readEnumValue(IngredientType.class);
        if(type == IngredientType.SINGLE)
            return Single.read(buffer);
        return Multi.read(buffer);
    }

    public static ItemStackIngredient deserialize(@Nullable JsonElement json) {
        if(json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Ingredient cannot be null");
        }
        if(json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            int size = jsonArray.size();
            if(size == 0) {
                throw new JsonSyntaxException("Ingredient array cannot be emtpy, at least 1 ingredient must be defined.");
            } else if(size > 1) {
                ItemStackIngredient[] ingredients = new ItemStackIngredient[size];
                for(int i = 0; i < size; i++) {
                    ingredients[i] = deserialize(jsonArray.get(i));
                }
                return createMulti(ingredients);
            }
            json = jsonArray.get(0);
        }
        if(!json.isJsonObject())
            throw new JsonSyntaxException("Expected item to be object or array of objects.");
        JsonObject jsonObject = json.getAsJsonObject();
        int amount = 1;
        if(jsonObject.has(JsonConstants.AMOUNT)) {
            JsonElement count = jsonObject.get(JsonConstants.AMOUNT);
            if(!JSONUtils.isNumber(count)) {
                throw new JsonSyntaxException("Expected amount to be a number that is one or larger.");
            }
            amount = count.getAsJsonPrimitive().getAsInt();
            if(amount < 1) {
                throw new JsonSyntaxException("Expected amount to be larger than or equal to 1");
            }
        }
        JsonElement jsonelement = JSONUtils.isJsonArray(jsonObject, JsonConstants.INGREDIENT) ? JSONUtils.getJsonArray(jsonObject, JsonConstants.INGREDIENT) :
                JSONUtils.getJsonObject(jsonObject, JsonConstants.INGREDIENT);
        Ingredient ingredient = Ingredient.deserialize(jsonelement);
        return from(ingredient, amount);
    }

    public static ItemStackIngredient createMulti(ItemStackIngredient... ingredients) {
        if (ingredients.length == 0) {
            //TODO: Throw error
        } else if (ingredients.length == 1) {
            return ingredients[0];
        }
        List<ItemStackIngredient> cleanedIngredients = new ArrayList<>();
        for (ItemStackIngredient ingredient : ingredients) {
            if (ingredient instanceof Multi) {
                //Don't worry about if our inner ingredients are multi as well, as if this is the only external method for
                // creating a multi ingredient, then we are certified they won't be of a higher depth
                cleanedIngredients.addAll(Arrays.asList(((Multi) ingredient).ingredients));
            } else {
                cleanedIngredients.add(ingredient);
            }
        }
        //There should be more than a single item or we would have split out earlier
        return new Multi(cleanedIngredients.toArray(new ItemStackIngredient[0]));
    }


    public static class Single extends ItemStackIngredient {
        @Nonnull
        private final Ingredient ingredient;
        private final int amount;

        public Single(@Nonnull Ingredient ingredient, int amount) {
            this.ingredient = Objects.requireNonNull(ingredient);
            this.amount = amount;
        }


        @Override
        public boolean test(@Nonnull ItemStack itemStack) {
            return testType(itemStack) && itemStack.getCount() >= amount;
        }

        @Override
        public boolean testType(@Nonnull ItemStack type) {
            return ingredient.test(type);
        }

        @Nonnull
        @Override
        public ItemStack getMatchingInstance(@Nonnull ItemStack type) {
            if(test(type)) {
                ItemStack matching = type.copy();
                matching.setCount(amount);
                return matching;
            }
            return ItemStack.EMPTY;
        }


        @Override
        public List<ItemStack> getRepresentations() {
            //TODO: Can this be cached some how
            List<ItemStack> representations = new ArrayList<>();
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                if (stack.getCount() == amount) {
                    representations.add(stack);
                } else {
                    ItemStack copy = stack.copy();
                    copy.setCount(amount);
                    representations.add(copy);
                }
            }
            return representations;
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeEnumValue(IngredientType.SINGLE);
            ingredient.write(buffer);
            buffer.writeVarInt(amount);
        }

        @Override
        public JsonElement serialize() {
            JsonObject json = new JsonObject();
            if(amount > 1) {
                json.addProperty(JsonConstants.AMOUNT, amount);
            }
            json.add(JsonConstants.INGREDIENT, ingredient.serialize());
            return json;
        }

        public static Single read(PacketBuffer buffer) {
            return new Single(Ingredient.read(buffer), buffer.readVarInt());
        }
    }

    public static class Multi extends ItemStackIngredient {

        private final ItemStackIngredient[] ingredients;
        protected Multi(@Nonnull ItemStackIngredient... ingredients) {
            this.ingredients = ingredients;
        }


        @Override
        public boolean testType(@Nonnull ItemStack type) {
            return Arrays.stream(ingredients).anyMatch(ingredient -> ingredient.testType(type));
        }

        @Nonnull
        @Override
        public ItemStack getMatchingInstance(@Nonnull  ItemStack type) {
            for (ItemStackIngredient ingredient : ingredients) {
                ItemStack matchingInstance = ingredient.getMatchingInstance(type);
                if (!matchingInstance.isEmpty()) {
                    return matchingInstance;
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public List<ItemStack> getRepresentations() {
            List<ItemStack> representations = new ArrayList<>();
            for (ItemStackIngredient ingredient : ingredients) {
                representations.addAll(ingredient.getRepresentations());
            }
            return representations;
        }

        @Override
        public void write(PacketBuffer buffer) {
            buffer.writeEnumValue(IngredientType.MULTI);
            buffer.writeVarInt(ingredients.length);
            for (ItemStackIngredient ingredient : ingredients) {
                ingredient.write(buffer);
            }
        }

        @Nonnull
        @Override
        public JsonElement serialize() {
            JsonArray json = new JsonArray();
            for (ItemStackIngredient ingredient : ingredients) {
                json.add(ingredient.serialize());
            }
            return json;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return Arrays.stream(ingredients).anyMatch(ingredient -> ingredient.test(itemStack));
        }
    }






    private enum IngredientType {
        SINGLE,
        MULTI
    }
}
