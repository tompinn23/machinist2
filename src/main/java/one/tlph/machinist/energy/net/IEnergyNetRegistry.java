package one.tlph.machinist.energy.net;

import net.minecraft.world.IWorld;

public interface IEnergyNetRegistry {

    /**
     * Register a new connector in the system.
     *
     * @param world The world this part is being loaded in.
     * @param part The part being loaded.
     */
    void onConnectorAdded(IWorld world, IEnergyNetPart part);

    /**
     * Call to remove a part from world lists.
     * @param world The world from which the part is being removed.
     * @param part The part being removed.
     */
    void onConnectorRemoved(IWorld world, IEnergyNetPart part);

    void addDeadGrid(IWorld world, EnergyNetBase grid);

    void addDirtyGrid(IWorld world, EnergyNetBase grid);
}
