package xzeroair.trinkets.events;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.entity.ai.EnderAiEdit;
import xzeroair.trinkets.entity.ai.EnderMoveAI;
import xzeroair.trinkets.entity.ai.EnderQueensKnightAI;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;

public class EnderQueenHandler {

	private ConfigEnderCrown serverConfig = TrinketsConfig.SERVER.Items.ENDER_CROWN;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void soundEvent(PlaySoundEvent event) {
		final EntityPlayerSP player = Minecraft.getMinecraft().player;
		if ((player != null) && (player.world != null)) {
			if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)) {
				if (event.getSound().getSoundLocation().toString().contentEquals("minecraft:entity.endermen.stare")) {
					event.setResultSound(null);
				}
			}
		}
	}

	@SubscribeEvent
	public void EndermanJoinWorld(EntityJoinWorldEvent event) {
		//Add Tiara AI to Enderman
		if (event.getEntity() instanceof EntityEnderman) {
			final EntityEnderman ender = (EntityEnderman) event.getEntity();

			for (final Object a : ender.targetTasks.taskEntries.toArray()) {
				final EntityAIBase ai = ((EntityAITaskEntry) a).action;
				if (ai.toString().startsWith("net.minecraft.entity.monster.EntityEnderman$AIFindPlayer")) {
					ender.targetTasks.removeTask(ai);
				}
			}
			ender.targetTasks.addTask(1, new EnderAiEdit(ender));
			ender.targetTasks.addTask(2, new EnderQueensKnightAI(ender));
			if (serverConfig.Follow) {
				ender.targetTasks.addTask(3, new EnderMoveAI(ender));
			}
		}
	}

	@SubscribeEvent
	public void EnderTeleportEvent(EnderTeleportEvent event) {
		if ((event.getEntity() instanceof EntityEnderman)) {
			final AxisAlignedBB bBox = event.getEntity().getEntityBoundingBox().grow(16, 4, 16);
			final List<EntityPlayer> entLivList = event.getEntity().getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, bBox);
			if (!entLivList.isEmpty()) {
				for (final EntityPlayer stuff : entLivList) {
					final EntityPlayer player = stuff;
					if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)) {
						if (!event.getEntity().isInWater()) {
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		if (!serverConfig.attackBack && (event.getEntity() instanceof EntityEnderman)) {
			if (event.getTarget() instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) event.getTarget();
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)) {
					((EntityLiving) event.getEntity()).setAttackTarget(null);
				}
			}
		}
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
		if (!serverConfig.expDrop && (event.getAttackingPlayer() != null)) {
			if (event.getEntityLiving() instanceof EntityEnderman) {
				if (TrinketHelper.AccessoryCheck(event.getAttackingPlayer(), ModItems.trinkets.TrinketEnderTiara)) {
					event.setDroppedExperience(0);
				}
			}
		}
	}

	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
		if (!serverConfig.itemDrop && (event.getSource().getTrueSource() instanceof EntityPlayer)) {
			if (event.getEntityLiving() instanceof EntityEnderman) {
				if (TrinketHelper.AccessoryCheck((EntityPlayer) event.getSource().getTrueSource(), ModItems.trinkets.TrinketEnderTiara)) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			if (event.getSource().getTrueSource() instanceof EntityLiving) {
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)) {
					String string = "The void protects me!";
					String string2 = "Go, my loyal subject!";
					//TODO Fix VIP
					VipStatus vip = Capabilities.getVipStatus(player);
					if (vip != null) {
						String quote = vip.getRandomQuote();
						if (!quote.isEmpty()) {
							string = quote;
						}
					}
					TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
					TextComponentString message2 = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string2);
					final int chanceNum = serverConfig.chance;
					final int chance = new Random().nextInt(chanceNum);
					if (chance == 0) {
						if (serverConfig.dmgChance) {
							player.sendStatusMessage(message, false);
							event.setAmount(0);
						}
						if (serverConfig.spawnChance) {
							player.sendStatusMessage(message2, false);
							final EntityEnderman knight = new EntityEnderman(player.getEntityWorld());
							final double x = player.getPosition().getX();
							final double y = player.getPosition().getY();
							final double z = player.getPosition().getZ();
							knight.setPosition(x, y, z);
							player.getEntityWorld().spawnEntity(knight);
							if (event.getSource().getTrueSource() != null) {
								knight.setAttackTarget((EntityLivingBase) event.getSource().getTrueSource());
							}
						}
					}
				}
			}
		}
	}

}
