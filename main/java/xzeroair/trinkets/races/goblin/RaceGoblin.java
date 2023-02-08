package xzeroair.trinkets.races.goblin;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.client.model.GoblinEars;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.abilities.other.AbilityWolfMount;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class RaceGoblin extends EntityRacePropertiesHandler {

	public static final GoblinConfig serverConfig = TrinketsConfig.SERVER.races.goblin;
	//	private int cooldown = 0;

	public RaceGoblin(@Nonnull EntityLivingBase e) {
		super(e, EntityRaces.goblin);
	}

	@Override
	public void startTransformation() {
		this.addAbility(new AbilityClimbing());
		this.addAbility(new AbilityWolfMount());
	}

	@Override
	public void whileTransformed() {
	}

	@Override
	public void targetedByEnemy(EntityLivingBase enemy) {
		if (serverConfig.friendly_creepers) {
			if (enemy instanceof EntityCreeper) {
				((EntityCreeper) enemy).setAttackTarget(null);
			}
		}
	}

	@Override
	public float isHurt(DamageSource source, float dmg) {
		if (source.isExplosion()) {
			if (!TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketDamageShield)) {
				final float maxHP = entity.getHealth();
				final float modifier = ((maxHP * 100) / 20F) * 0.01F;
				final float clampModifier = MathHelper.clamp(modifier, 0.01F, 1F);
				return dmg * clampModifier;
			}
		}
		if (source.isFireDamage()) {
			float modifier = 0.8F;
			if (source.getDamageType().equalsIgnoreCase("Lava")) {
				modifier = 0.5f;
			}
			return dmg * modifier;
		}
		return dmg;
	}

	@Override
	public boolean attackedEntity(EntityLivingBase target, DamageSource source, float dmg) {
		boolean attack = super.attackedEntity(target, source, dmg);
		if (serverConfig.creepers_explode) {
			if (attack) {
				if (target instanceof EntityCreeper) {
					if (!((EntityCreeper) target).hasIgnited()) {
						((EntityCreeper) target).ignite();
					}
					if (((EntityCreeper) target).getCreeperState() == -1) {
						((EntityCreeper) target).setCreeperState(1);
					}
				}
			}
		}
		return attack;
	}

	private final ModelBase ears = new GoblinEars();
	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/ears.png");
	public static final ResourceLocation TEXTURE_INNER = new ResourceLocation(Reference.MODID + ":" + "textures/inner_ears.png");
	public static final ResourceLocation TEXTURE_OUTER = new ResourceLocation(Reference.MODID + ":" + "textures/outer_ears.png");

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering) {
			return;
		}
		if (this.showTraits()) {
			GlStateManager.pushMatrix();
			if (entity.isSneaking()) {
				GlStateManager.translate(0, 0.2, 0);
			}
			if (renderer instanceof RenderPlayer) {
				final RenderPlayer rend = (RenderPlayer) renderer;
				rend.getMainModel().bipedHead.postRender(scale);
			}
			if (entity.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
				GlStateManager.translate(0.0F, -0.02F, -0.045F);
				GlStateManager.scale(1.1F, 1.1F, 1.1F);
			}
			final float[] rgb = ColorHelper.getRGBColor(this.getTraitVariant() == 1 ? this.getAltTraitColor() : this.getTraitColor());
			final float[] rgb2 = ColorHelper.getRGBColor(this.getTraitVariant() == 1 ? this.getTraitColor() : this.getAltTraitColor());
			final float fscale = 0.34F;
			GlStateManager.scale(fscale, fscale, fscale);
			final double x = 0.0;
			final double y = -1.2;
			final double z = -0.6;
			final double height = 1;
			final double width = 1;
			final float u = 0;
			final float v = 0;
			final int uWidth = 16;
			final int vHeight = 16;
			final float tileWidth = 64;
			final float tileHeight = 32;
			final double xR = 0.50;
			final double xL = -xR;
			float rot = 30F;
			final int solidVariant = 2;
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

			GlStateManager.pushMatrix();
			GlStateManager.rotate(-rot, 0, 1, 0);
			GlStateManager.rotate(-10, 1, 0, 0);
			//			GlStateManager.rotate(20, 0, 0, 1);
			if (this.getTraitVariant() == solidVariant) {
				DrawingHelper.Draw(TEXTURE, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
				DrawingHelper.Draw(TEXTURE, x + xR, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
			} else {
				DrawingHelper.Draw(TEXTURE_INNER, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
				DrawingHelper.Draw(TEXTURE_OUTER, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
				DrawingHelper.Draw(TEXTURE_OUTER, x + xR, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.rotate(rot, 0, 1, 0);
			GlStateManager.rotate(-10, 1, 0, 0);
			//			GlStateManager.rotate(-20, 0, 0, 1);
			if (this.getTraitVariant() == solidVariant) {
				DrawingHelper.Draw(TEXTURE, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
				DrawingHelper.Draw(TEXTURE, x + xL, y, z + 0.0001, u, v + 16, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
			} else {
				DrawingHelper.Draw(TEXTURE_INNER, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
				DrawingHelper.Draw(TEXTURE_OUTER, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
				DrawingHelper.Draw(TEXTURE_OUTER, x + xL, y, z + 0.0001, u, v + 16, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
			}
			GlStateManager.popMatrix();

			GlStateManager.enableLighting();
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.popMatrix();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		//		super.doRenderPlayerPre(entity, x, y, z, renderer, partialTick);
		final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if ((entity == Minecraft.getMinecraft().player) && (screen != null) && !((screen instanceof GuiChat) || (screen instanceof GuiEntityProperties) || (screen instanceof ManaHud))) {
			return;
		}
		if ((this.isTransforming() || this.isTransformed()) && !properties.isNormalSize()) {
			final double hScale = properties.getHeightValue() * 0.01D;
			final double wScale = properties.getWidthValue() * 0.01D;
			final double xLoc = (x / wScale) - x;
			final double yLoc = (y / hScale) - y;
			final double zLoc = (z / wScale) - z;

			final double yOffset = entity.getYOffset();
			final Entity mount = entity.getRidingEntity();
			//			double vanillaOffset = mount.posY + mount.getMountedYOffset() + entity.getYOffset();// + 0.15 * prevRearingAmount
			final double mountedOffset = entity.isRiding() && (mount != null) ? (mount.getMountedYOffset()) : 0;
			//			final double offsetDifference = entity.isRiding() && (mount != null) ? (mount.height - mountedOffset) : 0;
			//						final double retMountedOffset = mountedOffset - ((offsetDifference) * 0.66D);
			final double retMountedOffset = -(mountedOffset + yOffset) - 0.1D;
			if (entity.isRiding() && !(mount instanceof AlphaWolf)) {
				GlStateManager.translate(0, mountedOffset, 0);
				GlStateManager.translate(0, -yOffset, 0);
				GlStateManager.translate(0, retMountedOffset, 0);
			}
			GlStateManager.scale(wScale, hScale, wScale);
			if (entity.isRiding() && !(mount instanceof AlphaWolf)) {
				GlStateManager.translate(0, -retMountedOffset, 0);
				GlStateManager.translate(0, yOffset, 0);
				GlStateManager.translate(0, -mountedOffset, 0);
			}
			GlStateManager.translate(xLoc, yLoc, zLoc);
		}
	}

}
