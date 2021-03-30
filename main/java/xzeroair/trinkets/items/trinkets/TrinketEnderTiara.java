package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.Tiara;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Crown;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;

public class TrinketEnderTiara extends AccessoryBase {

	public static final ConfigEnderCrown serverConfig = TrinketsConfig.SERVER.Items.ENDER_CROWN;
	public static final Crown clientConfig = TrinketsConfig.CLIENT.items.ENDER_CROWN;

	public TrinketEnderTiara(String name) {
		super(name);
		this.setUUID("a45dbc1c-17e9-40b4-b6a3-09dea74355b7");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		LycanitesCompat.removeInstability(player);
		EntityProperties prop = Capabilities.getEntityRace(player);
		if (serverConfig.water_hurts) {
			if ((player.isInWater() || player.isWet())) {
				if ((player.ticksExisted % 20) == 0) {
					if ((prop != null) && (prop.getMagic().spendMana(5F))) {

					} else {
						if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye)) {
							player.attackEntityFrom(DamageSource.WITHER, 4);
						} else {
							player.attackEntityFrom(DamageSource.WITHER, 2);
						}
					}
				}
			}
		}

		if (serverConfig.compat.tan.immuneToCold) {
			SurvivalCompat.immuneToCold(player);
		}
	}

	@Override
	public void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		//		if (event.getSource().getDamageType().equalsIgnoreCase("dragon_ice")) {
		//			event.setCanceled(true);
		//		}
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {
		List<Potion> hypothermia = SurvivalCompat.getHypothermiaEffects();
		if (!hypothermia.isEmpty()) {
			for (Potion hypo : hypothermia) {
				if ((hypo != null) && event.getPotionEffect().getPotion().equals(hypo)) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
	}

	ModelBase tiara = new Tiara();

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, float partialTicks, float scale, boolean isBauble) {
		if (!clientConfig.doRender) {
			return;
		}
		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0, 0.2, 0);
		}
		renderer.getMainModel().bipedHead.postRender(scale);
		if (player.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
			GlStateManager.translate(0.0F, -0.02F, -0.045F);
			GlStateManager.scale(1.1F, 1.1F, 1.1F);
		}
		tiara.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1F);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
