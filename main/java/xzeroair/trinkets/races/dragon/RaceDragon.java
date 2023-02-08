package xzeroair.trinkets.races.dragon;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.traits.abilities.AbilityBlockFinder;
import xzeroair.trinkets.traits.abilities.AbilityFireBreathing;
import xzeroair.trinkets.traits.abilities.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.AbilityFlying;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class RaceDragon extends EntityRacePropertiesHandler {

	public static final DragonConfig serverConfig = TrinketsConfig.SERVER.races.dragon;

	public RaceDragon(@Nonnull EntityLivingBase e) {
		super(e, EntityRaces.dragon);
	}

	@Override
	public void startTransformation() {
		this.addAbility(new AbilityNightVision().toggleAbility(true));
		this.addAbility(new AbilityFireImmunity());
		if (TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder) {
			this.addAbility(new AbilityBlockFinder());
		}
		if (serverConfig.creative_flight) {
			this.addAbility(
					new AbilityFlying()
							.setFlightEnabled(serverConfig.creative_flight)
							.setSpeedEnabled(serverConfig.creative_flight_speed)
							.setFlightSpeed((float) serverConfig.flight_speed)
							.setFlightCost(serverConfig.flight_cost)
			);
		}
		if (serverConfig.breath_damage > 0) {
			this.addAbility(new AbilityFireBreathing());
		}
		if ((Trinkets.ToughAsNails || Trinkets.SimpleDifficulty) && serverConfig.compat.tan.immuneToHeat) {
			this.addAbility(new AbilityHeatImmunity());
		}
	}

	@Override
	public void whileTransformed() {
		if (entity.world.isRemote) {
			if (!entity.onGround) {
				lastTick = tick;
				tick += 48;
			}
			if ((tick >= (1210)) || (entity.onGround)) {
				tick = 0;
				lastTick = 0;
			}
		}
	}

	@Override
	public boolean canFly() {
		return super.canFly() && this.showTraits();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Client~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/dragon_wings.png");
	public static final ResourceLocation TEXTURE_ARMS = new ResourceLocation(Reference.MODID + ":" + "textures/dragon_wings_arms.png");
	public static final ResourceLocation TEXTURE_LEATHER = new ResourceLocation(Reference.MODID + ":" + "textures/dragon_wings_leather.png");

	protected int tick, lastTick = 0;
	protected float armSwing = 0;
	protected int wingFrames = 0;

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering || (!this.showTraits())) {
			return;
		}
		GlStateManager.pushMatrix();
		//			float flap = MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI);
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

		final int solidVariant = 2;
		final int base = entity.onGround ? 40 : 50;
		final int wing = 20;
		final int tip = -20;
		final int rotX = 0;
		final int rotZ = -10;

		final double[][] frames = new double[121][10];

		wingFrames = frames.length;

		final float tickT = (lastTick + ((tick - lastTick) * partialTicks));
		int angleTick = (int) (tickT * 0.1F);
		this.getWingFrames(angleTick, base, wing, tip, rotX, rotZ, frames);
		if (angleTick >= (frames.length)) {
			angleTick = frames.length - 1;
		}

		final double x = -12;
		final double y = isSlim ? -19 : -20;
		final double z = 0;
		final int width = 8;
		final int height = 32;
		final int uWidth = 16;
		final int vHeight = 64;
		final int texWidth = 64;
		final int texHeight = 64;

		final int innerWidth = 16;
		final int innerHeight = 32;
		final int innerUWidth = 32;
		final int innerVHeight = 64;
		final int innerTexWidth = 64;
		final int innerTexHeight = 64;

		final int outerwidth = 8;
		final int outHeight = 32;
		final int outerUWidth = 16;
		final int outerVHeight = 64;
		final int outerTexWidth = 64;
		final int outerTexHeight = 64;

		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		//		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.pushMatrix();
		final float[] rgb = ColorHelper.getRGBColor(this.getTraitVariant() == 1 ? this.getAltTraitColor() : this.getTraitColor());
		final float[] rgb2 = ColorHelper.getRGBColor(this.getTraitVariant() == 1 ? this.getTraitColor() : this.getAltTraitColor());
		final float alpha = 1F;
		GlStateManager.scale(0.9, 0.9, 0.9);
		GlStateManager.rotate((float) frames[angleTick][0], 0, 1, 0);
		if (this.getTraitVariant() == solidVariant) {
			DrawingHelper.Draw(TEXTURE, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
		} else {
			DrawingHelper.Draw(TEXTURE_ARMS, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
			DrawingHelper.Draw(TEXTURE_LEATHER, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
		}
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate((float) frames[angleTick][1], 0, 1, 0);
		GlStateManager.translate(-x, -y, -z);
		if (this.getTraitVariant() == solidVariant) {
			DrawingHelper.Draw(TEXTURE, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
		} else {
			DrawingHelper.Draw(TEXTURE_ARMS, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
			DrawingHelper.Draw(TEXTURE_LEATHER, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
		}
		GlStateManager.translate(x - innerWidth, y, z);
		GlStateManager.rotate((float) (frames[angleTick][1] + (float) frames[angleTick][2]), 0, 1, 0);
		GlStateManager.translate(-(x - innerWidth), -y, -z);
		if (this.getTraitVariant() == solidVariant) {
			DrawingHelper.Draw(TEXTURE, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
		} else {
			DrawingHelper.Draw(TEXTURE_ARMS, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
			DrawingHelper.Draw(TEXTURE_LEATHER, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
		}
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.9, 0.9, 0.9);
		GlStateManager.rotate((float) -frames[angleTick][0], 0, 1, 0);
		if (this.getTraitVariant() == solidVariant) {
			DrawingHelper.Draw(TEXTURE, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
		} else {
			DrawingHelper.Draw(TEXTURE_ARMS, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
			DrawingHelper.Draw(TEXTURE_LEATHER, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
		}
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate((float) -frames[angleTick][1], 0, 1, 0);
		GlStateManager.translate(-x, -y, -z);
		if (this.getTraitVariant() == solidVariant) {
			DrawingHelper.Draw(TEXTURE, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
		} else {
			DrawingHelper.Draw(TEXTURE_ARMS, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
			DrawingHelper.Draw(TEXTURE_LEATHER, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
		}
		GlStateManager.translate(x - innerWidth, y, z);
		GlStateManager.rotate((float) -(frames[angleTick][1] + (float) frames[angleTick][2]), 0, 1, 0);
		GlStateManager.translate(-(x - innerWidth), -y, -z);
		if (this.getTraitVariant() == solidVariant) {
			DrawingHelper.Draw(TEXTURE, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
		} else {
			DrawingHelper.Draw(TEXTURE_ARMS, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
			DrawingHelper.Draw(TEXTURE_LEATHER, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
		}
		GlStateManager.popMatrix();

		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		//		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}

	private double[][] getWingFrames(int tickPos, double base, double wing, double tip, double rotX, double rotZ, double[][] frames) {
		frames[0] = new double[] { base, wing, tip, rotX, rotZ };
		int position = 1;
		final int s = this.frames(15);
		for (int i1 = 0; i1 < s; i1++) {
			frames[position][0] = base - i1;
			frames[position][1] = wing - i1;
			frames[position][2] = tip;
			frames[position][3] = rotX;
			frames[position][4] = rotZ;
			position++;
		}
		final int pos1 = position;
		final int s1 = this.frames(10);
		for (int i1 = 0; i1 < s1; i1++) {
			frames[position][0] = frames[pos1 - 1][0] - i1;
			frames[position][1] = frames[pos1 - 1][1] - i1;
			frames[position][2] = frames[pos1 - 1][2];
			frames[position][3] = frames[pos1 - 1][3];
			frames[position][4] = frames[pos1 - 1][4];
			position++;
		}
		final int pos2 = position;
		final int s2 = this.frames(10);
		for (int i1 = 0; i1 < s2; i1++) {
			frames[position][0] = frames[pos2 - 1][0];
			frames[position][1] = frames[pos2 - 1][1] + i1;
			frames[position][2] = frames[pos2 - 1][2];
			frames[position][3] = frames[pos2 - 1][3] + (i1);
			frames[position][4] = frames[pos2 - 1][4] - (i1);
			position++;
		}
		final int pos3 = position;
		final int s3 = this.frames(10);
		for (int i1 = 0; i1 < s3; i1++) {
			frames[position][0] = frames[pos3 - 1][0] + i1;
			frames[position][1] = frames[pos3 - 1][1] + i1;
			frames[position][2] = frames[pos3 - 1][2];
			frames[position][3] = frames[pos3 - 1][3] + (i1);
			frames[position][4] = frames[pos3 - 1][4];
			position++;
		}
		final int pos4 = position;
		final int s4 = this.frames(10);
		for (int i1 = 0; i1 < s4; i1++) {
			frames[position][0] = frames[pos4 - 1][0] + i1;
			frames[position][1] = frames[pos4 - 1][1] + i1;
			frames[position][2] = frames[pos4 - 1][2];
			frames[position][3] = frames[pos4 - 1][3] + i1;
			frames[position][4] = frames[pos4 - 1][4];
			position++;
		}
		final int pos5 = position;
		final int s5 = this.frames(10);
		for (int i1 = 0; i1 < s5; i1++) {
			frames[position][0] = frames[pos5 - 1][0] + i1;
			frames[position][1] = frames[pos5 - 1][1] + i1;
			frames[position][2] = frames[pos5 - 1][2];
			frames[position][3] = frames[pos5 - 1][3];
			frames[position][4] = frames[pos5 - 1][4];
			position++;
		}
		final int pos6 = position;
		final int s6 = this.frames(10);
		for (int i1 = 0; i1 < s6; i1++) {
			frames[position][0] = frames[pos6 - 1][0];
			frames[position][1] = frames[pos6 - 1][1] + i1;
			frames[position][2] = frames[pos6 - 1][2];
			frames[position][3] = frames[pos6 - 1][3];
			frames[position][4] = frames[pos6 - 1][4];
			position++;
		}
		final int pos7 = position;
		final int s7 = this.frames(10);
		for (int i1 = 0; i1 < s7; i1++) {
			frames[position][0] = frames[pos7 - 1][0];
			frames[position][1] = frames[pos7 - 1][1] + i1;
			frames[position][2] = frames[pos7 - 1][2] - i1;
			frames[position][3] = frames[pos7 - 1][3];
			frames[position][4] = frames[pos7 - 1][4];
			position++;
		}
		final int pos8 = position;
		final int s8 = this.frames(10);
		for (int i1 = 0; i1 < s8; i1++) {
			frames[position][0] = frames[pos8 - 1][0];
			frames[position][1] = frames[pos8 - 1][1] - i1;
			frames[position][2] = frames[pos8 - 1][2];
			frames[position][3] = frames[pos8 - 1][3] - (i1);
			frames[position][4] = frames[pos8 - 1][4] + (i1);
			position++;
		}
		final int pos9 = position;
		final int s9 = this.frames(15);
		for (int i1 = 0; i1 < s9; i1++) {
			frames[position][0] = frames[pos9 - 1][0];
			frames[position][1] = frames[pos9 - 1][1] - i1;
			frames[position][2] = frames[pos9 - 1][2] - i1;
			frames[position][3] = frames[pos9 - 1][3] - (i1);
			frames[position][4] = frames[pos9 - 1][4];
			position++;
		}
		final int pos10 = position;
		final int s10 = this.frames(10);
		for (int i1 = 0; i1 < s10; i1++) {
			frames[position][0] = frames[pos10 - 1][0];
			frames[position][1] = frames[pos10 - 1][1] - i1;
			frames[position][2] = frames[pos10 - 1][2] + i1;
			frames[position][3] = frames[pos10 - 1][3];
			frames[position][4] = frames[pos10 - 1][4];
			position++;
		}
		//		System.out.println(wingFrames);
		//		int posL = position;
		//		int sL = this.frames(1000);
		//		for (int i1 = 0; i1 < sL; i1++) {
		//			frames[position][0] = frames[posL - 1][0];
		//			frames[position][1] = frames[posL - 1][1];
		//			frames[position][2] = frames[posL - 1][2];
		//			frames[position][3] = frames[posL - 1][3];
		//			frames[position][4] = frames[posL - 1][4];
		//			position++;
		//		}
		return frames;
	}

	private double[][] section(int length, int position, int savedPos, int incBase, int incWing, int incTip, int incRotX, int incRotY, double[][] frames) {
		final int sL = this.frames(length);
		for (int i1 = 0; i1 < sL; i1++) {
			frames[position][0] = frames[savedPos - 1][0] + incBase;
			frames[position][1] = frames[savedPos - 1][1] + incWing;
			frames[position][2] = frames[savedPos - 1][2] + incTip;
			frames[position][3] = frames[savedPos - 1][3] + incRotX;
			frames[position][4] = frames[savedPos - 1][4] + incRotY;
		}
		return frames;
	}

	private int frames(int amount) {
		if ((wingFrames - amount) >= 0) {
			wingFrames -= amount;
			return amount;
		} else {
			if (wingFrames > 0) {
				return wingFrames;
			} else {
				return 0;
			}
		}
	}
}