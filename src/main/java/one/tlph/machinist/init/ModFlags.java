package one.tlph.machinist.init;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.lwjgl.system.CallbackI;

import java.util.function.BooleanSupplier;

public class ModFlags {

    private ModFlags() {}

    private static final FlagManager FLAG_MANAGER = new FlagManager();

    public static FlagManager manager() {

        return FLAG_MANAGER;
    }

    public static void setFlag(String flag, boolean enable) {

        FLAG_MANAGER.setFlag(flag, enable);
    }

    public static void setFlag(String flag, BooleanSupplier condition) {

        FLAG_MANAGER.setFlag(flag, condition);
    }

    public static BooleanSupplier getFlag(String flag) {

        return FLAG_MANAGER.getFlag(flag);
    }

    public static final String GEN_COPPER = "gen_copper";




    public static class FlagManager {

        public static final BooleanSupplier TRUE = () -> true;
        public static final BooleanSupplier FALSE = () -> false;

        private static final Object2ObjectOpenHashMap<String, BooleanSupplier> FLAGS = new Object2ObjectOpenHashMap<>(64);

        private FlagManager() {
        }

        private BooleanSupplier getOrCreateFlag(String flag) {
            FLAGS.putIfAbsent(flag, FALSE);
            return FLAGS.get(flag);
        }

        public synchronized void setFlag(String flag, boolean enable) {
            FLAGS.put(flag, enable ? TRUE : FALSE);
        }

        public synchronized void setFlag(String flag, BooleanSupplier condition) {
            FLAGS.put(flag, condition == null ? FALSE : condition);
        }

        public BooleanSupplier getFlag(String flag) {
            return () -> getOrCreateFlag(flag).getAsBoolean();
        }
    }
}
