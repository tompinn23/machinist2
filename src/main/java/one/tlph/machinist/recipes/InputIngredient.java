package one.tlph.machinist.recipes;

import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public interface InputIngredient<T>  extends Predicate<T> {
    boolean testType(@Nonnull T type);

    T getMatchingInstance(T type);

    @Nonnull
    List<T> getRepresentations();

    void write(PacketBuffer buffer);

    @Nonnull
    JsonElement serialize();

}
