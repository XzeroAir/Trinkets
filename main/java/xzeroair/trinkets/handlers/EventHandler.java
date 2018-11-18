package xzeroair.trinkets.handlers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.CapDataMessage;
import xzeroair.trinkets.network.MobCapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketBaubleSync;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.SizeHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

public class EventHandler {

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		if(event.player.getEntityWorld().playerEntities.contains(event.player)) {
			EntityPlayer player = event.player;
			boolean client = player.world.isRemote;
			if(client) {
				//				if(Loader.isModLoaded("metamorph")) {
				//					System.out.println("True");
				//				}
				//				System.out.println(ClimbHandler.movingForward(player));
				//				System.out.println(player.getHorizontalFacing().getAxisDirection() + " : " + player.getHorizontalFacing().getAxis() + " : " + player.getHorizontalFacing());
				//				if(Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityDonkey) {
				//					EntityDonkey l = (EntityDonkey) Minecraft.getMinecraft().objectMouseOver.entityHit;
				//					System.out.println(l.width);
				//				}
			}

			if (player.hasCapability(CapPro.sizeCapability, null)) {
				Item ringCheck = TrinketHelper.getBaubleType(player, TrinketType.rings);
				ICap cap = player.getCapability(CapPro.sizeCapability, null);

				if((ringCheck == ModItems.small_ring)) {
					cap.setTrans(true);
					cap.setTarget(25);
					if(cap.getTrans() == true) {
						SizeHelper.fairyRing(player, cap.getTarget(), cap.getSize(), cap);
					}
				}

				if(ringCheck == ModItems.dwarf_ring) {
					cap.setTrans(true);
					cap.setTarget(75);
					if(cap.getTrans() == true) {
						SizeHelper.dwarfRing(player, cap.getTarget(), cap.getSize(), cap);
					}
				}

				if((ringCheck == null)) {
					cap.setTrans(false);
					cap.setTarget(100);

					if(cap.getSize() != cap.getTarget()) {
						if(cap.getTrans() == false) {
							if((player.stepHeight != 0.6f) && ((TrinketsConfig.CLIENT.C01_Step_Height != false))){
								player.stepHeight = 0.6f;
							}
						}
					}
				}
				SizeHelper.eyeHeightHandler(player, cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
				if(cap.getSize() != cap.getTarget()) {
					SizeHelper.sizeHandler(player, cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
				}
				//				if(!client && (event.phase == TickEvent.Phase.END)) {
				//					if((player.ticksExisted%200)==0) {
				//						EntityTracker et = ((WorldServer) player.world).getEntityTracker();
				//						if(!et.getTrackingPlayers(player).isEmpty()) {
				//							IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
				//							for(int i = 0; i < baubles.getSlots(); i++) {
				//								if(!baubles.getStackInSlot(i).isEmpty()) {
				//									if((baubles.getStackInSlot(i).getItem() == ModItems.small_ring) || (baubles.getStackInSlot(i).getItem() == ModItems.dwarf_ring)) {
				//										et.sendToTracking(player, NetworkHandler.INSTANCE.getPacketFrom(new PacketBaubleSync(player, i)));
				//									}
				//								}
				//							}
				//						}
				//					} else {
				//						//						System.out.println(player.getName() + "   " + ringCheck);
				//					}
				//				}
			}
		}

		//		DebugHandler.debugstuff(event.player);
	}

	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		if(event.getEntityPlayer() != null) {
			EntityPlayer player = event.getEntityPlayer();
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			Item ringCheck = TrinketHelper.getBaubleType(player, TrinketType.rings);
			if((ringCheck == ModItems.small_ring)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			boolean client = player.world.isRemote;
			Item ringCheck = TrinketHelper.getBaubleType(player, TrinketType.rings);
			if((ringCheck == ModItems.small_ring) || (ringCheck == ModItems.dwarf_ring)) {
				if((event.getSound() == SoundEvents.BLOCK_STONE_STEP) || (event.getSound() == SoundEvents.BLOCK_GRASS_STEP) || (event.getSound() == SoundEvents.BLOCK_CLOTH_STEP) || (event.getSound() == SoundEvents.BLOCK_WOOD_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_GRAVEL_STEP) || (event.getSound() == SoundEvents.BLOCK_SNOW_STEP) || (event.getSound() == SoundEvents.BLOCK_GLASS_STEP) || (event.getSound() == SoundEvents.BLOCK_METAL_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_ANVIL_STEP) || (event.getSound() == SoundEvents.BLOCK_LADDER_STEP) || (event.getSound() == SoundEvents.BLOCK_SLIME_STEP)) {
					if(!client) {
						if(!event.getEntity().isSprinting()) {
							event.setVolume(0.0F);
						} else {
							event.setVolume(0.1F);
						}
						//						if(((player.motionZ < -0.1F) || ((player.motionZ > 0.1F))) || ((player.motionX < -0.1F) || ((player.motionX > 0.1F)))) {
						//							//						event.setVolume(1.0F);
						//						} else {
						//							event.setVolume(0.0F);
						//						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void breakEvent(BlockEvent.BreakEvent event) {
		//		int posA = event.getPlayer().getPosition().getX();
		//		int posB = event.getPlayer().getPosition().getY();
		//		int posC = event.getPlayer().getPosition().getZ();
		//		EntityPlayer player = (EntityPlayer) event.getPlayer();
		//			if(event.getPlayer() instanceof EntityPlayer) {
		//				if(player.inventory.getCurrentItem().getItem() == ModItems.glowing_ingot) {
		//				player.world.createExplosion(null, posA, posB, posC, 1.0f, true);
		//				}
		//			}
	}

	@SubscribeEvent
	public void blockDrops(BlockEvent.HarvestDropsEvent event) {
		//		if(event.getHarvester().inventory.getCurrentItem().getItem() == ModItems.glowing_ingot){
		//			if(event.getState().getBlock() instanceof BlockPlanks) {
		//				event.getDrops().clear();
		//				event.getDrops().add(ModItems.glowing_ingot.getDefaultInstance());
		//			}
		//		}
	}

	@SubscribeEvent
	public void itemPickup(PlayerEvent.ItemPickupEvent event) {
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event){
		if((event.getEntityLiving() instanceof EntityLiving) && (!(event.getEntityLiving() instanceof EntityPlayer)) && event.getEntity().hasCapability(CapPro.sizeCapability, null)) {
			EntityLiving entity = (EntityLiving) event.getEntityLiving();
			ICap cap = entity.getCapability(CapPro.sizeCapability, null);
			if(cap.getTrans() == true) {
				//				entity.setGlowing(true);
				if(((entity.ticksExisted%800)==0)) {
					//					entity.setGlowing(false);
					cap.setTarget(100);
					if((entity.height != cap.getDefaultHeight()) || (entity.width != cap.getDefaultWidth())) {
						entity.height = cap.getDefaultHeight();
						entity.width = cap.getDefaultWidth();
					}
					cap.setTrans(false);
					if(!entity.world.isRemote) {
						NetworkHandler.INSTANCE.sendToAll(new MobCapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), entity.width, entity.height, cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEntityId()));
					}
				}
				SizeHelper.mobSize(entity, cap);
			} else {

			}
			if(cap.getSize() != cap.getTarget()) {
				SizeHelper.sizeHandler(event.getEntity(), cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
			}
		}
	}

	@SubscribeEvent
	public void startTracking(StartTracking event) {
		if(event.getEntityPlayer() != null) {
			EntityPlayer player = event.getEntityPlayer();
			boolean client = player.world.isRemote;
			if((event.getTarget() != null) && event.getTarget().hasCapability(CapPro.sizeCapability, null)) {
				Entity entity = event.getTarget();
				ICap cap = entity.getCapability(CapPro.sizeCapability, null);
				if(entity instanceof EntityPlayer) {
					IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer)entity);
					for(int i = 0; i < baubles.getSlots(); i++) {
						if(!baubles.getStackInSlot(i).isEmpty()) {
							if((baubles.getStackInSlot(i).getItem() == ModItems.small_ring) || (baubles.getStackInSlot(i).getItem() == ModItems.dwarf_ring)) {
								if(!client) {
									NetworkHandler.INSTANCE.sendTo(new PacketBaubleSync((EntityPlayer)entity, i), (EntityPlayerMP)player);
								}
							}
						}
					}
					if(!client) {
						NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEyeHeight(), entity.getEntityId()), (EntityPlayerMP)player);
					}
				} else {
					if(!client) {
						NetworkHandler.INSTANCE.sendTo(new MobCapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), entity.width, entity.height, cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEntityId()), (EntityPlayerMP)player);
					}
				}
			}
		}
	}

}
