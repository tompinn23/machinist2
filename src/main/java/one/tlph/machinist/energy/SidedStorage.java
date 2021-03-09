package one.tlph.machinist.energy;

import net.minecraft.client.renderer.SpriteAwareVertexBuilder;
import net.minecraft.util.Direction;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.stream.Collectors;

public class SidedStorage<T> {

    private final T nullValue;
    private final EnumMap<Direction, T> sideMap;

    public SidedStorage(T nullValue, EnumMap<Direction, T> sideVals) {
        this.nullValue = nullValue;
        this.sideMap = sideVals;
    }

    public static <T> SidedStorage<T> create(SidedSupplier<T> supplier) {
        return new SidedStorage<T>(supplier.get(null),
                Arrays.stream(Direction.values())
                        .collect(Collectors.toMap(side -> side,
                                side -> supplier.get((Direction)side),
                                (a, b) -> b,
                                () -> new EnumMap<Direction, T>(Direction.class))));
    }


    public T getSide(@Nullable  Direction side) {
        if(side == null) {
            return this.nullValue;
        }
        return sideMap.get(side);
    }


    @FunctionalInterface
    public interface SidedSupplier<T> {
        T get(@Nullable Direction side);
    }
}
