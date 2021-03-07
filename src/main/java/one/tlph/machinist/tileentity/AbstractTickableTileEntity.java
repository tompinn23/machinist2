package one.tlph.machinist.tileentity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public abstract class AbstractTickableTileEntity<B extends AbstractBlock> extends AbstractTileEntity<B> implements ITickableTileEntity {

    private int syncTicks;
    private long ticks;

    public AbstractTickableTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        ticks = 0;
        syncTicks = 0;
    }


    @Override
    public void tick() {
        if(this.world != null) {
            if (this.ticks == 0)
                onFirstTick();
            if(doPostTicks()) {
                int i = postTick();
                if(i > -1 && !isRemote()) {
                    sync(i);
                }
            }
            this.ticks++;

            if(!isRemote()) {
                //Check if sync ticks >= 0
                if(this.syncTicks > -1) {
                    this.syncTicks--;
                    //Tick it down to 0.
                }
                //When it hits 0 sync the tileentity.
                if(this.syncTicks == 0) {
                    sync();
                }
            } else {
                clientTick();
            }

        }


    }

    protected long getTicks() {
        return ticks;
    }



    public void onFirstTick() {}

    protected boolean doPostTicks() {
        return true;
    }

    protected int postTick() {
        return -1;
    }

    protected void clientTick() {

    }

    /**
     * Syncs the TileEntity after specified delay. Smallest delay value wins.
     * @param delay Delay in ticks.
     */
    public void sync(int delay) {
        if(!isRemote()) {
            if(this.syncTicks <= 0 || delay < this.syncTicks) {
                //Check if single player.
                this.syncTicks = !((MinecraftServer)LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER)).isDedicatedServer() ? 2 : delay;
            }
        }
    }

}
