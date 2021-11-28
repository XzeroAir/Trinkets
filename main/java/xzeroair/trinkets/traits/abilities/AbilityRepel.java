package xzeroair.trinkets.traits.abilities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.handlers.TickHandler;

public class AbilityRepel extends AbilityBase implements ITickableAbility, IToggleAbility {

	private static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (enabled) {
			if (serverConfig.exhaustion) {
				final MagicStats magic = Capabilities.getMagicStats(entity);
				if (magic != null) {
					float exhaustRate = serverConfig.exhaust_rate;
					TickHandler counter = this.getCounter("repel.ticks", serverConfig.exhaust_ticks, false);
					if (exhaustRate <= magic.getMana()) {
						if (counter.Tick()) {
							magic.spendMana(exhaustRate);
						}
					} else {
						return;
					}
				}
			}
			final AxisAlignedBB bBox = entity.getEntityBoundingBox();
			final List<Entity> entityList = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(1.1));
			for (final Entity repelledEntity : entityList) {
				if ((repelledEntity != null) && !(repelledEntity instanceof EntityPlayer)) {
					String e = EntityRegistry.getEntry(repelledEntity.getClass()).getRegistryName().toString();
					for (String s : serverConfig.repelledEntities) {
						if (e.equalsIgnoreCase(s)) {
							Vec3d playerVec3 = entity.getLookVec();
							repelledEntity.motionX = (playerVec3.x * 0.3D);
							repelledEntity.motionY = (playerVec3.y * 0.3D);
							repelledEntity.motionZ = (playerVec3.z * 0.3D);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean abilityEnabled() {
		return enabled;
	}

	@Override
	public IToggleAbility toggleAbility(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public IToggleAbility toggleAbility(int value) {
		this.value = value;
		return this;
	}

}
