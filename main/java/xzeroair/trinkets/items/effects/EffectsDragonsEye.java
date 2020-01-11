package xzeroair.trinkets.items.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class EffectsDragonsEye {

	private static List<BlockPos> targets = new ArrayList<>();

	public static List<BlockPos> getTargets() {
		return targets;
	}

	public static void clearTargets() {
		if(!targets.isEmpty()) {
			targets.clear();
		}
	}

	public static void playerTicks(ItemStack stack, EntityLivingBase entity) {
		if((!TrinketsConfig.SERVER.DRAGON_EYE.oreFinder == false)) {
			if(entity instanceof EntityPlayer) {
				final int vd = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.DR.C00_VD;
				final int hd = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.DR.C001_HD;
				final int rf = TrinketsConfig.CLIENT.DRAGON_EYE.C00_RR;
				final EntityPlayer player = (EntityPlayer) entity;
				final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
				if((iCap != null) && (iCap.oreTarget() != -1)) {
					if((player.ticksExisted % rf) == 0) {
						final int length = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks.length;
						if(!(iCap.oreTarget() > length)) {
							if(iCap.oreTarget() > length) {
								return;
							}
							String Type = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks[iCap.oreTarget()];
							if(Type.contains(":")) {
								BlockDetection(player, player.getEntityBoundingBox().grow(hd, vd, hd), Type);
								if(Type.contains("minecraft:chest")) {
									chestCartDetect(player, player.getEntityBoundingBox().grow(hd, vd, hd), Type);
								}
							} else {
								BlockDetectionByOreDictionary(player, player.getEntityBoundingBox().grow(hd, vd, hd), Type);
								if(Type.equalsIgnoreCase("chest")) {
									Type = "minecraft:chest";
									chestCartDetect(player, player.getEntityBoundingBox().grow(hd, vd, hd), Type);
								}
							}
						}
					}
				}
			}
		}
	}

	private static void chestCartDetect(EntityPlayer entity, AxisAlignedBB aabb, String ore) {
		final List<EntityMinecartChest> cartList = entity.getEntityWorld().getEntitiesWithinAABB(EntityMinecartChest.class, aabb);
		if(!cartList.isEmpty()) {
			for(final EntityMinecartChest chestCart : cartList) {
				SpawnParticle(chestCart.getPosition(), entity, ore);
			}
		}
	}
	private static void BlockDetectionByOreDictionary(EntityPlayer player, AxisAlignedBB aabb, String ore) {
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
					final IBlockState state = player.world.getBlockState(pos);
					final Block block = state.getBlock();
					if(!block.getRegistryName().toString().contentEquals("minecraft:air")) {
						final ItemStack blockStack = new ItemStack(block, 1, block.getMetaFromState(state));
						if((blockStack != null) && !blockStack.isEmpty()) {
							for(final String oreDictionary : OreTrackingHelper.getOreNames(blockStack)) {
								if(oreDictionary.contentEquals(ore)) {
									SpawnParticleByOreDictionary(pos, player, ore);
								}
							}
						}
					}
				}
			}
		}
	}

	private static void BlockDetection(EntityPlayer player, AxisAlignedBB aabb, String ore) {
		String oreName = ore.toLowerCase();
		String meta = "";
		final String getName = OreTrackingHelper.translateOreName(ore);
		if(oreName.contains("[")) {
			final int metaStart = ore.indexOf("[");
			final int metaEnd = ore.lastIndexOf("]");
			oreName = ore.substring(0, metaStart);
			meta = ore.substring(metaStart+1, metaEnd);
		}
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
					final IBlockState state = player.world.getBlockState(pos);
					final Block block = state.getBlock();
					if(!block.getRegistryName().toString().contentEquals("minecraft:air")) {
						if(meta.isEmpty()) {
							if(block.getRegistryName().toString().contentEquals(oreName)) {
								SpawnParticle(pos, player, ore);
							}
						} else {
							if(block.getRegistryName().toString().contentEquals(oreName) && !meta.isEmpty()) {
								if(block.getMetaFromState(state) == Integer.parseInt(meta)) {
									SpawnParticle(pos, player, ore);
								}
							}
						}
					}
				}
			}
		}
	}

	public static void SpawnParticleByOreDictionary(BlockPos pos, EntityPlayer player, String ore) {
		final String getName = ore.replace("ore", "");
		float r = 1;//TrinketHelper.getColor(Type, 0);
		float g = 1;//TrinketHelper.getColor(Type, 1);
		float b = 1;//TrinketHelper.getColor(Type, 2);
		for(final Object parseTypeforColor:OreTrackingHelper.oreTypesLoaded()) {
			if(getName.contains(parseTypeforColor.toString())) {
				final String color = parseTypeforColor.toString();
				r = TrinketHelper.getColor(color, 0);
				g = TrinketHelper.getColor(color, 1);
				b = TrinketHelper.getColor(color, 2);
			}
		}
		GlStateManager.pushMatrix();
		double X = pos.getX();
		double Y = pos.getY();
		double Z = pos.getZ();
		if(TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl) {
			final boolean sneaking = TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Sneak.contentEquals("SNEAK") && player.isSneaking();
			final boolean standing = TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Sneak.contentEquals("STAND") && !player.isSneaking();
			final boolean both = TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Sneak.contentEquals("BOTH");
			final float volume = (float) player.getDistance(pos.getX(), pos.getY(), pos.getZ());
			if(volume < 10) {
				if(((sneaking && !standing) == true) || ((standing && !sneaking) == true) || (both == true)) {
					final float configVolume = (float) TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Volume/1000;
					final float v = MathHelper.clamp((-volume/10)+1F, 0.0F, configVolume);
					if(pos != null) {
						player.world.playSound(player, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
					}
				}
			}
		}
		X =  Reference.random.nextDouble() + X;
		Y =  Reference.random.nextDouble() + Y;
		Z =  Reference.random.nextDouble() + Z;
		final ParticleGreed effect = new ParticleGreed(player.world, X, Y, Z, X, Y, Z, r, g, b);
		Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		GlStateManager.popMatrix();
	}

	public static void SpawnParticle(BlockPos pos, EntityPlayer player, String ore) {
		final String getName = OreTrackingHelper.translateOreName(ore);
		float r = 1;//TrinketHelper.getColor(Type, 0);
		float g = 1;//TrinketHelper.getColor(Type, 1);
		float b = 1;//TrinketHelper.getColor(Type, 2);
		for(final Object parseTypeforColor:OreTrackingHelper.oreTypesLoaded()) {
			if(getName.contains(parseTypeforColor.toString())) {
				final String color = parseTypeforColor.toString();
				r = TrinketHelper.getColor(color, 0);
				g = TrinketHelper.getColor(color, 1);
				b = TrinketHelper.getColor(color, 2);
			}
		}
		GlStateManager.pushMatrix();
		double X = pos.getX();
		double Y = pos.getY();
		double Z = pos.getZ();
		if(TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl) {
			final boolean sneaking = TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Sneak.contentEquals("SNEAK") && player.isSneaking();
			final boolean standing = TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Sneak.contentEquals("STAND") && !player.isSneaking();
			final boolean both = TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Sneak.contentEquals("BOTH");
			final float volume = (float) player.getDistance(pos.getX(), pos.getY(), pos.getZ());
			if(volume < 10) {
				if(((sneaking && !standing) == true) || ((standing && !sneaking) == true) || (both == true)) {
					final float configVolume = (float) TrinketsConfig.CLIENT.DRAGON_EYE.Dragon_Growl_Volume/1000;
					final float v = MathHelper.clamp((-volume/10)+1F, 0.0F, configVolume);
					if(pos != null) {
						player.world.playSound(player, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
					}
				}
			}
		}
		X =  Reference.random.nextDouble() + X;
		Y =  Reference.random.nextDouble() + Y;
		Z =  Reference.random.nextDouble() + Z;
		final ParticleGreed effect = new ParticleGreed(player.world, X, Y, Z, X, Y, Z, r, g, b);
		Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		GlStateManager.popMatrix();
	}
}
