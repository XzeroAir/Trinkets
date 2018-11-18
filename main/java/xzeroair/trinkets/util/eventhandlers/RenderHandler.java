package xzeroair.trinkets.util.eventhandlers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.client.renderer.PlayerRenderAlt;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.ItemCap.ItemProvider;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

public class RenderHandler {

	private Minecraft mc = Minecraft.getMinecraft();
	private IResourceManager resourceManager;
	private EntityRenderer renderer, prevRenderer;
	private static RenderPlayer render;
	private float slowYaw, yawTest;
	private int slowYawInt, yawTestInt;
	private PlayerRenderAlt altRender;

	@SubscribeEvent
	public void renderPlayerSizePre(RenderPlayerEvent.Pre event) {
		//		double x = player.posX;
		//		double y = player.posY;
		//		double z = player.posZ;
		//		float yaw = player.rotationYaw;
		//		float pitch = player.rotationPitch;

		if(event.getEntityPlayer() != null) {
			EntityPlayer player = event.getEntityPlayer();
			RenderPlayer renderPlayer = event.getRenderer();
			Item ringCheck = TrinketHelper.getBaubleType(player, TrinketType.rings);
			if(player.hasCapability(CapPro.sizeCapability, null)) {
				ICap nbt = player.getCapability(CapPro.sizeCapability, null);
				int size = nbt.getSize();
				float scale = (float) size/100;
				if((nbt.getTrans() == true)) {
					GlStateManager.pushMatrix();
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					if (player.isRiding()) {
						if((ringCheck == ModItems.small_ring)) {
							if(player.getRidingEntity() instanceof EntityMinecart) {
								event.setCanceled(true);
								if(altRender == null) {
									String s = ((AbstractClientPlayer)player).getSkinType();
									//								System.out.println(s);
									if(s.contains("slim")) {
										//									System.out.println(s + "---True");
										altRender = new PlayerRenderAlt(Minecraft.getMinecraft().getRenderManager(), true);
									} else {
										//									System.out.println(s + "---False");
										altRender = new PlayerRenderAlt(Minecraft.getMinecraft().getRenderManager(), false);
									}
								} else {
									int yaw = (int)player.rotationYaw;
									if (yaw<0) {              //due to the yaw running a -360 to positive 360
										//								yaw+=360;    //not sure why it's that way
										//						        yaw+=22;    //centers coordinates you may want to drop this line
										yaw%=360;  //and this one if you want a strict interpretation of the zones
									}
									int facing = yaw/45;  //  360degrees divided by 45 == 8 zones
									slowYawInt = 0;
									slowYaw = player.rotationYaw;
									if(slowYaw < player.rotationYaw) {
										slowYaw -= 360;
									} else {
										slowYaw += 360;
									}
									float t = 0.0F;
									float h = 0.0F;
									//								float t = MathHelper.cos(slowYaw/60);
									//								float h = MathHelper.sin(slowYaw/60);
									GlStateManager.translate(-h, 2.1F, t);
									altRender.doRender((AbstractClientPlayer)player, event.getX(), event.getY(), event.getZ(), player.rotationYawHead, event.getPartialRenderTick());

								}
							} else {
								GlStateManager.translate(0F, 1.8F, 0F);
							}
						}
						if((ringCheck == ModItems.dwarf_ring)) {
							GlStateManager.translate(0, 0.125F, 0);
						}
					} else {
					}
				} else {
					if(nbt.getSize() < nbt.getTarget()) {
						GlStateManager.pushMatrix();
						GlStateManager.scale(scale, scale, scale);
						GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void renderPlayerSizePost(RenderPlayerEvent.Post event) {
		if (event.getEntityPlayer() != null) {
			EntityPlayer player = event.getEntityPlayer();
			RenderPlayer renderPlayer = event.getRenderer();
			Item ringCheck = TrinketHelper.getBaubleType(player, TrinketType.rings);
			if(player.hasCapability(CapPro.sizeCapability, null)) {
				ICap nbt = player.getCapability(CapPro.sizeCapability, null);
				int size = nbt.getSize();
				if(nbt.getTrans() == true) {
					if(player.isRiding()) {
						if((ringCheck == ModItems.small_ring)) {

						}
					}
					GlStateManager.popMatrix();
				} else {
					if(nbt.getSize() < nbt.getTarget()) {
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderSpecialPre(RenderLivingEvent.Specials.Pre<AbstractClientPlayer> event) {
		//		if((event.getEntity() instanceof AbstractClientPlayer)) {
		//			AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
		//			Item ringCheck = TrinketHelper.getBaubleType(player, TrinketType.rings);
		//			if(player.hasCapability(CapPro.sizeCapability, null)) {
		//				ICap nbt = player.getCapability(CapPro.sizeCapability, null);
		//				int size = nbt.getSize();
		//				float scale = (float) size/100;
		//				GlStateManager.pushMatrix();
		//				GlStateManager.translate(0.0F, -10.0F, 0.0F);
		//				GlStateManager.popMatrix();
		//				System.out.println(size);
		//				if((nbt.getTrans() == true)) {
		//					GlStateManager.pushMatrix();
		//					GlStateManager.scale(scale, scale, scale);
		//					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
		//					if (player.isRiding()) {
		//						if((ringCheck == ModItems.small_ring)) {
		//							GlStateManager.translate((event.getX() / scale) - event.getX(), event.getY() + 1.8F, (event.getZ() / scale) - event.getZ());
		//						}
		//						if((ringCheck == ModItems.dwarf_ring)) {
		//							GlStateManager.translate(0, 0.125F, 0);
		//						}
		//					}
		//				} else {
		//					if(nbt.getSize() < nbt.getTarget()) {
		//						GlStateManager.pushMatrix();
		//						GlStateManager.scale(scale, scale, scale);
		//						GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
		//					}
		//				}
		//			}
		//		}
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		//		System.out.println("Hey! this code is running! better go Catch it!");
		if(Minecraft.getMinecraft().player != null) {
			int vd = TrinketsConfig.CLIENT.effects.DR.C00_VD;
			int hd = TrinketsConfig.CLIENT.effects.DR.C001_HD;
			int rf = TrinketsConfig.CLIENT.effects.C00_RR;
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack baubleCheck = TrinketHelper.getBaubleTypeStack(player, TrinketType.head);
			//		if(player.getHeldItemMainhand().getItem() == ModItems.glowing_ingot) {
			//
			//		}
			if(baubleCheck.getItem() == ModItems.dragons_eye) {
				ItemCap nbt = baubleCheck.getCapability(ItemProvider.itemCapability, null);

				if ((!TrinketsConfig.CLIENT.effects.C01_Dragon_Eye == false)) {
					if(nbt.oreType() != 0) {
						GlStateManager.pushMatrix();
						oreDetection(player, player.getEntityBoundingBox().grow(hd, vd, hd), rf, nbt.oreType());
						if(nbt.oreType() == 54) {
							chestCartDetect(player, player.getEntityBoundingBox().grow(hd, vd, hd), rf, nbt.oreType());
						}
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void renderTickEvent(TickEvent.RenderTickEvent event) {

		//		if (mc.world != null) {
		// System.out.println("Hey! this code is running! better go Catch it!");
		//			EntityPlayer player = mc.player;
		//		}
	}

	@SubscribeEvent
	public void RenderOverlayEvent(RenderGameOverlayEvent event) {
		//		if(event.getType() == event.getType().POTION_ICONS) {
		//			if() {
		//
		//			}
		//		}
	}

	@SubscribeEvent
	public void RenderLivingEvent(RenderLivingEvent.Pre event) {
		if(event.getEntity() instanceof EntityLiving) {
			if((!(event.getEntity() instanceof EntityPlayer)) && event.getEntity().hasCapability(CapPro.sizeCapability, null)) {
				ICap mobNBT = event.getEntity().getCapability(CapPro.sizeCapability, null);
				if(mobNBT.getTrans() == true) {
					float scale = (float)mobNBT.getSize()/100;
					GlStateManager.pushMatrix();
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
				} else {
					if(mobNBT.getSize() < 100) {
						float scale = (float)mobNBT.getSize()/100;
						GlStateManager.pushMatrix();
						GlStateManager.scale(scale, scale, scale);
						GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void RenderLivingEvent(RenderLivingEvent.Post event) {
		if(event.getEntity() instanceof EntityLiving) {
			if((!(event.getEntity() instanceof EntityPlayer)) && event.getEntity().hasCapability(CapPro.sizeCapability, null)) {
				ICap mobNBT = event.getEntity().getCapability(CapPro.sizeCapability, null);
				if(mobNBT.getTrans() == true) {
					GlStateManager.popMatrix();
				} else {
					if(mobNBT.getSize() < 100) {
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}

	public static void chestCartDetect(Entity entity, AxisAlignedBB aabb, int rf, int type) {
		List<EntityMinecartChest> cartList = entity.getEntityWorld().getEntitiesWithinAABB(EntityMinecartChest.class, aabb);
		if(!cartList.isEmpty()) {
			for(EntityMinecartChest chestCart : cartList) {
				double X = (Reference.random.nextDouble() + chestCart.posX) - 0.5;
				double Y = Reference.random.nextDouble() + chestCart.posY;
				double Z = (Reference.random.nextDouble() + chestCart.posZ) - 0.5;
				List<Float> color = TrinketHelper.getColor(type);
				if(((entity.ticksExisted % rf) == 0)) {
					if(entity.isSneaking()) {
						float volume = (float) entity.getDistance(X, Y, Z);
						float v = MathHelper.clamp((-volume/10)+1, 0.1F, 1.0F);
						entity.world.playSound((EntityPlayer) entity, chestCart.getPosition(), SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
					}
					ParticleGreed effect = new ParticleGreed(entity.world, X, Y, Z, X, Y, Z, color.get(0), color.get(1), color.get(2));
					Minecraft.getMinecraft().effectRenderer.addEffect(effect);
				}
			}
		}
	}

	private static void oreDetection(Entity entity, AxisAlignedBB aabb, int rf, int type) {
		int i = MathHelper.floor(aabb.minX);
		int j = MathHelper.floor(aabb.maxX + 1.0D);
		int k = MathHelper.floor(aabb.minY);
		int l = MathHelper.floor(aabb.maxY + 1.0D);
		int i1 = MathHelper.floor(aabb.minZ);
		int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
		;
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					BlockPos pos = new BlockPos(k1, l1, i2);
					IBlockState state = entity.world.getBlockState(pos);
					Block block = state.getBlock();
					if (entity.world.isRemote) {
						double X = Reference.random.nextDouble() + pos.getX();
						double Y = Reference.random.nextDouble() + pos.getY();
						double Z = Reference.random.nextDouble() + pos.getZ();
						List<Float> color = TrinketHelper.getColor(type);
						if ((block == block.getBlockById(type)) && ((entity.ticksExisted % rf) == 0)) {
							if(entity.isSneaking()) {
								float volume = (float) entity.getDistance(X, Y, Z);
								float v = MathHelper.clamp((-volume/10)+1, 0.1F, 1.0F);
								entity.world.playSound((EntityPlayer) entity, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
							}
							ParticleGreed effect = new ParticleGreed(entity.world, X, Y, Z, X, Y, Z, color.get(0), color.get(1), color.get(2));
							Minecraft.getMinecraft().effectRenderer.addEffect(effect);
						}
					}
				}
			}
		}
	}

	private static void renderBlockAt(Block block, IBlockState meta, BlockPos pos) {
		double renderPosX = Minecraft.getMinecraft().getRenderManager().viewerPosX;
		double renderPosY = Minecraft.getMinecraft().getRenderManager().viewerPosY;
		double renderPosZ = Minecraft.getMinecraft().getRenderManager().viewerPosZ;
		// GlStateManager.pushMatrix();
		GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
		GlStateManager.disableDepth();
		// GlStateManager.pushMatrix();
		// GlStateManager.enableBlend();
		// GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		// Minecraft.getMinecraft().renderEngine.bindTexture(new
		// ResourceLocation("xat:textures/particle/greed.png"));
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
		// GlStateManager.color(1, 1, 1, 1);
		brd.renderBlockBrightness(meta, 1.0F);
		// GlStateManager.color(1F, 1F, 1F, 1F);
		// GlStateManager.disableBlend();
		GlStateManager.enableDepth();
		// GlStateManager.popMatrix();
		// GlStateManager.popMatrix();
	}

	public static void drawSelectionBoxMask(AxisAlignedBB box, float red, float green, float blue, float alpha) {
		drawMask(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
	}

	public static void drawMask(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red,
			float green, float blue, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		drawMask(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
		tessellator.draw();
	}

	public static void drawMask(BufferBuilder b, double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float red, float green, float blue, float alpha) {
		// up
		b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
		// b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
		// down
		b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
		// b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
		// north
		b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
		// b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
		// south
		b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
		// b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
		// east
		b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
		// b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
		// west
		b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
		b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
		b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
		// b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
	}
}
