package xzeroair.trinkets.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import xzeroair.trinkets.util.Reference;

//@formatter:off
public class JumpAttribute {

	public JumpAttribute() {
	}

	//	0.33319999363422365
	public static final IAttribute Jump = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".jump",
			(double) 1.0F, (double)0.0F, (double) Integer.MAX_VALUE
			).setDescription("Jump Height").setShouldWatch(true);

	public static final IAttribute stepHeight = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".stepheight",
			(double) 0.6F, (double) 0.0F, (double) Integer.MAX_VALUE
	).setDescription("Step Height").setShouldWatch(true);

}
