package one.tlph.machinist.util;

import javax.annotation.Nonnull;

public class ModUtils {

    @Nonnull
    // Get rid of "Returning null from Nonnull method" warnings
    @SuppressWarnings("ConstantConditions")
    public static <T> T _null() {
        return null;
    }
}
