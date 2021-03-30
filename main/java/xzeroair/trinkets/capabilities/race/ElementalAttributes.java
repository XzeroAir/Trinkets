package xzeroair.trinkets.capabilities.race;

import net.minecraft.entity.EntityLivingBase;

public class ElementalAttributes {

	EntityLivingBase e;
	MagicStats magic;

	public ElementalAttributes(EntityLivingBase entity, MagicStats magic) {
		e = entity;
		this.magic = magic;
	}

}
