package xzeroair.trinkets.items.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.traits.abilities.AbilityIceImmunity;

public class IceResistance extends BasePotion {

	public IceResistance(String name, int duration, int color, boolean isBadEffect) {
		super(name, color, duration, isBadEffect);
	}

	@Override
	public boolean isInstant() {
		return false;//super.isInstant();
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	/*
	 * Only Ever Called When Instant
	 */
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
		//		System.out.println("You Drank me, Do something");
	}

	/*
	 * Runs when it's not an instant potion, and when it's ready
	 */
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		//		System.out.println("You Drank me");
		try {
			Capabilities.getEntityProperties(entity, prop -> {
				final AbilityHandler handler = prop.getAbilityHandler();
				final AbilityIceImmunity ability = new AbilityIceImmunity();
				handler.registerAbility(this.getName(), new SlotInformation(ItemHandlerType.POTION), ability);
			});
		} catch (Exception e) {
		}
	}

}
