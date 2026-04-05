package xzeroair.trinkets.client.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class TrinketRingEffects {


    public static void renderLineToBlock(Entity entity, BlockPos targetPos) {
        Minecraft mc = Minecraft.getMinecraft();
        Vec3d playerPos = mc.player.getPositionVector().add(0, mc.player.getEyeHeight(), 0);
        Vec3d blockPosVec = new Vec3d(targetPos).add(0.5, 0.5, 0.5);

        double distance = playerPos.distanceTo(blockPosVec);
        float alpha = (float) Math.max(0, 1 - distance / 50.0); // fade over 50 blocks

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(2.0f);

        GlStateManager.color(1.0f, 0.0f, 0.0f, alpha); // red line with fading alpha

//        mc.getRenderManager().renderEngine.bindTexture(null);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
//        buffer.begin(GL11.GL_LINE, DefaultVertexFormats.POSITION);
//        buffer.pos(playerPos.x - mc.getRenderManager().renderPosX, playerPos.y - mc.getRenderManager().renderPosY, playerPos.z - mc.getRenderManager().renderPosZ).endVertex();
//        buffer.pos(blockPosVec.x - mc.getRenderManager().renderPosX, blockPosVec.y - mc.getRenderManager().renderPosY, blockPosVec.z - mc.getRenderManager().renderPosZ).endVertex();
//        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void renderRing(Entity entity, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int segments = 8;
        float baseRadius = entity.width * 20.0f;

        float time = entity.ticksExisted + partialTicks;

        float speedX = 0.0f;
        float speedY = 10.0f;
        float speedZ = 10.0f;

        float rotX = ((time * speedX) % 360f);
        float rotY = ((time * speedY) % 180f);
        float rotZ = ((time * speedZ) % 180f);

        float pulse = (float) Math.sin(time * 0.5f) * (baseRadius * 0.8F);
        float radius = baseRadius + pulse;

        float alpha = 0.5f + 0.3f * (float) Math.sin(time * 0.2f);
        double x = 0;
        double y = 0;//entity.height * 0.5;
        double z = 0;
        float tPos = entity.width * 0.5F;
        float yPos = entity.height * 0.5F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, yPos, -0.0);
//        GlStateManager.rotate(90F, 0, 0, 1);
        renderRing(tessellator, buffer, segments, rotX, 0, 0, radius, 0.2F, 0.5F, 1.0F, alpha);
//        renderRing(tessellator, buffer, segments, 0, rotY, 0, radius, 0.2F, 0.5F, 1.0F, alpha);
//        renderRing(tessellator, buffer, segments, 0, 0, rotZ, radius, 0.2F, 0.5F, 1.0F, alpha);
//        GlStateManager.rotate(-90F, 0, 0, 1);
        GlStateManager.popMatrix();
    }

    public static void renderRing(Tessellator tessellator, BufferBuilder buffer, int segments, float rotX, float rotY, float rotZ, float radius, float r, float g, float b, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        GlStateManager.glLineWidth(10f);
        GlStateManager.rotate(rotX, 1, 0, 0);
        GlStateManager.rotate(rotY, 0, 1, 0);
        GlStateManager.rotate(rotZ, 0, 0, 1);
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;

            double px = Math.cos(angle) * radius;
            double pz = Math.sin(angle) * radius;

            buffer.pos(px, 0, pz).color(r, g, b, alpha).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.glLineWidth(2F);
        GlStateManager.popMatrix();
    }
}
