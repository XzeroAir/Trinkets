package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Sea;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigSeaStone;
import xzeroair.trinkets.util.handlers.ClimbHandler;

public class TrinketSea extends AccessoryBase {

	public static final ConfigSeaStone serverConfig = TrinketsConfig.SERVER.Items.SEA_STONE;
	public static final Sea clientConfig = TrinketsConfig.CLIENT.items.SEA_STONE;

	public TrinketSea(String name) {
		super(name);
		this.setUUID("6029aecd-318e-4b45-8c36-2ddd7f481e36");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		if (serverConfig.underwater_breathing) {
			if (!serverConfig.always_full) {
				if (player.getAir() < 20) {
					player.setAir(20);
					//					BetterDivingCompat.setOxygen(player, 100);
				}
			} else {
				player.setAir(300);
				//				BetterDivingCompat.setOxygen(player, 300);
			}
			if (Loader.isModLoaded("better_diving")) {
				player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 150, 0, false, false));
			}
		}
		if (serverConfig.compat.tan.prevent_thirst) {
			SurvivalCompat.clearThirst(player);
		}
		if (serverConfig.Swim_Tweaks == true) {
			final BlockPos head = player.getPosition();
			final IBlockState headBlock = player.world.getBlockState(head);
			final Block block = headBlock.getBlock();
			if ((player.isInWater() || player.isInLava()) && (block != Blocks.AIR)) {
				ClimbHandler movementHandler = new ClimbHandler(player, player.world);
				double motion = 0.1;
				final double bouyance = 0.25;
				if (player.isInLava()) {
					motion = 0.09;
				}
				if (!player.isSneaking()) {
					player.motionY = 0f;
					if ((movementHandler.movingForward(player.getHorizontalFacing()) == true)) {
						if (((player.motionX > motion) || (player.motionX < -motion)) || ((player.motionZ > motion) || (player.motionZ < -motion))) {
							player.motionY += MathHelper.clamp(player.getLookVec().y / 1, -bouyance, bouyance);
						}
					}
				} else {
					if ((movementHandler.movingForward(player.getHorizontalFacing()) == false)) {
						if (!(player.motionY > 0)) {
							if (player.isInLava()) {
								player.motionY *= 1.75;
							} else {
								player.motionY *= 1.25;
							}
						} else {

						}

					}
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

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, float partialTicks, float scale, boolean isBauble) {
		if (!clientConfig.doRender) {
			return;
		}
		float offsetY = 0.16F;
		float offsetZ = 0.14F;
		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
		}
		renderer.getMainModel().bipedBody.postRender(scale);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(0F, -offsetY, offsetZ);
		if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
			GlStateManager.translate(0F, 0, -(offsetZ - 0.2F));
		}
		float bS = 3f;
		GlStateManager.scale(scale * bS, scale * bS, scale * bS);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {
		List<Potion> thirsts = SurvivalCompat.getThirstEffects();
		if (!thirsts.isEmpty()) {
			for (Potion thirst : thirsts) {
				if ((thirst != null) && event.getPotionEffect().getPotion().equals(thirst)) {
					event.setResult(Result.DENY);
				}
			}
		}
		Potion parasites = SurvivalCompat.getSDParasitesPotionEffect();
		if ((parasites != null) && event.getPotionEffect().getPotion().equals(parasites)) {
			event.setResult(Result.DENY);
		}
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		ModelResourceLocation worn = new ModelResourceLocation(this.getRegistryName().toString() + "_worn", "inventory");
		ModelBakery.registerItemVariants(this, normal, worn);
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				if (true) {
					return worn;
				} else {
					return normal;
				}
			}
		});
	}

}
