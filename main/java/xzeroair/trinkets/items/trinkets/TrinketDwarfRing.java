package xzeroair.trinkets.items.trinkets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketDwarfRing extends AccessoryBase {

	private static final UUID uuid = UUID.fromString("d222c4fa-0e05-4b90-98c0-1f574d9d2558");

	public static List Incompatible = new ArrayList<>();

	public TrinketDwarfRing(String name) {
		super(name);
		TrinketHelper.SizeTrinkets.add(this);
		Incompatible = TrinketHelper.getIncompatibleRaceTrinket(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.DWARF_RING.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		EffectsDwarfRing.DwarfTicks(player);
	}

	@Override
	public void eventLivingJump(ItemStack stack, EntityLivingBase player) {

	}

	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		EffectsDwarfRing.DwarfLogIn(player, stack);
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		EffectsDwarfRing.DwarfLogout(player);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
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
				if (!EntityRaceHelper.getRace(player).contentEquals("dwarf_stout")) {
					EffectsDwarfRing.DwarfEquip(stack, player);
				}
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		if (!EntityRaceHelper.getRace(player).contentEquals("dwarf_stout")) {
			if (!TrinketHelper.AccessoryCheck(player, stack.getItem())) {
				EffectsDwarfRing.DwarfUnequip(stack, player);
			}
		}
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.DWARF_RING.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
