package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityDodge;
import xzeroair.trinkets.traits.abilities.AbilityLightningBolt;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketArcingOrb extends AccessoryBase {

	public final ConfigArcingOrb serverConfig = TrinketsConfig.SERVER.Items.ARCING_ORB;

	public TrinketArcingOrb(String name) {
		super(name);
		this.setUUID("249e65db-7dea-4825-8489-e6aa99a70be1");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "dodge", serverConfig.dodgeAbility);
		final KeyEntry key1 = new OptionEntry("dodgecost", serverConfig.dodgeAbility, serverConfig.dodgeCost);
		final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "dodgestun", serverConfig.dodgeStuns);
		final KeyEntry key3 = new LangEntry(this.getTranslationKey(stack), "boltattack", serverConfig.attackAbility);
		final KeyEntry key4 = new OptionEntry("boltcost", serverConfig.attackAbility, serverConfig.attackCost);
		final KeyEntry key5 = new OptionEntry("boltdamage", serverConfig.attackAbility, serverConfig.attackDmg);
		final KeyEntry key6 = new KeyBindEntry("arckb", serverConfig.attackAbility, ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName());
		return helper.formatAddVariables(translation, key, key1, key2, key3, key4, key5, key6);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityLightningBolt());
		abilities.add(new AbilityDodge());
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
