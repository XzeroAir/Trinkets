package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.DeCap;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.util.Reference;

public class CompatabilitiesHandler {

	@SubscribeEvent
	public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
		if((event.getObject() != null) && event.getObject().isNonBoss() && !event.getObject().hasCapability(CapPro.sizeCapability, null)) {
			if(event.getObject() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getObject();
				int size = 100;
				boolean transformed = false;
				int target = 100;
				float width = player.width;
				float height = player.height;
				float defaultWidth = player.width;
				float defaultHeight = player.height;
				ICap cap = new DeCap(size, transformed, target, width, height, defaultWidth, defaultHeight);
				event.addCapability(new ResourceLocation(Reference.MODID, "Capability"), new CapPro(cap));
			} else {
				Entity entity = event.getObject();
				int size = 100;
				boolean transformed = false;
				int target = 100;
				float width = entity.width;
				float height = entity.height;
				float defaultWidth = entity.width;
				float defaultHeight = entity.height;
				ICap cap = new DeCap(size, transformed, target, width, height, defaultWidth, defaultHeight);
				event.addCapability(new ResourceLocation(Reference.MODID, "Capability"), new CapPro(cap));
			}
		}
	}
	@SubscribeEvent
	public void onItemAddCapabilites(AttachCapabilitiesEvent<ItemStack> event) {
		//		if(!event.getObject().hasCapability(ItemProvider.itemCapability, null)) {
		//			if (event.getObject() instanceof ItemStack) {
		//				ItemStack item = event.getObject();
		//				if(item.getItem() == ModItems.dragons_eye) {
		//					int type = -1;
		//					boolean nightVision = false;
		//					ItemCap cap = new DefaultItemCapability(type, nightVision);
		//					event.addCapability(new ResourceLocation(Reference.MODID, "ItemCapability"), new ItemProvider(cap));
		//				}
		//			}
		//		}
	}
}
