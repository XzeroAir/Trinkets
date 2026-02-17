package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ISleepAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.PotionHelper.PotionHolder;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityWellRested extends Ability implements IPotionAbility, ISleepAbility, IHeldAbility {

    public AbilityWellRested() {
        super(Abilities.wellRested);
    }

    @Override
    public SleepResult onStartSleeping(EntityLivingBase entity, BlockPos pos, SleepResult result) {
        return result;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
        String fear = "";
        String insomnia = "";
        for (String s : TrinketsConfig.SERVER.Items.TEDDY_BEAR.immunities) {
            if (s.toLowerCase().equals("lycanitesmobs:fear")) {
                fear = "lycanitesmobs:fear";
            }
            if (s.toLowerCase().equals("lycanitesmobs:insomnia")) {
                insomnia = "lycanitesmobs:insomnia";
            }
        }
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("fear", Trinkets.MOD_COMPAT.LycanitesMobs && TrinketsConfig.compat.lycanites && !fear.isEmpty(), new TextComponentTranslation("effect.fear").getFormattedText());
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("insomnia", Trinkets.MOD_COMPAT.LycanitesMobs && TrinketsConfig.compat.lycanites && !insomnia.isEmpty(), new TextComponentTranslation("effect.insomnia").getFormattedText());
        return helper.formatAddVariables(key, renderID, key1, key1);
    }

    @Override
    public void onWakeUp(EntityLivingBase entity, boolean wakeImmediately, boolean updatedWorld, boolean setSpawn) {
        if (entity.world.isRemote) {
            return;
        }
        if (TrinketsConfig.SERVER.Items.TEDDY_BEAR.sleep_bonus) {
            if (entity instanceof EntityPlayer) {
                if (((EntityPlayer) entity).isPlayerFullyAsleep()) {

                } else {
                    return;
                }
            }
            final String[] config = TrinketsConfig.SERVER.Items.TEDDY_BEAR.buffs;
            int amount = TrinketsConfig.SERVER.Items.TEDDY_BEAR.randomBuff;
            if (amount > config.length) {
                amount = config.length;
            }
            if (amount > 0) {
                String potID = "";
                for (int i = 0; i < amount; i++) {
                    final int potRand = Reference.random.nextInt(config.length);
                    potID += config[potRand] + ",";
                }
                if (!potID.isEmpty()) {
                    final String[] pots = potID.split(",");
                    for (final String p : pots) {
                        if (!p.isEmpty()) {
                            final PotionHolder potion = PotionHelper.getPotionHolder(p);
                            if (!entity.isPotionActive(potion.getPotion())) {
                                entity.addPotionEffect(potion.getPotionEffect());
                            } else {
                                entity.getActivePotionEffect(potion.getPotion()).combine(potion.getPotionEffect());
                            }
                        }
                    }
                }
            } else {
                for (final String potID : config) {
                    final PotionHolder potion = PotionHelper.getPotionHolder(potID);
                    if (potion.getPotion() != null) {
                        entity.addPotionEffect(potion.getPotionEffect());
                    }
                }
            }
        }
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final String e = effect.getPotion().getRegistryName().toString();
        for (final String immunity : TrinketsConfig.SERVER.Items.TEDDY_BEAR.immunities) {
            final Potion fear = Potion.getPotionFromResourceLocation(immunity);
            if ((fear != null) && e.contentEquals(fear.getRegistryName().toString())) {
                return true;
            }
        }
        return cancel;
    }

}
