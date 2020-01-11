package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.foods.Dwarf_Stout;
import xzeroair.trinkets.items.foods.Fairy_Food;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class PlayerEventMC {

	@SubscribeEvent
	public void startTracking(PlayerEvent.StartTracking event) {
		if(event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			final boolean client = player.world.isRemote;
			if((event.getTarget() != null) && (event.getTarget() instanceof EntityLivingBase) && event.getTarget().hasCapability(SizeCapPro.sizeCapability, null)) {
				final EntityLivingBase entity = (EntityLivingBase) event.getTarget();
				if(!client) {
					NetworkHandler.sendPlayerDataTo(entity, cap, (EntityPlayerMP) player);
				}
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {

		final EntityPlayer oldPlayer = event.getOriginal();
		final EntityPlayer newPlayer = event.getEntityPlayer();

		if(TrinketsConfig.SERVER.Food.keep_effects) {
			if(oldPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
				final AttributeModifier fairyFood = oldPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(Fairy_Food.getUUID());
				final AttributeModifier dwarfFood = oldPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(Dwarf_Stout.getUUID());
				//			final AttributeModifier newfairyFood = newPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(Fairy_Food.getUUID());
				//			final AttributeModifier newdwarfFood = newPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(Dwarf_Stout.getUUID());
				if(newPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
					if(dwarfFood != null) {
						RaceAttribute.addModifier(newPlayer, 2, Dwarf_Stout.getUUID(), 2);
					}
					if(fairyFood != null) {
						RaceAttribute.addModifier(newPlayer, 1, Fairy_Food.getUUID(), 2);
					}
				}
			}
		}

		if(event.isWasDeath() && (event.getOriginal().world.getGameRules().getBoolean("keepInventory") == true)) {
			final ISizeCap oldCap = event.getOriginal().getCapability(SizeCapPro.sizeCapability, null);
			final ISizeCap newCap = event.getEntityPlayer().getCapability(SizeCapPro.sizeCapability, null);
			ItemStack stack = null;

			if(TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketDragonsEye)) {
				stack = TrinketHelper.getAccessory(event.getEntityPlayer(), ModItems.trinkets.TrinketDragonsEye);
			}
			if(TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketPolarized)) {
				stack = TrinketHelper.getAccessory(event.getEntityPlayer(), ModItems.trinkets.TrinketPolarized);
			}
			if((stack != null)) {
				final IAccessoryInterface cap = stack.getCapability(TrinketProvider.itemCapability, null);
				if((cap != null) && !event.getEntityPlayer().world.isRemote) {
					cap.setAbility(false);
					NetworkHandler.sendItemDataTo(event.getEntityPlayer(), stack, cap, true, (EntityPlayerMP) event.getEntityPlayer());
				}
			}

			if(TrinketHelper.AccessoryCheck(event.getOriginal(), ModItems.trinkets.TrinketFairyRing)
					|| TrinketHelper.AccessoryCheck(event.getOriginal(), ModItems.trinkets.TrinketDwarfRing)
					|| !oldCap.getFood().contains("none")) {
				copySizeFrom(oldPlayer, newPlayer);
			}
		}
	}

	public static void copySizeFrom(EntityPlayer original, EntityPlayer newPlayer) {
		final ISizeCap oldCap = original.getCapability(SizeCapPro.sizeCapability, null);
		final ISizeCap newCap = newPlayer.getCapability(SizeCapPro.sizeCapability, null);
		if((newCap != null) && (oldCap != null)) {
			newCap.setTrans(oldCap.getTrans());
			newCap.setTarget(oldCap.getTarget());
			newCap.setSize(oldCap.getSize());
			newCap.setFood(oldCap.getFood());
			newCap.setWidth(oldCap.getWidth());
			newCap.setHeight(oldCap.getHeight());
			newCap.setDefaultWidth(oldCap.getDefaultWidth());
			newCap.setDefaultHeight(oldCap.getDefaultHeight());
			newPlayer.eyeHeight = original.eyeHeight;
		}
	}

}
