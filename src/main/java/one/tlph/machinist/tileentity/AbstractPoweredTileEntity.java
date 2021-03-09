package one.tlph.machinist.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import one.tlph.machinist.blocks.AbstractBlock;
import one.tlph.machinist.energy.Energy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbstractPoweredTileEntity<B extends AbstractBlock> extends AbstractTickableTileEntity<B> {
    @CapabilityInject(IEnergyStorage.class)
    public static final Capability<IEnergyStorage> ENERGY_CAPABILITY = CapabilityEnergy.ENERGY;



    public final Energy energyStorage = Energy.create(0);
    private final LazyOptional<IEnergyStorage> ENERGY_CAP = LazyOptional.of(() -> new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return AbstractPoweredTileEntity.this.energyStorage.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return AbstractPoweredTileEntity.this.energyStorage.extractEnergy(maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return AbstractPoweredTileEntity.this.energyStorage.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return AbstractPoweredTileEntity.this.energyStorage.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return AbstractPoweredTileEntity.this.energyStorage.canExtract();
        }

        @Override
        public boolean canReceive() {
            return AbstractPoweredTileEntity.this.energyStorage.canReceive();
        }
    });

    public AbstractPoweredTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    @Override
    protected void readSync(CompoundNBT nbt) {
        if(!keepEnergy()) {
            this.energyStorage.read(nbt, true, false);
        }
        super.readSync(nbt);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        if(keepEnergy()) {
            this.energyStorage.read(nbt, false, false);
        }
        super.readStorable(nbt);
    }

    @Override
    protected CompoundNBT writeSync(CompoundNBT nbt) {
        if(!keepEnergy()) {
            this.energyStorage.write(nbt, true, false);
        }
        return super.writeSync(nbt);
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        if(keepEnergy()) {
            this.energyStorage.write(nbt, false, false);
        }
        return super.writeStorable(nbt);
    }

    public boolean keepEnergy() {
        return false;
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.ENERGY_CAP.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap ==  ENERGY_CAPABILITY) {
            return this.ENERGY_CAP.cast();
        }
        return super.getCapability(cap, side);
    }
}
