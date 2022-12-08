package xzeroair.trinkets.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
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
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.util.compat.baubles.BaublesHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class BaubleEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.END) {
			final EntityPlayer player = Minecraft.getMinecraft().player;
			if ((player == null) || !player.isEntityAlive() || (player.world == null))
				return;
			BaublesHelper.getBaublesHandler(player, handler -> {
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
			World world = player.getEntityWorld();
			final boolean client = world.isRemote;
			BaublesHelper.getBaublesHandler(player, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!client && (world instanceof WorldServer)) {
						if (!empty) {
							Capabilities.getTrinketProperties(stack, properties -> {
								properties.itemEquipped(stack, player);
							});
						}
						final SyncItemDataPacket packet = new SyncItemDataPacket(player, stack, stack.getTagCompound(), i, ItemHandlerType.BAUBLES, true, !empty);
						NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
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
	//	}

	@SubscribeEvent
	public void PlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
		final EntityPlayer player = event.player;
		BaublesHelper.getBaublesHandler(player, handler -> {
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
				BaublesHelper.getBaublesHandler(targetPlayer, handler -> {
					for (int i = 0; i < handler.getSlots(); i++) {
						final ItemStack stack = handler.getStackInSlot(i);
						final boolean empty = stack.isEmpty();
						final SyncItemDataPacket packet = new SyncItemDataPacket(targetPlayer, stack, stack.getTagCompound(), i, ItemHandlerType.BAUBLES, true, !empty);
						NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
					}
				});
			}
		}
	}

	@SubscribeEvent
	public void PlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
		EntityPlayer player = event.player;
		BaublesHelper.getBaublesHandler(player, handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				final ItemStack stack = handler.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
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
			BaublesHelper.getBaublesHandler(player, handler -> {
				for (int i = 0; i < handler.getSlots(); i++) {
					final ItemStack stack = handler.getStackInSlot(i);
					boolean empty = stack.isEmpty();
					if (!empty && (stack.getItem() instanceof IAccessoryInterface)) {
						final IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
						item.eventPlayerTick(stack, player);
					}
				}
			});
		}
	}

	@SubscribeEvent
	public void potionApplicable(PotionApplicableEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		BaublesHelper.getBaublesHandler(entity, handler -> {
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
		EntityLivingBase entity = event.getEntityLiving();
		BaublesHelper.getBaublesHandler(entity, handler -> {
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
		EntityLivingBase entity = event.getEntityLiving();
		BaublesHelper.getBaublesHandler(entity, handler -> {
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
		EntityLivingBase entity = event.getEntityLiving();
		BaublesHelper.getBaublesHandler(entity, handler -> {
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
		EntityLivingBase attacked = event.getEntityLiving();
		BaublesHelper.getBaublesHandler(attacked, handler -> {
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
			BaublesHelper.getBaublesHandler(attacker, handler -> {
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
		EntityLivingBase attacked = event.getEntityLiving();
		BaublesHelper.getBaublesHandler(attacked, handler -> {
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
			BaublesHelper.getBaublesHandler(attacker, handler -> {
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
		EntityPlayer player = event.getAttackingPlayer();
		BaublesHelper.getBaublesHandler(player, handler -> {
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
			EntityLivingBase entity = (EntityLivingBase) event.getSource().getTrueSource();
			BaublesHelper.getBaublesHandler(entity, handler -> {
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
		BaublesHelper.getBaublesHandler(player, handler -> {
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
		BaublesHelper.getBaublesHandler(player, handler -> {
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
		BaublesHelper.getBaublesHandler(player, handler -> {
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
}
