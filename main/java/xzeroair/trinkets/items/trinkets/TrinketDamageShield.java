package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityResistance;
import xzeroair.trinkets.traits.abilities.compat.firstaid.AbilityIgnoreHeadshot;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Shield;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;

public class TrinketDamageShield extends AccessoryBase {

	public static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;
	public static final Shield clientConfig = TrinketsConfig.CLIENT.items.DAMAGE_SHIELD;

	public TrinketDamageShield(String name) {
		super(name);
		this.setUUID("c0885371-20dd-4c56-86eb-78f24d9fe777");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void initAbilities(EntityLivingBase entity) {
		this.addAbility(entity, Abilities.safeGuard, new AbilityResistance());
		if (Trinkets.FirstAid && serverConfig.compat.firstaid.chance_ignore) {
			this.addAbility(entity, Abilities.firstAidReflex, new AbilityIgnoreHeadshot());
		}
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		try {
			final VipStatus status = Capabilities.getVipStatus(player);
			if (status != null) {
				if (this.stackHasStatus(stack)) {
					if (this.getTagCompoundSafe(stack).getInteger("status") != status.getStatus()) {
						this.getTagCompoundSafe(stack).setInteger("status", status.getStatus());
					}
				}
			}
		} catch (final Exception e) {
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		this.removePlayerStatus(stack);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		this.addPlayerStatus(stack, player);
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
		if (!TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			this.removePlayerStatus(stack);
		}
	}

	private boolean stackHasStatus(ItemStack stack) {
		return this.getTagCompoundSafe(stack).hasKey("status");
	}

	private void addPlayerStatus(ItemStack stack, EntityLivingBase player) {
		if (this.stackHasStatus(stack)) {
			this.getTagCompoundSafe(stack).removeTag("status");
		}
		final VipStatus status = Capabilities.getVipStatus(player);
		if (status != null) {
			this.getTagCompoundSafe(stack).setInteger("status", status.getStatus());
		}
	}

	private void removePlayerStatus(ItemStack stack) {
		if (this.stackHasStatus(stack)) {
			this.getTagCompoundSafe(stack).removeTag("status");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation bro = new ModelResourceLocation(this.getRegistryName().toString() + "_bro", "inventory");
		final ModelResourceLocation panda = new ModelResourceLocation(this.getRegistryName().toString() + "_panda", "inventory");
		final ModelResourceLocation vip = new ModelResourceLocation(this.getRegistryName().toString() + "_vip", "inventory");
		final ModelResourceLocation artsy = new ModelResourceLocation(this.getRegistryName().toString() + "_artsy", "inventory");
		final ModelResourceLocation twilight = new ModelResourceLocation(this.getRegistryName().toString() + "_twilight", "inventory");
		ModelBakery.registerItemVariants(this, normal, bro, panda, vip, artsy, twilight);
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				if (TrinketDamageShield.this.stackHasStatus(stack)) {
					if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 0) {
						return normal;
					} else if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 1) {
						return vip;
					} else if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 2) {
						return bro;
					} else if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 3) {
						return panda;
					} else if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 4) {
						return artsy;
					} else if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 5) {
						return twilight;
					} else {
						return vip;
					}
				} else {
					return normal;
				}
			}
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale, boolean isBauble) {
		if (!clientConfig.doRender) {
			return;
		}
		final float offsetX = 0.17F;
		final float offsetY = 0.22F;
		final float offsetZ = 0.16F;
		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
		}
		renderer.getMainModel().bipedBody.postRender(scale);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(offsetX, -offsetY, offsetZ);
		if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
			GlStateManager.translate(offsetX - 0.14F, 0, -(offsetZ - 0.2F));
		}
		final float bS = 3f;
		GlStateManager.scale(scale * bS, scale * bS, scale * bS);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}
}