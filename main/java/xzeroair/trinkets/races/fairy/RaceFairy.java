package xzeroair.trinkets.races.fairy;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.Wings;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Fairy;
import xzeroair.trinkets.util.handlers.ClimbHandler;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class RaceFairy extends EntityRacePropertiesHandler {

	public static final FairyConfig serverConfig = TrinketsConfig.SERVER.races.fairy;
	public static final Fairy clientConfig = TrinketsConfig.CLIENT.items.FAIRY_RING;
	private ClimbHandler climbing;

	public RaceFairy(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.fairy);
		climbing = new ClimbHandler(e, e.world);
	}

	@Override
	public void endTransformation() {
		this.removeFlyingAbility();
	}

	private void addFlyingAbility() {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (!player.isCreative() && (serverConfig.creative_flight == true)) {
				if ((player.capabilities.allowFlying != true)) {
					player.capabilities.allowFlying = true;
					if (serverConfig.creative_flight_speed && player.world.isRemote) {
						player.capabilities.setFlySpeed((float) serverConfig.flight_speed);
					}
				}
			}
		}
	}

	private void removeFlyingAbility() {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (!player.isCreative() && (serverConfig.creative_flight == true)) {
				if ((player.capabilities.allowFlying == true)) {
					player.capabilities.isFlying = false;
					player.capabilities.allowFlying = false;
					if (serverConfig.creative_flight_speed && player.world.isRemote) {
						if (player.capabilities.getFlySpeed() != 0.05F) {
							player.capabilities.setFlySpeed(0.05f);
						}
					}
				}
			}
		}
	}

	@Override
	public void whileTransformed() {
		if (entity instanceof EntityPlayer) { // Start Player Effects
			EntityPlayer player = (EntityPlayer) entity;
			if (properties.showTraits()) {
				this.addFlyingAbility();
			} else {
				this.removeFlyingAbility();
			}
			if ((player.getRidingEntity() instanceof EntityBoat)) {
				EntityBoat boat = (EntityBoat) player.getRidingEntity();
				Entity controller = boat.getControllingPassenger();
				if (controller == player) {
					player.dismountRidingEntity();
				}

			}
			if (((serverConfig.climbing != false))) {
				if (!player.onGround) {
					if (player.collidedHorizontally) {
						if (climbing.canClimb()) {
							if (!player.isSneaking()) {
								player.motionY = 0.1f;
							}
							if (player.isSneaking()) {
								player.motionY = 0f;
							}
						}
					}
				}
			}
		} else { // End player effects
		}
	}

	@Override
	public void mountedEntity(EntityMountEvent event) {
		if (event.isMounting() && (event.getEntityBeingMounted() instanceof EntityBoat)) {
			EntityBoat boat = (EntityBoat) event.getEntityBeingMounted();
			Entity controller = boat.getControllingPassenger();
			if ((controller != null) && (controller instanceof EntityPlayer)) {
			} else {
				event.setCanceled(true);
			}
		}
	}

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings.png");
	int tick = 0;
	float armSwing = 0;

	private ModelBase wings = new Wings();

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering) {
			return;
		}
		GlStateManager.pushMatrix();
		if (isFake) {
			GlStateManager.pushMatrix();
			if (renderer instanceof RenderPlayer) {
				RenderPlayer r = (RenderPlayer) renderer;
				r.getMainModel().bipedBody.postRender(scale);
			}
			if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
				GlStateManager.translate(0F, -0.1F, 0.06F);
				GlStateManager.scale(1.1F, 1.1F, 1.1F);
			}
			if (entity.isSneaking()) {
				GlStateManager.translate(0F, 0F, -0.1F);
			}
			GlStateManager.scale(scale, scale, scale);
			wings.render(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, entity.rotationYaw, entity.rotationPitch, 1);
			GlStateManager.popMatrix();
		} else {
			if (properties.showTraits()) {
				final float flap = MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI);
				int angle = 54;
				if (!entity.onGround || (flap != armSwing)) {
					tick += 1 * 6;
				}
				if (((tick > angle) || ((flap == armSwing) && entity.onGround))) {
					tick = 0;
				}

				Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
				if (entity.isSneaking()) {
					GlStateManager.translate(0F, 0.2F, 0F);
				}
				if (renderer instanceof RenderPlayer) {
					RenderPlayer rend = (RenderPlayer) renderer;
					rend.getMainModel().bipedBody.postRender(scale);
				}
				GlStateManager.scale(scale, scale, scale);
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.translate(0, -2F, 0);
				if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
					GlStateManager.translate(-0.4F, -1F, 0F);
				}

				double x = 18;
				double y = 0;
				double z = -1;

				int barWidth = 16;
				int barHeight = 16;
				int barCutoffWidth = 36;
				int barCutoffHeight = 42;
				int texWidth = 36;
				int texHeight = 42;

				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.enableBlend();
				GlStateManager.rotate(angle - tick, 0, 1, 0);
				GlStateManager.translate(-1, 0, 0);
				GlStateManager.color(properties.getTraitColorHandler().getRed(), properties.getTraitColorHandler().getGreen(), properties.getTraitColorHandler().getBlue(), properties.getTraitOpacity());
				DrawingHelper.Draw(-x, y, z, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight);
				GlStateManager.popMatrix();

				GlStateManager.pushMatrix();
				GlStateManager.rotate(-angle + tick, 0, 1, 0);
				GlStateManager.translate(-1, 0, 0);
				DrawingHelper.Draw(-x, y, -z, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight);
				GlStateManager.enableLighting();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
				armSwing = flap;
			}
		}
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(RenderPlayerEvent.Pre event) {
		if (entity.isRiding()) {
			double t = 1.8;
			GlStateManager.translate(0, t, 0);
		}
	}

}
