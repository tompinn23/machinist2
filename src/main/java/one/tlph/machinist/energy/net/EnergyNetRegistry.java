package one.tlph.machinist.energy.net;

import net.minecraft.world.IWorld;
import one.tlph.machinist.Machinist;

import java.util.HashMap;

public class EnergyNetRegistry implements IEnergyNetRegistry {


    public static final EnergyNetRegistry INSTANCE = new EnergyNetRegistry();

    @Override
    public void onConnectorAdded(IWorld world, IEnergyNetPart part) {
        EnergyNetWorldRegistry registry;
        if(this._registries.containsKey(world)) {
            registry = this._registries.get(world);
        } else {
            registry = new EnergyNetWorldRegistry(world);
            this._registries.put(world, registry);
        }
        registry.onPartAdded(part);

    }

    @Override
    public void onConnectorRemoved(IWorld world, IEnergyNetPart part) {
        if (this._registries.containsKey(world))
            this._registries.get(world).onConnectorRemoved(part);
    }

    @Override
    public void addDeadGrid(IWorld world, EnergyNetBase grid) {
        if (this._registries.containsKey(world))
            this._registries.get(world).addDeadGrid(grid);
        else
            Machinist.logger.warn("Energy Net %d in world %s marked as dead, but that world is not tracked! Net is being ignored.", grid.hashCode(), world);
    }

    @Override
    public void addDirtyGrid(IWorld world, EnergyNetBase grid) {
        if (this._registries.containsKey(world))
            this._registries.get(world).addDirtyGrid(grid);
        else
            throw new IllegalArgumentException("Adding a dirty EnergyGrid to a world that has no registered controllers!");
    }

    protected void tickStart(final IWorld world) {
        if(this._registries.containsKey(world)) {
            final EnergyNetWorldRegistry reg = this._registries.get(world);
            reg.processGridChanges();
            reg.tickStart();
        }
    }

    /**
     * Called when the world has finished loading a chunk.
     * @param world The world which has finished loading a chunk
     * @param chunkX The X coordinate of the chunk
     * @param chunkZ The Z coordinate of the chunk
     */
    protected void onChunkLoaded(final IWorld world, final int chunkX, final int chunkZ) {

        if (this._registries.containsKey(world))
            this._registries.get(world).onChunkLoaded(chunkX, chunkZ);
    }

    /**
     * Called whenever a world is unloaded. Unload the relevant registry, if we have one.
     * @param world The world being unloaded.
     */
    protected void onWorldUnloaded(final IWorld world) {

        if (this._registries.containsKey(world)) {
            this._registries.get(world).onWorldUnloaded();
            this._registries.remove(world);
        }
    }



    private EnergyNetRegistry() {
        this._registries = new HashMap<IWorld, EnergyNetWorldRegistry>(2);
    }

    private HashMap<IWorld, EnergyNetWorldRegistry> _registries;
}
