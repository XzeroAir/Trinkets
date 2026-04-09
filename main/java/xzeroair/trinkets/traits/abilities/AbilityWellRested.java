package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.util.math.BlockPos;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ISleepAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityWellRested;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.PotionHelper.PotionHolder;

import javax.annotation.Nonnull;

public class AbilityWellRested extends Ability implements ISleepAbility, IHeldAbility {

    protected final ConfigAbilityWellRested CONFIG;

    protected String[] SLEEP_BONUSES;
    protected int SLEEP_BONUSES_RANDOM;

    public AbilityWellRested() {
        this(TrinketsConfig.SERVER.ABILITIES.WELL_RESTED);
    }

    public AbilityWellRested(ConfigAbilityWellRested config) {
        super(TrinketsRegistryNames.ModAbilities.WELL_RESTED);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.SLEEP_BONUSES = config.SLEEP_BONUSES;
        this.SLEEP_BONUSES_RANDOM = config.SLEEP_BONUSES_RANDOM;
    }

    @Override
    public SleepResult onStartSleeping(EntityLivingBase entity, BlockPos pos, SleepResult result) {
        return result;
    }

    @Override
    public void onWakeUp(@Nonnull EntityLivingBase entity, boolean wakeImmediately, boolean updatedWorld, boolean setSpawn) {
        if (entity.world.isRemote) {
            return;
        }
        if (entity instanceof EntityPlayer) {
            if (((EntityPlayer) entity).isPlayerFullyAsleep()) {

            } else {
                return;
            }
        }
        final String[] config = this.SLEEP_BONUSES;
        int amount = this.SLEEP_BONUSES_RANDOM;
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
