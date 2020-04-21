package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.network.IncreasedAttackRangePacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PolarizedStoneSyncPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class EventHandlerClient {

	int Dragon_Ability = 0;
	int Polarized_Ability = 0;
	int TARGET = 0;
	int AUX = 0;

	private boolean check, check2, check3 = false;
	float cooldown = 0;

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if ((event.side == Side.CLIENT) && (event.phase == Phase.START)) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null)) {

				if (TrinketsConfig.SERVER.GUI.guiEnabled && ModKeyBindings.TRINKET_GUI.isPressed()) {
					NetworkHandler.INSTANCE.sendToServer(new OpenTrinketGui());
				}
			}
		}
		if (event.phase == Phase.END) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null)) {
				if (ModKeyBindings.POLARIZED_STONE_ABILITY.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					Polarized_Ability = 1;
				} else {
					Polarized_Ability = 0;
				}
				if (ModKeyBindings.DRAGONS_EYE_ABILITY.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					Dragon_Ability = 1;
				} else {
					Dragon_Ability = 0;
				}
				if (ModKeyBindings.DRAGONS_EYE_TARGET.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					TARGET = 1;
				} else {
					TARGET = 0;
				}
				if (ModKeyBindings.AUX_KEY.isKeyDown() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					AUX = 1;
				} else {
					AUX = 0;
				}
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if (FMLClientHandler.instance().getClient().inGameHasFocus) {
						TextComponentString magnet = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
						TextComponentString repel = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
						if ((Polarized_Ability == 1) && (AUX == 0)) {
							iCap.toggleMainAbility(!iCap.mainAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							player.sendMessage(magnet);
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, true, stack.getItemDamage()));
						}
						if ((Polarized_Ability == 1) && (AUX == 1)) {
							iCap.toggleAltAbility(!iCap.altAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							player.sendMessage(repel);
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, true, stack.getItemDamage()));
						}
					}
				}
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDragonsEye);
					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if (iCap == null) {
						return;
					}
					if ((Dragon_Ability == 1) && (AUX == 0)) {
						iCap.toggleMainAbility(!iCap.mainAbility());
						NetworkHandler.sendItemDataServer(player, stack, iCap, true);
					}
					if ((Dragon_Ability == 1) && (AUX == 1)) {
						iCap.toggleAltAbility(!iCap.altAbility());
					}
					if (TrinketsConfig.SERVER.DRAGON_EYE.oreFinder != false) {
						final int size = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks.length;
						final int off = size - size - 1;
						final int max = size - 1;

						if ((TARGET == 1) && (AUX == 0)) {
							if (iCap.Target() < size) {
								iCap.setTarget(iCap.Target() + 1);
							}
							if (iCap.Target() == size) {
								iCap.setTarget(off);
							}
						}
						if ((TARGET == 1) && (AUX == 1)) {
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
						if (TARGET == 1) {
							TextComponentTranslation UnkownTarget = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.notfound");
							TextComponentTranslation FinderOn = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.on");
							TextComponentTranslation FinderOff = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.off");
							if ((iCap.Target() != off)) {
								String Type = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks[iCap.Target()];
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
									String Message = getName.equalsIgnoreCase("Air") ? NotFound : FoundTarget;
									player.sendMessage(new TextComponentString(Message));
									NetworkHandler.sendItemDataServer(player, stack, iCap, true);
								} else {
									String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
									player.sendMessage(new TextComponentString(offMode));
									NetworkHandler.sendItemDataServer(player, stack, iCap, true);
								}
								// Here
							} else { // Is On
								String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
								player.sendMessage(new TextComponentString(offMode));
								NetworkHandler.sendItemDataServer(player, stack, iCap, true);
							}
						}
					}
				}
			}
			if (cooldown > 0) {
				cooldown--;
			} else {
				cooldown = 0;
			}
		}
		//
	}

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {
		KeyBinding lClick = Minecraft.getMinecraft().gameSettings.keyBindAttack;
		if (lClick.isKeyDown() && (cooldown == 0)) {
			EntityPlayer player = event.getEntityPlayer();
			IAttributeInstance reach = player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
			if ((reach.getAttributeValue() > 5) && (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketTitanRing) || EntityRaceHelper.getRace(player).contentEquals("titan_spirit"))) {

				RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, reach.getAttributeValue() * 0.8, 1D, true);

				RayTraceHelper.rayTraceEntity(beam, target -> {
					if (target instanceof EntityLivingBase) {
						NetworkHandler.INSTANCE.sendToServer(new IncreasedAttackRangePacket(player, (EntityLivingBase) target, Capabilities.getEntityRace(player)));
						if (cooldown == 0) {
							cooldown = player.getCooldownPeriod();
						}
						RayTraceHelper.drawAttackSweep(player.world, beam.getEnd(), EnumParticleTypes.SWEEP_ATTACK);
						return true;
					} else {
						return false;
					}
				});
			}
		}
	}

	@SubscribeEvent
	public void ItemToolTipEvent(ItemTooltipEvent event) {
		if ((event.getItemStack() != null) && (event.getEntityPlayer() != null)) {
			ItemStack stack = event.getItemStack();
			if (stack.getItem().equals(Items.POTIONITEM)) {
				final PotionType pot = PotionUtils.getPotionFromItem(stack);
				if (pot.equals(ModPotionTypes.Enhanced) ||
						pot.equals(ModPotionTypes.Restorative) ||
						pot.equals(ModPotionTypes.DwarfType) ||
						pot.equals(ModPotionTypes.FairyType) ||
						pot.equals(ModPotionTypes.TitanType)) {
					TranslationHelper.addPotionTooltips(stack, event.getEntityPlayer().world, event.getToolTip());
				}
			}
		}
	}
}
