package xzeroair.trinkets.items.trinkets;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketWeightless extends AccessoryBase {

	public TrinketWeightless(String name) {
		super(name);
	}

	private final UUID uuid = UUID.fromString("ba6e840e-46b2-4cb7-af4a-5f681333abe5");

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.WEIGHTLESS_STONE.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.WEIGHTLESS_STONE.Attributes, this.uuid);
		if (!player.onGround) {
			player.motionY = 0;
			if ((!(player.isSneaking())) && player.isSwingInProgress) {
				player.motionY += 0.1;
			}
			if (player.isSneaking() && player.isSwingInProgress) {
				player.motionY -= 0.1;
			}
		}
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (player.inventory.getCurrentItem().getItem() == this) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 2, 1));
			}
		}
	}

	@Override
	public void eventLivingFall(LivingFallEvent event, ItemStack stack, EntityLivingBase player) {
		event.setDamageMultiplier(0.1F);
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, this.uuid);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if (TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			if (!(cap.Slot() == -1)) {
				super.playerEquipped(stack, player);
			} else {
				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.WEIGHTLESS_STONE.Attributes, this.uuid);
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		AttributeHelper.removeAttributes(player, this.uuid);
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.WEIGHTLESS_STONE.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}

}