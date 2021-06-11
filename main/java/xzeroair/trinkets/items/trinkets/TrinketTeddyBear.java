package xzeroair.trinkets.items.trinkets;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigTeddyBear;

public class TrinketTeddyBear extends AccessoryBase {

	public static final ConfigTeddyBear serverConfig = TrinketsConfig.SERVER.Items.TEDDY_BEAR;

	public TrinketTeddyBear(String name) {
		super(name);
		this.setUUID("33b34669-715d-4caa-a31e-9c643c52ba66");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String crafter = "";
		if (!this.getTagCompoundSafe(stack).getString("crafter.name").isEmpty()) {
			crafter = this.getTagCompoundSafe(stack).getString("crafter.name") + "'s ";
		}
		return crafter + super.getItemStackDisplayName(stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		if (this.getTagCompoundSafe(stack).getString("crafter.id").isEmpty()) {
			if (entity instanceof EntityPlayer) {
				if (((EntityPlayer) entity).getUniqueID() != null) {
					this.getTagCompoundSafe(stack).setString("crafter.id", ((EntityPlayer) entity).getUniqueID().toString());
				}
			}
		} else {
			EntityPlayer player = entity.world.getPlayerEntityByUUID(UUID.fromString(this.getTagCompoundSafe(stack).getString("crafter.id")));
			if ((player != null) && this.getTagCompoundSafe(stack).getString("crafter.name").isEmpty()) {
				this.getTagCompoundSafe(stack).setString("crafter.name", player.getDisplayNameString());
			}
		}
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		LycanitesCompat.removeFear(player);
		LycanitesCompat.removeInsomnia(player);
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {

	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}