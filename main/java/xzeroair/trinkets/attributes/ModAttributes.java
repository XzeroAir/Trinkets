package xzeroair.trinkets.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import xzeroair.trinkets.util.Reference;

//@formatter:off
public class ModAttributes {

    public static final IAttribute damage = new RangedAttribute(
            (IAttribute) null, Reference.MODID + ".damage",
            0.0, Double.MIN_VALUE, Double.MAX_VALUE
    ).setDescription("Damage").setShouldWatch(true);

    public static final IAttribute damageProjectile = new RangedAttribute(
            (IAttribute) null, Reference.MODID + ".damage.projectile",
            0.0, Double.MIN_VALUE, Double.MAX_VALUE
    ).setDescription("Projectile Damage").setShouldWatch(true);

    public static final IAttribute damageMagic = new RangedAttribute(
            (IAttribute) null, Reference.MODID + ".entityMagic.damage",
            0.0, Double.MIN_VALUE, Double.MAX_VALUE
    ).setDescription("Magic Damage").setShouldWatch(true);

}
