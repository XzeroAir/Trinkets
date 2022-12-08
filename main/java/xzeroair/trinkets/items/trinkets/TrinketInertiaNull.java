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
import xzeroair.trinkets.traits.abilities.AbilitySturdy;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigInertiaNull;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketInertiaNull extends AccessoryBase {

	public static final ConfigInertiaNull serverConfig = TrinketsConfig.SERVER.Items.INERTIA_NULL;

	public TrinketInertiaNull(String name) {
		super(name);
		this.setUUID("8192af5d-98de-4c1e-a125-e99864b99634");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new OptionEntry("inertianullfd", true, ((100F - (serverConfig.falldamage_amount * 100F)) + "%"));
		return helper.formatAddVariables(translation, key);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		final float fallMultiplier = serverConfig.fall_damage ? serverConfig.falldamage_amount : 0;
		abilities.add(new AbilitySturdy().setFallMultiplier(fallMultiplier));
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