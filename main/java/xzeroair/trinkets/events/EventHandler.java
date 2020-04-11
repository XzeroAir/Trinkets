package xzeroair.trinkets.events;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.items.effects.EffectsTitanRing;
import xzeroair.trinkets.items.foods.Dwarf_Stout;
import xzeroair.trinkets.items.foods.Fairy_Food;
import xzeroair.trinkets.items.foods.Titan_Spirit;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.SizeHelper;

public class EventHandler {

	@SubscribeEvent
	public void pickupEvent(EntityItemPickupEvent event) {

	}

	private boolean check, check2, check3 = false;

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer player = event.player;
		final boolean client = player.world.isRemote;
		final double x = player.posX;
		final double y = player.posY;
		final double z = player.posZ;
		if (((player.ticksExisted % 120) == 0) && !this.check) {

		}
		//		System.out.println(this.list.isEmpty());
		//		if (!this.list.isEmpty()) {
		//			for (String s : this.list) {
		//				if (player.getUniqueID().toString().contentEquals(s) || player.getName().contentEquals(s)) {
		//					player.sendMessage(new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + "Zero Deaths!"));
		//				}
		//			}
		//		}

		// System.out.println(MathHelper.clamp((1F/0)*100, Integer.MIN_VALUE,
		// Integer.MAX_VALUE));

		// if(((player.ticksExisted%20)==0) && client) {
		// GlStateManager.pushMatrix();
		// final ParticleLightning effect = new ParticleLightning(player.world,
		// player.posX, player.posY, player.posZ, x, y, z, 0f, 0.7f, 1f);
		// // System.out.println("Called?");
		// Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		// GlStateManager.popMatrix();
		// }
		// if(player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE)
		// != null) {
		// final IAttributeInstance race =
		// player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
		// // System.out.println(race.getBaseValue());
		// System.out.println(race.getAttributeValue());
		// for(final AttributeModifier rm : race.getModifiers()) {
		// System.out.println(rm.getAmount()+ " " + rm.getName() + " " +
		// rm.getOperation());
		// }
		// }

		// if(TrinketHelper.AccessoryCheck(player,
		// ModItems.trinkets.TrinketDamageShield)) {
		// final IAccessoryInterface cap = TrinketHelper.getAccessory(player,
		// ModItems.trinkets.TrinketDamageShield).getCapability(TrinketProvider.itemCapability,
		// null);
		// System.out.println(cap.hitCount());
		// }
		// float percentage = 100/MathHelper.clamp((1F/0)*100, 0, 1000);
		// float translatedFloat = MathHelper.clamp(percentage/100, Float.MIN_VALUE,
		// Float.MAX_VALUE);
		// System.out.println(translatedFloat);

		if ((player != null) && !(player.isDead)) {
			EffectsPolarizedStone.processBauble(player);
		}

