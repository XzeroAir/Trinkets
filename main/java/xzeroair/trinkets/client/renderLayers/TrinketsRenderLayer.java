package xzeroair.trinkets.client.renderLayers;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketsRenderLayer implements LayerRenderer<EntityPlayer> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings.png");
	public static final ResourceLocation TEXTURE_GREY = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings_grey.png");
	public static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings_yellow.png");
	public static final ResourceLocation TEXTURE_GREEN = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings_green.png");
	public static final ResourceLocation TEXTURE_WHITE = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings_white.png");

	int tick = 0;
	float armSwing = 0;

	@Override
	public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		if ((player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) || !TrinketsConfig.CLIENT.rendering) {
			return;
		}

		RaceProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			final float yaw = player.prevRotationYawHead + ((player.rotationYawHead - player.prevRotationYawHead) * partialTicks);
			final float yawOffset = player.prevRenderYawOffset + ((player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
			final float pitch = player.prevRotationPitch + ((player.rotationPitch - player.prevRotationPitch) * partialTicks);

			GlStateManager.pushMatrix();
			if (TrinketsConfig.CLIENT.FAIRY_RING.doRender) {
				GlStateManager.pushMatrix();
				List<Item> CheckList = TrinketHelper.getIncompatibleRaceTrinket(ModItems.trinkets.TrinketFairyRing);
				if ((!TrinketHelper.AccessoryCheck(player, CheckList)) && TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing)) {
					Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
					EffectsFairyRing.renderFairy(player, partialTicks, cap);
				} else if (EntityRaceHelper.getRace(player).contentEquals("fairy_dew") && !TrinketHelper.AccessoryCheck(player, CheckList)) {
					final int time = 0;
					final float f = 1.0F;
					final float flap = MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI);
					if (player.capabilities.isFlying || (flap != this.armSwing)) {
						this.tick++;
						//						this.tick++;
					}
					if (((this.tick > 45) || ((flap == this.armSwing) && !player.capabilities.isFlying))) {
						this.tick = 0;
					}
					final float r = 1f;
					final float g = 1f;
					final float b = 1f;
					final float a = 0.5f;

					Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_YELLOW);

					GlStateManager.scale(0.04f, 0.04f, 0.04f);
					GlStateManager.rotate(90, 0, 1, 0);
					GlStateManager.rotate(4, 0, 0, 1);
					GlStateManager.translate(-4, -12, 0);
					if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
						GlStateManager.translate(0F, 0F, 0.06F);
					}

					GlStateManager.enableAlpha();
					GlStateManager.enableBlend();

					double x = 29;
					double y = 0;
					double z = 2;

					if (player.isSneaking()) {
						x = 22;
						y = 0;
						z = 8;
						GlStateManager.rotate(90F / (float) Math.PI, 0.0F, 0.0F, 1.0F);
					}
					if (player.isSprinting()) {
						GlStateManager.rotate(25F / (float) Math.PI, 0.0F, 0.0F, 1.0F);
					}

					GlStateManager.pushMatrix();
					GlStateManager.translate(0, 0, 0);
					GlStateManager.rotate(45 - this.tick, 0, 1, 0);
					Draw(-x, y, z, 0, 0, 28, 48, 0.0208333333333333f, r, g, b, a);
					GlStateManager.popMatrix();

					GlStateManager.pushMatrix();
					GlStateManager.translate(0, 0, 0);
					GlStateManager.rotate(-45 + this.tick, 0, 1, 0);
					Draw(-x, y, -z, 0, 0, 28, 48, 0.0208333333333333f, r, g, b, a);
					GlStateManager.popMatrix();
					this.armSwing = flap;
				}
				GlStateManager.popMatrix();
			}
			if (Loader.isModLoaded("baubles")) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
				for (int i = 0; i < baubles.getSlots(); i++) {
					final ItemStack stack = baubles.getStackInSlot(i);
					if (stack.getItem() instanceof IAccessoryInterface) {
						final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
						GlStateManager.pushMatrix();
						GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
						GlStateManager.color(1F, 1F, 1F, 1F);
						trinket.playerRender(stack, player, partialTicks, false);
						GlStateManager.popMatrix();
					}
				}
			}
			final ITrinketContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			if (Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					final ItemStack stack = Trinket.getStackInSlot(i);
					if (stack.getItem() instanceof IAccessoryInterface) {
						final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
						GlStateManager.pushMatrix();
						GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
						GlStateManager.color(1F, 1F, 1F, 1F);
						trinket.playerRender(stack, player, partialTicks, false);
						GlStateManager.popMatrix();
					}
				}
			}
			GlStateManager.popMatrix();
		}
	}

	public static void Draw(double x, double y, double z, int texX, int texY, float width, float height, float scale, float r, float g, float b, float a) {
		final int l1 = 240;
		final int l2 = 0;
		final Tessellator tes = Tessellator.getInstance();
		final BufferBuilder buffer = tes.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		// Bottom Left
		buffer.pos(x + 0, y + height, z).tex((texX + 0) * scale, (texY + height) * scale).color(r, g, b, a).endVertex();
		// bottom right
		buffer.pos(x + width, y + height, z).tex((texX + width) * scale, (texY + height) * scale).color(r, g, b, a).endVertex();
		// top right
		buffer.pos(x + width, y + 0, z).tex((texX + width) * scale, (texY + 0) * scale).color(r, g, b, a).endVertex();
		// top left
		buffer.pos(x + 0, y + 0, z).tex((texX + 0) * scale, (texY + 0) * scale).color(r, g, b, a).endVertex();
		tes.draw();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}