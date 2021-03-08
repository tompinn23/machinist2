package one.tlph.machinist.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import one.tlph.machinist.blocks.EnergyNet.EnergyConduitTileEntity;

public class CableRenderer extends TileEntityRenderer<EnergyConduitTileEntity> {

    private static final ConduitModel MODEL = new ConduitModel();

    public CableRenderer(final TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(EnergyConduitTileEntity te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        matrix.push();
        matrix.translate(0.5, 1.5, 0.5);
        matrix.translate(0.0, -0.125, 0.0);
        matrix.scale(1.0f, -1.0f, -1.0f);
        MODEL.render(te, this, matrix, rtb, light, ov);
        matrix.pop();
    }
}
