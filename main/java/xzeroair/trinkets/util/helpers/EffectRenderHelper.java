package xzeroair.trinkets.util.helpers;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class EffectRenderHelper {

	private final static Random rand = new Random();

	private static Vec3d end;

	public static void setEnd(Vec3d location) {
		end = location;
	}

	public static Vec3d getEnd() {
		return end;
	}

	public static void renderSegment(BufferBuilder buffer, Vec3d start, Vec3d end, float[] color) {

		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		final boolean testB = false;
		final Vec3d posDiff = testB? start.subtract(end):end.subtract(start);
		final double rD = 0;
		final double minX = (start.x + posDiff.x) + (start.x - end.x);
		final double minY = (start.y + posDiff.y) + (start.y - end.y);
		final double minZ = (start.z + posDiff.z) + (start.z - end.z);
		buffer.pos(minX, minY, minZ).tex(0, 0).lightmap(240, 240).color(color[0], color[1], color[2], color[3]).endVertex();
		final double maxX = start.x + posDiff.x + rD;
		final double maxY = start.y + posDiff.y + rD;
		final double maxZ = start.z + posDiff.z + rD;
		buffer.pos(maxX, maxY, maxZ).tex(0, 0).lightmap(240, 240).color(color[0], color[1], color[2], color[3]).endVertex();

	}

}
