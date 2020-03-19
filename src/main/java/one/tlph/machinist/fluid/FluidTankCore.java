package one.tlph.machinist.fluid;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import javax.annotation.Nullable;

public class FluidTankCore implements IFluidTank {

    protected FluidStack fluid;
    protected int capacity;
    protected boolean locked;

    public FluidTankCore(int capacity) {
        this(null, capacity);
    }

    public FluidTankCore(FluidStack fluid, int capacity) {
        this.fluid = fluid;
        this.capacity = capacity;
    }

    public FluidTankCore(Fluid fluid, int amt, int capacity) {
        this(new FluidStack(fluid, amt), capacity);
    }

    public FluidTankCore readFromNBT(CompoundNBT nbt) {
        FluidStack fluid = null;
        locked = false;
        if(!nbt.contains("Empty")) {
            fluid = FluidStack.loadFluidStackFromNBT(nbt);
            locked = nbt.getBoolean("Lock") && fluid != null;
        }
        setFluid(fluid);
        return this;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        if(fluid != null) {
            fluid.writeToNBT(nbt);
            nbt.putBoolean("Lock", locked);
        }
        else {
            nbt.putString("Empty", "");
        }
        return nbt;
    }

    public FluidTankCore setLock(Fluid fluid) {
        locked = fluid != null;
        if (locked) {
            if (this.fluid == null || !this.fluid.getFluid().equals(fluid)) {
                this.fluid = new FluidStack(fluid, 0);
            }
        }
        return this;
    }

    public void setLocked(boolean lock) {

        if (lock) {
            setLocked();
        } else {
            clearLocked();
        }
    }

    public void setLocked() {

        if (locked || this.fluid == null) {
            return;
        }
        locked = true;
    }

    public void clearLocked() {

        locked = false;
        if (this.getFluidAmount() <= 0) {
            this.fluid = null;
        }
    }

    public void setFluid(FluidStack fluid) {

        this.fluid = fluid;
    }

    public void setCapacity(int capacity) {

        this.capacity = capacity;
    }

    /**
     * Only ever call this on a LOCKED tank. Be really sure you know WTF you are doing.
     */
    public void modifyFluidStored(int amount) {

        if (!locked) {
            return;
        }
        this.fluid.setAmount(this.fluid.getAmount() + amount);
       

        if (this.fluid.getAmount() > capacity) {
            this.fluid.setAmount(capacity);
        } else if (this.fluid.getAmount() < 0) {
            this.fluid.setAmount(0);
        }
    }

    public boolean isLocked() {

        return locked;
    }

    public int getSpace() {

        if (fluid == null) {
            return capacity;
        }
        return fluid.getAmount() >= capacity ? 0 : capacity - fluid.getAmount();
    }


    @Nullable
    @Override
    public FluidStack getFluid() {
        return fluid;
    }

    @Override
    public int getFluidAmount() {
        if(fluid == null)
            return 0;
        return fluid.getAmount();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

//    @Override
//    public FluidTankInfo getInfo() {
//        return new FluidTankInfo(this);
//    }


	@Override
	public boolean isFluidValid(FluidStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
        if(resource == null) {
            return 0;
        }

        if(action == action.SIMULATE) {
            if(fluid == null) {
                return Math.min(capacity, resource.getAmount());
            }
            if(!fluid.isFluidEqual(resource)) {
                return 0;
            }
            return Math.min(capacity -fluid.getAmount(), resource.getAmount());
        }
        if(fluid == null) {
            fluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
            return fluid.getAmount();
        }
        if(!fluid.isFluidEqual(resource))
            return 0;
        int filled = capacity - fluid.getAmount();
        if(resource.getAmount() < filled) {
            fluid.setAmount(fluid.getAmount() + resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }
        return filled;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
        if (fluid == null || locked && fluid.getAmount() <= 0) {
            return null;
        }
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = new FluidStack(fluid, drained);
        if (action == action.EXECUTE) {
            fluid.setAmount(fluid.getAmount() - drained);
            if (fluid.getAmount() <= 0) {
                if (locked) {
                    fluid.setAmount(0);
                } else {
                    fluid = null;
                }
            }
        }
        return stack;
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource == null || !resource.isFluidEqual(fluid)) {
            return null;
        }
        return drain(resource.getAmount(), action);
	}
}

