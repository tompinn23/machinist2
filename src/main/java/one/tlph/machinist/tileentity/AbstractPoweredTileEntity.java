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
import one.tlph.machinist.energy.SidedConfig;
import one.tlph.machinist.energy.SidedStorage;
import one.tlph.machinist.energy.TransferType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;

public class AbstractPoweredTileEntity<B extends AbstractBlock> extends AbstractTickableTileEntity<B> {
    @CapabilityInject(IEnergyStorage.class)
    public static final Capability<IEnergyStorage> ENERGY_CAPABILITY = CapabilityEnergy.ENERGY;



    public final Energy energyStorage = Energy.create(0);
    public final SidedConfig sidedConfig;
    public final SidedStorage<LazyOptional<IEnergyStorage>> energySides = SidedStorage.create(this::createSidedEnergy);

    private LazyOptional<IEnergyStorage> createSidedEnergy(Direction side) {
        return LazyOptional.of(() -> new IEnergyStorage() {
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
                return AbstractPoweredTileEntity.this.canExtract(side);
            }

            @Override
            public boolean canReceive() {
                return AbstractPoweredTileEntity.this.canReceive(side);
            }
        });
    }


    public AbstractPoweredTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.sidedConfig = new SidedConfig(this);
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
        this.sidedConfig.read(nbt);
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
        this.sidedConfig.write(nbt);
        return super.writeStorable(nbt);
    }

    public boolean keepEnergy() {
        return false;
    }


    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        for(Direction side : Direction.values()) {
            this.energySides.getSide(side).invalidate();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap ==  ENERGY_CAPABILITY) {
            return this.energySides.getSide(side).cast();
        }
        return super.getCapability(cap, side);
    }

    protected boolean canReceive(Direction side) {
        return this.sidedConfig.canInsert(side);
    }

    protected boolean canExtract(Direction side) {
        return this.sidedConfig.canExtract(side);
    }

    public SidedConfig getSidedConfig() {
        return sidedConfig;
    }
}
