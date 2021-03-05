package one.tlph.machinist.energy.net;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import one.tlph.machinist.api.multiblock.MultiblockControllerBase;

import java.util.Set;

public interface IEnergyNetPart {
    BlockPos getWorldPos();

    boolean isPartInvalid();

    Set<EnergyNetBase> attachToNeighbours();

    EnergyNetBase createNewGrid();

    void assertDetached();

    void onAttached(EnergyNetBase energyNetBase);

    boolean hasNetSaveData();

    void onNetDataAssimilated();

    void becomeNetSaveDelegate();

    void forfeitNetSaveDelegate();

    boolean isGridSaveDelegate();

    void onDetached(EnergyNetBase energyNetBase);

    void onAssimilated(EnergyNetBase energyNetBase);

    CompoundNBT getNetSaveData();

    void setUnvisited();

    void setVisited();

    IEnergyNetPart[] getNeighboringParts();


    boolean isVisited();

    void onOrphaned(EnergyNetBase energyNetBase, int originalSize, int visitedParts);

    EnergyNetBase getGridController();

    Class<? extends EnergyNetBase> getGridControllerType();

    boolean isConnected();
}
