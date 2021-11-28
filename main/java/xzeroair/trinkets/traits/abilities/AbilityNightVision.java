package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.keybinds.IKeyBindInterface;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;

public class AbilityNightVision extends AbilityBase implements ITickableAbility, IToggleAbility, IKeyBindInterface {

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
	public void removeAbility(EntityLivingBase entity) {
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
		this.enabled = enabled;
		return this;
	}

	@Override
	public AbilityNightVision toggleAbility(int value) {
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
		enabled = !enabled;
		return true;
	}

	@Override
	public void saveStorage(NBTTagCompound nbt) {
		nbt.setBoolean("xat.nightvision", enabled);
	}

	@Override
	public void loadStorage(NBTTagCompound nbt) {
		if (nbt.hasKey("xat.nightvision")) {
			enabled = nbt.getBoolean("xat.nightvision");
		}
	}

}
