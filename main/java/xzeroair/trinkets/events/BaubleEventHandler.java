package xzeroair.trinkets.events;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class BaubleEventHandler {

	@SubscribeEvent
	public void EntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			if (player.isDead || (player.world == null)) {
				return;
			}
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if ((stack != null) && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface bauble = (IAccessoryInterface) stack.getItem();
					bauble.eventEntityJoinWorld(stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.END) {
			final EntityPlayer player = Minecraft.getMinecraft().player;
			if ((player == null) || player.isDead || (player.world == null)) {
				return;
			}
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if ((stack != null) && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface bauble = (IAccessoryInterface) stack.getItem();
					bauble.eventClientTick(stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void PlayerLoggedInEvent(PlayerLoggedInEvent event) {
		if ((event.player.world != null)) {
			final EntityPlayer player = event.player;
			final boolean client = player.world.isRemote;

			// config Sync
			if (!client) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
				if (baubles != null) {
					for (int i = 0; i < baubles.getSlots(); i++) {
						ItemStack stack = baubles.getStackInSlot(i);
						if (!stack.isEmpty()) {
							TrinketProperties prop = Capabilities.getTrinketProperties(stack);
							if (prop != null) {
								prop.sendInformationToPlayer(player, player);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void PlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
		final EntityPlayer player = event.player;

		final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (int i = 0; i < baubles.getSlots(); i++) {
			final ItemStack stack = baubles.getStackInSlot(i);
			if ((stack != null) && (stack.getItem() instanceof IAccessoryInterface)) {
				final IAccessoryInterface bauble = (IAccessoryInterface) stack.getItem();
				bauble.eventPlayerLogout(stack, player);
			}
		}
	}

	@SubscribeEvent
	public void PlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
	}

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer player = event.player;
		if ((event.phase == Phase.END)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface bauble = (IAccessoryInterface) stack.getItem();
					bauble.eventPlayerTick(stack, player);
				}
			}
			final ITrinketContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			if (Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					final ItemStack stack = Trinket.getStackInSlot(i);
					if (stack.getItem() instanceof IBauble) {
						final IBauble trinket = (IBauble) stack.getItem();
						trinket.onWornTick(stack, player);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void LivingUpdate(LivingUpdateEvent event) {

	}

	@SubscribeEvent
	public void potionApplicable(PotionApplicableEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if ((stack != null) && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface bauble = (IAccessoryInterface) stack.getItem();
					bauble.eventPotionApplicable(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		if (event.getEntityLiving().world.isRemote && (event.getEntityLiving() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if ((stack != null) && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface bauble = (IAccessoryInterface) stack.getItem();
					bauble.eventLivingJump(stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if ((stack != null) && (stack.getItem() instanceof IAccessoryInterface)) {
					final IAccessoryInterface bauble = (IAccessoryInterface) stack.getItem();
					bauble.eventLivingFall(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		if (event.getTarget() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getTarget();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventSetAttackTarget(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
		if (event.getAttackingPlayer() instanceof EntityPlayer) {
			final EntityPlayer player = event.getAttackingPlayer();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingExperienceDrops(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingDrops(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void deathEvent(LivingDamageEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingDamageAttacked(event, stack, player);
				}
			}
		}
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingDamageAttacker(event, stack, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventPlayerHurt(event, stack, player);
				}
			}
		}
		if ((event.getEntityLiving() != null) && (event.getSource().getTrueSource() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					trinket.eventLivingHurt(event, stack, player);
				}
			}
		}
	}
}
