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
import xzeroair.trinkets.traits.abilities.AbilityPoisonAffinity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPoisonStone;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketPoisonStone extends AccessoryBase {

	public static final ConfigPoisonStone serverConfig = TrinketsConfig.SERVER.Items.POISON_STONE;

	public TrinketPoisonStone(String name) {
		super(name);
		this.setUUID("e86e5b58-1b62-4a54-bba1-6594de844c2e");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "poison", serverConfig.poison);
		final KeyEntry key1 = new OptionEntry("poisonchance", serverConfig.poison, MathHelper.clamp((1F / serverConfig.poison_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
		final KeyEntry key2 = new OptionEntry("poisonduration", serverConfig.poison, (serverConfig.poison_duration / 20) + "s");
		final KeyEntry key3 = new LangEntry(this.getTranslationKey(stack), "bonusdamage", serverConfig.bonus_damage);
		final float pa = serverConfig.bonus_damage_amount <= 0 ? serverConfig.bonus_damage_amount : serverConfig.bonus_damage_amount - 1;
		final KeyEntry key4 = new OptionEntry("damagemultiplier", serverConfig.bonus_damage, helper.translateAttributeValue(1, pa));
		return helper.formatAddVariables(translation, key, key1, key2, key3, key4);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityPoisonAffinity());
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