package one.tlph.machinist.energy;

import com.google.common.primitives.Ints;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class Energy implements IEnergyStorage {

    public static Energy EMPTY = Energy.create(0);

    private long energyStored;
    private long maxEnergy;
    private long maxExtract;
    private long maxReceive;

    protected Energy( long maxEnergy, long maxExtract, long maxReceive) {
        this.energyStored = 0;
        this.maxEnergy = maxEnergy;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public static Energy create(int i) {
        return new Energy(i, 0, 0);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        long energyReceived = Math.min(maxEnergy - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energyStored += energyReceived;
        return Ints.saturatedCast(energyReceived);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        long energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energyStored -= energyExtracted;

        return Ints.saturatedCast(energyStored);
    }

    @Override
    public int getEnergyStored() {
        return Ints.saturatedCast(energyStored);
    }

    @Override
    public int getMaxEnergyStored() {
        return Ints.saturatedCast(maxEnergy);
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    public Energy read(CompoundNBT nbt, boolean capacity, boolean transfer) {
        return read(nbt, "energy_storage", capacity, transfer);
    }

    public Energy read(CompoundNBT nbt, String key, boolean capactity, boolean transfer) {
        if(capactity)
            this.maxEnergy = nbt.getLong(key + "_energy_capacity");
        this.energyStored = nbt.getLong(key + "_energy_stored");
        if(transfer) {
            this.maxExtract = nbt.getLong(key + "_energy_extract");
            this.maxReceive = nbt.getLong(key + "_energy_recieve");
        }
        return this;
    }

    public Energy write(boolean capacity, boolean transfer) {
        return write(new CompoundNBT(), capacity, transfer);
    }

    public Energy write(CompoundNBT nbt, boolean capacity, boolean transfer) {
        return write(nbt, "energy_storage", capacity, transfer);
    }

    public Energy write(CompoundNBT nbt, String key, boolean capacity, boolean transfer) {
        if(capacity)
            nbt.putLong(key + "_energy_capacity", this.maxEnergy);
        nbt.putLong(key + "_energy_stored", this.energyStored);
        if(transfer) {
            nbt.putLong(key + "_energy_recieve", this.maxReceive);
            nbt.putLong(key + "_energy_extract", this.maxExtract);
        }
        return this;
    }

    public long produce(long amount) {
        long produced = Math.min(this.maxEnergy - this.energyStored, Math.max(0, amount));
        this.energyStored += produced;
        return produced;
    }

    public long consume(long amount) {
        long consumed = Math.min(this.energyStored, Math.max(0, amount));
        this.energyStored -= consumed;
        return consumed;
    }

    public Energy setCapacity(long amount) {
        this.maxEnergy = amount;
        return this;
    }

    public long getCapacity() {
        return this.maxEnergy;
    }

    public void setRecieve(long recieve) {
        this.maxReceive = recieve;
    }
    public void setExtract(long extract) {
        this.maxExtract = extract;
    }


    public enum Units {
        EU,
        FE,
        J,
        RF,
        MJ,
        T
    };

    public static double convertFrom(Units unit, double amt) {
        switch(unit) {
            case EU:
                return amt * 4;
            case FE:
                return amt;
            case J:
                return (long)Math.floor(amt * 0.4);
            case MJ:
                return amt * 10;
            case RF:
                return amt;
            case T:
                return amt;
            default:
                return amt;
        }
    }

    public static double convertTo(Units unit, double amt) {
        switch(unit) {
            case EU:
                return amt * 0.25;
            case FE:
                return amt;
            case J:
                return amt * 2.5;
            case MJ:
                return amt * 0.1;
            case RF:
            case T:
            default:
                return amt;
        }
    }

    public static int extract(@Nullable TileEntity tile, Direction direction, long energy, boolean simulate) {
        return tile == null ? 0 : get(tile, direction).orElse(EMPTY).extractEnergy(Ints.saturatedCast(energy), simulate);
    }

    public static int receive(@Nullable TileEntity tile, Direction direction, long energy, boolean simulate) {
        return tile == null ? 0 : get(tile, direction).orElse(EMPTY).receiveEnergy(Ints.saturatedCast(energy), simulate);
    }


    public static boolean canReceive(@Nullable TileEntity tile, @Nullable Direction direction) {
        return get(tile, direction).isPresent() && get(tile, direction).orElse(EMPTY).canReceive();
    }

    public static boolean canExtract(@Nullable TileEntity tile, @Nullable Direction direction) {
        return get(tile, direction).isPresent() && get(tile, direction).orElse(EMPTY).canExtract();
    }

    public static boolean isPresent(@Nullable TileEntity tile, @Nullable Direction direction) {
        return get(tile, direction).isPresent();
    }

    public static IEnergyStorage getNullable(@Nullable TileEntity tile, @Nullable Direction direction) {
        return get(tile, direction).orElse(Energy.EMPTY);
    }

    public static LazyOptional<IEnergyStorage> get(@Nullable TileEntity tile, @Nullable Direction direction) {
        return tile == null ? LazyOptional.empty() : tile.getCapability(CapabilityEnergy.ENERGY, direction != null ? direction.getOpposite() : null);
    }
}
