package one.tlph.machinist.util;

import net.minecraft.nbt.CompoundNBT;

public class Progress {

    private int ticks;
    private int maxTime;
    private boolean tickDown;

    public Progress(int max) {
        this(max, false);
    }

    public Progress(int max, boolean tickDown) {
        this.maxTime = max;
        this.tickDown = tickDown;
    }

    public void tick() {
        if(tickDown) {
            this.ticks--;
        } else {
            this.ticks++;
        }
    }

    public int get() {
        return ticks;
    }


    public void set(int tick) {
        this.ticks = tick;
    }

    public void setMax(int maxTicks) {
        this.maxTime = maxTicks;
    }


    public boolean check() {
        if(this.ticks == (tickDown ? 0 : maxTime)) {
            this.ticks = tickDown ? maxTime : 0;
            return true;
        }
        return false;
    }

    public void read(CompoundNBT nbt, String key, boolean maxTime) {
        this.ticks = nbt.getInt(key);
        if(maxTime)
            this.maxTime = nbt.getInt(key + "_max");
    }

    public void write(CompoundNBT nbt, String key, boolean maxTime) {
        nbt.putInt(key, this.ticks);
        if(maxTime)
            nbt.putInt(key + "_max", this.maxTime);
    }


    public void read(CompoundNBT nbt, String key) {
        read(nbt, key, false);
    }

    public void write(CompoundNBT nbt, String key) {
        write(nbt, key, false);
    }
}
