package xzeroair.trinkets.items.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.items.base.BasePotion;

public class IceResistance extends BasePotion {

	public IceResistance(String name, int duration, int color, boolean isBadEffect) {
		super(name, duration, color, isBadEffect);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {

	}
}