		//		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		RaceProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			if ((event.phase == Phase.END)) {
				AbstractAttributeMap attributes = player.getAttributeMap();
				if (attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
					final IAttributeInstance race = attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE);
					final AttributeModifier fairyFood = race.getModifier(Fairy_Food.getUUID());
					final AttributeModifier dwarfFood = race.getModifier(Dwarf_Stout.getUUID());
					final AttributeModifier titanFood = race.getModifier(Titan_Spirit.getUUID());
					final AttributeModifier fairyPotion = race.getModifier(EffectsFairyRing.getUUID());
					final AttributeModifier dwarfPotion = race.getModifier(EffectsDwarfRing.getUUID());
					final AttributeModifier titanPotion = race.getModifier(EffectsTitanRing.getUUID());
					//					boolean fairy = (fairyFood != null) || (fairyPotion != null);
					//					boolean dwarf = (dwarfFood != null) || (dwarfPotion != null);
					//					boolean titan = (titanFood != null) || (titanPotion != null);
					boolean notFairy = (dwarfPotion != null) || (titanPotion != null);
					boolean notDwarf = (fairyPotion != null) || (titanPotion != null);
					boolean notTitan = (fairyPotion != null) || (dwarfPotion != null);
					boolean fairy = fairyPotion != null ? true : (fairyFood != null) && !notFairy ? true : false;
					boolean dwarf = dwarfPotion != null ? true : (dwarfFood != null) && !notDwarf ? true : false;
					boolean titan = titanPotion != null ? true : (titanFood != null) && !notTitan ? true : false;
					if (!race.getModifiers().isEmpty()) {
						if (fairy) {
							if (!cap.getFood().contentEquals("fairy_dew")) {
								EffectsFairyRing.FairyEquip(null, player);
								cap.setFood("fairy_dew");
							}
						}
						if (dwarf) {
							if (!cap.getFood().contentEquals("dwarf_stout")) {
								EffectsDwarfRing.DwarfEquip(null, player);
								cap.setFood("dwarf_stout");
							}
						}
						if (titan) {
							if (!cap.getFood().contentEquals("titan_spirit")) {
								EffectsTitanRing.TitanEquip(null, player);
								cap.setFood("titan_spirit");
							}
						}
						if (!TrinketHelper.AccessoryCheck(player, TrinketHelper.SizeTrinkets)) {
							if (cap.getFood().contentEquals("fairy_dew")) {
								EffectsFairyRing.FairyTicks(player);
							}
							if (cap.getFood().contentEquals("dwarf_stout")) {
								EffectsDwarfRing.DwarfTicks(player);
							}
							if (cap.getFood().contentEquals("titan_spirit")) {
								EffectsTitanRing.TitansTicks(player);
							}
						}
					} else {
						if (!cap.getFood().contentEquals("none")) {
							cap.setFood("none");
						}

						if (!TrinketHelper.AccessoryCheck(player, TrinketHelper.SizeTrinkets)) {
							cap.setTarget(100);
						}
					}
				}
				int i = 0;
				for (Item trinket : TrinketHelper.SizeTrinkets) {
					if (TrinketHelper.AccessoryCheck(player, trinket)) {
						i++;
					}
				}
				if ((i > 1)) {
					cap.setTarget(100);
				}
				if (cap.getTarget() != 100) {
					cap.setTrans(true);
				}
				if (cap.getTarget() == 100) {
					if (cap.getTrans() == true) {
						if (!player.isCreative() && (TrinketsConfig.SERVER.FAIRY_RING.creative_flight == true)) {
							player.capabilities.isFlying = false;
							player.capabilities.allowFlying = false;
							if (TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed && player.world.isRemote) {
								if (player.capabilities.getFlySpeed() != 0.05F) {
									player.capabilities.setFlySpeed(0.05f);
								}
							}
						}
					}
					AttributeHelper.removeAttributes(player, EffectsFairyRing.getUUID());
					AttributeHelper.removeAttributes(player, EffectsDwarfRing.getUUID());
					AttributeHelper.removeAttributes(player, EffectsTitanRing.getUUID());
					cap.setTrans(false);
				}
			}
			SizeHelper.eyeHeightHandler(player, cap);
			if (cap.getSize() != cap.getTarget()) {
				SizeHelper.sizeHandler(cap);
			}
		}
	}

	@SubscribeEvent
	public void EntityUpdate(LivingUpdateEvent event) {
		//		final EntityLivingBase entity = event.getEntityLiving();
		//		RaceProperties cap = Capabilities.getEntityRace(entity);
		//
		//		if ((cap != null) && !(entity instanceof EntityPlayer)) {
		//			AbstractAttributeMap attributes = entity.getAttributeMap();
		//			if (attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
		//				String fairy = "fairy_dew";
		//				String dwarf = "dwarf_stout";
		//				String titan = "titan_spirit";
		//				if (EntityRaceHelper.getRace(entity).contentEquals(fairy)) {
		//					if (!cap.getFood().contentEquals(fairy)) {
		//						EffectsFairyRing.FairyEquip(null, entity);
		//						cap.setFood(fairy);
		//					}
		//					if (cap.getFood().contentEquals(fairy)) {
		//						EffectsFairyRing.FairyTicks(entity);
		//					}
		//				}
		//				if (EntityRaceHelper.getRace(entity).contentEquals(dwarf)) {
		//					if (!cap.getFood().contentEquals(dwarf)) {
		//						EffectsDwarfRing.DwarfEquip(null, entity);
		//						cap.setFood(dwarf);
		//					}
		//					if (cap.getFood().contentEquals(dwarf)) {
		//						EffectsDwarfRing.DwarfTicks(entity);
		//					}
		//				}
		//				if (EntityRaceHelper.getRace(entity).contentEquals(titan)) {
		//					if (!cap.getFood().contentEquals(titan)) {
		//						EffectsTitanRing.TitanEquip(null, entity);
		//						cap.setFood(titan);
		//					}
		//					if (cap.getFood().contentEquals(titan)) {
		//						EffectsTitanRing.TitansTicks(entity);
		//					}
		//				}
		//			} else {
		//				if (!cap.getFood().contentEquals("none")) {
		//					cap.setFood("none");
		//				}
		//				if (cap.getTarget() != 100) {
		//					cap.setTarget(100);
		//				}
		//			}
		//			if (cap.getTarget() != 100) {
		//				cap.setTrans(true);
		//			}
		//			if (cap.getTarget() == 100) {
		//				if (cap.getTrans() == true) {
		//					AttributeHelper.removeAttributes(entity, EffectsFairyRing.getUUID());
		//					AttributeHelper.removeAttributes(entity, EffectsDwarfRing.getUUID());
		//					AttributeHelper.removeAttributes(entity, EffectsTitanRing.getUUID());
		//					entity.height = cap.getDefaultHeight();
		//					entity.width = cap.getDefaultWidth();
		//					cap.setTrans(false);
		//				}
		//			}
		//			//			System.out.println(cap.getSize() + " " + cap.getTarget() + "  " + cap.getTrans());
		//			if (cap.getSize() != cap.getTarget()) {
		//				SizeHelper.sizeHandler(cap);
		//				SizeHandler.setSize(entity, cap);
		//			}
		//		}

	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent event) {
	}

	@SubscribeEvent
	public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
		final ItemStack stack = event.getItem();
		if ((event.getEntityLiving() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if ((stack.getItem() instanceof Fairy_Food)) {
				if (TrinketsConfig.SERVER.Potion.potion_thirst) {
					if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing)) {
						TANCompat.addThirst(player, 10, 10);
						TANCompat.clearThirst(player);
					} else {
						TANCompat.addThirst(player, 5, 0);
						TANCompat.clearThirst(player);
					}
				}
			}
			if (stack.getItem() instanceof Dwarf_Stout) {
				if (TrinketsConfig.SERVER.Potion.potion_thirst) {
					if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
						TANCompat.addThirst(player, 5, 20);
						TANCompat.clearThirst(player);
					} else {
						TANCompat.addThirst(player, 5, 0);
						TANCompat.clearThirst(player);
					}
				}
			}
			if (stack.getItem() instanceof Titan_Spirit) {
				if (TrinketsConfig.SERVER.Potion.potion_thirst) {
					if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketTitanRing)) {
						TANCompat.addThirst(player, 5, 20);
						TANCompat.clearThirst(player);
					} else {
						TANCompat.addThirst(player, 5, 0);
						TANCompat.clearThirst(player);
					}
				}
			}
			//				if (stack.getItem().equals(Items.POTIONITEM)) {
			//					final PotionType pot = PotionUtils.getPotionFromItem(stack);
			//					if (pot.equals(ModPotionTypes.Enhanced)) {
			//
			//					}
			//				}
		}
	}

	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		if (event.getEntityPlayer() != null) {
			if (TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketDwarfRing)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {
		if ((event.getSound() != null) && (event.getEntity() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			RaceProperties cap = Capabilities.getEntityRace(player);
			final boolean client = player.world.isRemote;
			if (TrinketHelper.AccessoryCheck(player, TrinketHelper.SizeTrinkets) || !cap.getFood().contains("none")) {
				if ((event.getSound() == SoundEvents.BLOCK_STONE_STEP) || (event.getSound() == SoundEvents.BLOCK_GRASS_STEP) || (event.getSound() == SoundEvents.BLOCK_CLOTH_STEP) || (event.getSound() == SoundEvents.BLOCK_WOOD_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_GRAVEL_STEP) || (event.getSound() == SoundEvents.BLOCK_SNOW_STEP) || (event.getSound() == SoundEvents.BLOCK_GLASS_STEP) || (event.getSound() == SoundEvents.BLOCK_METAL_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_ANVIL_STEP) || (event.getSound() == SoundEvents.BLOCK_LADDER_STEP) || (event.getSound() == SoundEvents.BLOCK_SLIME_STEP)) {
					if (!client) {
						if (!event.getEntity().isSprinting()) {
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
