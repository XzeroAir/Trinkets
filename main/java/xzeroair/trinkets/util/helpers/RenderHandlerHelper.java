package xzeroair.trinkets.util.helpers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.capabilities.ItemCap.IItemCap;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class RenderHandlerHelper {

	public static void renderPlayerPre(RenderPlayerEvent.Pre event) {
		if(event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			if(player.hasCapability(SizeCapPro.sizeCapability, null)) {
				final ISizeCap nbt = player.getCapability(SizeCapPro.sizeCapability, null);
				final int size = nbt.getSize();
				final float scale = (float) size/100;
				if((nbt.getTrans() == true)) {
					GlStateManager.pushMatrix();

					if((Loader.isModLoaded("artemislib"))) {
						if(nbt.getSize() != nbt.getTarget()) {
							GlStateManager.scale(scale, scale, scale);
						}
						if(player.isSneaking()) {
							GlStateManager.translate(0F, 0.125F, 0F);
						}
					} else {
						GlStateManager.scale(scale, scale, scale);
					}

					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					if (player.isRiding()) {
						if(TrinketHelper.baubleCheck(player, ModItems.fairy_ring)) {
							GlStateManager.translate(0F, 1.8F, 0F);
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

	public static void renderPlayerPost(RenderPlayerEvent.Post event) {
		if (event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			if(player.hasCapability(SizeCapPro.sizeCapability, null)) {
				final ISizeCap nbt = player.getCapability(SizeCapPro.sizeCapability, null);
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

	public static void renderSpecialPre(RenderLivingEvent.Specials.Pre event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			if(player.hasCapability(SizeCapPro.sizeCapability, null)) {
				final ISizeCap nbt = player.getCapability(SizeCapPro.sizeCapability, null);
				if(TrinketHelper.baubleCheck(player, ModItems.fairy_ring) || TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
					GlStateManager.pushMatrix();
					final float scale = (float)nbt.getTarget()/100;
					GlStateManager.translate(0, 0.3/scale, 0);
				}
			}
		}
	}

	public static void renderSpecialPost(RenderLivingEvent.Specials.Post event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			if(player.hasCapability(SizeCapPro.sizeCapability, null)) {
				if(TrinketHelper.baubleCheck(player, ModItems.fairy_ring) || TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
					GlStateManager.popMatrix();
				}
			}
		}
	}


	public static void renderWorld() {
		if(Minecraft.getMinecraft().player != null) {
			final int vd = TrinketsConfig.CLIENT.effects.DR.C00_VD;
			final int hd = TrinketsConfig.CLIENT.effects.DR.C001_HD;
			final int rf = TrinketsConfig.CLIENT.effects.C00_RR;
			final EntityPlayer player = Minecraft.getMinecraft().player;
			if(TrinketHelper.baubleCheck(player, ModItems.dragons_eye)) {
				if(TrinketHelper.hasCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye))) {
					final IItemCap nbt = TrinketHelper.getBaubleCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye));
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


	private static void chestCartDetect(Entity entity, AxisAlignedBB aabb, String ore) {
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
								if(block == Block.getBlockFromItem(stack.getItem())) {
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
