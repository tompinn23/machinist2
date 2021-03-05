package one.tlph.machinist.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.awt.image.DirectColorModel;

public class EnergyUtils {
	
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

	public static boolean canReceive(@Nullable TileEntity tile, @Nullable Direction direction) {
		return get(tile, direction).isPresent() && get(tile, direction).orElseThrow(NullPointerException::new).canReceive();
	}

	public static boolean canExtract(@Nullable TileEntity tile, @Nullable Direction direction) {
		return get(tile, direction).isPresent() && get(tile, direction).orElseThrow(NullPointerException::new).canExtract();
	}

	public static boolean isPresent(@Nullable TileEntity tile, @Nullable Direction direction) {
		return get(tile, direction).isPresent();
	}

	public static LazyOptional<IEnergyStorage> get(@Nullable TileEntity tile, @Nullable Direction direction) {
		return tile == null ? LazyOptional.empty() : tile.getCapability(CapabilityEnergy.ENERGY, direction != null ? direction.getOpposite() : null);
	}

}
