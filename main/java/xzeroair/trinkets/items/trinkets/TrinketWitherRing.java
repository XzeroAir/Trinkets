package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWitherAffinity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigWitherRing;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketWitherRing extends AccessoryBase {

	public static final ConfigWitherRing serverConfig = TrinketsConfig.SERVER.Items.WITHER_RING;

	public TrinketWitherRing(String name) {
		super(name);
		this.setUUID("bca63279-4a19-4891-b4b0-a5a2f76e4b90");
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "wither", serverConfig.wither);
		final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "leech", serverConfig.leech);
		final KeyEntry key2 = new OptionEntry("witherchance", serverConfig.wither, MathHelper.clamp((1F / serverConfig.wither_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
		final KeyEntry key3 = new OptionEntry("leechamount", serverConfig.leech, (serverConfig.leech_amount * 0.5));
		return helper.formatAddVariables(translation, key, key1, key2, key3);
	}

	@Override
	public String[] getAttributeConfig() {
		return serverConfig.attributes;
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityWitherAffinity());
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