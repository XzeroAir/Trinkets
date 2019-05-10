package xzeroair.trinkets.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.capabilities.sizeCap.SizeDefaultCap;
import xzeroair.trinkets.util.Reference;

public class CapabilitiesHandler {

	@SubscribeEvent
	public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
		if((event.getObject() instanceof EntityPlayer) && !event.getObject().hasCapability(SizeCapPro.sizeCapability, null)) {
			final EntityPlayer player = (EntityPlayer) event.getObject();
			final int size = 100;
			final boolean transformed = false;
			final int target = 100;
			final float width = player.width;
			final float height = player.height;
			final float defaultWidth = player.width;
			final float defaultHeight = player.height;
			final ISizeCap cap = new SizeDefaultCap(size, transformed, target, width, height, defaultWidth, defaultHeight);
			event.addCapability(new ResourceLocation(Reference.MODID, "Capability"), new SizeCapPro(cap));
		}
	}
}
