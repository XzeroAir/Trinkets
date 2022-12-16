<<<<<<< Updated upstream
package xzeroair.trinkets.events;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.util.TrinketsConfig;

public class MovementHandler extends EventBaseHandler {

	// TODO Crashed because Unresolved Compilation Problem from prop.isNormalHeight(). Possible Random Crash?
	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		//		final AxisAlignedBB bb = event.getEntityBoundingBox();
		final EntityPlayer player = event.getEntityPlayer();
		//		final double d0 = entity.width / 2.0D;
		//		final AxisAlignedBB nbb = new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0);
		//		event.setEntityBoundingBox(nbb);
		//				final World world = event.getEntityPlayer().getEntityWorld();
		Capabilities.getEntityProperties(player, prop -> {
			if (!prop.isNormalSize()) {
				event.setCanceled(true);
			}
		});
		//				if ((cap != null) && (!cap.isNormalSize())) {
		//					event.setCanceled(true);
		//					final boolean enabled = true;
		//					final boolean noClip = false;
		//					if (enabled) {
		//						final double heightMod = cap.getRaceHeight() * 0.0D;//0.0D;
		//						final double widthMod = 0.35D;
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX - (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ + (cap.getRaceWidth() * widthMod)
		//						);
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX - (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ - (cap.getRaceWidth() * widthMod)
		//						);
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX + (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ - (cap.getRaceWidth() * widthMod)
		//						);
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX + (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ + (cap.getRaceWidth() * widthMod)
		//						);
		//					}
		//				}
		//			}
	}

	private boolean isOpenBlockSpace(World world, BlockPos pos) {
		final IBlockState iblockstate = world.getBlockState(pos);
		return !iblockstate.getBlock().isNormalCube(iblockstate, world, pos);
	}

	private boolean isHeadspaceFree(World world, BlockPos pos, int height) {
		for (int y = 0; y < height; y++) {
			if (!this.isOpenBlockSpace(world, pos.add(0, y, 0))) {
				return false;
			}
		}
		return true;
	}

	protected boolean pushOutOfBlocks(boolean noClip, World world, Entity entity, float height, double x, double y, double z) {
		if (noClip) {
			return false;
		} else {
			final BlockPos blockpos = new BlockPos(x, y, z);
			final double d0 = x - blockpos.getX();
			final double d1 = z - blockpos.getZ();

			final int entHeight = Math.max((int) Math.ceil(height), 1);
			//			System.out.println(entHeight + " | " + Math.ceil(height));

			final boolean inTranslucentBlock = !this.isHeadspaceFree(world, blockpos, entHeight);

			if (inTranslucentBlock) {
				int i = -1;
				double d2 = 9999.0D;

				if (this.isHeadspaceFree(world, blockpos.west(), entHeight) && (d0 < d2)) {
					d2 = d0;
					i = 0;
				}

				if (this.isHeadspaceFree(world, blockpos.east(), entHeight) && ((1.0D - d0) < d2)) {
					d2 = 1.0D - d0;
					i = 1;
				}

				if (this.isHeadspaceFree(world, blockpos.north(), entHeight) && (d1 < d2)) {
					d2 = d1;
					i = 4;
				}

				if (this.isHeadspaceFree(world, blockpos.south(), entHeight) && ((1.0D - d1) < d2)) {
					d2 = 1.0D - d1;
					i = 5;
				}

				final float f = 0.1F;

				if (i == 0) {
					entity.motionX = -0.10000000149011612D;
				}

				if (i == 1) {
					entity.motionX = 0.10000000149011612D;
				}

				if (i == 4) {
					entity.motionZ = -0.10000000149011612D;
				}

				if (i == 5) {
					entity.motionZ = 0.10000000149011612D;
				}
			}

			return false;
		}
	}

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		// TODO Add Config to disable attribute
		final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
		if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
			entity.motionY += (attribute.getAttributeValue() - 0.42F);
			if (entity.isSprinting()) {
				entity.motionX *= attribute.getAttributeValue() / 0.42F;
				entity.motionZ *= attribute.getAttributeValue() / 0.42F;
			}
		}
		//				entity.motionY = 0.368129F;
		Capabilities.getEntityProperties(entity, prop -> {
			if (TrinketsConfig.SERVER.misc.movement) {
				if (prop.getRaceHandler().isTransforming()) {
					entity.motionY = 0;
					return;
				}
			}
			prop.getRaceHandler().jump();
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IJumpAbility)) {
						((IJumpAbility) ability).jump(entity);
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final float baseDistance = event.getDistance();
		final float baseMultiplier = event.getDamageMultiplier();

		// ATTRIBUTE
		final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
		if ((attribute != null)) {
			final double value = attribute.getAttributeValue();
			final float multi = (float) (value / 0.42F);
			final float distance = (3F * (multi - 1));//(3F * 2F);
			event.setDistance(baseDistance - distance);
		}
		// END ATTRIBUTE

		boolean cancel = Capabilities.getEntityProperties(entity, false, (prop, bool) -> {
			float fallDistance = event.getDistance();
			float damageMultiplier = event.getDamageMultiplier();

			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IJumpAbility)) {
						final IJumpAbility fall = (IJumpAbility) ability;
						final float abilityDistance = fall.fallDistance(entity, fallDistance);
						if (fallDistance != abilityDistance) {
							fallDistance = abilityDistance;
						}
						final float abilityModifier = fall.fallDamageMultiplier(entity, damageMultiplier);
						if (damageMultiplier != abilityModifier) {
							damageMultiplier = abilityModifier;
						}
						final boolean abilityCancel = fall.fall(entity, fallDistance, damageMultiplier, bool);
						if (bool != abilityCancel) {
							bool = abilityCancel;
						}
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			if (fallDistance != baseDistance) {
				event.setDistance(MathHelper.clamp(fallDistance, 0, fallDistance));
			}
			if (damageMultiplier != baseMultiplier) {
				event.setDamageMultiplier(MathHelper.clamp(damageMultiplier, 0, damageMultiplier));
			}

			prop.getRaceHandler().fall(event);

			return event.isCanceled() || (event.getDistance() <= 0) || (event.getDamageMultiplier() <= 0) || bool;
		});
		if (cancel) {
			this.cancelEvent(event);
		}
	}
}
=======
package xzeroair.trinkets.events;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.util.TrinketsConfig;

public class MovementHandler extends EventBaseHandler {

