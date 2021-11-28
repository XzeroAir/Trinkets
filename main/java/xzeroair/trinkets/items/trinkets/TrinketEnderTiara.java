package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.model.Tiara;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityEnderQueen;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Crown;
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
	public void initAbilities(EntityLivingBase entity) {
		this.addAbility(entity, Abilities.enderQueen, new AbilityEnderQueen());
		if ((Trinkets.ToughAsNails || Trinkets.SimpleDifficulty) && serverConfig.compat.tan.immuneToCold) {
			this.addAbility(entity, Abilities.survivalColdImmunity, new AbilityColdImmunity());
		}
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
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
	public void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale, boolean isBauble) {
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
