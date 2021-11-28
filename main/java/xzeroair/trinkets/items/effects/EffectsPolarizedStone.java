package xzeroair.trinkets.items.effects;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.handlers.ItemEffectHandler;
import xzeroair.trinkets.util.handlers.TickHandler;

public class EffectsPolarizedStone {

	private static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	public static void collectDrops(ItemStack stack, EntityLivingBase entity) {
		final TrinketProperties cap = Capabilities.getTrinketProperties(stack);

		if ((cap != null)) {
			if (cap.mainAbility()) {
				final AxisAlignedBB bBox = entity.getEntityBoundingBox();
				final Predicate<Entity> lootPredicate = Predicates.and(
						EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE,
						ent -> (ent instanceof EntityItem) || ((ent instanceof EntityXPOrb) && serverConfig.collectXP)
				);
				final Predicate<EntityPlayer> otherPlayerPredicate = Predicates.and(
						EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE,
						ent -> (ent != null) && ent.canBeCollidedWith() && ((ent.getHeldItemMainhand().getItem() instanceof TrinketPolarized) || TrinketHelper.AccessoryCheck(ent, ModItems.trinkets.TrinketPolarized))
				);
				final List<Entity> Loot = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(serverConfig.PR.HD, serverConfig.PR.VD, serverConfig.PR.HD), lootPredicate);
				final List<EntityPlayer> others = entity.world.getEntitiesWithinAABB(EntityPlayer.class, bBox.grow(serverConfig.PR.HD, serverConfig.PR.VD, serverConfig.PR.HD), otherPlayerPredicate);

				for (final Entity loot : Loot) {
					final double distance = loot.getDistance(entity.posX, entity.posY, entity.posZ);
					boolean someonesCloser = false;
					for (final EntityPlayer otherP : others) {
						final float dist = loot.getDistance(otherP);
						if (dist < distance) {
							someonesCloser = true;
						}
					}
					if (!someonesCloser) {
						handleLoot(entity, loot);
					}
				}
			}
		}
	}

	private static void handleLoot(Entity entity, Entity drop) {
		if (!serverConfig.instant_pickup) {
			if ((drop instanceof EntityItem) || (serverConfig.collectXP && (drop instanceof EntityXPOrb))) {
				ItemEffectHandler.pull(drop, entity.posX, entity.posY, entity.posZ);
			}
		} else {
			if ((entity instanceof EntityPlayer)) {
				final EntityPlayer player = (EntityPlayer) entity;
				if (!player.world.isRemote) {
					if (drop instanceof EntityItem) {
						pickupItem(player, drop);
					} else {
						if (serverConfig.collectXP && (drop instanceof EntityXPOrb)) {
							pickupXP(player, drop);
						}
					}
				}
			} else {
				ItemEffectHandler.pull(drop, entity.posX, entity.posY, entity.posZ);
			}
		}
	}

	private static void pickupItem(EntityPlayer player, Entity itemEntity) {
		if (itemEntity instanceof EntityItem) {
			final EntityItem item = (EntityItem) itemEntity;
			if (!(item.getItem().getItem() instanceof TrinketPolarized)) {
				for (final ItemStack slotStack : player.inventory.mainInventory) {
					if (slotStack.isEmpty()) {
						player.addItemStackToInventory(item.getItem());
					} else if (slotStack.isItemEqual(item.getItem())) {
						if (slotStack.isStackable()) {
							// if (item.getItem().areItemStackShareTagsEqual(slotStack, item.getItem())) {
							if (ItemStack.areItemStackShareTagsEqual(slotStack, item.getItem())) {
								if ((slotStack.getCount() + item.getItem().getCount()) <= slotStack.getMaxStackSize()) {
									player.addItemStackToInventory(item.getItem());
								} else {
									if ((slotStack.getMaxStackSize() - slotStack.getCount()) > 0) {
										player.addItemStackToInventory(item.getItem().splitStack((slotStack.getMaxStackSize() - slotStack.getCount())));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static void pickupXP(EntityPlayer player, Entity xpOrb) {
		if (xpOrb instanceof EntityXPOrb) {
			final EntityXPOrb xp = (EntityXPOrb) xpOrb;
			final ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);

			if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
				final float ratio = itemstack.getItem().getXpRepairRatio(itemstack);
				final int iE = Math.min(roundAverage(xp.xpValue * ratio), itemstack.getItemDamage());
				xp.xpValue -= roundAverage(iE / ratio);
				itemstack.setItemDamage(itemstack.getItemDamage() - iE);
			}

			if (xp.xpValue > 0) {
				player.addExperience(xp.xpValue);
			}
			if (!xp.isDead) {
				xp.setDead();
			}
		}
	}

	private static int roundAverage(float value) {
		final double floor = Math.floor(value);
		return (int) floor + (Math.random() < (value - floor) ? 1 : 0);
	}

	public static void blockArrows(ItemStack stack, EntityLivingBase entity) {
		final TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null) && cap.altAbility()) {
			if (serverConfig.exhaustion) {
				final Predicate<EntityLivingBase> filter = Predicates.and(
						EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE
				);
				final boolean flag = filter.apply(entity);
				if (flag) {
					final MagicStats magic = Capabilities.getMagicStats(entity);
					if (magic != null) {
						final float exhaustRate = serverConfig.exhaust_rate;
						final TickHandler counter = cap.getCounter("repel.ticks", serverConfig.exhaust_ticks, false);
						if (exhaustRate <= magic.getMana()) {
							if (counter.Tick()) {
								magic.spendMana(exhaustRate);
							}
						} else {
							return;
						}
					}
				}
			}

			try {
				final AxisAlignedBB bBox = entity.getEntityBoundingBox();
				final List<String> cfg = Arrays.asList(serverConfig.repelledEntities);
				final Predicate<Entity> Targets = Predicates.and(
						EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE,
						ent -> (ent != null) && !(ent instanceof EntityPlayer) &&
								cfg.contains(EntityRegistry.getEntry(ent.getClass()).getRegistryName().toString())
				);
				final List<Entity> entityList = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(1.1), Targets);
				for (final Entity repelledEntity : entityList) {
					final Vec3d playerVec3 = entity.getLookVec();
					repelledEntity.motionX = playerVec3.x * 0.3D;
					repelledEntity.motionY = playerVec3.y * 0.3D;
					repelledEntity.motionZ = playerVec3.z * 0.3D;
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean onHotbar(EntityPlayer player) {
		boolean onHotBar = false;
		//		for (int i = 0; i < player.inventory.getHotbarSize(); i++) {
		//			if (player.inventory.isHotbar(i)) {
		for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
			if (InventoryPlayer.isHotbar(i)) {
				if (player.inventory.getStackInSlot(i).getItem() instanceof TrinketPolarized) {
					onHotBar = true;
					final ItemStack Magnet = player.inventory.getStackInSlot(i);
					final TrinketProperties ICap = Capabilities.getTrinketProperties(Magnet);
					if ((Magnet != null) && (ICap != null)) {
						if (ICap.mainAbility()) {
							EffectsPolarizedStone.collectDrops(Magnet, player);
						}
						if (serverConfig.repell) {
							if (ICap.altAbility()) {
								EffectsPolarizedStone.blockArrows(Magnet, player);
							}
						}
					}
				}
			}
		}
		return onHotBar;
	}

	public static boolean whileHeld(EntityPlayer player) {
		boolean held = false;
		if ((player.getHeldItemMainhand().getItem() instanceof TrinketPolarized)) {
			held = true;
			final ItemStack Magnet = player.getHeldItemMainhand();
			final TrinketProperties ICap = Capabilities.getTrinketProperties(Magnet);
			if ((Magnet != null) && (ICap != null)) {
				if (ICap.mainAbility()) {
					EffectsPolarizedStone.collectDrops(Magnet, player);
				}
				if (serverConfig.repell) {
					if (ICap.altAbility()) {
						EffectsPolarizedStone.blockArrows(Magnet, player);
					}
				}
			}
		} else {
			if ((player.getHeldItemOffhand().getItem() instanceof TrinketPolarized)) {
				held = true;
				final ItemStack Magnet = player.getHeldItemOffhand();
				final TrinketProperties ICap = Capabilities.getTrinketProperties(Magnet);
				if ((Magnet != null) && (ICap != null)) {
					if (ICap.mainAbility()) {
						EffectsPolarizedStone.collectDrops(Magnet, player);
					}
					if (serverConfig.repell) {
						if (ICap.altAbility()) {
							EffectsPolarizedStone.blockArrows(Magnet, player);
						}
					}
				}
			}
		}
		return held;
	}

	public static void processBauble(EntityPlayer player) {
		if (!TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
			if (!whileHeld(player)) {
				onHotbar(player);
			}
		} else {
			final ItemStack magnet = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
			final TrinketProperties iCap = Capabilities.getTrinketProperties(magnet);
			if ((iCap != null) && !(iCap.mainAbility() || iCap.altAbility())) {
				if (!whileHeld(player)) {
					onHotbar(player);
				}
			}
		}
	}

	public static void handleStatus(ItemStack stack, TrinketProperties iCap) {
		if (iCap.mainAbility() && !iCap.altAbility()) {
			stack.setItemDamage(1);
		} else if (iCap.altAbility() && !iCap.mainAbility()) {
			stack.setItemDamage(2);
		} else if (iCap.altAbility() && iCap.mainAbility()) {
			stack.setItemDamage(3);
		} else {
			stack.setItemDamage(0);
		}
	}
}
