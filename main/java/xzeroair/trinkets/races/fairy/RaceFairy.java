package xzeroair.trinkets.races.fairy;

import java.util.Arrays;
import java.util.List;

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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.Wings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.abilities.AbilityFlying;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Fairy;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class RaceFairy extends EntityRacePropertiesHandler {

	public static final FairyConfig serverConfig = TrinketsConfig.SERVER.races.fairy;
	public static final Fairy clientConfig = TrinketsConfig.CLIENT.items.FAIRY_RING;
	public static List<String> disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);

	public RaceFairy(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.fairy);
		disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);
	}

	@Override
	public void startTransformation() {
		if (serverConfig.creative_flight) {
			this.addAbility(
					Abilities.creativeFlight, new AbilityFlying()
							.setFlightEnabled(serverConfig.creative_flight)
							.setSpeedEnabled(serverConfig.creative_flight_speed)
							.setFlightSpeed((float) serverConfig.flight_speed)
							.setFlightCost(serverConfig.flight_cost)
			);
		}
		if (serverConfig.climbing) {
			this.addAbility(Abilities.blockClimbing, new AbilityClimbing());
		}
	}

	@Override
	public void whileTransformed() {
		if (!entity.world.isRemote && entity.isRiding()) {
			final Entity mount = entity.getRidingEntity();
			if ((mount != null) && !this.mountEntity(mount)) {
				entity.dismountRidingEntity();
			}
		}
	}

	@Override
	public boolean mountEntity(Entity mount) {
		if (this.isCreativePlayer()) {
			return true;
		} else if (!serverConfig.canMount) {
			return false;
		} else if (!disallowedMounts.isEmpty()) {
			try {
				final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
				final String modID = regName.getNamespace();
				final String entityID = regName.getPath();
				final boolean exists = disallowedMounts.contains(modID + ":*") || disallowedMounts.contains(regName.toString());
				if (exists) {
					if (!serverConfig.canControlBoats && (mount instanceof EntityBoat)) {
						final EntityBoat boat = (EntityBoat) mount;
						final Entity controller = boat.getControllingPassenger();
						if ((controller == null) || (controller == entity)) {
							return false;
						}
					}
					return serverConfig.whitelist;
				} else {
					if (!serverConfig.canControlBoats && (mount instanceof EntityBoat)) {
						final EntityBoat boat = (EntityBoat) mount;
						final Entity controller = boat.getControllingPassenger();
						if ((controller == null) || (controller == entity)) {
							return false;
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return !serverConfig.whitelist;
		} else {
			if (!serverConfig.canControlBoats && (mount instanceof EntityBoat)) {
				final EntityBoat boat = (EntityBoat) mount;
				final Entity controller = boat.getControllingPassenger();
				if ((controller == null) || (controller == entity)) {
					return false;
				}
			}
			return true;
		}
	}
	//	@Override
	//	public boolean mountEntity(Entity mount) {
	//		if (!serverConfig.canMount) {
	//			return false;
	//		}
	//		try {
	//			final String regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName().toString();
	//			final boolean nope = !disallowedMounts.isEmpty() && disallowedMounts.contains(regName);
	//			if (nope) {
	//				return false;
	//			}
	//		} catch (final Exception e) {
	//			e.printStackTrace();
	//		}
	//		if ((mount instanceof EntityBoat)) {
	//			final EntityBoat boat = (EntityBoat) mount;
	//			final Entity controller = boat.getControllingPassenger();
	//			if ((controller != null) && (controller instanceof EntityPlayer)) {
	//			} else {
	//				return false;
	//			}
	//		} else {
	//			//			if (mount.height >= (entity.height * 1.4F)) {
	//			//				return false;
	//			//			}
	//		}
	//		return true;
	//	}

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings.png");

	int tick = 0;
	float armSwing = 0;

	private final ModelBase wings = new Wings();

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering) {
			return;
		}
		GlStateManager.pushMatrix();
		if (isFake) {
			if (properties.showTraits()) {
				GlStateManager.pushMatrix();
				if (renderer instanceof RenderPlayer) {
					final RenderPlayer r = (RenderPlayer) renderer;
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
			}
		} else {
			if (properties.showTraits()) {
				final float flap = MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI);
				final int angle = 54;
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
					final RenderPlayer rend = (RenderPlayer) renderer;
					rend.getMainModel().bipedBody.postRender(scale);
				}
				GlStateManager.scale(scale, scale, scale);
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.translate(0, -2F, 0);
				if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
					GlStateManager.translate(-0.4F, -1F, 0F);
				}

				final double x = 18;
				final double y = 0;
				final double z = -1;

				final int barWidth = 16;
				final int barHeight = 16;
				final int barCutoffWidth = 36;
				final int barCutoffHeight = 42;
				final int texWidth = 36;
				final int texHeight = 42;

				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.enableBlend();
				GlStateManager.rotate(angle - tick, 0, 1, 0);
				GlStateManager.translate(-1, 0, 0);
				GlStateManager.color(properties.getTraitColorHandler().getRed(), properties.getTraitColorHandler().getGreen(), properties.getTraitColorHandler().getBlue());
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
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		if (entity.isRiding()) {
			final double t = 1.8;
			GlStateManager.translate(0, t, 0);
		}
	}

}
