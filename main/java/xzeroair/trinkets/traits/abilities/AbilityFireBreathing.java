package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.keybinds.IKeyBindInterface;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.MovingThrownProjectile;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class AbilityFireBreathing extends AbilityBase implements IKeyBindInterface {

	protected double breathStage = 0;

	protected boolean DragonBreath(Entity entity) {
		if (breathStage > 3) {
			breathStage = 0;
		}
		if (breathStage == 0) {
			final EntityProperties prop = Capabilities.getEntityRace(entity);
			if (prop == null) {
				return false;
			}
			final MagicStats magic = Capabilities.getMagicStats(entity);
			if (magic != null) {
				if (!magic.spendMana(TrinketsConfig.SERVER.races.dragon.breath_cost)) {
					return false;
				}
			}
			final int bcolor = prop.getTraitColorHandler().getDecimal();
			final World world = entity.getEntityWorld();
			final float headPosX = (float) (entity.posX + (1.8F * 1 * 0.3F * Math.cos(((entity.rotationYaw + 90) * Math.PI) / 180)));
			final float headPosZ = (float) (entity.posZ + (1.8F * 1 * 0.3F * Math.sin(((entity.rotationYaw + 90) * Math.PI) / 180)));
			final float headPosY = (float) ((entity.posY + (entity.getEyeHeight() * 0.8)));// - 0.10000000149011612D);//(float) (entity.posY + (1.8F * 1 * 0.3F));
			final double d2 = entity.getLookVec().x;
			final double d3 = entity.getLookVec().y;
			final double d4 = entity.getLookVec().z;
			//			final double d2 = hitLoc.x - entity.posX;//entity.getLookVec().x;
			//			final double d3 = hitLoc.y - entity.posY;//entity.getLookVec().y;
			//			final double d4 = hitLoc.z - entity.posZ;//entity.getLookVec().z;
			//			float inaccuracy = 0.25F;
			//			d2 = d2 + (Reference.random.nextGaussian() * 0.007499999832361937D * inaccuracy);
			//			d3 = d3 + (Reference.random.nextGaussian() * 0.007499999832361937D * inaccuracy);
			//			d4 = d4 + (Reference.random.nextGaussian() * 0.007499999832361937D * inaccuracy);
			world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((Reference.random.nextFloat() * 0.4F) + 0.8F));
			if (!world.isRemote) {
				//			if (Trinkets.proxy.getSide() == Side.SERVER) {
				//TODO Have a max life, tick it down, then kill the projectile, use the life to show decide on the look
				final MovingThrownProjectile breath = new MovingThrownProjectile(entity.getEntityWorld(), (EntityLivingBase) entity, d2, d3, d4, bcolor);
				breath.setPosition(headPosX, headPosY, headPosZ);
				breath.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 1.5F, 1.0F);
				world.spawnEntity(breath.setColor(bcolor));
			}
		}
		breathStage += 1;
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getKey() {
		return ModKeyBindings.RACE_ABILITY.getDisplayName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getAuxKey() {
		return ModKeyBindings.AUX_KEY.getDisplayName();
	}

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		if (magic != null) {
			return magic.getMana() >= TrinketsConfig.SERVER.races.dragon.breath_cost;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(Entity entity, boolean Aux) {
		if (this.DragonBreath(entity)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onKeyRelease(Entity entity, boolean Aux) {
		if (breathStage != 0) {
			breathStage = 0;
		}
		return true;
	}

}
