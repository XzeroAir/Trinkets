package xzeroair.trinkets.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.capabilities.sizeCap.SizeDefaultCap;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.util.Reference;

public class CapabilitiesHandler {

	@SubscribeEvent
	public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
		if ((event.getObject() instanceof EntityPlayer)) {
			event.addCapability(new ResourceLocation(Reference.MODID,"container"), new TrinketContainerProvider(new TrinketContainerHandler()));
		}
		if((event.getObject() instanceof EntityPlayer) && !event.getObject().hasCapability(SizeCapPro.sizeCapability, null)) {
			final EntityPlayer player = (EntityPlayer) event.getObject();
			final int size = 100;
			final boolean transformed = false;
			final String food = "none";
			final int target = 100;
			final float width = player.width;
			final float height = player.height;
			final float defaultWidth = player.width;
			final float defaultHeight = player.height;
			final ISizeCap cap = new SizeDefaultCap(size, transformed, food, target, width, height, defaultWidth, defaultHeight);
			event.addCapability(new ResourceLocation(Reference.MODID, "Capability"), new SizeCapPro(cap));
		}
	}

	@SubscribeEvent
	public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
		if((event.getObject() != null) && !event.getObject().isEmpty()) {
			//			final ItemStack stack = event.getObject();
			//			if (stack.isEmpty() || !(stack.getItem() instanceof IAccessoryInterface) || stack.hasCapability(Capabilities.CAPABILITY_ITEM_TRINKET, null)
			//					|| event.getCapabilities().values().stream().anyMatch(c -> c.hasCapability(Capabilities.CAPABILITY_ITEM_TRINKET, null))) {
			//				return;
			//			} else {
			//				event.addCapability(new ResourceLocation(Reference.MODID, "ItemCap"), new ICapabilityProvider() {
			//
			//					@Override
			//					public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			//						return capability == Capabilities.CAPABILITY_ITEM_TRINKET;
			//					}
			//
			//					@Nullable
			//					@Override
			//					public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			//						if ((capability != null) && (capability == Capabilities.CAPABILITY_ITEM_TRINKET)) {
			//							return (T) stack.getItem();
			//						}
			//						return null;
			//						//						return capability == Capabilities.CAPABILITY_ITEM_TRINKET
			//						//								? Capabilities.CAPABILITY_ITEM_TRINKET.cast((IAccessoryInterface) stack.getItem())
			//						//										: null;
			//					}
			//				});
			//			}
		}
	}
}
