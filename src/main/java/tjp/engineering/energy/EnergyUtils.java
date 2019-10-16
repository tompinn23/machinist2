package tjp.engineering.energy;

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
	
}
