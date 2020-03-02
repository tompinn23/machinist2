package tjp.machinist.energy.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

public abstract class MultiblockGrid<T extends IGridTile> {
    public NoComodSet<T> nodeSet = new NoComodSet<>();
    public NoComodSet<T> idleSet = new NoComodSet<>();

    public void addBlock(T block) {
        if(block.isNode()) {
            addNode(block);
        }
        else {
            addIdle(block);
        }
    }

    public void addNode(T node) {
        nodeSet.add(node);
        if(idleSet.contains(node))
            idleSet.remove(node);
        onMajorGridChange();
        balanceGrid();

    }

    public void addIdle(T node) {
        idleSet.add(node);
        if(nodeSet.contains(node)) {
            nodeSet.remove(node);
            onMajorGridChange();
        }
        else {
            boolean flag = false;
            for (byte s = 0; s < EnumFacing.VALUES.length; s++) {
                if (node.isSideConnected(s)) {
                    if (flag) {
                        onMajorGridChange();
                        break;
                    } else {
                        flag = true;
                    }
                }
            }
        }
        balanceGrid();
    }

    public void destroy() {
        nodeSet.clear();
        idleSet.clear();
        //TODO: GridRegistry.
    }

    public void destroyNode(IGridTile node) {
        node.setGrid(null);
    }

    public void mergeGrids(MultiblockGrid<T> otherGrid) {
        if(!otherGrid.nodeSet.isEmpty()) {
            for(T block : otherGrid.nodeSet) {
                block.setGrid(this);
                addBlock(block);
            }
            onMajorGridChange();
        }

        if(!otherGrid.idleSet.isEmpty()) {
            for(T block : otherGrid.idleSet) {
                block.setGrid(this);
                addBlock(block);
            }
            onMajorGridChange();
        }
        onMinorGridChange();
        otherGrid.destroy();
    }

    public boolean canGridsMerger(MultiblockGrid grid) {
        return this.getClass() == grid.getClass();
    }

    public void resetMultiBlocks() {

        for (IGridTile aBlock : nodeSet) {
            aBlock.setValidForForming();
        }
        for (IGridTile aBlock : idleSet) {
            aBlock.setValidForForming();
        }
    }

    /*
     * Called at the end of a world tick
     */
    public void tickGrid() {

    }

    /*
     * Called whenever a set changes so that grids that rely on set sizes can rebalance.
     */
    public void balanceGrid() {

    }

    public void removeBlock(T oldBlock) {

        destroyNode(oldBlock);

        if (oldBlock.isNode()) {
            nodeSet.remove(oldBlock);
            onMajorGridChange();
        } else {
            idleSet.remove(oldBlock);
        }
        if (nodeSet.isEmpty() && idleSet.isEmpty()) {
            //worldGrid.oldGrids.add(this);
            return;
        }
        byte s = 0;

        for (byte i = 0; i < 6; i++) {
            if (oldBlock.isSideConnected(i)) {
                s++;
            }
        }
        if (s <= 1) {
            balanceGrid();
            onMinorGridChange();
            return;
        }
        onMajorGridChange();
       // worldGrid.gridsToRecreate.add(this);
    }

    public void onMajorGridChange() {

    }

    public void onMinorGridChange() {

    }

    public int size() {

        return nodeSet.size() + idleSet.size();
    }

    public void doTickProcessing(long deadline) {

    }

    public boolean isTickProcessing() {

        return false;
    }

    public boolean isFirstMultiblock(T block) {

        return !nodeSet.isEmpty() ? nodeSet.iterator().next() == block : !idleSet.isEmpty() && idleSet.iterator().next() == block;
    }

    public abstract boolean canAddBlock(IGridTile aBlock);

    public void addInfo(List<ITextComponent> info, EntityPlayer player, boolean debug) {

        if (debug) {
            addInfo(info, "Type", getClass().getSimpleName());
            addInfo(info, "size", size());
        }
    }

    protected final void addInfo(List<ITextComponent> info, String type, Object value) {

        //info.add(new TextComponentTranslation("info.thermaldynamics.info." + type).appendText(": ").appendSibling(ChatHelper.getChatComponent(value)));
    }
}
