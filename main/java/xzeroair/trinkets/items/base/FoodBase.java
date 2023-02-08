package xzeroair.trinkets.items.base;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class FoodBase extends ItemFood implements IsModelLoaded {

	private UUID uuid;
	protected int cooldown = 0;
	protected boolean canEat = true;

	public FoodBase(String name, int heal, float saturation) {
		super(heal, saturation, false);
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(Trinkets.trinketstab);
	}

	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		return helper.formatAddVariables(translation);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltips, ITooltipFlag flagIn) {
		super.addInformation(stack, world, tooltips, flagIn);
		if (world == null) {
			return;
		}
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		for (int i = 1; i < 10; i++) {
			final int index = i;
			final String string = helper.getLangTranslation(stack.getTranslationKey() + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
			if (!helper.isStringEmpty(string)) {
				tooltips.add(
						string
				);
			}
		}
		final TextComponentTranslation ctrl = new TextComponentTranslation(Reference.MODID + ".holdctrl");
		final boolean tanEnabled = Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails;
		final boolean faEnabled = Trinkets.FirstAid;
		final boolean evEnabled = Trinkets.EnhancedVisuals && TrinketsConfig.compat.enhancedvisuals;
		final String TAN = !tanEnabled ? "" : helper.getLangTranslation(stack.getTranslationKey() + ".compat.tan", lang -> this.customItemInformation(stack, world, flagIn, 11, lang));
		final String FA = !faEnabled ? "" : helper.getLangTranslation(stack.getTranslationKey() + ".compat.firstaid", lang -> this.customItemInformation(stack, world, flagIn, 12, lang));
		final String EV = !evEnabled ? "" : helper.getLangTranslation(stack.getTranslationKey() + ".compat.enhancedvisuals", lang -> this.customItemInformation(stack, world, flagIn, 13, lang));
		if (GuiScreen.isCtrlKeyDown()) {
			if (!helper.isStringEmpty(TAN)) {
				tooltips.add(
						TAN + helper.gold + " (Tough as Nails)"
				);
			}
			if (!helper.isStringEmpty(FA)) {
				tooltips.add(
						FA + helper.gold + " (First Aid)"
				);
			}
			if (!helper.isStringEmpty(EV)) {
				tooltips.add(
						EV + helper.gold + " (Enhanced Visuals)"
				);
			}
		} else {
			if ((!helper.isStringEmpty(TAN)) ||
					(!helper.isStringEmpty(EV)) ||
					(!helper.isStringEmpty(FA))

			) {
				tooltips.add(helper.reset + "" + helper.dGray + ctrl.getFormattedText());
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack);
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		if (!uuid.isEmpty()) {
			this.uuid = UUID.fromString(uuid);
		} else {
			this.uuid = UUID.randomUUID();
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (cooldown > 0) {
			canEat = false;
			cooldown--;
		} else {
			canEat = true;
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		super.onCreated(stack, world, player);
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public boolean getEdible() {
		return canEat;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}