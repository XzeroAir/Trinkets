package xzeroair.trinkets.events;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.items.foods.Dwarf_Stout;
import xzeroair.trinkets.items.foods.Fairy_Food;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;
import xzeroair.trinkets.util.helpers.SizeHelper;

public class EventHandler {

	@SubscribeEvent
	public void pickupEvent(EntityItemPickupEvent event) {

	}

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer player = event.player;
		final boolean client = player.world.isRemote;

		if((player != null) && !(player.isDead)) {
			EffectsPolarizedStone.processBauble(player);
		}

		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if (cap != null) {
			if((event.phase == Phase.END)) {
				if(player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
					final IAttributeInstance race = player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
					final AttributeModifier fairyFood = player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(Fairy_Food.getUUID());
					final AttributeModifier dwarfFood = player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(Dwarf_Stout.getUUID());
					final AttributeModifier fairyPotion = player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(EffectsFairyRing.getUUID());
					final AttributeModifier dwarfPotion = player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(EffectsDwarfRing.getUUID());
					if(race != null) {
						if(!race.getModifiers().isEmpty()) {
						} else {
							if(!cap.getFood().contentEquals("none")) {
								cap.setFood("none");
							}
							if(!(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing))) {
								cap.setTarget(100);
							}
						}
					}
					if(((fairyFood != null) && (dwarfPotion == null)) || (((dwarfFood == null) || (dwarfFood != null)) && (fairyPotion != null))) {
						if(!cap.getFood().contentEquals("fairy_dew")) {
							if(!TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
								EffectsFairyRing.FairyEquip(null, player);
							}
						}
						cap.setFood("fairy_dew");
						if(!TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
							EffectsFairyRing.FairyTicks(player);
						}
					}
					if(((dwarfFood != null) && (fairyPotion == null)) || (((fairyFood == null) || (fairyFood != null)) && (dwarfPotion != null))) {
						if(!cap.getFood().contentEquals("dwarf_stout")) {
							if(!TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing)) {
								EffectsDwarfRing.DwarfEquip(null, player);
							}
						}
						cap.setFood("dwarf_stout");
						EffectsDwarfRing.DwarfTicks(player);
					}
				}
				if((cap.getTarget() == 25) || (cap.getTarget() == 75) || (cap.getTarget() == 200)) {
					cap.setTrans(true);
				}
				if((cap.getTarget() == 100)) {
					if(cap.getTrans() == true) {
						if(!player.isCreative() && (TrinketsConfig.SERVER.FAIRY_RING.creative_flight == true)) {
							player.capabilities.isFlying = false;
							player.capabilities.allowFlying = false;
							if(TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed && player.world.isRemote) {
								if(player.capabilities.getFlySpeed() != 0.05F) {
									player.capabilities.setFlySpeed(0.05f);
								}
							}
						}
					}
					cap.setTrans(false);
				}
			}
			SizeHelper.eyeHeightHandler(player, cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
			if(cap.getSize() != cap.getTarget()) {
				SizeHelper.sizeHandler(cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
			}
		}
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent event) {
	}

	@SubscribeEvent
	public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
		final ItemStack stack = event.getItem();
		if((event.getEntityLiving() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			if((cap != null) ) {
				if((stack.getItem() instanceof Fairy_Food)) {
					if(TrinketsConfig.SERVER.Potion.potion_thirst) {
						if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing)) {
							TANCompat.addThirst(player, 10, 10);
							TANCompat.clearThirst(player);
						} else {
							TANCompat.addThirst(player, 5, 0);
							TANCompat.clearThirst(player);
						}
					}
				}
				if(stack.getItem() instanceof Dwarf_Stout) {
					if(TrinketsConfig.SERVER.Potion.potion_thirst) {
						if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
							TANCompat.addThirst(player, 5, 20);
							TANCompat.clearThirst(player);
						} else {
							TANCompat.addThirst(player, 5, 0);
							TANCompat.clearThirst(player);
						}
					}
				}
				if(stack.getItem().equals(Items.POTIONITEM)) {
					final PotionType pot = PotionUtils.getPotionFromItem(stack);
					if(pot.equals(ModPotionTypes.Enhanced)) {
						player.heal(5);
						if(TrinketsConfig.SERVER.Potion.potion_thirst) {
							TANCompat.addThirst(player, 5, 5);
							TANCompat.clearThirst(player);
						}
					}
					if(pot.equals(ModPotionTypes.Restorative)) {
						player.heal(20);
						if(TrinketsConfig.SERVER.Potion.potion_thirst) {
							TANCompat.addThirst(player, 20, 20);
							TANCompat.clearThirst(player);
							TANCompat.ClearTempurature(player);
						}
						if(!cap.getFood().contentEquals("none")) {
							RaceAttribute.removeModifier(player, Fairy_Food.getUUID());
							RaceAttribute.removeModifier(player, Dwarf_Stout.getUUID());
							RaceAttribute.removeModifier(player, EffectsFairyRing.getUUID());
							RaceAttribute.removeModifier(player, EffectsDwarfRing.getUUID());
							EffectsFairyRing.FairyUnequip(null, player);
							EffectsDwarfRing.DwarfUnequip(null, player);
							if(Loader.isModLoaded("firstaid")) {
								FirstAidCompat.resetHP(player);
							}
						}
						player.clearActivePotions();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		if(event.getEntityPlayer() != null) {
			if(TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketDwarfRing)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			final boolean client = player.world.isRemote;
			if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)  || !cap.getFood().contains("none")) {
				if((event.getSound() == SoundEvents.BLOCK_STONE_STEP) || (event.getSound() == SoundEvents.BLOCK_GRASS_STEP) || (event.getSound() == SoundEvents.BLOCK_CLOTH_STEP) || (event.getSound() == SoundEvents.BLOCK_WOOD_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_GRAVEL_STEP) || (event.getSound() == SoundEvents.BLOCK_SNOW_STEP) || (event.getSound() == SoundEvents.BLOCK_GLASS_STEP) || (event.getSound() == SoundEvents.BLOCK_METAL_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_ANVIL_STEP) || (event.getSound() == SoundEvents.BLOCK_LADDER_STEP) || (event.getSound() == SoundEvents.BLOCK_SLIME_STEP)) {
					if(!client) {
						if(!event.getEntity().isSprinting()) {
							event.setVolume(0.0F);
						} else {
							event.setVolume(0.1F);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MODID)) {
			ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		}
	}
}
