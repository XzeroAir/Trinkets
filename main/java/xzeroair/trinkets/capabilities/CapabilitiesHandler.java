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
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.blocks.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityTeddyBear;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.TileEntityCap.TileEntityProperties;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.traits.elements.IElementProvider;
import xzeroair.trinkets.util.Reference;

public class CapabilitiesHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onAddCapabilites(AttachCapabilitiesEvent<Entity> event) {
        final Entity entity = event.getObject();
        final boolean isLiving = event.getObject() instanceof EntityLivingBase;
        if (TrinketHelper.isEntityBoss(entity) || entity.getEntityWorld() == null) {
            return;
        }
        if (isLiving) {
            final boolean isPlayer = event.getObject() instanceof EntityPlayer;
            if (isPlayer) {
                event.addCapability(new ResourceLocation(Reference.MODID, "container"), new TrinketContainerProvider(new TrinketContainerHandler()));
                if (!entity.hasCapability(Capabilities.VIP_STATUS, null)) {
                    event.addCapability(new ResourceLocation(Reference.MODID, "vip"), new CapabilityProviderBase<>(Capabilities.VIP_STATUS, new VipStatus((EntityPlayer) entity)));
                }
                if (!entity.hasCapability(Capabilities.ENTITY_MAGIC, null)) {
                    event.addCapability(new ResourceLocation(Reference.MODID, "magic"), new CapabilityProviderBase<>(Capabilities.ENTITY_MAGIC, new MagicStats((EntityLivingBase) entity)));
                }
                if (!entity.hasCapability(Capabilities.ENTITY_PROPERTIES, null)) {
                    event.addCapability(new ResourceLocation(Reference.MODID, "race"), new CapabilityProviderBase<>(Capabilities.ENTITY_PROPERTIES, new EntityProperties((EntityPlayer) entity)));
                }
//                if (!entity.hasCapability(Capabilities.ENTITY_ELEMENTAL_ATTRIBUTES, null)) {
//                    event.addCapability(new ResourceLocation(Reference.MODID, "Elements"), new CapabilityProviderBase<>(Capabilities.ENTITY_ELEMENTAL_ATTRIBUTES, new EntityElementalAttributes((EntityPlayer) entity)));
//                }
            }
        }
    }

    @SubscribeEvent
    public void TileEntityCapability(AttachCapabilitiesEvent<TileEntity> event) {
        TileEntity TileEntity = event.getObject();
        if ((event.getObject() instanceof TileEntityMoonRose)) {
            if (!TileEntity.hasCapability(Capabilities.TILE_ENTITY_PROPERTIES, null)) {
                event.addCapability(new ResourceLocation(Reference.MODID, "TrinketTE"), new CapabilityProviderBase<>(Capabilities.TILE_ENTITY_PROPERTIES, new TileEntityProperties(TileEntity).setHasEssence(true)));
            }
        }
        if ((event.getObject() instanceof TileEntityTeddyBear)) {
            if (!TileEntity.hasCapability(Capabilities.TILE_ENTITY_PROPERTIES, null)) {
                event.addCapability(new ResourceLocation(Reference.MODID, "TrinketTE"), new CapabilityProviderBase<>(Capabilities.TILE_ENTITY_PROPERTIES, new TileEntityProperties(TileEntity).setHasEssence(false)));
            }
        }
    }

    @SubscribeEvent
    public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        final ItemStack stack = event.getObject();
        if (!stack.isEmpty()) {
//            if ((stack.getItem() instanceof IElementProvider) && !stack.hasCapability(Capabilities.ITEM_ELEMENTAL_ATTRIBUTES, null)) {
//                event.addCapability(new ResourceLocation(Reference.MODID, "Element"), new CapabilityProviderBase<>(Capabilities.ITEM_ELEMENTAL_ATTRIBUTES, new ItemElementalAttributes(stack)));
//            }
            if ((stack.getItem() instanceof ITrinketInterface) && !stack.hasCapability(Capabilities.ITEM_TRINKET, null)) {
                TrinketProperties newProperties = new TrinketProperties(stack);
                if (stack.getItem() instanceof IElementProvider) {
                    newProperties.getElementalAttributes().setPrimaryElement(((IElementProvider) stack.getItem()).getPrimaryElement());
                }
                event.addCapability(new ResourceLocation(Reference.MODID, "Trinket"), new CapabilityProviderBase<>(Capabilities.ITEM_TRINKET, newProperties));
            }
        }
    }
}
