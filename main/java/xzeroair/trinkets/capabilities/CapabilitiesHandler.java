package xzeroair.trinkets.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.Trinket.TrinketProvider;
import xzeroair.trinkets.capabilities.manaCap.ManaProvider;
import xzeroair.trinkets.capabilities.race.RaceProvider;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class CapabilitiesHandler {

	@SubscribeEvent
	public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
		if ((event.getObject() instanceof EntityPlayer)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "container"), new TrinketContainerProvider(new TrinketContainerHandler()));
		}
		if ((event.getObject() instanceof EntityPlayer) && !event.getObject().hasCapability(Capabilities.PLAYER_MANA, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "Mana"), new ManaProvider());
		}

		if ((event.getObject() instanceof EntityLivingBase) && !event.getObject().hasCapability(Capabilities.ENTITY_RACE, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "Race"), new RaceProvider());
		}
	}

	@SubscribeEvent
	public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
		if ((!event.getObject().isEmpty() && (event.getObject().getItem() instanceof IAccessoryInterface)) && !event.getObject().hasCapability(Capabilities.ITEM_TRINKET, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "Trinket"), new TrinketProvider());
		}
	}
}
