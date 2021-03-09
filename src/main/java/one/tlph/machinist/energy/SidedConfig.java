package one.tlph.machinist.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import one.tlph.machinist.tileentity.AbstractPoweredTileEntity;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;

public class SidedConfig {

    private final TransferType[] sides = new TransferType[6];
    private final AbstractPoweredTileEntity te;

    public SidedConfig(AbstractPoweredTileEntity te) {
        this.te = te;
        Arrays.fill(sides, TransferType.NONE);
    }

    public void init(TransferType type) {
        Arrays.fill(sides, type);
    }

    public void read(CompoundNBT nbt) {
        int[] vals = nbt.getIntArray("sidedConfig");
        for(int i = 0; i < sides.length; i++) {
            sides[i] = TransferType.values()[vals[i]];
        }
    }

    public void write(CompoundNBT nbt) {
        nbt.putIntArray("sidedConfig", Arrays.stream(sides).mapToInt(transfer -> transfer.ordinal()).toArray());
    }

    public void next() {
        for(int i = 0; i < sides.length; i++) {
            sides[i] = sides[i].next();
        }
    }

    public void next(Direction side) {
        sides[side.getIndex()] = sides[side.getIndex()].next();
    }


    public boolean canExtract(Direction side) {
        return this.sides[side.getIndex()] == TransferType.INOUT || this.sides[side.getIndex()] == TransferType.OUT;
    }

    public boolean canInsert(Direction side) {
        return this.sides[side.getIndex()] == TransferType.INOUT || this.sides[side.getIndex()] == TransferType.IN;
    }
}
