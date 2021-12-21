package xzeroair.trinkets.events;

import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.capabilities.statushandler.TrinketStatusEffect;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.items.trinkets.TrinketTeddyBear;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IInteractionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IItemUseAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ItemObjectHolder;
import xzeroair.trinkets.util.helpers.CallHelper;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.PotionHelper.PotionHolder;

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

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer entity = event.player;
		if (!entity.isEntityAlive() || (entity.getEntityWorld() == null)) {
			return;
		}
		//		if (entity.getName().contentEquals("Airgre") && !entity.world.isRemote) {
		//			final List<EntityPlayer> players = entity.world.playerEntities;
		//			for (final EntityPlayer player : players) {
		//				if (player.getName().contentEquals("XzeroAir")) {
		//					final EntityProperties cap = Capabilities.getEntityRace(player);
		//					if (cap != null) {
		//						//						System.out.println(player.isRiding() + " Hm");
		//					}
		//				}
		//			}
		//			final List<Entity> entities = entity.world.loadedEntityList;
		//			for (final Entity e : entities) {
		//				if ((e instanceof EntityLivingBase) && !(e instanceof EntityPlayer)) {
		//					if (e.getName().contentEquals("entity.Dummy.name")) {
		//						continue;
		//					}
		//					if (e instanceof AlphaWolf) {
		//						final AlphaWolf wolf = (AlphaWolf) e;
		//						wolf.isEntityAlive();
		//					}
		//					System.out.println(e.getName());
		//				}
		//			}
		//		}
		if ((event.phase == Phase.START)) {
			try {
				final EntityProperties cap = Capabilities.getEntityRace(entity);
				if (cap != null) {
					cap.onUpdatePre();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		} else {
			//		if ((event.phase == Phase.END)) {
			try {
				final VipStatus status = Capabilities.getVipStatus(entity);
				if (status != null) {
					status.onUpdate();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			this.raceHandlerTick(entity);
			this.effectHandlerTick(entity);
			this.magicHandlerTick(entity);
			try {
				EffectsPolarizedStone.processBauble(entity);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void EntityUpdate(LivingUpdateEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		if (!(entity instanceof EntityPlayer)) {
			this.raceHandlerTick(entity);
			this.effectHandlerTick(entity);
			// TODO ADD MAGIC TO ENTITIES?
		}
	}

	private void raceHandlerTick(EntityLivingBase entity) {
		try {
			final EntityProperties cap = Capabilities.getEntityRace(entity);
			if (cap != null) {
				cap.onUpdate();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void magicHandlerTick(EntityLivingBase entity) {
		try {
			final MagicStats cap = Capabilities.getMagicStats(entity);
			if (cap != null) {
				cap.onUpdate();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void effectHandlerTick(EntityLivingBase entity) {
		try {
			final StatusHandler cap = Capabilities.getStatusHandler(entity);
			if (cap != null) {
				cap.onUpdate();
			}
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
			final EntityProperties cap = Capabilities.getEntityRace(player);
			if ((cap != null) && !cap.isNormalHeight()) {
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
	public void potionApply(PotionApplicableEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			if (!entity.isEntityAlive() || (entity.getEntityWorld() == null)) {
				return;
			}
			boolean cancel = false;
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IPotionAbility)) {
						cancel = ((IPotionAbility) handler).potionApplied(entity, event.getPotionEffect(), cancel);
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getName());
					e.printStackTrace();
				}
			}

			if (cap.getRaceProperties().potionBeingApplied(event.getPotionEffect())) {
				cancel = true;
			}
			if (cancel) {
				event.setResult(Result.DENY);
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Interaction Events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {
		final EntityProperties cap = Capabilities.getEntityRace(event.getEntityLiving());
		if (cap != null) {
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IInteractionAbility)) {
						((IInteractionAbility) handler).interact(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos());
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
			try {
				cap.getRaceProperties().interact(event);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void playerInteractEntity(PlayerInteractEvent.EntityInteract event) {
		if (this.isEventCanceled(event)) {
			return;
		}
		final EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IInteractionAbility)) {
						((IInteractionAbility) handler).interactEntity(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getTarget());
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
			prop.getRaceProperties().interactWithEntity(event);
		}
	}

	@SubscribeEvent
	public void playerInteractEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event) {
		if (this.isEventCanceled(event)) {
			return;
		}
		final EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IInteractionAbility)) {
						((IInteractionAbility) handler).interactEntitySpecific(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getTarget(), event.getLocalPos());
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void playerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (this.isEventCanceled(event)) {
			return;
		}
		final EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IInteractionAbility)) {
						final ImmutablePair<Result, Result> results = ((IInteractionAbility) handler).rightClickBlock(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getHitVec(), event.getUseBlock(), event.getUseItem());
						if (results != null) {
							if (results.getLeft() != null) {
								event.setUseBlock(results.getLeft());
							}
							if (results.getRight() != null) {
								event.setUseItem(results.getRight());
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void playerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (this.isEventCanceled(event)) {
			return;
		}
		final EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IInteractionAbility)) {
						final ImmutablePair<Result, Result> results = ((IInteractionAbility) handler).leftClickBlock(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos(), event.getHitVec(), event.getUseBlock(), event.getUseItem());
						if (results != null) {
							if (results.getLeft() != null) {
								event.setUseBlock(results.getLeft());
							}
							if (results.getRight() != null) {
								event.setUseItem(results.getRight());
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void playerRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (this.isEventCanceled(event)) {
			return;
		}
		final EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IInteractionAbility)) {
						((IInteractionAbility) handler).rightClickWithItem(event.getEntityLiving(), event.getWorld(), event.getItemStack(), event.getHand(), event.getFace(), event.getPos());
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Potion Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void onMount(EntityMountEvent event) {
		if (event.getEntityMounting() instanceof EntityLivingBase) {
			final EntityLivingBase entity = (EntityLivingBase) event.getEntityMounting();
			final EntityProperties cap = Capabilities.getEntityRace(entity);
			if (cap != null) {
				try {
					if (event.isMounting()) {
						final boolean bool = !cap.getRaceProperties().mountEntity(event.getEntityBeingMounted());
						if (event.isCancelable() && !event.isCanceled() && bool) {
							event.setCanceled(bool);
						}
					} else if (event.isDismounting()) {
						final boolean bool = !cap.getRaceProperties().dismountedEntity(event.getEntityBeingMounted());
						if (event.isCancelable() && !event.isCanceled() && bool) {
							event.setCanceled(bool);
						}
					} else {

					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemStartUsingEvent(LivingEntityUseItemEvent.Start event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final ItemStack stack = event.getItem();
		final int duration = event.getDuration();
		final int dur = duration;
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IItemUseAbility)) {
						if (dur >= 0) {
							((IItemUseAbility) handler).onItemStartUse(entity, stack, dur);
						} else {
							break;
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
			if (duration != dur) {
				event.setDuration(dur);
			}
		}
	}

	@SubscribeEvent
	public void onItemUsingTickEvent(LivingEntityUseItemEvent.Tick event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final ItemStack stack = event.getItem();
		final int duration = event.getDuration();
		int dur = duration;
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IItemUseAbility)) {
						if (dur >= 0) {
							dur = ((IItemUseAbility) handler).onItemUseTick(entity, stack, dur);
						} else {
							break;
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
			prop.getRaceProperties().bowDrawing(stack, dur);
			if (duration != dur) {
				event.setDuration(dur);
			}
		}
	}

	@SubscribeEvent
	public void onItemStopUseEvent(LivingEntityUseItemEvent.Stop event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final ItemStack stack = event.getItem();
		final int duration = event.getDuration();
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IItemUseAbility)) {
						((IItemUseAbility) handler).onItemUseStop(entity, stack, duration);
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}

			try {
				if (event.getItem().getItem() instanceof ItemBow) {
					if (event.getDuration() < 72000) {
						final int charge = 72000 - event.getDuration();
						prop.getRaceProperties().bowUsed(event.getItem(), charge);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void onItemFinishUseEvent(LivingEntityUseItemEvent.Finish event) {

		final EntityLivingBase entity = event.getEntityLiving();
		final ItemStack stack = event.getItem();
		final int duration = event.getDuration();
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IItemUseAbility)) {
						((IItemUseAbility) handler).onItemUseFinish(entity, stack, duration);
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
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
							final String itemString = CallHelper.getStringFromArray(itemConfig, 0);
							final String metaString = CallHelper.getStringFromArray(itemConfig, 1);
							final String levelString = CallHelper.getStringFromArray(itemConfig, 2);
							final String durationString = CallHelper.getStringFromArray(itemConfig, 3);
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
		if (TrinketsConfig.SERVER.mana.mana_enabled) {
			try {
				final MagicStats magic = Capabilities.getMagicStats(entity);
				if (magic != null) {
					final Map<String, ItemObjectHolder> configObjects = ConfigHelper.getMagicRecoveryItems();
					if ((configObjects != null) && !configObjects.isEmpty()) {
						final boolean flag1 = configObjects.containsKey(event.getItem().getItem().getRegistryName().toString() + ":" + event.getItem().getItemDamage());
						final boolean flag2 = configObjects.containsKey(event.getItem().getItem().getRegistryName().toString() + ":" + OreDictionary.WILDCARD_VALUE);
						if (flag1 || flag2) {
							final String key = flag2 ? event.getItem().getItem().getRegistryName().toString() + ":" + OreDictionary.WILDCARD_VALUE : event.getItem().getItem().getRegistryName().toString() + ":" + event.getItem().getItemDamage();
							final ItemObjectHolder item = configObjects.get(key);
							if (event.getItem().getItem().getRegistryName().toString().equalsIgnoreCase(item.getObject())) {
								if ((item.getMeta() == OreDictionary.WILDCARD_VALUE) || (event.getItem().getItemDamage() == item.getMeta())) {
									if (item.getParams().length > 0) {
										final String recoveryAmount = item.getParams()[0];
										if (recoveryAmount.endsWith("%")) {
											final float amount = MathHelper.clamp(Float.parseFloat(recoveryAmount.replace("%", "")), 0, 100);
											magic.addMana(magic.getMaxMana() * (amount * 0.01F));
										} else {
											final float amount = Float.parseFloat(recoveryAmount);
											magic.addMana(amount);
										}
									}
								}
							}
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	//	@SubscribeEvent // Server only?
	//	public void ItemPickupEvent(ItemPickupEvent event) {

	//	}

	@SubscribeEvent // Both
	public void craftedSomething(ItemCraftedEvent event) {
		final ItemStack stack = event.crafting;
		if (stack.getItem() instanceof TrinketTeddyBear) {
			final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
			if (event.player != null) {
				if (event.player.getUniqueID() != null) {
					prop.getTagCompoundSafe(stack).setString("crafter.id", event.player.getUniqueID().toString());
				} else {
					prop.getTagCompoundSafe(stack).setString("crafter.name", event.player.getDisplayNameString());
				}
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Interaction End~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

}
