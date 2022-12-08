package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigGlowRing;

public class TrinketGlowRing extends AccessoryBase {

	public static final ConfigGlowRing serverConfig = TrinketsConfig.SERVER.Items.GLOW_RING;

	public TrinketGlowRing(String name) {
		super(name);
		this.setUUID("c7100557-afaf-4e69-b538-ef4ed550b470");
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityNightVision().toggleAbility(true));
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