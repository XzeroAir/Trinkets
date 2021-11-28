package xzeroair.trinkets.traits.abilities;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Sea;
import xzeroair.trinkets.util.config.trinkets.ConfigSeaStone;

public class AbilityWaterAffinity extends AbilityBase implements ITickableAbility, IMiningAbility {

	public static final ConfigSeaStone serverConfig = TrinketsConfig.SERVER.Items.SEA_STONE;
	public static final Sea clientConfig = TrinketsConfig.CLIENT.items.SEA_STONE;

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (serverConfig.underwater_breathing) {
			if (!serverConfig.always_full) {
				if (entity.getAir() < 20) {
					entity.setAir(20);
				}
			} else {
				entity.setAir(300);
			}
			if (Loader.isModLoaded("better_diving")) {
				entity.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 150, 0, false, false));
			}
		}
		//TODO REDO THIS
		if ((serverConfig.Swim_Tweaks == true) && !((entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying)) {
			final BlockPos head = entity.getPosition();
			final IBlockState headBlock = entity.world.getBlockState(head);
			final Block block = headBlock.getBlock();
			if ((entity.isInWater() || entity.isInLava()) && (block != Blocks.AIR)) {
				double motion = 0.1;
				final double bouyance = 0.25;
				if (entity.isInLava()) {
					motion = 0.09;
				}
				if (!entity.isSneaking()) {
					entity.motionY = 0f;
					if ((this.movingForward(entity, entity.getHorizontalFacing()) == true)) {
						if (((entity.motionX > motion) || (entity.motionX < -motion)) || ((entity.motionZ > motion) || (entity.motionZ < -motion))) {
							entity.motionY += MathHelper.clamp(entity.getLookVec().y / 1, -bouyance, bouyance);
						}
					}
				} else {
					if ((this.movingForward(entity, entity.getHorizontalFacing()) == false)) {
						if (!(entity.motionY > 0)) {
							if (entity.isInLava()) {
								entity.motionY *= 1.75;
							} else {
								entity.motionY *= 1.25;
							}
						} else {

						}

					}
				}
			}
		}
	}

	@Override
	public ImmutablePair<Boolean, Float> breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		if (TrinketsConfig.SERVER.Items.SEA_STONE.Swim_Tweaks) {
			if (entity.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(entity)) {
				float speed = originalSpeed;
				speed *= 5F;
				if (!entity.onGround) {
					speed *= 5F;
				}
				if (newSpeed < speed) {
					return new ImmutablePair<>(false, speed);
				}
			}
		}
		return null;
	}

	private boolean movingForward(EntityLivingBase entity, EnumFacing facing) {
		if (((facing.getDirectionVec().getX() * entity.motionX) > 0) || ((facing.getDirectionVec().getZ() * entity.motionZ) > 0)) {
			return true;
		}
		return false;
	}

}
