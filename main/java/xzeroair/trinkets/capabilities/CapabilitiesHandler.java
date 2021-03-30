package xzeroair.trinkets.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.TileEntityCap.ManaEssenceProvider;
import xzeroair.trinkets.capabilities.Trinket.TrinketProvider;
import xzeroair.trinkets.capabilities.Vip.VipStatusProvider;
import xzeroair.trinkets.capabilities.race.EntityCapabilityProvider;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class CapabilitiesHandler {

	@SubscribeEvent
	public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
		if ((event.getObject() instanceof EntityPlayer) && !event.getObject().hasCapability(Capabilities.VIP_STATUS, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "VIP"), new VipStatusProvider((EntityLivingBase) event.getObject()));
		}
		if ((event.getObject() instanceof EntityPlayer)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "container"), new TrinketContainerProvider(new TrinketContainerHandler()));
		}
		if ((event.getObject() instanceof EntityLivingBase) && !event.getObject().hasCapability(Capabilities.ENTITY_RACE, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "Race"), new EntityCapabilityProvider((EntityLivingBase) event.getObject()));
		}
	}

	@SubscribeEvent
	public void TileEntityCapability(AttachCapabilitiesEvent<TileEntity> event) {
		if ((event.getObject() instanceof TileEntityMoonRose) && !event.getObject().hasCapability(Capabilities.TILE_ENTITY_MANA_ESSENCE, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "ManaEssence"), new ManaEssenceProvider(event.getObject()));
		}
	}

	@SubscribeEvent
	public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
		if ((!event.getObject().isEmpty() && (event.getObject().getItem() instanceof IAccessoryInterface)) && !event.getObject().hasCapability(Capabilities.ITEM_TRINKET, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "Trinket"), new TrinketProvider(event.getObject()));
		}
	}
}
