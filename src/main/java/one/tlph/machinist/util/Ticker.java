package one.tlph.machinist.util;

import net.minecraft.nbt.CompoundNBT;

public class Ticker {
    private int ticks;
    private int threshold;
    private boolean tickDown;

    public Ticker(int threshold) {
        this(threshold, false);
    }

    public Ticker(int threshold, boolean tickDown) {
        this.threshold = threshold;
        this.ticks = 0;
        this.tickDown = tickDown;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void tick() {
        this.ticks++;
    }

    public int get() {
        return this.ticks;
    }



    public boolean check() {
        if(tickDown) {
            if(this.ticks <= 0) {
                this.ticks = threshold;
                return true;
            };
        } else {
            if(this.ticks >= threshold) {
                this.ticks = 0;
                return true;
            }
        }
        return false;
    }

    public void read(CompoundNBT nbt, String key) { read(nbt, key, false); }

    public void read(CompoundNBT nbt, String key, boolean threshold) {
        this.ticks = nbt.getInt(key + "_ticks");
        if(threshold)
            this.threshold = nbt.getInt(key + "_threshold");
    }

    public void write(CompoundNBT nbt, String key) {
        write(nbt, key, false);
    }

    public void write(CompoundNBT nbt, String key, boolean threshold) {
        nbt.putInt(key + "_ticks", this.ticks);
        if(threshold)
            nbt.putInt(key + "_threshold", this.threshold);
    }


}
