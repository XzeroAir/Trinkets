package xzeroair.trinkets.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import xzeroair.trinkets.util.Reference;

public class MagicAttributes {

	public static final IAttribute MAX_MANA = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".entityMagic.maxMana",
			100.0, 0, Double.MAX_VALUE
	).setDescription("Max Mana").setShouldWatch(true);

	public static final IAttribute regen = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".entityMagic.regen",
			1.0, 0, Double.MAX_VALUE
	).setDescription("Mana Regen").setShouldWatch(true);

	public static final IAttribute regenCooldown = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".entityMagic.regen.cooldown",
			1.0, 0, Double.MAX_VALUE
	).setDescription("Magic Regen Cooldown").setShouldWatch(true);

	public static final IAttribute affinity = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".entityMagic.affinity",
			0.0, 0, Double.MAX_VALUE
	).setDescription("Magic Affinity").setShouldWatch(true);

	// Make Potions give Regen?
	// Magic Affinity Attribute?
	// Add Element to Magic Stats

}
