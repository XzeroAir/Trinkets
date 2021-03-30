package xzeroair.trinkets.items.trinkets;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketPolarized extends AccessoryBase {

	public static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	public TrinketPolarized(String name) {
		super(name);
		this.setUUID("1ed98d9e-3075-45e0-b6f7-fcdff24caed4");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
		this.addPropertyOverride(new ResourceLocation("in_use"), new IItemPropertyGetter() {

			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				final Entity entity = entityIn;

				if ((worldIn == null) && (entity != null)) {
					worldIn = entity.world;
				}

				if (worldIn == null) {
					return 0.0F;
				} else {
					if (stack.getItemDamage() == 1) {
						return 1F;
					}
					if (stack.getItemDamage() == 2) {
						return 2F;
					}
					if (stack.getItemDamage() == 3) {
						return 3F;
					}
					return 0F;
				}
			}

		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		final ItemStack stack = player.getHeldItem(handIn);
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		if ((stack != null) && (iCap != null)) {
			if (stack.getItemDamage() == 0) {
				if (!player.isSneaking()) {
					stack.setItemDamage(1);
					iCap.toggleMainAbility(true);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(2);
					iCap.toggleAltAbility(true);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			if (stack.getItemDamage() == 2) {
				if (player.isSneaking()) {
					stack.setItemDamage(0);
					iCap.toggleAltAbility(false);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(3);
					iCap.toggleMainAbility(true);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			if (stack.getItemDamage() == 1) {
				if (!player.isSneaking()) {
					stack.setItemDamage(0);
					iCap.toggleMainAbility(false);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(3);
					iCap.toggleAltAbility(true);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			if (stack.getItemDamage() == 3) {
				if (player.isSneaking()) {
					stack.setItemDamage(1);
					iCap.toggleAltAbility(false);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(2);
					iCap.toggleMainAbility(false);
					if (player.world.isRemote) {
						ITextComponent message = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
						player.sendStatusMessage(message, true);
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			iCap.sendInformationToServer(player);
		}
		player.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		EffectsPolarizedStone.collectDrops(stack, player);
		if (serverConfig.repell) {
			EffectsPolarizedStone.blockArrows(stack, player);
		}
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
	public String getTranslationKey(ItemStack stack) {
		// Name + Item Damage equals the Lang File Name
		return super.getTranslationKey() + "." + 0;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
		Trinkets.proxy.registerItemRenderer(this, 1, "inventory");
		Trinkets.proxy.registerItemRenderer(this, 2, "inventory");
		Trinkets.proxy.registerItemRenderer(this, 3, "inventory");
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}
}