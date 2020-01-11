package xzeroair.trinkets.events;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.IContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketEventHandler {

	@SubscribeEvent
	public void EntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();

			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if(stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventEntityJoinWorld(stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void PlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
		final EntityPlayer player = event.player;

		final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
		for (int i = 0; i < Trinket.getSlots(); i++) {
			final ItemStack stack = Trinket.getStackInSlot(i);
			if (stack.getItem() instanceof IAccessoryInterface) {
				final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
				trinket.eventPlayerLogout(stack, player);
			}
		}
	}

	@SubscribeEvent
	public void PlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {

	}

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer player = event.player;
		final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
		if((event.phase == Phase.END)) {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventPlayerTick(stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void LivingUpdate(LivingUpdateEvent event) {

	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		if(event.getTarget() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getTarget();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventSetAttackTarget(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
		if(event.getAttackingPlayer() instanceof EntityPlayer) {
			final EntityPlayer player = event.getAttackingPlayer();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingExperienceDrops(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
		if(event.getSource().getTrueSource() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingDrops(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void deathEvent(LivingDamageEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingDamageAttacked(event, stack, player);
				}
			}
		}
		if(event.getSource().getTrueSource() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingDamageAttacker(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventPlayerHurt(event, stack, player);
				}
			}
		}
		if((event.getEntityLiving() != null) && (event.getSource().getTrueSource() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingHurt(event, stack, player);
				}
			}
		}
	}


	@SubscribeEvent
	public void livingJump(LivingJumpEvent event){
		if(event.getEntityLiving().world.isRemote && (event.getEntityLiving() instanceof EntityPlayer)){
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			if(Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					final ItemStack stack = Trinket.getStackInSlot(i);
					if (stack.getItem() instanceof IAccessoryInterface) {
						final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
						trinket.eventLivingJump(stack, player);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			if(Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					final ItemStack stack = Trinket.getStackInSlot(i);
					if (stack.getItem() instanceof IAccessoryInterface) {
						final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
						trinket.eventLivingFall(event, stack, player);
					}
				}
			}
		}
	}


	@SubscribeEvent
	public void playerDeath(PlayerDropsEvent event) {
		if ((event.getEntity() instanceof EntityPlayer)
				&& !event.getEntity().world.isRemote
				&& !event.getEntity().world.getGameRules().getBoolean("keepInventory")) {
			dropItemsAt(event.getEntityPlayer(),event.getDrops(),event.getEntityPlayer());
		}
	}

	public void dropItemsAt(EntityPlayer player, List<EntityItem> drops, Entity e) {
		final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
		for (int i = 0; i < Trinket.getSlots(); i++) {
			if ((Trinket.getStackInSlot(i) != null) && !Trinket.getStackInSlot(i).isEmpty()) {
				final EntityItem ei = new EntityItem(e.world,
						e.posX, e.posY + e.getEyeHeight(), e.posZ,
						Trinket.getStackInSlot(i).copy());
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
	}

	@SubscribeEvent
	public void PlayerCloneEvent(PlayerEvent.Clone event)
	{
		try {
			final TrinketContainerHandler bco = (TrinketContainerHandler) event.getOriginal().getCapability(TrinketContainerProvider.containerCap, null);
			final NBTTagCompound nbt = bco.serializeNBT();
			final TrinketContainerHandler bcn = (TrinketContainerHandler) event.getEntityPlayer().getCapability(TrinketContainerProvider.containerCap, null);
			bcn.deserializeNBT(nbt);
		} catch (final Exception e) {

		}
	}
}
