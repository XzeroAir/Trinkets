package xzeroair.trinkets.items.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.handlers.ItemEffectHandler;

public class EffectsPolarizedStone {

	private static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	private final float playerDistance = 0;
	private static float otherDistance = 0;
	private final float otherID = 0;
	private final float dropDistance = 0;
	private static List<String> drops = new ArrayList<>();

	private static List<String> getDrops() {
		return drops;
	}

	private static void clearDrops() {
		if (!drops.isEmpty()) {
			drops.clear();
		}
	}

	private static float getOtherDistance() {
		return otherDistance;
	}

	private static void setOtherDistance(float distance) {
		otherDistance = distance;
	}

	public static void collectDrops(ItemStack stack, EntityLivingBase entity) {
		if ((entity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) entity;
			AxisAlignedBB bBox = entity.getEntityBoundingBox();
			List<Entity> List = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(serverConfig.PR.HD, serverConfig.PR.VD, serverConfig.PR.HD));
			TrinketProperties cap = Capabilities.getTrinketProperties(stack);

			if ((cap != null)) {// && !(player.inventory.getFirstEmptyStack() < 0)) {
				if (cap.mainAbility()) {
					for (final Entity targets : List) {
						if (targets instanceof EntityItem) {
							if (!getDrops().contains(String.valueOf(targets.getEntityId()))) {
								getDrops().add(String.valueOf(targets.getEntityId()));
							}
						}
						if (serverConfig.collectXP && (targets instanceof EntityXPOrb)) {
							if (!getDrops().contains(String.valueOf(targets.getEntityId()))) {
								getDrops().add(String.valueOf(targets.getEntityId()));
							}
						}
					}
					if (!getDrops().isEmpty()) {
						for (int i = 0; i < getDrops().size(); i++) {
							if ((getDrops().size() > 0) && (i < getDrops().size())) {
								final String dropID = getDrops().get(i);
								if ((dropID != null) && !dropID.isEmpty()) {
									final Entity drop = player.world.getEntityByID(Integer.parseInt(dropID));
									if (drop != null) {
										for (final Entity players : List) {
											if ((players instanceof EntityPlayer) && (players != player)) {
												final EntityPlayer otherPlayer = (EntityPlayer) players;
												if (cap.mainAbility()) {
													if ((getOtherDistance() == 0) || (players.getDistance(drop) < getOtherDistance())) {
														setOtherDistance(players.getDistance(drop));
													}
												}
											}
										}
										if ((player.getDistance(drop) < getOtherDistance()) || (getOtherDistance() == 0)) {
											if (!serverConfig.instant_pickup) {
												if ((drop instanceof EntityItem) || (serverConfig.collectXP && (drop instanceof EntityXPOrb))) {
													ItemEffectHandler.pull(drop, player.posX, player.posY, player.posZ);
												}
											} else {
												if (!player.world.isRemote) {
													if (drop instanceof EntityItem) {
														final EntityItem item = (EntityItem) drop;
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
													} else {
														if (serverConfig.collectXP && (drop instanceof EntityXPOrb)) {
															final EntityXPOrb xp = (EntityXPOrb) drop;
															player.addExperience(xp.xpValue);
															if (!xp.isDead) {
																xp.setDead();
															}
														}
													}
												}
											}
										}
										setOtherDistance(0);
									}
								}
							}
						}
						clearDrops();
					}
				} else {
					clearDrops();
				}
			}
		}
	}

	public static void blockArrows(ItemStack stack, EntityLivingBase entity) {
		if ((entity instanceof EntityPlayer)) {
			final TrinketProperties cap = Capabilities.getTrinketProperties(stack);
			if ((cap != null) && cap.altAbility()) {
				if (serverConfig.exhaustion) {
					if (!((EntityPlayer) entity).isCreative()) {
						EntityProperties prop = Capabilities.getEntityRace(entity);
						if (prop != null) {
							float exhaustRate = serverConfig.exhaust_rate;
							if (exhaustRate <= prop.getMagic().getMana()) {
								if ((entity.ticksExisted % serverConfig.exhaust_ticks) == 0) {
									prop.getMagic().spendMana(exhaustRate);
									//									((EntityPlayer) entity).getFoodStats().addExhaustion(serverConfig.exhaust_rate);
								}
							} else {
								if (entity instanceof EntityPlayer) {
									if ((entity.ticksExisted % 60) == 0) {
										String Message = "No MP";
										((EntityPlayer) entity).sendStatusMessage(new TextComponentString(Message), true);
									}
								}
								return;
							}
						}
					}
				}
				final AxisAlignedBB bBox = entity.getEntityBoundingBox();
				final List<Entity> entityList = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(1.1));
				for (final Entity repelledEntity : entityList) {
					if ((repelledEntity != null) && !(repelledEntity instanceof EntityPlayer)) {
						String e = EntityRegistry.getEntry(repelledEntity.getClass()).getRegistryName().toString();
						for (String s : serverConfig.repelledEntities) {
							if (e.equalsIgnoreCase(s)) {
								Vec3d playerVec3 = entity.getLookVec();
								repelledEntity.motionX = (playerVec3.x * 0.3D);
								repelledEntity.motionY = (playerVec3.y * 0.3D);
								repelledEntity.motionZ = (playerVec3.z * 0.3D);
							}
						}
					}
				}
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
			;
		} else {
			final ItemStack magnet = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
			final TrinketProperties iCap = Capabilities.getTrinketProperties(magnet);
			if ((iCap != null) && !(iCap.mainAbility() || iCap.altAbility())) {
				if (!whileHeld(player)) {
					onHotbar(player);
				}
				;
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
