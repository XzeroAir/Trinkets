package xzeroair.trinkets.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EffectRenderer	{
	
    public static void renderBox(Tessellator p_190055_1_, BufferBuilder p_190055_2_, double p_190055_3_, double p_190055_5_, double p_190055_7_, double p_190055_9_, double p_190055_11_, double p_190055_13_)
    {
        GlStateManager.glLineWidth(2.0F);
        p_190055_2_.begin(3, DefaultVertexFormats.POSITION_COLOR);
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_13_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).endVertex();
        p_190055_1_.draw();
        GlStateManager.glLineWidth(1.0F);
    }

    public static void startDrawingESPs(AxisAlignedBB bb, float r, float b, float g)
    {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.5F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4d(r, b, g, 0.185F);
        drawBoundingBox(bb);
      //  GL11.glColor4d(r, b, g, 1.0F);
      //  drawOutlinedBoundingBox(bb);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    public static void drawBoundingBox(AxisAlignedBB axisalignedbb)
    {

        BufferBuilder tessellator = Tessellator.getInstance().getBuffer();
        Tessellator tessellatorr = Tessellator.getInstance();
      //  tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
      //  tessellatorr.draw();
      //  tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
     //   tessellatorr.draw();
     //   tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
      //  tessellatorr.draw();
      //  tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
      //  tessellatorr.draw();
      //  tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
     //   tessellatorr.draw();
     //   tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).endVertex();
        tessellator.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).endVertex();
        tessellatorr.draw();
    }
    public static void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB)
    {
        BufferBuilder var2 = Tessellator.getInstance().getBuffer();
        Tessellator tessellator = Tessellator.getInstance();

      // var2.begin(3, DefaultVertexFormats.POSITION_TEX);
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
     //   tessellator.draw();
     //   var2.begin(3, DefaultVertexFormats.POSITION_TEX);
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
     //   tessellator.draw();
     //   var2.begin(1, DefaultVertexFormats.POSITION_TEX);
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        var2.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        var2.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }
}
