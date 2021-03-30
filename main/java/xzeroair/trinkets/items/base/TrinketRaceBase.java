package xzeroair.trinkets.items.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.AttributeConfigWrapper;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;
import xzeroair.trinkets.util.handlers.ItemAttributeHandler;

public class TrinketRaceBase extends AccessoryBase {

	public TransformationRingConfig serverConfig;

	protected EntityRace race;

	public TrinketRaceBase(String name, EntityRace race, TransformationRingConfig config, ConfigAttribs attrib) {
		super(name);
		this.race = race;
		serverConfig = config;
		attributesConfig = new AttributeConfigWrapper(attrib);
		attributes = new ItemAttributeHandler();
		ModItems.RaceTrinkets.ITEMS.add(this);
	}

	public EntityRace getRacialTraits() {
		return race;
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
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
