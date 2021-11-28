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
import xzeroair.trinkets.capabilities.TileEntityCap.ManaEssenceProperties;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class CapabilitiesHandler {

	@SubscribeEvent
	public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
		final Entity entity = event.getObject();
		if (!entity.isNonBoss()) {
			return;
		}
		final boolean isLiving = event.getObject() instanceof EntityLivingBase;
		if (isLiving) {
			final boolean isPlayer = event.getObject() instanceof EntityPlayer;
			if (isPlayer) {
				event.addCapability(new ResourceLocation(Reference.MODID, "container"), new TrinketContainerProvider(new TrinketContainerHandler()));
				if (!entity.hasCapability(Capabilities.VIP_STATUS, null)) {
					event.addCapability(new ResourceLocation(Reference.MODID, "VIP"), new CapabilityProviderBase<>(Capabilities.VIP_STATUS, new VipStatus(this.getEntityAsPlayer(entity))));//new VipStatusProvider(this.getEntityAsLiving(entity)));
				}
			}
			if (!entity.hasCapability(Capabilities.STATUS_HANDLER, null)) {
				event.addCapability(new ResourceLocation(Reference.MODID, "StatusHandler"), new CapabilityProviderBase<>(Capabilities.STATUS_HANDLER, new StatusHandler(this.getEntityAsLiving(entity))));//new StatusHandlerProvider(this.getEntityAsLiving(entity)));
			}
			if (!entity.hasCapability(Capabilities.ENTITY_RACE, null)) {
				if (TrinketsConfig.SERVER.Potion.players_only) {
					if (isPlayer) {
						event.addCapability(new ResourceLocation(Reference.MODID, "Race"), new CapabilityProviderBase<>(Capabilities.ENTITY_RACE, new EntityProperties(this.getEntityAsPlayer(entity))));//new StatusHandlerProvider(this.getEntityAsLiving(entity)));
					}
				} else {
					event.addCapability(new ResourceLocation(Reference.MODID, "Race"), new CapabilityProviderBase<>(Capabilities.ENTITY_RACE, new EntityProperties(this.getEntityAsLiving(entity))));//new StatusHandlerProvider(this.getEntityAsLiving(entity)));
				}
			}
			if (!entity.hasCapability(Capabilities.ENTITY_MAGIC, null)) {
				event.addCapability(new ResourceLocation(Reference.MODID, "MagicStats"), new CapabilityProviderBase<>(Capabilities.ENTITY_MAGIC, new MagicStats(this.getEntityAsLiving(entity))));
			}
		}
	}

	private EntityPlayer getEntityAsPlayer(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return (EntityPlayer) entity;
		} else {
			return null;
		}
	}

	private EntityLivingBase getEntityAsLiving(Entity entity) {
		if (entity instanceof EntityLivingBase) {
			return (EntityLivingBase) entity;
		} else {
			return null;
		}
	}

	@SubscribeEvent
	public void TileEntityCapability(AttachCapabilitiesEvent<TileEntity> event) {
		if ((event.getObject() instanceof TileEntityMoonRose) && !event.getObject().hasCapability(Capabilities.TILE_ENTITY_MANA_ESSENCE, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "ManaEssence"), new CapabilityProviderBase<>(Capabilities.TILE_ENTITY_MANA_ESSENCE, new ManaEssenceProperties(event.getObject(), null)));//new ManaEssenceProvider(event.getObject()));
		}
	}

	@SubscribeEvent
	public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
		if ((!event.getObject().isEmpty() && (event.getObject().getItem() instanceof IAccessoryInterface)) && !event.getObject().hasCapability(Capabilities.ITEM_TRINKET, null)) {
			event.addCapability(new ResourceLocation(Reference.MODID, "Trinket"), new CapabilityProviderBase<>(Capabilities.ITEM_TRINKET, new TrinketProperties(event.getObject())));
		}
	}
}
