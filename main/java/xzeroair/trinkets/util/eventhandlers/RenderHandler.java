package xzeroair.trinkets.util.eventhandlers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.client.renderer.PlayerRenderAlt;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class RenderHandler {

	private float slowYaw;
	private PlayerRenderAlt altRender;

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		if(event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			if(player.hasCapability(CapPro.sizeCapability, null)) {
				final ICap nbt = player.getCapability(CapPro.sizeCapability, null);
				final int size = nbt.getSize();
				final float scale = (float) size/100;
				if((nbt.getTrans() == true)) {
					GlStateManager.pushMatrix();
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					if (player.isRiding()) {
						if(TrinketHelper.baubleCheck(player, ModItems.small_ring)) {
							if(player.getRidingEntity() instanceof EntityMinecart) {
								event.setCanceled(true);
								if(altRender == null) {
									final String s = ((AbstractClientPlayer)player).getSkinType();
									if(s.contains("slim")) {
										altRender = new PlayerRenderAlt(Minecraft.getMinecraft().getRenderManager(), true);
									} else {
										altRender = new PlayerRenderAlt(Minecraft.getMinecraft().getRenderManager(), false);
									}
								} else {
									slowYaw = player.rotationYaw;
									if(slowYaw < player.rotationYaw) {
										slowYaw -= 360;
									} else {
										slowYaw += 360;
									}
									final float t = 0.0F;
									final float h = 0.0F;
									GlStateManager.translate(-h, 2.1F, t);
									altRender.doRender((AbstractClientPlayer)player, event.getX(), event.getY(), event.getZ(), player.rotationYawHead, event.getPartialRenderTick());

								}
							} else {
								GlStateManager.translate(0F, 1.8F, 0F);
							}
						}
						if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
							GlStateManager.translate(0, 0.125F, 0);
						}
					}
				} else {
					if(nbt.getSize() != nbt.getTarget()) {
						GlStateManager.pushMatrix();
						GlStateManager.scale(scale, scale, scale);
						GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void renderPlayerPost(RenderPlayerEvent.Post event) {
		if (event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			if(player.hasCapability(CapPro.sizeCapability, null)) {
				final ICap nbt = player.getCapability(CapPro.sizeCapability, null);
				if(nbt.getTrans() == true) {
					GlStateManager.popMatrix();
				} else {
					if(nbt.getSize() != nbt.getTarget()) {
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void RenderLivingEvent(RenderLivingEvent.Pre event) {
		if(event.getEntity() instanceof EntityLiving) {
			if((!(event.getEntity() instanceof EntityPlayer)) && event.getEntity().hasCapability(CapPro.sizeCapability, null)) {
				final ICap mobNBT = event.getEntity().getCapability(CapPro.sizeCapability, null);
				if(mobNBT.getTrans() == true) {
					final float scale = (float)mobNBT.getSize()/100;
					GlStateManager.pushMatrix();
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
				} else {
					if(mobNBT.getSize() != 100) {
						final float scale = (float)mobNBT.getSize()/100;
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
				final ICap mobNBT = event.getEntity().getCapability(CapPro.sizeCapability, null);
				if(mobNBT.getTrans() == true) {
					GlStateManager.popMatrix();
				} else {
					if(mobNBT.getSize() != 100) {
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		if(Minecraft.getMinecraft().player != null) {
			final int vd = TrinketsConfig.CLIENT.effects.DR.C00_VD;
			final int hd = TrinketsConfig.CLIENT.effects.DR.C001_HD;
			final int rf = TrinketsConfig.CLIENT.effects.C00_RR;
			final EntityPlayer player = Minecraft.getMinecraft().player;
			if(TrinketHelper.baubleCheck(player, ModItems.dragons_eye)) {
				if(TrinketHelper.hasCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye))) {
					final ItemCap nbt = TrinketHelper.getBaubleCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye));
					if ((!TrinketsConfig.CLIENT.effects.C01_Dragon_Eye == false)) {
						if((nbt.oreType() != -1)) {
							if((player.ticksExisted % rf) == 0) {
								GlStateManager.pushMatrix();
								final String Type = OreTrackingHelper.oreTypesLoaded().get(nbt.oreType()).toString();
								oreDetection(player, player.getEntityBoundingBox().grow(hd, vd, hd), Type);
								if(Type.contains("Chest")) {
									if(TrinketsConfig.SERVER.C04_DE_Chests != false) {
										chestCartDetect(player, player.getEntityBoundingBox().grow(hd, vd, hd), Type);
									}
								}
								GlStateManager.popMatrix();
							}
						}
					}
				}
			}
		}
	}

	public static void chestCartDetect(Entity entity, AxisAlignedBB aabb, String ore) {
		final List<EntityMinecartChest> cartList = entity.getEntityWorld().getEntitiesWithinAABB(EntityMinecartChest.class, aabb);
		if(!cartList.isEmpty()) {
			for(final EntityMinecartChest chestCart : cartList) {
				final double X = (Reference.random.nextDouble() + chestCart.posX) - 0.5;
				final double Y = Reference.random.nextDouble() + chestCart.posY;
				final double Z = (Reference.random.nextDouble() + chestCart.posZ) - 0.5;
				final float r = TrinketHelper.getColor(ore, 0);
				final float g = TrinketHelper.getColor(ore, 1);
				final float b = TrinketHelper.getColor(ore, 2);
				if(entity.isSneaking()) {
					final float volume = (float) entity.getDistance(X, Y, Z);
					final float v = MathHelper.clamp((-volume/10)+1, 0.1F, 1.0F);
					entity.world.playSound((EntityPlayer) entity, chestCart.getPosition(), SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
				}
				final ParticleGreed effect = new ParticleGreed(entity.world, X, Y, Z, X, Y, Z, r, g, b);
				Minecraft.getMinecraft().effectRenderer.addEffect(effect);
			}
		}
	}

	private static void oreDetection(Entity entity, AxisAlignedBB aabb, String ore) {
		final int i = MathHelper.floor(aabb.minX);
		final int j = MathHelper.floor(aabb.maxX + 1.0D);
		final int k = MathHelper.floor(aabb.minY);
		final int l = MathHelper.floor(aabb.maxY + 1.0D);
		final int i1 = MathHelper.floor(aabb.minZ);
		final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
		;
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					final BlockPos pos = new BlockPos(k1, l1, i2);
					final IBlockState state = entity.world.getBlockState(pos);
					final Block block = state.getBlock();
					final float r = TrinketHelper.getColor(ore, 0);
					final float g = TrinketHelper.getColor(ore, 1);
					final float b = TrinketHelper.getColor(ore, 2);
					final double X = Reference.random.nextDouble() + pos.getX();
					final double Y = Reference.random.nextDouble() + pos.getY();
					final double Z = Reference.random.nextDouble() + pos.getZ();
					if(!ore.contains("Chest")) {
						final List list = OreTrackingHelper.getTargetOres(ore);
						for(int ListI = 0;ListI < list.size();ListI++) {
							for(final ItemStack stack : OreDictionary.getOres(list.get(ListI).toString())) {
								if(block == block.getBlockFromItem(stack.getItem())) {
									if(block.getMetaFromState(state) == stack.getItem().getMetadata(stack)) {
										if(entity.isSneaking()) {
											final float volume = (float) entity.getDistance(X, Y, Z);
											final float v = MathHelper.clamp((-volume/10)+1, 0.1F, 1.0F);
											entity.world.playSound((EntityPlayer) entity, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
										}
										final ParticleGreed effect = new ParticleGreed(entity.world, X, Y, Z, X, Y, Z, r, g, b);
										Minecraft.getMinecraft().effectRenderer.addEffect(effect);
									}
								}
							}
						}
					} else {
						if(TrinketsConfig.SERVER.C04_DE_Chests != false) {
							if(block.getLocalizedName().contains(ore)) {
								if(entity.isSneaking()) {
									final float volume = (float) entity.getDistance(X, Y, Z);
									final float v = MathHelper.clamp((-volume/10)+1, 0.1F, 1.0F);
									entity.world.playSound((EntityPlayer) entity, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
								}
								final ParticleGreed effect = new ParticleGreed(entity.world, X, Y, Z, X, Y, Z, r, g, b);
								Minecraft.getMinecraft().effectRenderer.addEffect(effect);
							}
						}
					}
				}
			}
		}
	}
}
