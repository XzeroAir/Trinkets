package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigWeightlessStone;

public class TrinketWeightless extends AccessoryBase {

	public static final ConfigWeightlessStone serverConfig = TrinketsConfig.SERVER.Items.WEIGHTLESS_STONE;

	public TrinketWeightless(String name) {
		super(name);
		this.setUUID("ba6e840e-46b2-4cb7-af4a-5f681333abe5");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
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

}