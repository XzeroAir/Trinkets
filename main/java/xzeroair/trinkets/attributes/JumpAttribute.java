package xzeroair.trinkets.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import xzeroair.trinkets.util.Reference;

public class JumpAttribute {

	public JumpAttribute() {
	}

	//	0.33319999363422365
	public static final IAttribute Jump = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".jump",
			0.42F, 0F, 256F
	).setDescription("Jump Height").setShouldWatch(true);

	public static final IAttribute stepHeight = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".stepheight",
			0.6F, 0F, 256F
	).setDescription("Step Height").setShouldWatch(true);

}
