package one.tlph.machinist.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import one.tlph.machinist.Machinist;
import one.tlph.machinist.integration.jei.crusher.CrusherRecipeCategory;
import one.tlph.machinist.recipes.MachinistRecipeType;

import java.util.Objects;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static IJeiHelpers jeiHelper;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Machinist.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new CrusherRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
        registration.addRecipes(world.getRecipeManager().getRecipesForType(MachinistRecipeType.CRUSHER), CrusherRecipeCategory.UID);
    }
}