	// TODO Crashed because Unresolved Compilation Problem from prop.isNormalHeight(). Possible Random Crash?
	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		//		final AxisAlignedBB bb = event.getEntityBoundingBox();
		final EntityPlayer player = event.getEntityPlayer();
		//		final double d0 = entity.width / 2.0D;
		//		final AxisAlignedBB nbb = new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0);
		//		event.setEntityBoundingBox(nbb);
		//				final World world = event.getEntityPlayer().getEntityWorld();
		Capabilities.getEntityProperties(player, prop -> {
			if (!prop.isNormalSize()) {
				event.setCanceled(true);
			}
		});
		//				if ((cap != null) && (!cap.isNormalSize())) {
		//					event.setCanceled(true);
		//					final boolean enabled = true;
		//					final boolean noClip = false;
		//					if (enabled) {
		//						final double heightMod = cap.getRaceHeight() * 0.0D;//0.0D;
		//						final double widthMod = 0.35D;
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX - (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ + (cap.getRaceWidth() * widthMod)
		//						);
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX - (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ - (cap.getRaceWidth() * widthMod)
		//						);
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX + (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ - (cap.getRaceWidth() * widthMod)
		//						);
		//						this.pushOutOfBlocks(
		//								noClip, world, player, cap.getRaceHeight(),
		//								player.posX + (cap.getRaceWidth() * widthMod), player.posY + heightMod, player.posZ + (cap.getRaceWidth() * widthMod)
		//						);
		//					}
		//				}
		//			}
	}

	private boolean isOpenBlockSpace(World world, BlockPos pos) {
		final IBlockState iblockstate = world.getBlockState(pos);
		return !iblockstate.getBlock().isNormalCube(iblockstate, world, pos);
	}

	private boolean isHeadspaceFree(World world, BlockPos pos, int height) {
		for (int y = 0; y < height; y++) {
			if (!this.isOpenBlockSpace(world, pos.add(0, y, 0))) {
				return false;
			}
		}
		return true;
	}

	protected boolean pushOutOfBlocks(boolean noClip, World world, Entity entity, float height, double x, double y, double z) {
		if (noClip) {
			return false;
		} else {
			final BlockPos blockpos = new BlockPos(x, y, z);
			final double d0 = x - blockpos.getX();
			final double d1 = z - blockpos.getZ();

			final int entHeight = Math.max((int) Math.ceil(height), 1);
			//			System.out.println(entHeight + " | " + Math.ceil(height));

			final boolean inTranslucentBlock = !this.isHeadspaceFree(world, blockpos, entHeight);

			if (inTranslucentBlock) {
				int i = -1;
				double d2 = 9999.0D;

				if (this.isHeadspaceFree(world, blockpos.west(), entHeight) && (d0 < d2)) {
					d2 = d0;
					i = 0;
				}

				if (this.isHeadspaceFree(world, blockpos.east(), entHeight) && ((1.0D - d0) < d2)) {
					d2 = 1.0D - d0;
					i = 1;
				}

				if (this.isHeadspaceFree(world, blockpos.north(), entHeight) && (d1 < d2)) {
					d2 = d1;
					i = 4;
				}

				if (this.isHeadspaceFree(world, blockpos.south(), entHeight) && ((1.0D - d1) < d2)) {
					d2 = 1.0D - d1;
					i = 5;
				}

				final float f = 0.1F;

				if (i == 0) {
					entity.motionX = -0.10000000149011612D;
				}

				if (i == 1) {
					entity.motionX = 0.10000000149011612D;
				}

				if (i == 4) {
					entity.motionZ = -0.10000000149011612D;
				}

				if (i == 5) {
					entity.motionZ = 0.10000000149011612D;
				}
			}

			return false;
		}
	}

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		// TODO Add Config to disable attribute
		final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
		if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
			entity.motionY += (attribute.getAttributeValue() - 0.42F);
			if (entity.isSprinting()) {
				entity.motionX *= attribute.getAttributeValue() / 0.42F;
				entity.motionZ *= attribute.getAttributeValue() / 0.42F;
			}
		}
		//				entity.motionY = 0.368129F;
		Capabilities.getEntityProperties(entity, prop -> {
			if (TrinketsConfig.SERVER.misc.movement) {
				if (prop.getRaceHandler().isTransforming()) {
					entity.motionY = 0;
					return;
				}
			}
			prop.getRaceHandler().jump();
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IJumpAbility)) {
						((IJumpAbility) ability).jump(entity);
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final float baseDistance = event.getDistance();
		final float baseMultiplier = event.getDamageMultiplier();

		// ATTRIBUTE
		final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
		if ((attribute != null)) {
			final double value = attribute.getAttributeValue();
			final float multi = (float) (value / 0.42F);
			final float distance = (3F * (multi - 1));//(3F * 2F);
			event.setDistance(baseDistance - distance);
		}
		// END ATTRIBUTE

		boolean cancel = Capabilities.getEntityProperties(entity, false, (prop, bool) -> {
			float fallDistance = event.getDistance();
			float damageMultiplier = event.getDamageMultiplier();

			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IJumpAbility)) {
						final IJumpAbility fall = (IJumpAbility) ability;
						final float abilityDistance = fall.fallDistance(entity, fallDistance);
						if (fallDistance != abilityDistance) {
							fallDistance = abilityDistance;
						}
						final float abilityModifier = fall.fallDamageMultiplier(entity, damageMultiplier);
						if (damageMultiplier != abilityModifier) {
							damageMultiplier = abilityModifier;
						}
						final boolean abilityCancel = fall.fall(entity, fallDistance, damageMultiplier, bool);
						if (bool != abilityCancel) {
							bool = abilityCancel;
						}
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			if (fallDistance != baseDistance) {
				event.setDistance(MathHelper.clamp(fallDistance, 0, fallDistance));
			}
			if (damageMultiplier != baseMultiplier) {
				event.setDamageMultiplier(MathHelper.clamp(damageMultiplier, 0, damageMultiplier));
			}

			prop.getRaceHandler().fall(event);

			return event.isCanceled() || (event.getDistance() <= 0) || (event.getDamageMultiplier() <= 0) || bool;
		});
		if (cancel) {
			this.cancelEvent(event);
		}
	}
}
>>>>>>> Stashed changes
