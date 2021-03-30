package xzeroair.trinkets.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.compat.simpledifficulty.SDCompat;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class ViewerItem extends Item implements IsModelLoaded, IAccessoryInterface {

	public ViewerItem(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);

		//		ModItems.misc.ITEMS.add(this);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("" + TextFormatting.RED + "This is a Debug Item");
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		EntityProperties props = Capabilities.getEntityRace(player);
		if (props.getMagic().needMana()) {
			props.getMagic().setMana(props.getMagic().getMaxMana());
		}
		if (player.getHealth() < player.getMaxHealth()) {
			player.heal(player.getMaxHealth());
		}
		SDCompat.ClearTempurature(player);
		if ((player.ticksExisted % 300) == 0) {
			SDCompat.addThirst(player, 10, 0);
		}
		//		SDCapabilities.getThirstData(player).setThirstLevel(1);
		//		player.motionY += 0.075;
	}

	@Override
	public void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		if (event.getEntityLiving() == player) {
			System.out.println(event.getAmount());
			event.setAmount(0);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		//Hm
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean ItemEnabled() {
		return false;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
