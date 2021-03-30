package one.tlph.machinist.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import one.tlph.machinist.blocks.PortalHeater.BlockPortalHeater;
import one.tlph.machinist.blocks.PortalHeater.PortalHeaterTileEntity;

import java.util.List;

public class PortalHeater extends TileEntityRenderer<PortalHeaterTileEntity> {

    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public PortalHeater(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PortalHeaterTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(tileEntityIn.getBlockState().get(BlockPortalHeater.ACTIVE)) {
            long i = tileEntityIn.getWorld().getGameTime();
            Direction direction = tileEntityIn.getBlockState().get(BlockPortalHeater.FACING);
            renderBeamSegment(matrixStackIn, bufferIn, partialTicks, i, 1, tileEntityIn.laserDistance, new float[]{1F, 1F, 1F}, direction);
        }
    }

    private static void renderBeamSegment(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, float partialTicks, long totalWorldTime, int yOffset, int height, float[] colors, Direction direction) {
        renderBeamSegment(matrixStackIn, bufferIn, TEXTURE_BEACON_BEAM, partialTicks, 1.5F, totalWorldTime, yOffset, height, colors, 0.1F, 0.15F, direction);
    }

    @Override
    public boolean isGlobalRenderer(PortalHeaterTileEntity te) {
        return true;
    }

    public static void renderBeamSegment(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, ResourceLocation textureLocation, float partialTicks, float textureScale, long totalWorldTime, int yOffset, int height, float[] colors, float beamRadius, float glowRadius, Direction direction) {
        int i = yOffset + height;
        matrixStackIn.push();
        switch(direction) {
            case NORTH:
                matrixStackIn.translate(0.5D, 0.55D, 1.4D);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0f));
                break;
            case SOUTH:
                matrixStackIn.translate(0.5D, 0.55D,  -0.4D);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(0f));
                break;
            case EAST:
                matrixStackIn.translate(-0.4D, 0.55D, 0.5D);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0f));
                break;
            case WEST:
                matrixStackIn.translate(1.4D, 0.55D, 0.5D);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270.0f));
                break;
        }
        float f = (float)Math.floorMod(totalWorldTime, 40L) + partialTicks;
        float f1 = height < 0 ? f : -f;
        float f2 = MathHelper.frac(f1 * 0.2F - (float)MathHelper.floor(f1 * 0.1F));
        float f3 = colors[0];
        float f4 = colors[1];
        float f5 = colors[2];
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * 2.25F - 45.0F));
        //matrixStackIn.rotate(Vector3f.ZN.rotation(45.0f));
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = -beamRadius;
        float f10 = 0.0F;
        float f11 = 0.0F;
        float f12 = -beamRadius;
        float f13 = 0.0F;
        float f14 = 1.0F;
        float f15 = -1.0F + f2;
        float f16 = (float)height * textureScale * (0.5F / beamRadius) + f15;
        renderPart(matrixStackIn, bufferIn.getBuffer(RenderType.getBeaconBeam(textureLocation, false)), f3, f4, f5, 1.0F, yOffset, i, 0.0F, beamRadius, beamRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
        matrixStackIn.pop();
        f6 = -glowRadius;
        float f7 = -glowRadius;
        f8 = -glowRadius;
        f9 = -glowRadius;
        f13 = 0.0F;
        f14 = 1.0F;
        f15 = -1.0F + f2;
        f16 = (float)height * textureScale + f15;
        renderPart(matrixStackIn, bufferIn.getBuffer(RenderType.getBeaconBeam(textureLocation, true)), f3, f4, f5, 0.125F, yOffset, i, f6, f7, glowRadius, f8, f9, glowRadius, glowRadius, glowRadius, 0.0F, 1.0F, f16, f15);
        matrixStackIn.pop();
    }

    private static void renderPart(MatrixStack matrixStackIn, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, int yMin, int yMax, float uAreZero, float beamRadius1, float beamRadius2, float youAreZero2, float float9, float zeroAgain, float moreZero, float float12, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, uAreZero, beamRadius1, beamRadius2, youAreZero2, u1, u2, v1, v2);
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, moreZero, float12, float9, zeroAgain, u1, u2, v1, v2);
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, beamRadius2, youAreZero2, moreZero, float12, u1, u2, v1, v2);
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, float9, zeroAgain, uAreZero, beamRadius1, u1, u2, v1, v2);

    }

    private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, int yMin, int yMax, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x1, z1, u2, v1);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x1, z1, u2, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x2, z2, u1, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x2, z2, u1, v1);
    }

    private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, int y, float x, float z, float texU, float texV) {
        bufferIn.pos(matrixPos, z, (float)x,  (float)y).color(red, green, blue, alpha).tex(texU, texV).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

}
