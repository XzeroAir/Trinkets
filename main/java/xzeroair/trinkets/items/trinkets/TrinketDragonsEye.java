package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityBlockFinder;
import xzeroair.trinkets.traits.abilities.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Dragon;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;

public class TrinketDragonsEye extends AccessoryBase {

	public static final ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
	public static final Dragon clientConfig = TrinketsConfig.CLIENT.items.DRAGON_EYE;

	public TrinketDragonsEye(String name) {
		super(name);
		this.setUUID("6a345136-49b7-4b71-88dc-87301e329ac1");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void initAbilities(EntityLivingBase entity) {
		this.addAbility(entity, Abilities.nightVision, new AbilityNightVision().toggleAbility(true));
		this.addAbility(entity, Abilities.fireImmunity, new AbilityFireImmunity());
		if ((Trinkets.ToughAsNails || Trinkets.SimpleDifficulty) && serverConfig.compat.tan.immuneToHeat) {
			this.addAbility(entity, Abilities.survivalHeatImmunity, new AbilityHeatImmunity());
		}
		if (serverConfig.oreFinder) {
			this.addAbility(entity, Abilities.blockDetection, new AbilityBlockFinder());
		}
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
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