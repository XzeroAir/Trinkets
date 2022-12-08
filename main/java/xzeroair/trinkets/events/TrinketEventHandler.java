package xzeroair.trinkets.events;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.END) {
			final EntityPlayer player = Minecraft.getMinecraft().player;
			if ((player == null) || !player.isEntityAlive() || (player.world == null))
				return;
			TrinketHelper.getTrinketHandler(player, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventClientTick(stack, player);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public void PlayerLoggedInEvent(PlayerLoggedInEvent event) {
		if ((event.player.world != null)) {
			final EntityPlayer player = event.player;
			final World world = player.getEntityWorld();
			final boolean client = world.isRemote;

			// sync Trinkets
			TrinketHelper.getTrinketHandler(player, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					final boolean empty = stack.isEmpty();
					if (!client && (world instanceof WorldServer)) {
						if (!empty) {
							Capabilities.getTrinketProperties(stack, properties -> {
								properties.itemEquipped(stack, player);
							});
						}
						final SyncItemDataPacket packet = new SyncItemDataPacket(player, stack, stack.getTagCompound(), i, ItemHandlerType.TRINKETS, true, !empty);
						NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
						//							NetworkHandler.sendToClients(
						//									(WorldServer) world, player.getPosition(),
						//									packet
						//							);
					}
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventPlayerLogin(stack, player);
					}
				}
			});
		}
	}

	//	@SubscribeEvent
	//	public void EntityJoinWorld(EntityJoinWorldEvent event) {
	//		if (event.getEntity() instanceof EntityPlayer) {
	//			final EntityPlayer player = (EntityPlayer) event.getEntity();
	//			TrinketHelper.getTrinketHandlerWithConsumer(player, (trinkets) -> {
	//				for (int i = 0; i < trinkets.getSlots(); i++) {
	//					final ItemStack stack = trinkets.getStackInSlot(i);
	//					final boolean empty = stack.isEmpty();
	//					if (!empty) {
	//						if (stack.getItem() instanceof IAccessoryInterface) {
	//							final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
	//							trinket.eventEntityJoinWorld(stack, player);
	//						}
	//					}
	//				}
	//			});
	//		}
	//	}

	@SubscribeEvent
	public void PlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
		final EntityPlayer player = event.player;
		TrinketHelper.getTrinketHandler(player, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventPlayerLogout(stack, player);
				}
			}
		});
	}

	@SubscribeEvent
	public void playerStartTracking(PlayerEvent.StartTracking event) {
		final EntityPlayer player = event.getEntityPlayer();
		final Entity target = event.getTarget();
		final World world = player.getEntityWorld();
		if (!world.isRemote && (world instanceof WorldServer)) {
			if (target instanceof EntityPlayer) {
				final EntityPlayer targetPlayer = (EntityPlayer) target;
				TrinketHelper.getTrinketHandler(targetPlayer, handler -> {
					for (int i = 0; i < handler.getSlots(); i++) {
						final ItemStack stack = handler.getStackInSlot(i);
						final boolean empty = stack.isEmpty();
						final SyncItemDataPacket packet = new SyncItemDataPacket(targetPlayer, stack, stack.getTagCompound(), i, ItemHandlerType.TRINKETS, true, !empty);
						NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
					}
				});
			}
		}
	}

	@SubscribeEvent
	public void PlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
		EntityPlayer player = event.player;
		TrinketHelper.getTrinketHandler(player, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventPlayerChangedDimension(stack, player, event.fromDim, event.toDim);
				}
			}
		});
	}

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer player = event.player;
		if (!player.isEntityAlive())
			return;
		if ((event.phase == Phase.END)) {
			TrinketHelper.getTrinketHandler(player, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventPlayerTick(stack, player);
					}
					//					if (stack.getItem() instanceof IBauble) {
					//						((IBauble) stack.getItem()).onWornTick(stack, player);
					//					}
				}
			});
		}
	}

	@SubscribeEvent
	public void LivingUpdate(LivingUpdateEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		if (!(entity instanceof EntityPlayer)) {
			TrinketHelper.getTrinketHandler(entity, trinkets -> {
				for (int i = 0; i < trinkets.getSlots(); i++) {
					final ItemStack stack = trinkets.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventLivingUpdateTick(stack, entity);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public void potionApplicable(PotionApplicableEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		TrinketHelper.getTrinketHandler(entity, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventPotionApplicable(stack, entity, event);
				}
			}
		});
	}

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		TrinketHelper.getTrinketHandler(entity, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventLivingJump(stack, entity);
				}
			}
		});
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		TrinketHelper.getTrinketHandler(entity, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventLivingFall(stack, entity, event);
				}
			}
		});
	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		final EntityLivingBase entity = event.getTarget();
		TrinketHelper.getTrinketHandler(entity, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventSetAttackTarget(stack, entity, event.getEntityLiving());
				}
			}
		});
	}

	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
		final EntityLivingBase attacked = event.getEntityLiving();
		TrinketHelper.getTrinketHandler(attacked, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventLivingHurtAttacked(stack, attacked, event);
				}
			}
		});
		if ((event.getSource().getTrueSource() instanceof EntityLivingBase)) {
			final EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			TrinketHelper.getTrinketHandler(attacker, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventLivingHurtAttacker(stack, attacker, event);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public void deathEvent(LivingDamageEvent event) {
		final EntityLivingBase attacked = event.getEntityLiving();
		TrinketHelper.getTrinketHandler(attacked, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventLivingDamageAttacked(stack, attacked, event);
				}
			}
		});
		if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
			final EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			TrinketHelper.getTrinketHandler(attacker, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventLivingDamageAttacker(stack, attacker, event);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
		final EntityPlayer player = event.getAttackingPlayer();
		TrinketHelper.getTrinketHandler(player, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventLivingExperienceDrops(stack, player, event);
				}
			}
		});
	}

	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
			final EntityLivingBase entity = (EntityLivingBase) event.getSource().getTrueSource();
			TrinketHelper.getTrinketHandler(entity, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventLivingDrops(stack, entity, event);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public void onAttemptToBreakBlock(BreakSpeed event) {
		EntityPlayer player = event.getEntityPlayer();
		TrinketHelper.getTrinketHandler(player, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventBreakSpeed(stack, player, event);
				}
			}
		});
	}

	@SubscribeEvent
	public void onBlockBroken(BlockEvent.BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		TrinketHelper.getTrinketHandler(player, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventBlockBreak(stack, player, event);
				}
			}
		});
	}

	@SubscribeEvent
	public void onBlockDrops(BlockEvent.HarvestDropsEvent event) {
		EntityPlayer player = event.getHarvester();
		TrinketHelper.getTrinketHandler(player, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				boolean empty = stack.isEmpty();
				if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
					item.eventBlockDrops(stack, player, event);
				}
			}
		});
	}

	//	@SubscribeEvent
	//	public void playerRespawn(PlayerRespawnEvent event) {
	//
	//	}

	@SubscribeEvent
	public void playerDeath(PlayerDropsEvent event) {
		if ((event.getEntity() instanceof EntityPlayer)
				&& !event.getEntity().world.isRemote
				&& !event.getEntity().world.getGameRules().getBoolean("keepInventory")) {
			this.dropItemsAt(event.getEntityPlayer(), event.getDrops(), event.getEntityPlayer());
		}
	}

	public void dropItemsAt(EntityPlayer player, List<EntityItem> drops, Entity e) {
		TrinketHelper.getTrinketHandler(player, Trinket -> {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				if (!Trinket.getStackInSlot(i).isEmpty()) {
					final EntityItem ei = new EntityItem(
							e.world,
							e.posX, e.posY + e.getEyeHeight(), e.posZ,
							Trinket.getStackInSlot(i).copy()
					);
					ei.setPickupDelay(40);
					final float f1 = e.world.rand.nextFloat() * 0.5F;
					final float f2 = e.world.rand.nextFloat() * (float) Math.PI * 2.0F;
					ei.motionX = -MathHelper.sin(f2) * f1;
					ei.motionZ = MathHelper.cos(f2) * f1;
					ei.motionY = 0.20000000298023224D;
					drops.add(ei);
					Trinket.setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		});
	}

	@SubscribeEvent
	public void PlayerCloneEvent(PlayerEvent.Clone event) {
		try {
			final TrinketContainerHandler bco = (TrinketContainerHandler) event.getOriginal().getCapability(TrinketContainerProvider.containerCap, null);
			final NBTTagCompound nbt = bco.serializeNBT();
			final TrinketContainerHandler bcn = (TrinketContainerHandler) event.getEntityPlayer().getCapability(TrinketContainerProvider.containerCap, null);
			bcn.deserializeNBT(nbt);
		} catch (final Exception e) {

		}
	}
}
