package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;

public class AbilityNightVision extends Ability implements ITickableAbility, IToggleAbility, IKeyBindInterface {

	public AbilityNightVision() {
		super(Abilities.nightVision);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (!entity.world.isRemote) {
			if (this.abilityEnabled()) {
				entity.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 410, 0, false, false));
			} else {
				if (entity.isPotionActive(MobEffects.NIGHT_VISION)) {
					entity.removePotionEffect(MobEffects.NIGHT_VISION);
				}
			}
		} else {
			if (entity.getEntityWorld().isRemote) {
				if (entity.isPotionActive(MobEffects.NIGHT_VISION)) {
					entity.getActivePotionEffect(MobEffects.NIGHT_VISION).setPotionDurationMax(true);
				}
			}
		}
	}

	@Override
	public void onAbilityRemoved(EntityLivingBase entity) {
		if (entity.isPotionActive(MobEffects.NIGHT_VISION)) {
			entity.removePotionEffect(MobEffects.NIGHT_VISION);
		}
	}

	@Override
	public boolean abilityEnabled() {
		return enabled;
	}

	@Override
	public AbilityNightVision toggleAbility(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
		}
		return this;
	}

	@Override
	public AbilityNightVision toggleAbility(int value) {
		if (value > 0) {
			enabled = true;
		} else {
			enabled = false;
		}
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getKey() {
		return ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getAuxKey() {
		return ModKeyBindings.AUX_KEY.getDisplayName();
	}

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {
		this.toggleAbility(!enabled);
		return true;
	}

	@Override
	public NBTTagCompound saveStorage(NBTTagCompound compound) {
		compound.setBoolean("enabled", enabled);
		return compound;
	}

	@Override
	public void loadStorage(NBTTagCompound compound) {
		if (compound.hasKey("enabled")) {
			enabled = compound.getBoolean("enabled");

		}
	}

}
