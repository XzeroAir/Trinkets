package xzeroair.trinkets.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import xzeroair.trinkets.util.Reference;

public class FlyingAttribute {

    public FlyingAttribute() {
    }

    public static final IAttribute Fly_Speed = new RangedAttribute((IAttribute) null, Reference.MODID + ".flyspeed", 0.05F, 0F, 256F).setDescription("Fly Speed").setShouldWatch(true);

}
