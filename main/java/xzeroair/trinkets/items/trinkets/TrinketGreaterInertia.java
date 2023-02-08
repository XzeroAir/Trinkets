package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityReduceKinetic;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigGreaterInertia;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketGreaterInertia extends AccessoryBase {

	public static final ConfigGreaterInertia serverConfig = TrinketsConfig.SERVER.Items.GREATER_INERTIA;

	public TrinketGreaterInertia(String name) {
		super(name);
		this.setUUID("e119ae9a-93b2-4053-ab3c-81108c16ff27");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new OptionEntry("greaterinertiafd", true, ((100F - (serverConfig.falldamage_amount * 100F)) + "%"));
		return helper.formatAddVariables(translation, key);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		final float fallMultiplier = serverConfig.fall_damage ? serverConfig.falldamage_amount : 0;
		abilities.add(new AbilityReduceKinetic().setFallMultiplier(fallMultiplier));
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
