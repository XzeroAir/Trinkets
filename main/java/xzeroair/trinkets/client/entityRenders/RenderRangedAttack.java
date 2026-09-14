package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.client.particles.ParticleColoredSnow;
import xzeroair.trinkets.client.particles.ParticleLightning;
import xzeroair.trinkets.entity.EntityRangedAttack;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nonnull;
import java.util.*;

public class RenderRangedAttack extends Render<EntityRangedAttack> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/particle/dragon_breath.png");
    private static final int FRAME_COUNT = 31;
    private static final int GROWTH_FRAME_COUNT = 15;
    private static final int CORE_LIFETIME = FRAME_COUNT;
    private static final int CORE_FADE_START_FRAME = GROWTH_FRAME_COUNT;
    private static final int WISP_START_TICK = 3;
    private static final int WISP_START_FRAME = 16;
    private static final int WISP_FRAME_COUNT = FRAME_COUNT - WISP_START_FRAME;
    private static final int WISP_LIFETIME = FRAME_COUNT / 2;

    private static final int SNOW_COLOR = 0xFFFFFF;
    private static final double SNOW_HORIZONTAL_SPREAD = 0.65D;
    private static final double SNOW_VERTICAL_SPREAD = 0.45D;
    private static final double LIGHTNING_ARC_LENGTH = 0.35D;

    private final Map<Integer, BreathTrail> trails = new HashMap<>();

    protected RenderRangedAttack(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0F;
        this.shadowOpaque = 0F;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean shouldRender(@Nonnull EntityRangedAttack entity, @Nonnull ICamera camera, double camX, double camY, double camZ) {
        if (entity.getVisualType() != EntityRangedAttack.VISUAL_LASER) {
            return super.shouldRender(entity, camera, camX, camY, camZ);
        }
        return entity.isInRangeToRender3d(camX, camY, camZ);
    }

    @Override
    public void doRender(@Nonnull EntityRangedAttack entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getVisualType() == EntityRangedAttack.VISUAL_LASER) {
            this.renderLaserBeam(entity, x, y, z, partialTicks);
        } else if (entity.getVisualType() == EntityRangedAttack.VISUAL_ICE) {
            this.captureSnow(entity, partialTicks);
        } else if (entity.getVisualType() == EntityRangedAttack.VISUAL_LIGHTNING_BOLT) {
            this.captureLightningBolt(entity, partialTicks);
        } else if (entity.getVisualType() == EntityRangedAttack.VISUAL_LIGHTNING) {
            this.captureLightning(entity, partialTicks);
        } else {
            this.captureWisp(entity, partialTicks);
            this.renderCoreFrame(x, y, z, entity.ticksExisted + partialTicks, entity.getColor());
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private void renderLaserBeam(EntityRangedAttack entity, double x, double y, double z, float partialTicks) {
        final Vec3d head = new Vec3d(this.interpolateX(entity, partialTicks), this.interpolateY(entity, partialTicks), this.interpolateZ(entity, partialTicks));
        final Vec3d end = new Vec3d(x, y, z);
        final Vec3d start = end.add(entity.getVisualOrigin().subtract(head));
        if (end.squareDistanceTo(start) <= 1.0E-7D) {
            return;
        }
        final int color = entity.getColor();
        final float red = (color >> 16 & 255) / 255.0F;
        final float green = (color >> 8 & 255) / 255.0F;
        final float blue = (color & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        this.renderLaserCuboid(buffer, start, end, 0.12D, red, green, blue, 0.20F);
        this.renderLaserCuboid(buffer, start, end, 0.06D, Math.max(0.05F, red), Math.max(0.05F, green), Math.max(0.05F, blue), 1.0F);

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }

    private void renderLaserCuboid(BufferBuilder buffer, Vec3d start, Vec3d end, double halfWidth, float red, float green, float blue, float alpha) {
        final Vec3d direction = end.subtract(start).normalize();
        Vec3d right = direction.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D));
        final boolean alternateRightAxis = right.lengthSquared() <= 1.0E-7D;
        if (alternateRightAxis) {
            right = direction.crossProduct(new Vec3d(1.0D, 0.0D, 0.0D));
        }
        right = right.normalize().scale(halfWidth);
        final Vec3d up = right.crossProduct(direction).normalize().scale(halfWidth);
        final Vec3d startBottomLeft = start.subtract(right).subtract(up);
        final Vec3d startBottomRight = start.add(right).subtract(up);
        final Vec3d startTopRight = start.add(right).add(up);
        final Vec3d startTopLeft = start.subtract(right).add(up);
        final Vec3d endBottomLeft = end.subtract(right).subtract(up);
        final Vec3d endBottomRight = end.add(right).subtract(up);
        final Vec3d endTopRight = end.add(right).add(up);
        final Vec3d endTopLeft = end.subtract(right).add(up);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        this.addLaserFace(buffer, startBottomLeft, startBottomRight, endBottomRight, endBottomLeft, red, green, blue, alpha);
        this.addLaserFace(buffer, startTopRight, startTopLeft, endTopLeft, endTopRight, red, green, blue, alpha);
        this.addLaserFace(buffer, startBottomRight, startTopRight, endTopRight, endBottomRight, red, green, blue, alpha);
        this.addLaserFace(buffer, startTopLeft, startBottomLeft, endBottomLeft, endTopLeft, red, green, blue, alpha);
        this.addLaserFace(buffer, startBottomRight, startBottomLeft, startTopLeft, startTopRight, red, green, blue, alpha);
        this.addLaserFace(buffer, endBottomLeft, endBottomRight, endTopRight, endTopLeft, red, green, blue, alpha);
        Tessellator.getInstance().draw();
    }

    private void addLaserFace(BufferBuilder buffer, Vec3d first, Vec3d second, Vec3d third, Vec3d fourth, float red, float green, float blue, float alpha) {
        buffer.pos(first.x, first.y, first.z).color(red, green, blue, alpha).endVertex();
        buffer.pos(second.x, second.y, second.z).color(red, green, blue, alpha).endVertex();
        buffer.pos(third.x, third.y, third.z).color(red, green, blue, alpha).endVertex();
        buffer.pos(fourth.x, fourth.y, fourth.z).color(red, green, blue, alpha).endVertex();
    }

    private void captureWisp(EntityRangedAttack entity, float partialTicks) {
        final float renderTime = entity.world.getTotalWorldTime() + partialTicks;
        final int entityId = entity.getEntityId();
        BreathTrail trail = this.trails.get(entityId);
        if (trail == null) {
            trail = new BreathTrail(renderTime, this.interpolateX(entity, partialTicks), this.interpolateY(entity, partialTicks), this.interpolateZ(entity, partialTicks));
            this.trails.put(entityId, trail);
        }

        if (entity.ticksExisted < WISP_START_TICK) {
            trail.lastSeenTime = renderTime;
            return;
        }

        final int sampleTick = (int) renderTime;
        if (sampleTick > trail.lastSampleTick) {
            final double x = this.interpolateX(entity, partialTicks);
            final double y = this.interpolateY(entity, partialTicks);
            final double z = this.interpolateZ(entity, partialTicks);
            trail.wisps.add(new BreathWisp(x, y, z, entity.getColor(), renderTime));
            trail.lastSampleTick = sampleTick;
        }
        trail.lastSeenTime = renderTime;
    }

    private BreathTrail getTrail(EntityRangedAttack entity, float renderTime, float partialTicks) {
        final int entityId = entity.getEntityId();
        BreathTrail trail = this.trails.get(entityId);
        if (trail == null) {
            trail = new BreathTrail(renderTime, this.interpolateX(entity, partialTicks), this.interpolateY(entity, partialTicks), this.interpolateZ(entity, partialTicks));
            this.trails.put(entityId, trail);
        }
        trail.lastSeenTime = renderTime;
        return trail;
    }

    private void captureSnow(EntityRangedAttack entity, float partialTicks) {
        final float renderTime = entity.world.getTotalWorldTime() + partialTicks;
        final BreathTrail trail = this.getTrail(entity, renderTime, partialTicks);
        final int sampleTick = (int) renderTime;
        if (sampleTick <= trail.lastSampleTick) {
            return;
        }

        final Vec3d motion = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
        final Vec3d direction = motion.lengthSquared() > 1.0E-7D ? motion.normalize() : new Vec3d(0.0D, 0.0D, 0.0D);
        final double x = this.interpolateX(entity, partialTicks);
        final double y = this.interpolateY(entity, partialTicks);
        final double z = this.interpolateZ(entity, partialTicks);
        for (int i = 0; i < 4; i++) {
            final double spreadX = (entity.world.rand.nextDouble() - 0.5D) * SNOW_HORIZONTAL_SPREAD;
            final double spreadY = (entity.world.rand.nextDouble() - 0.5D) * SNOW_VERTICAL_SPREAD;
            final double spreadZ = (entity.world.rand.nextDouble() - 0.5D) * SNOW_HORIZONTAL_SPREAD;
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleColoredSnow(entity.world, x + spreadX, y + spreadY, z + spreadZ, direction.x * 0.5D, direction.y * 0.5D, direction.z * 0.5D, SNOW_COLOR));
        }
        trail.lastSampleTick = sampleTick;
    }

    private void captureLightning(EntityRangedAttack entity, float partialTicks) {
        final float renderTime = entity.world.getTotalWorldTime() + partialTicks;
        final BreathTrail trail = this.getTrail(entity, renderTime, partialTicks);
        final int sampleTick = (int) renderTime;
        if (sampleTick <= trail.lastSampleTick) {
            return;
        }

        final Vec3d end = new Vec3d(this.interpolateX(entity, partialTicks), this.interpolateY(entity, partialTicks), this.interpolateZ(entity, partialTicks));
        final Vec3d motion = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
        final Vec3d start = motion.lengthSquared() > 1.0E-7D ? end.subtract(motion.normalize()
                                                                                  .scale(Math.min(LIGHTNING_ARC_LENGTH, Math.sqrt(motion.lengthSquared())))) : end;
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightning(entity.world, start.x, start.y, start.z, end.x, end.y, end.z, entity.getColor(), 0.8F, false, 0.55F, 6));
        trail.lastSampleTick = sampleTick;
    }

    private void captureLightningBolt(EntityRangedAttack entity, float partialTicks) {
        final float renderTime = entity.world.getTotalWorldTime() + partialTicks;
        final int entityId = entity.getEntityId();
        BreathTrail trail = this.trails.get(entityId);
        final boolean firstSample = trail == null;
        if (firstSample) {
            trail = new BreathTrail(renderTime, this.interpolateX(entity, partialTicks), this.interpolateY(entity, partialTicks), this.interpolateZ(entity, partialTicks));
            this.trails.put(entityId, trail);
        }
        final int sampleTick = (int) renderTime;
        if (!firstSample && sampleTick <= trail.lastSampleTick) {
            return;
        }

        final Vec3d start = entity.getVisualOrigin();
        final Vec3d end = new Vec3d(this.interpolateX(entity, partialTicks), this.interpolateY(entity, partialTicks), this.interpolateZ(entity, partialTicks));
        if (end.squareDistanceTo(start) > 1.0E-7D) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightning(entity.world, start.x, start.y, start.z, end.x, end.y, end.z, entity.getColor(), 0.9F, false, 3.0F));
            if (!trail.soundPlayed) {
                Minecraft.getMinecraft().player.world.playSound(start.x, start.y, start.z, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 0.2F, 0.6F, true);
                trail.soundPlayed = true;
            }
        }
        trail.lastSampleTick = sampleTick;
        trail.lastSeenTime = renderTime;
    }

    @SubscribeEvent
    public void renderWisps(RenderWorldLastEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if ((minecraft.world == null) || (minecraft.getRenderViewEntity() == null)) {
            this.trails.clear();
            return;
        }

        final float partialTicks = event.getPartialTicks();
        final float renderTime = minecraft.world.getTotalWorldTime() + partialTicks;
        final Entity camera = minecraft.getRenderViewEntity();
        final double cameraX = camera.prevPosX + ((camera.posX - camera.prevPosX) * partialTicks);
        final double cameraY = camera.prevPosY + ((camera.posY - camera.prevPosY) * partialTicks);
        final double cameraZ = camera.prevPosZ + ((camera.posZ - camera.prevPosZ) * partialTicks);
        final Iterator<BreathTrail> trails = this.trails.values().iterator();

        while (trails.hasNext()) {
            final BreathTrail trail = trails.next();
            final Iterator<BreathWisp> wisps = trail.wisps.iterator();
            while (wisps.hasNext()) {
                final BreathWisp wisp = wisps.next();
                final float age = renderTime - wisp.birthTime;
                if (age >= WISP_LIFETIME) {
                    wisps.remove();
                    continue;
                }
                this.renderWispFrame(wisp.x - cameraX, wisp.y - cameraY, wisp.z - cameraZ, age, wisp.color);
            }
            if (trail.wisps.isEmpty() && ((renderTime - trail.lastSeenTime) > 1.0F)) {
                trails.remove();
            }
        }
    }

    private double interpolateX(Entity entity, float partialTicks) {
        return entity.prevPosX + ((entity.posX - entity.prevPosX) * partialTicks);
    }

    private double interpolateY(Entity entity, float partialTicks) {
        return entity.prevPosY + ((entity.posY - entity.prevPosY) * partialTicks);
    }

    private double interpolateZ(Entity entity, float partialTicks) {
        return entity.prevPosZ + ((entity.posZ - entity.prevPosZ) * partialTicks);
    }

    private void renderCoreFrame(double x, double y, double z, float age, int color) {
        final int frame = Math.min(GROWTH_FRAME_COUNT - 1, (int) age);
        final float alpha = age < CORE_FADE_START_FRAME ? 1.0F : Math.max(0.0F, 1.0F - ((age - CORE_FADE_START_FRAME) / (CORE_LIFETIME - CORE_FADE_START_FRAME)));
        this.renderAtlasFrame(x, y, z, frame, color, alpha, 1.0F);
    }

    private void renderWispFrame(double x, double y, double z, float age, int color) {
        final int frame = WISP_START_FRAME + Math.min(WISP_FRAME_COUNT - 1, (int) ((age * WISP_FRAME_COUNT) / WISP_LIFETIME));
        final float alpha = Math.max(0.0F, 1.0F - (age / WISP_LIFETIME));
        this.renderAtlasFrame(x, y, z, frame, color, alpha, 0.75F);
    }

    private void renderAtlasFrame(double x, double y, double z, int frame, int color, float alpha, float scale) {
        final int column = frame % 16;
        final int row = frame < 16 ? 1 : 0;
        final float minU = column / 16.0F;
        final float maxU = minU + (1.0F / 16.0F);
        final float minV = row / 2.0F;
        final float maxV = minV + 0.5F;
        final float red = (color >> 16 & 255) / 255.0F;
        final float green = (color >> 8 & 255) / 255.0F;
        final float blue = (color & 255) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1.0F : 1.0F) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        this.bindTexture(TEXTURE);

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(-0.5D, -0.5D, 0.0D).tex(minU, maxV).color(red, green, blue, alpha).endVertex();
        buffer.pos(0.5D, -0.5D, 0.0D).tex(maxU, maxV).color(red, green, blue, alpha).endVertex();
        buffer.pos(0.5D, 0.5D, 0.0D).tex(maxU, minV).color(red, green, blue, alpha).endVertex();
        buffer.pos(-0.5D, 0.5D, 0.0D).tex(minU, minV).color(red, green, blue, alpha).endVertex();
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityRangedAttack entity) {
        return TEXTURE;
    }

    public static final Factory FACTORY = new Factory();

    public static class Factory implements IRenderFactory<EntityRangedAttack> {

        @Override
        public Render<? super EntityRangedAttack> createRenderFor(RenderManager manager) {
            return new RenderRangedAttack(manager);
        }
    }

    private static class BreathTrail {

        private final List<BreathWisp> wisps = new ArrayList<>();
        private int lastSampleTick;
        private float lastSeenTime;
        private boolean soundPlayed;

        private BreathTrail(float renderTime, double x, double y, double z) {
            this.lastSampleTick = (int) renderTime;
            this.lastSeenTime = renderTime;
        }
    }

    private static class BreathWisp {

        private final double x;
        private final double y;
        private final double z;
        private final int color;
        private final float birthTime;

        private BreathWisp(double x, double y, double z, int color, float birthTime) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.color = color;
            this.birthTime = birthTime;
        }
    }
}
