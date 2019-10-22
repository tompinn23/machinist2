package tjp.machinist.blocks.smelter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import tjp.machinist.util.OutputSlotHandler;

public class SmelterContainer extends Container {
	
	public class SmeltableSlotHandler extends SlotItemHandler {
		public SmeltableSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return SmelterTileEntity.isItemValidInput(stack);
		}
	}
	
	public class FuelSlotHandler extends SlotItemHandler {
		public FuelSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return SmelterTileEntity.isItemValidFuel(stack);
		}
	}
	
	
	private SmelterTileEntity te;
	
	
	public SmelterContainer(IInventory playerInventory, SmelterTileEntity te) {
		this.te = te;
		
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory) {
		//Slots for main inventory
		for(int row = 0; row < 3; ++row) {
			for(int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 84;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}
		
		for(int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = 58 + 84;
			this.addSlotToContainer(new Slot(playerInventory, row, x ,y));
		}
	}
	
	private void addOwnSlots() {
		// TODO Auto-generated method stub
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		//Fuel Slot
		addSlotToContainer(new FuelSlotHandler(itemHandler, 0, 56, 53));
		//Input Slot
		addSlotToContainer(new SmeltableSlotHandler(itemHandler, 1, 56, 17));
		//Output Slot
		addSlotToContainer(new OutputSlotHandler(itemHandler, 2, 116, 35));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < SmelterTileEntity.SIZE) {
				if(!this.mergeItemStack(itemstack1, SmelterTileEntity.SIZE, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, SmelterTileEntity.SIZE, false)) {
				return ItemStack.EMPTY;
			}
			
			if(itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		
		return itemstack;

	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.te.canInteractWith(playerIn);
	}
}
