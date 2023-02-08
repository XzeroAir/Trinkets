package xzeroair.trinkets.events;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.capabilities.statushandler.TrinketStatusEffect;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IItemUseAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.MPRecoveryItem;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.PotionHelper.PotionHolder;
import xzeroair.trinkets.util.helpers.StringUtils;

public class EventHandler extends EventBaseHandler {

	//	@SubscribeEvent
	//	public void RaceChanged(RaceChangedEvent event) {
	//		//		System.out.println(event.getEntityLiving().getName() + " Just Transformed From " + event.getCurrentRace().getName() + " To " + event.getNewRace().getName());
	//	}
	//
	//	@SubscribeEvent
	//	public void StartTransformation(startTransformationEvent event) {
	//		//		System.out.println(event.getNewRace().getName());
	//	}
	//
	//	@SubscribeEvent
	//	public void EndTransformation(endTransformationEvent event) {
	//		//		System.out.println(event.getPreviousRace().getName());
	//	}

	//	@SubscribeEvent
	//	public void worldTickEvent(WorldTickEvent event) {
	//		//		try {
	//		//			//			String cfgpath = Loader.instance().getConfigDir().toPath().toString();
	//		//			File cfgFolder = Trinkets.config.getConfigFile().getParentFile();
	//		//			File f = Utils.getFileLocation(cfgFolder.getPath(), "test.txt");
	//		//			if (f != null) {
	//		//				String cfgpath = f.toPath().toString();
	//		//				System.out.println(cfgpath);
	//		//			}
	//		//		} catch (Exception e) {
	//		//			e.printStackTrace();
	//		//		}
	//	}

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer entity = event.player;
		if (!entity.isEntityAlive() || (entity.getEntityWorld() == null)) {
			return;
		}
		if ((event.phase == Phase.START)) {
			try {
				Capabilities.getEntityProperties(entity, EntityProperties::onUpdatePre);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		} else {
			if (TrinketsConfig.SERVER.misc.retrieveVIP) {
				try {
					Capabilities.getVipStatus(entity, VipStatus::onUpdate);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			this.raceHandlerTick(entity);
			this.effectHandlerTick(entity);
			this.magicHandlerTick(entity);
		}
	}

	@SubscribeEvent
	public void EntityUpdate(LivingUpdateEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		if (!(entity instanceof EntityPlayer)) {
			this.raceHandlerTick(entity);
			this.effectHandlerTick(entity);
			// TODO ADD MAGIC TO ENTITIES?
			this.magicHandlerTick(entity);
		}
	}

	private void raceHandlerTick(EntityLivingBase entity) {
		try {
			//			entity.world.profiler.startSection("xatHandlerTick");
			Capabilities.getEntityProperties(entity, EntityProperties::onUpdate);
			//			entity.world.profiler.endSection();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void magicHandlerTick(EntityLivingBase entity) {
		try {
			//			entity.world.profiler.startSection("xat.magic.tick");
			Capabilities.getMagicStats(entity, MagicStats::onUpdate);
			//			entity.world.profiler.endSection();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void effectHandlerTick(EntityLivingBase entity) {
		try {
			//			entity.world.profiler.startSection("xat.effects.tick");
			Capabilities.getStatusHandler(entity, StatusHandler::onUpdate);
			//			entity.world.profiler.endSection();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {
		final Entity entity = event.getEntity();
		if ((event.getSound() != null) && (entity instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) entity;
			final boolean client = player.world.isRemote;
			Capabilities.getEntityProperties(player, prop -> {
				if (!prop.isNormalSize()) {
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
			});
		}
	}

	@SubscribeEvent
	public void potionApply(PotionApplicableEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		if (!entity.isEntityAlive() || (entity.getEntityWorld() == null)) {
			return;
		}
		Capabilities.getEntityProperties(entity, prop -> {
			boolean cancel = false;
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			if (prop.getRaceHandler().potionBeingApplied(event.getPotionEffect())) {
				cancel = true;
			}
			if (!cancel) {
				for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
					String key = entry.getKey();
					AbilityHolder holder = entry.getValue();
					try {
						final IAbilityInterface handler = holder.getAbility();
						if (handler instanceof IPotionAbility) {
							cancel = ((IPotionAbility) handler).potionApplied(entity, event.getPotionEffect(), cancel);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Potion Ability:" + key);
						e.printStackTrace();
					}
				}
			}
			if (cancel) {
				event.setResult(Result.DENY);
			}
		});
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Interaction Events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {
		Capabilities.getEntityProperties(
				event.getEntityLiving(), prop -> {
					//			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					//				try {
					//					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					//					if ((handler != null) && (handler instanceof IInteractionAbility)) {
					//						((IInteractionAbility) handler).interact(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos());
					//					}
					//				} catch (final Exception e) {
					//					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getRegistryID());
					//					e.printStackTrace();
					//				}
					//			}
					try {
						prop.getRaceHandler().interact(event);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
		);
	}

	@SubscribeEvent
	public void playerInteractEntity(PlayerInteractEvent.EntityInteract event) {
		Capabilities.getEntityProperties(
				event.getEntityLiving(), prop -> {
					//			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					//				try {
					//					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					//					if ((handler != null) && (handler instanceof IInteractionAbility)) {
					//						((IInteractionAbility) handler).interactEntity(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getTarget());
					//					}
					//				} catch (final Exception e) {
					//					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getRegistryID());
					//					e.printStackTrace();
					//				}
					//			}
					prop.getRaceHandler().interactWithEntity(event);
				}
		);
	}

	@SubscribeEvent
	public void playerInteractEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event) {
		//		Capabilities.getEntityProperties(event.getEntityLiving(), prop -> {
		//			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
		//				try {
		//					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
		//					if ((handler != null) && (handler instanceof IInteractionAbility)) {
		//						((IInteractionAbility) handler).interactEntitySpecific(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getTarget(), event.getLocalPos());
		//					}
		//				} catch (final Exception e) {
		//					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getRegistryID());
		//					e.printStackTrace();
		//				}
		//			}
		//		});
	}

	@SubscribeEvent
	public void playerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		//		Capabilities.getEntityProperties(event.getEntityLiving(), prop -> {
		//			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
		//				try {
		//					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
		//					if ((handler != null) && (handler instanceof IInteractionAbility)) {
		//						final ImmutablePair<Result, Result> results = ((IInteractionAbility) handler).rightClickBlock(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getHitVec(), event.getUseBlock(), event.getUseItem());
		//						if (results != null) {
		//							if (results.getLeft() != null) {
		//								event.setUseBlock(results.getLeft());
		//							}
		//							if (results.getRight() != null) {
		//								event.setUseItem(results.getRight());
		//							}
		//						}
		//					}
		//				} catch (final Exception e) {
		//					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getRegistryID());
		//					e.printStackTrace();
		//				}
		//			}
		//		});
	}

	@SubscribeEvent
	public void playerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		//		Capabilities.getEntityProperties(event.getEntityLiving(), prop -> {
		//			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
		//				try {
		//					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
		//					if ((handler != null) && (handler instanceof IInteractionAbility)) {
		//						final ImmutablePair<Result, Result> results = ((IInteractionAbility) handler).leftClickBlock(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getHitVec(), event.getUseBlock(), event.getUseItem());
		//						if (results != null) {
		//							if (results.getLeft() != null) {
		//								event.setUseBlock(results.getLeft());
		//							}
		//							if (results.getRight() != null) {
		//								event.setUseItem(results.getRight());
		//							}
		//						}
		//					}
		//				} catch (final Exception e) {
		//					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getRegistryID());
		//					e.printStackTrace();
		//				}
		//			}
		//		});
	}

	@SubscribeEvent
	public void playerRightClickItem(PlayerInteractEvent.RightClickItem event) {
		//		Capabilities.getEntityProperties(event.getEntityLiving(), prop -> {
		//			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
		//				try {
		//					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
		//					if ((handler != null) && (handler instanceof IInteractionAbility)) {
		//						((IInteractionAbility) handler).rightClickWithItem(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos());
		//					}
		//				} catch (final Exception e) {
		//					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getRegistryID());
		//					e.printStackTrace();
		//				}
		//			}
		//		});
	}

	@SubscribeEvent
	public void onMount(EntityMountEvent event) {
		if (event.getEntityMounting() instanceof EntityLivingBase) {
			final EntityLivingBase entity = (EntityLivingBase) event.getEntityMounting();
			Capabilities.getEntityProperties(entity, prop -> {
				final EntityRacePropertiesHandler handler = prop.getRaceHandler();
				try {
					final boolean bool = event.isMounting() ? !handler.mountEntity(event.getEntityBeingMounted()) : !handler.dismountedEntity(event.getEntityBeingMounted());
					if (bool) {
						this.cancelEvent(event);
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	@SubscribeEvent
	public void onItemStartUsingEvent(LivingEntityUseItemEvent.Start event) {
		final EntityLivingBase entity = event.getEntityLiving();
		Capabilities.getEntityProperties(entity, prop -> {
			final ItemStack stack = event.getItem();
			final int duration = event.getDuration();
			int dur = duration;
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					final IAbilityInterface handler = holder.getAbility();
					if (handler instanceof IItemUseAbility) {
						if (dur >= 0) {
							dur = ((IItemUseAbility) handler).onItemStartUse(entity, stack, dur);
						} else {
							break;
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			if (duration != dur) {
				event.setDuration(dur);
			}
		});
	}

	@SubscribeEvent
	public void onItemUsingTickEvent(LivingEntityUseItemEvent.Tick event) {
		final EntityLivingBase entity = event.getEntityLiving();
		Capabilities.getEntityProperties(entity, prop -> {
			final ItemStack stack = event.getItem();
			final int duration = event.getDuration();
			int dur = duration;
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					final IAbilityInterface handler = holder.getAbility();
					if (handler instanceof IItemUseAbility) {
						if (dur >= 0) {
							dur = ((IItemUseAbility) handler).onItemUseTick(entity, stack, dur);
						} else {
							break;
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			prop.getRaceHandler().bowDrawing(stack, dur);
			if (duration != dur) {
				event.setDuration(dur);
			}
		});
	}

	@SubscribeEvent
	public void onItemStopUseEvent(LivingEntityUseItemEvent.Stop event) {
		final EntityLivingBase entity = event.getEntityLiving();
		Capabilities.getEntityProperties(entity, prop -> {
			final ItemStack stack = event.getItem();
			final int duration = event.getDuration();
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					final IAbilityInterface handler = holder.getAbility();
					if (handler instanceof IItemUseAbility) {
						((IItemUseAbility) handler).onItemUseStop(entity, stack, duration);
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}

			//			try {
			//				if (event.getItem().getItem() instanceof ItemBow) {
			//					if (event.getDuration() < 72000) {
			//						final int charge = 72000 - event.getDuration();
			//						prop.getRaceHandler().bowUsed(event);//bowUsed(event.getItem(), charge);
			//					}
			//				}
			//			} catch (final Exception e) {
			//				e.printStackTrace();
			//			}
		});
	}

	@SubscribeEvent
	public void onItemFinishUseEvent(LivingEntityUseItemEvent.Finish event) {

		final EntityLivingBase entity = event.getEntityLiving();
		final ItemStack stack = event.getItem();
		final Item item = stack.getItem();
		final String regName = item.getRegistryName().toString();
		final String ModID = item.getRegistryName().getNamespace();
		final String ItemID = item.getRegistryName().getPath();
		final int duration = event.getDuration();
		Capabilities.getEntityProperties(
				entity, prop -> {
					Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
					for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
						String key = entry.getKey();
						AbilityHolder holder = entry.getValue();
						try {
							final IAbilityInterface handler = holder.getAbility();
							if (handler instanceof IItemUseAbility) {
								((IItemUseAbility) handler).onItemUseFinish(entity, stack, duration);
							}
						} catch (final Exception e) {
							Trinkets.log.error("Trinkets had an Error with Ability:" + key);
							e.printStackTrace();
						}
					}
					try {
						final FaelisConfig faelisConfig = TrinketsConfig.SERVER.races.faelis;
						if (prop.getCurrentRace().equals(EntityRaces.faelis) && faelisConfig.Invigorated) {
							final StatusHandler status = Capabilities.getStatusHandler(entity);
							if (status != null) {
								final String[] milkList = faelisConfig.milk;
								for (final String milk : milkList) {
									final String[] itemConfig = milk.split(";");
									final String itemString = StringUtils.getStringFromArray(itemConfig, 0);
									final String metaString = StringUtils.getStringFromArray(itemConfig, 1);
									final String levelString = StringUtils.getStringFromArray(itemConfig, 2);
									final String durationString = StringUtils.getStringFromArray(itemConfig, 3);
									//							final PotionHolder potion = PotionHelper.getPotionHolder(milk);
									//							if (potion.getPotion() != null) {
									//
									//							}
									if (event.getItem().getItem().getRegistryName().toString().equalsIgnoreCase(itemString)) {
										final int meta = metaString.isEmpty() ? OreDictionary.WILDCARD_VALUE : Integer.parseInt(metaString);
										final int level = levelString.isEmpty() ? 0 : Integer.parseInt(levelString);
										final int Iduration = durationString.isEmpty() ? faelisConfig.Invigorated_Duration : Integer.parseInt(durationString);
										if ((meta == OreDictionary.WILDCARD_VALUE) || (event.getItem().getMetadata() == meta)) {
											status.apply(new TrinketStatusEffect("Invigorated", Iduration, level, null));
											if (!entity.world.isRemote) {
												for (final String potID : faelisConfig.buffs) {
													final PotionHolder potion = PotionHelper.getPotionHolder(potID);
													if (potion.getPotion() != null) {
														entity.addPotionEffect(potion.getPotionEffect());
													}
												}
											}
										}
										break;
									}
								}
							}
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
		);
		if (TrinketsConfig.SERVER.mana.mana_enabled) {
			try {
				TreeMap<String, MPRecoveryItem> MagicRecoveryItems = ConfigHelper.TrinketConfigStorage.MagicRecoveryItems;
				float amount = 0;
				boolean multiplied = false;
				for (MPRecoveryItem entry : MagicRecoveryItems.values()) {
					if (entry.doesItemMatchEntry(stack)) {
						amount = entry.getAmount();
						multiplied = entry.isMultiplied();
						break;
					}
				}
				final float finalAmount = amount;
				final boolean finalMultiplied = multiplied;
				if ((finalAmount > 0) || (finalAmount < 0)) {
					Capabilities.getMagicStats(entity, magic -> {
						if (finalAmount > 0) {
							if (finalMultiplied) {
								magic.addMana(magic.getMaxMana() * (finalAmount * 0.01F));
							} else {
								magic.addMana(finalAmount);
							}
						} else {
							if (finalMultiplied) {
								magic.spendMana(magic.getMaxMana() * (finalAmount * 0.01F));
							} else {
								magic.spendMana(finalAmount);
							}
						}
					});
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	//	@SubscribeEvent // Server only?
	//	public void ItemPickupEvent(ItemPickupEvent event) {

	//	}

	//	@SubscribeEvent // Both
	//	public void craftedSomething(ItemCraftedEvent event) {
	//		final ItemStack stack = event.crafting;
	//		try {
	//			if (event.player != null) {
	//				Capabilities.getTrinketProperties(stack, (prop) -> {
	//					prop.getTagCompoundSafe(stack).setString("crafter.id", event.player.getUniqueID().toString());
	//					prop.getTagCompoundSafe(stack).setString("crafter.name", event.player.getDisplayNameString());
	//				});
	//			}
	//		} catch (final Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Interaction End~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

}
