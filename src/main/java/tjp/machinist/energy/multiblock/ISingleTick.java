package tjp.machinist.energy.multiblock;

import net.minecraft.world.World;

public interface ISingleTick {

    boolean existsYet();

    void singleTick();

    World world();

    boolean isOutdated();
}
