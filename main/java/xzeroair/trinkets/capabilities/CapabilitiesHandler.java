package xzeroair.trinkets.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.ITrinketInterface;
import xzeroair.trinkets.blocks.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityTeddyBear;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.TileEntityCap.TileEntityProperties;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class CapabilitiesHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
		final Entity entity = event.getObject();
		final boolean isLiving = event.getObject() instanceof EntityLivingBase;
		if (isLiving) {
			if (!entity.isNonBoss()) {
				return;
			}
			final boolean isPlayer = event.getObject() instanceof EntityPlayer;
			if (isPlayer) {
				event.addCapability(new ResourceLocation(Reference.MODID, "container"), new TrinketContainerProvider(new TrinketContainerHandler()));
				if (TrinketsConfig.SERVER.misc.retrieveVIP) {
					if (!entity.hasCapability(Capabilities.VIP_STATUS, null)) {
						event.addCapability(new ResourceLocation(Reference.MODID, "VIP"), new CapabilityProviderBase<>(Capabilities.VIP_STATUS, new VipStatus((EntityPlayer) entity)));
					}
				}
			}
			if (!entity.hasCapability(Capabilities.STATUS_HANDLER, null)) {
				event.addCapability(new ResourceLocation(Reference.MODID, "StatusHandler"), new CapabilityProviderBase<>(Capabilities.STATUS_HANDLER, new StatusHandler((EntityLivingBase) entity)));
			}
			if (!entity.hasCapability(Capabilities.ENTITY_PROPERTIES, null)) {
				if (TrinketsConfig.SERVER.Potion.players_only) {
					if (isPlayer) {
						event.addCapability(new ResourceLocation(Reference.MODID, "Race"), new CapabilityProviderBase<>(Capabilities.ENTITY_PROPERTIES, new EntityProperties((EntityPlayer) entity)));
					}
				} else {
					event.addCapability(new ResourceLocation(Reference.MODID, "Race"), new CapabilityProviderBase<>(Capabilities.ENTITY_PROPERTIES, new EntityProperties((EntityLivingBase) entity)));
				}
			}
			if (!entity.hasCapability(Capabilities.ENTITY_MAGIC, null)) {
				event.addCapability(new ResourceLocation(Reference.MODID, "MagicStats"), new CapabilityProviderBase<>(Capabilities.ENTITY_MAGIC, new MagicStats((EntityLivingBase) entity)));
			}
		}
	}

	@SubscribeEvent
	public void TileEntityCapability(AttachCapabilitiesEvent<TileEntity> event) {
		TileEntity TileEntity = event.getObject();
		if ((event.getObject() instanceof TileEntityMoonRose)) {
			if (!TileEntity.hasCapability(Capabilities.TILE_ENTITY_PROPERTIES, null)) {
				event.addCapability(
						new ResourceLocation(Reference.MODID, "ManaEssence"),
						new CapabilityProviderBase<>(
								Capabilities.TILE_ENTITY_PROPERTIES,
								new TileEntityProperties(TileEntity).setHasEssence(true)
						)
				);
			}
		}
		if ((event.getObject() instanceof TileEntityTeddyBear)) {
			if (!TileEntity.hasCapability(Capabilities.TILE_ENTITY_PROPERTIES, null)) {
				event.addCapability(
						new ResourceLocation(Reference.MODID, "TrinketTE"),
						new CapabilityProviderBase<>(
								Capabilities.TILE_ENTITY_PROPERTIES,
								new TileEntityProperties(TileEntity).setHasEssence(false)
						)
				);
			}
		}
	}

	@SubscribeEvent
	public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
		final ItemStack stack = event.getObject();
		if (!stack.isEmpty()) {
			if ((stack.getItem() instanceof ITrinketInterface) && !stack.hasCapability(Capabilities.ITEM_TRINKET, null)) {
				event.addCapability(
						new ResourceLocation(Reference.MODID, "Trinket"),
						new CapabilityProviderBase<>(
								Capabilities.ITEM_TRINKET,
								new TrinketProperties(stack)
						)
				);
			}
		}
	}
}
