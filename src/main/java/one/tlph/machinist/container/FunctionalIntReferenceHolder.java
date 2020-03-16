package one.tlph.machinist.container;

import net.minecraft.util.IntReferenceHolder;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class FunctionalIntReferenceHolder extends IntReferenceHolder {
    private final IntSupplier getter;
    private final IntConsumer setter;

    @Override
    public int get() {
        return this.getter.getAsInt();
    }

    @Override
    public void set(int val) {
        this.setter.accept(val);
    }

    public FunctionalIntReferenceHolder(final IntSupplier getter, final IntConsumer setter) {
        this.getter = getter;
        this.setter = setter;
    }
}
