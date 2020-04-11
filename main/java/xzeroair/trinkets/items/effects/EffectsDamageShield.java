package xzeroair.trinkets.items.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import scala.util.Random;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.CapabilityProperties;

public class EffectsDamageShield {

	public static void eventPlayerTick(ItemStack stack, EntityPlayer player) {
	}

	public static void eventEntityJoinWorld(ItemStack stack, EntityPlayer player) {
	}

	public static void eventPlayerLogout(ItemStack stack, EntityPlayer player) {
	}

	public static void eventLivingJump(ItemStack stack, EntityPlayer player) {
	}

	public static void eventLivingFall(LivingFallEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		if (event.getSource().canHarmInCreative()) {
			return;
		}
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		int rChance = new Random().nextInt(TrinketsConfig.SERVER.DAMAGE_SHIELD.compat.firstaid.chance_headshots);
		if (TrinketsConfig.SERVER.DAMAGE_SHIELD.damage_ignore) {
			if (TrinketsConfig.SERVER.DAMAGE_SHIELD.compat.firstaid.chance_ignore) {
				iCap.setStoredExp(rChance);
				if (player.getUniqueID().toString().contentEquals("7f184d63-9f9c-47a7-be03-8382145fb2c2") || player.getUniqueID().toString().contentEquals("f5f28614-4e8b-4788-ae78-b020493dc5cb")) {
					rChance = new Random().nextInt(4);
					iCap.setStoredExp(rChance);
				}
			}
			if (iCap.Count() < TrinketsConfig.SERVER.DAMAGE_SHIELD.hits) {
				if (event.getAmount() > 1) {
					iCap.setCount(iCap.Count() + 1);
					CapabilityProperties.setDamageShield_HitCount(iCap.Count());
				}
			}
			if (iCap.Count() >= TrinketsConfig.SERVER.DAMAGE_SHIELD.hits) {
				iCap.setCount(0);
				CapabilityProperties.setDamageShield_HitCount(iCap.Count());
				if (TrinketsConfig.SERVER.DAMAGE_SHIELD.special) {
					if (player.getUniqueID().toString().contentEquals("7f184d63-9f9c-47a7-be03-8382145fb2c2") || player.getUniqueID().toString().contentEquals("cdfccefb-1a2e-4fb8-a3b5-041da27fde61") || player.getUniqueID().toString().contentEquals("f5f28614-4e8b-4788-ae78-b020493dc5cb")) {
						player.sendMessage(new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + "Reeeeee!"));
					} else if (player.getUniqueID().toString().contentEquals("854adc0b-ae55-48d6-b7ba-e641a1eebf42")) {
						player.sendMessage(new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + "1, 2, 3, no, 4 Furries!"));
					}
				}
				event.setAmount(0);
			}
		}
		if (!TrinketsConfig.SERVER.DAMAGE_SHIELD.damage_ignore && TrinketsConfig.SERVER.DAMAGE_SHIELD.special) {
			if (player.getUniqueID().toString().contentEquals("7f184d63-9f9c-47a7-be03-8382145fb2c2") || player.getUniqueID().toString().contentEquals("f5f28614-4e8b-4788-ae78-b020493dc5cb")) {
				rChance = new Random().nextInt(4);
				iCap.setStoredExp(rChance);
			}
			if (iCap.Count() < 4) {
				if (event.getAmount() > 1) {
					iCap.setCount(iCap.Count() + 1);
				}
			}
			if (iCap.Count() >= 4) {
				iCap.setCount(0);
				if (player.getUniqueID().toString().contentEquals("7f184d63-9f9c-47a7-be03-8382145fb2c2") || player.getUniqueID().toString().contentEquals("f5f28614-4e8b-4788-ae78-b020493dc5cb")) {
					player.sendMessage(new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + "Reeeeee!"));
				}
				event.setAmount(0);
			}
		}
	}

	public static void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventBreakSpeed(BreakSpeed event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventBlockBreak(BreakEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventBlockDrops(HarvestDropsEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventSetAttackTarget(LivingSetAttackTargetEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventLivingExperienceDrops(LivingExperienceDropEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventLivingDrops(LivingDropsEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void eventLivingDamageAttacked(LivingDamageEvent event, ItemStack stack, EntityLivingBase player) {
		if (TrinketsConfig.SERVER.DAMAGE_SHIELD.explosion_resist) {
			if (event.getSource().isExplosion()) {
				// && (event.getSource().getTrueSource() instanceof EntityLiving)
				// if(TrinketHelper.baubleCheck(player, ModItems.baubles.BaubleDamageShield)) {
				if (event.getAmount() > 1f) {
					event.setAmount(event.getAmount() * TrinketsConfig.SERVER.DAMAGE_SHIELD.explosion_amount);
				}
				// }
			}
		}
	}

	public static void eventLivingDamageAttacker(LivingDamageEvent event, ItemStack stack, EntityPlayer player) {
	}

	public static void PlayerEquip(ItemStack stack, EntityPlayer player) {
	}

	public static void PlayerUnequip(ItemStack stack, EntityPlayer player) {
	}

	public static void PlayerRender(ItemStack stack, EntityPlayer player, float partialTicks, boolean isBauble) {
	}

}
