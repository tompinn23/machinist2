package one.tlph.machinist.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

public class MachineFrame extends Block {
    public MachineFrame(final Properties properties) {
    	super(properties);
    }

    @Override
    public boolean isVariableOpacity() {
        return true;
    }
}
