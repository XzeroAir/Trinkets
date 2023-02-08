package xzeroair.trinkets.traits.abilities.other;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IInteractionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class AbilityWolfMount extends Ability implements ITickableAbility, IKeyBindInterface, IInteractionAbility {

	protected static final GoblinConfig serverConfig = TrinketsConfig.SERVER.races.goblin;

	public AbilityWolfMount() {
		super(Abilities.mountEnhancement);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		final Counter counter = tickHandler.getCounter("mountAtkCooldown", 40, true, true, false, true, false);
		if ((counter != null)) {
			if ((counter.getTick() > 0)) {
				counter.Tick();
			}
		}
	}

	@Override
	public void interactEntity(EntityLivingBase entityLiving, World world, ItemStack itemStack, EnumHand hand, EnumFacing face, BlockPos pos, Entity target) {
	}

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			final World world = player.getEntityWorld();
			final boolean canRide = serverConfig.rider;
			if (!canRide) {
				return true;
			}
			double reachDistance = 5;
			final IAttributeInstance reachAttribe = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
			if (reachAttribe != null) {
				reachDistance = reachAttribe.getAttributeValue();
			}
			if (player.isRiding() && (player.getRidingEntity() instanceof AlphaWolf)) {
				final Counter counter = tickHandler.getCounter("mountAtkCooldown", 40, true, true, false, true, false);
				final AlphaWolf wolf = (AlphaWolf) player.getRidingEntity();
				if (counter.Tick()) {
					wolf.MountedAttack(player, reachDistance);
					counter.resetTick();
				}
			} else if (!player.isRiding()) {
				RayTraceResult result = RayTraceHelper.rayTrace(world, player, reachDistance * 0.5D, true);
				if ((result != null) && (result.typeOfHit == Type.ENTITY)) {
					if (result.entityHit instanceof EntityWolf) {
						this.MountWolf(player, (EntityWolf) result.entityHit);
					}
				}
			} else {

			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(Entity entity, boolean Aux) {
		return true;
	}

	@Override
	public boolean onKeyRelease(Entity entity, boolean Aux) {
		return true;
	}

	public boolean MountWolf(EntityLivingBase entity, EntityWolf wolf) {
		final World world = entity.getEntityWorld();
		final boolean canRide = serverConfig.rider;
		final boolean isOwner = (wolf.isTamed() && wolf.isOwner(entity));
		if (!canRide || !isOwner || wolf.isChild()) {
			return false;
		}
		if (!world.isRemote) {
			if (!entity.isRiding()) {
				final AlphaWolf newWolf = new AlphaWolf(world);
				final NBTTagCompound tag = new NBTTagCompound();
				wolf.writeToNBT(tag);
				tag.setString("id", EntityList.getKey(wolf.getClass()).toString());
				newWolf.storeOldWolf(tag);
				newWolf.setCustomNameTag(wolf.getCustomNameTag());
				newWolf.setLocationAndAngles(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, 0F);
				newWolf.setTamedBy(entity);
				newWolf.setHealth(wolf.getHealth());
				world.spawnEntity(newWolf);
				entity.startRiding(newWolf);
				wolf.setDead();
				return true;
			}
		}
		return false;
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
}
