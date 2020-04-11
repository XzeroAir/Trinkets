package xzeroair.trinkets.items.trinkets;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsTitanRing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketTitanRing extends AccessoryBase {

	public TrinketTitanRing(String name) {
		super(name);
		TrinketHelper.SizeTrinkets.add(this);
	}

	private static UUID uuid = UUID.fromString("c4f10831-7177-4538-8c3e-fcfcf3647207");

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.TITAN_RING.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.TITAN_RING.Attributes, uuid);
		EffectsTitanRing.TitansTicks(player);
	}

	@Override
	public void eventLivingFall(LivingFallEvent event, ItemStack stack, EntityLivingBase player) {
		EffectsTitanRing.TitanFall(event, player);
	}

	@Override
	public void eventLivingJump(ItemStack stack, EntityLivingBase player) {
		if ((TrinketsConfig.SERVER.TITAN_RING.step_height)) {
			EffectsTitanRing.TitanJump(player);
		}
	}

	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		EffectsTitanRing.TitanLogIn(player, stack);
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, uuid);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		// final List<Item> checkList = new ArrayList<>();
		// checkList.add(ModItems.trinkets.TrinketTitanRing);
		// checkList.add(ModItems.trinkets.TrinketDwarfRing);
		// checkList.add(ModItems.trinkets.TrinketFairyRing);
		//		for (final Item item : TrinketHelper.SizeTrinkets) {
		//			if (TrinketHelper.AccessoryCheck(player, item)) {
		//				return false;
		//			}
		//		}
		return super.playerCanEquip(stack, player);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			if (!(cap.Slot() == -1)) {
				super.playerEquipped(stack, player);
			} else {
				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
				if (!EntityRaceHelper.getRace(player).contentEquals("titan_spirit")) {
					EffectsTitanRing.TitanEquip(stack, player);
				}
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		if (!EntityRaceHelper.getRace(player).contentEquals("titan_spirit")) {
			if (!TrinketHelper.AccessoryCheck(player, stack.getItem())) {
				EffectsTitanRing.TitanUnequip(stack, player);
			}
		}
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.TITAN_RING.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}

}
