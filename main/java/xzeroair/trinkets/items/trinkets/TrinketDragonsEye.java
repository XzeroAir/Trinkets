package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsDragonsEye;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Dragon;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

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
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		TrinketProperties itemNBT = Capabilities.getTrinketProperties(stack);
		if (itemNBT != null) {
			if (itemNBT.mainAbility() == true) {
				player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 210, 0, false, false));
				if (serverConfig.prevent_blindness) {
					if (player.isPotionActive(MobEffects.BLINDNESS)) {
						player.removePotionEffect(MobEffects.BLINDNESS);
					}
				}
			} else {
				if (player.isPotionActive(MobEffects.NIGHT_VISION)) {
					final PotionEffect potion = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
					if ((potion.getDuration() > 0) && (potion.getDuration() < 220)) {
						player.removePotionEffect(MobEffects.NIGHT_VISION);
					}
				}
			}
		}
		player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 150, 0, false, false));
		if (player.isBurning()) {
			player.extinguish();
		}
		if (serverConfig.compat.tan.immuneToHeat) {
			SurvivalCompat.immuneToHeat(player);
		}
		if (player.world.isRemote) {
			EffectsDragonsEye.playerTicks(stack, player);
		}
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {
		List<Potion> hyperthermia = SurvivalCompat.getHyperthermiaEffects();
		if (!hyperthermia.isEmpty()) {
			for (Potion hyper : hyperthermia) {
				if ((hyper != null) && event.getPotionEffect().getPotion().equals(hyper)) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	KeyHandler Aux;
	KeyHandler DE_NV;
	KeyHandler DE_Target;

	@Override
	@SideOnly(Side.CLIENT)
	public void eventClientTick(ItemStack stack, EntityLivingBase entity) {
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		if ((iCap == null) || !(entity instanceof EntityPlayer) || !FMLClientHandler.instance().getClient().inGameHasFocus) {
			return;
		}
		if (Aux == null) {
			Aux = new KeyHandler(ModKeyBindings.AUX_KEY);
		}
		if (DE_NV == null) {
			DE_NV = new KeyHandler(ModKeyBindings.DRAGONS_EYE_ABILITY);
		}
		if (DE_Target == null) {
			DE_Target = new KeyHandler(ModKeyBindings.DRAGONS_EYE_TARGET);
		}
		EntityPlayer player = (EntityPlayer) entity;
		DE_NV.handler(iCap, press -> {
			if (!Aux.isDown()) {
				iCap.toggleMainAbility(!iCap.mainAbility());
				iCap.sendInformationToServer(player);
			} else {
				iCap.toggleAltAbility(!iCap.altAbility());
			}
			return true;
		}, null, null);
		if (TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder != false) {
			final int size = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks.length;
			final int off = size - size - 1;
			final int max = size - 1;

			DE_Target.handler(
					iCap,
					press -> {
						if (!Aux.isDown()) {
							if (iCap.Target() < size) {
								iCap.setTarget(iCap.Target() + 1);
							}
							if (iCap.Target() == size) {
								iCap.setTarget(off);
							}
						} else {
							if (iCap.Target() > (off - 1)) {
								iCap.setTarget(iCap.Target() - 1);
							}
							if (iCap.Target() == (off - 1)) {
								iCap.setTarget(max);
							}
						}
						if (iCap.Target() > size) {
							iCap.setTarget(off);
						}
						TextComponentTranslation UnkownTarget = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.notfound");
						TextComponentTranslation FinderOn = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.on");
						TextComponentTranslation FinderOff = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.off");
						if ((iCap.Target() != off)) {
							String Type = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks[iCap.Target()];
							String getName = "Air";
							if (Type.contains(":") || Type.contains("[") || Type.contains("]")) {
								Type = Type.toLowerCase();
								getName = OreTrackingHelper.translateOreName(Type);
							} else {
								Type = Type.replace("ore", "");
								final String first = Type.substring(0, 1).toUpperCase();
								final String second = Type.substring(1).toLowerCase();
								getName = first + second;
							}
							if ((iCap.Target() != off)) {
								String NotFound = TranslationHelper.formatLangKeys(stack, UnkownTarget);
								String FoundTarget = TranslationHelper.formatLangKeys(stack, FinderOn);
								String Message = getName.equalsIgnoreCase("Air") ? NotFound : FoundTarget.replace("Chest", "Treasure Chests");
								player.sendStatusMessage(new TextComponentString(Message), true);
								iCap.sendInformationToServer(player);
							} else {
								String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
								player.sendStatusMessage(new TextComponentString(offMode), true);
								iCap.sendInformationToServer(player);
							}
							// Here
						} else { // Is On
							String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
							player.sendStatusMessage(new TextComponentString(offMode), true);
							iCap.sendInformationToServer(player);
						}
						return true;
					},
					down -> {
						return true;
					},
					release -> {
						return true;
					}
			);
		}
	}

	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		TrinketProperties ICap = Capabilities.getTrinketProperties(stack);
		if ((ICap != null) && !player.world.isRemote) {
			ICap.setTarget(-1);
			ICap.sendInformationToPlayer(player, player);
			//			NetworkHandler.sendItemDataTo(player, stack, ICap, true, (EntityPlayerMP) player);
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
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}