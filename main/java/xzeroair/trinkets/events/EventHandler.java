package xzeroair.trinkets.events;

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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.items.trinkets.TrinketTeddyBear;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.util.TrinketStatusEffect;
import xzeroair.trinkets.util.TrinketsConfig;

public class EventHandler {

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
		final EntityPlayer player = event.player;
		if (player.isDead || (player.getHealth() <= 0)) {
			return;
		}
		if ((event.phase == Phase.END)) {
			VipStatus status = Capabilities.getVipStatus(player);
			if (status != null) {
				status.onUpdate();
			}
			EntityProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				cap.onUpdate();
			}
		}
		if ((player != null) && !(player.isDead)) {
			EffectsPolarizedStone.processBauble(player);
		}
		//		if (player.isPotionActive(MobEffects.RESISTANCE)) {
		//			PotionEffect effect = player.getActivePotionEffect(MobEffects.RESISTANCE);
		//			if (effect.getAmplifier() < 2) {
		//			System.out.println(effect.getDuration() + " Duration, Amplifier " + effect.getAmplifier());
		//			}
		//		}
	}

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {
		EntityProperties cap = Capabilities.getEntityRace(event.getEntityLiving());
		if (cap != null) {
			cap.getRaceProperties().interact(event);
		}
	}

	@SubscribeEvent
	public void EntityUpdate(LivingUpdateEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		if (!(entity instanceof EntityPlayer)) {
			EntityProperties cap = Capabilities.getEntityRace(entity);
			if (cap != null) {
				cap.onUpdate();
			}
		}
	}

	@SubscribeEvent
	public void onMount(EntityMountEvent event) {
		Entity entity = event.getEntityMounting();
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			EntityProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				cap.getRaceProperties().mountedEntity(event);
			}
		}
	}

	@SubscribeEvent
	public void potionApply(PotionApplicableEvent event) {
		if ((event.getEntityLiving() == null) || (event.getEntityLiving().getEntityWorld() == null)) {
			return;
		}
		EntityProperties cap = Capabilities.getEntityRace(event.getEntityLiving());
		if (cap != null) {
			cap.getRaceProperties().potionBeingApplied(event);
		}
	}

	@SubscribeEvent
	public void onItemUsingTickEvent(LivingEntityUseItemEvent.Tick event) {
		EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			if (event.getItem().getItem() instanceof ItemBow) {
				int charge = 72000 - event.getDuration();
				prop.getRaceProperties().bowDrawing(event.getItem(), charge);
			}
		}
	}

	@SubscribeEvent
	public void onItemStopUseEvent(LivingEntityUseItemEvent.Stop event) {
		EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			if (event.getItem().getItem() instanceof ItemBow) {
				if (event.getDuration() < 72000) {
					int charge = 72000 - event.getDuration();
					prop.getRaceProperties().bowUsed(event.getItem(), charge);
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemFinishUseEvent(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
			if (prop != null) {
				String[] recoveryItems = TrinketsConfig.SERVER.mana.recovery;
				for (String item : recoveryItems) {
					String[] itemConfig = item.split(":");
					if (itemConfig.length > 0) {
						if (event.getItem().getItem().getRegistryName().toString().equalsIgnoreCase(itemConfig[0] + ":" + itemConfig[1])) {
							if (itemConfig.length > 1) {
								if (event.getItem().getItemDamage() == Integer.parseInt(itemConfig[2])) {
									if (itemConfig.length > 2) {
										String recoveryAmount = itemConfig[3];
										if (recoveryAmount.endsWith("%")) {
											float amount = MathHelper.clamp(Float.parseFloat(recoveryAmount.replace("%", "")), 0, 100);
											prop.getMagic().addMana(prop.getMagic().getMaxMana() * (amount * 0.01F));
										} else {
											float amount = Float.parseFloat(recoveryAmount);
											prop.getMagic().addMana(amount);
										}
									}
								}
							}
						}
					}
				}
				FaelisConfig faelisConfig = TrinketsConfig.SERVER.races.faelis;
				if (prop.getCurrentRace().equals(EntityRaces.faelis) && faelisConfig.Invigorated) {
					String[] Milk = faelisConfig.milk;
					for (String s : Milk) {
						if (event.getItem().getItem().getRegistryName().toString().contentEquals(s)) {
							prop.getStatusHandler().apply(new TrinketStatusEffect("Invigorated", faelisConfig.Invigorated_Duration, 0, null));
							break;
						}
					}
				}
			}
		}
	}

	@SubscribeEvent // Server only?
	public void ItemPickupEvent(ItemPickupEvent event) {
		//		ItemStack stack = event.getStack();
		//		if (stack.getItem() instanceof TrinketTeddyBear) {
		//			System.out.println("Picked Up Bear");
		//		}
	}

	@SubscribeEvent // Both
	public void craftedSomething(ItemCraftedEvent event) {
		ItemStack stack = event.crafting;
		if (stack.getItem() instanceof TrinketTeddyBear) {
			TrinketProperties prop = Capabilities.getTrinketProperties(stack);
			if (event.player != null) {
				if (event.player.getUniqueID() != null) {
					prop.getTagCompoundSafe(stack).setString("crafter.id", event.player.getUniqueID().toString());
				} else {
					prop.getTagCompoundSafe(stack).setString("crafter.name", event.player.getDisplayNameString());
				}
			}
			//			stack.setStackDisplayName(event.player.getName() + "'s " + stack.getDisplayName());
		}
	}

	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {
		if ((event.getSound() != null) && (event.getEntity() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final boolean client = player.world.isRemote;
			EntityProperties cap = Capabilities.getEntityRace(player);
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
}
