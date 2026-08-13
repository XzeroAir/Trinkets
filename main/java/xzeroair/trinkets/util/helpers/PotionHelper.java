package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.util.config.ConfigHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PotionHelper {

    public static class PotionHolder {
        private Potion potion;
        private PotionEffect effect;
        private int duration;
        private int amplifier;

        public PotionHolder(String configPot) {
            this.initPotion(configPot);
        }

        public Potion getPotion() {
            return this.potion;
        }

        public PotionEffect getPotionEffect() {
            return this.effect;
        }

        public int getDuration() {
            return this.duration;
        }

        public int getAmplifier() {
            return this.amplifier;
        }

        private void initPotion(String config) {
            if (!config.isEmpty()) {
                String potionRegName = "";
                String[] configString = config.split(":");
                String modID = configString[0];
                potionRegName = modID;
                if (configString.length > 1) {
                    String effectID = configString[1];
                    potionRegName = modID + ":" + effectID;
                } else {
                    potionRegName = "minecraft:" + potionRegName;
                }
                Potion potion = Potion.getPotionFromResourceLocation(potionRegName);
                if (potion != null) {
                    int duration = 300;
                    int amplifier = 0;
                    if (configString.length > 2) {
                        try {
                            String durationString = configString[2];
                            duration = Integer.parseInt(durationString);
                        } catch (Exception ignored) {

                        }
                        if (configString.length > 3) {
                            try {
                                String amplifierString = configString[3];
                                amplifier = Integer.parseInt(amplifierString);
                            } catch (Exception ignored) {

                            }
                        }
                    }
                    this.potion = potion;
                    this.duration = duration;
                    this.amplifier = amplifier;
                    this.effect = new PotionEffect(potion, duration, amplifier, false, false);
                }
            }
        }
    }

    public static PotionHolder getPotionHolder(String potConfig) {
        return new PotionHolder(potConfig);
    }

    public static boolean isModPotionActive(EntityLivingBase entity, String modID, String potionEffect) {
        Potion potion = getModPotion(modID, potionEffect);
        return (potion != null) && entity.isPotionActive(potion);
    }

    @Nullable
    public static Potion getModPotion(@Nonnull String modID, String potionEffect) {
        if (modID.equalsIgnoreCase("minecraft") || Loader.isModLoaded(modID)) {
            return Potion.getPotionFromResourceLocation(modID + ":" + potionEffect);
        }
        return null;
    }

    public static boolean isPotionEffect(final PotionEffect effect, @Nonnull String... strings) {
        for (String string : strings) {
            if (isPotionEffect(effect, string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPotionEffect(final PotionEffect effect, String string) {
        if (effect != null) {
            return StringUtils.parseResourceLocationSafely(effect.getPotion().getRegistryName(), string);
        }
        return false;
    }

    public static boolean isPotionEffect(final PotionEffect effect, String modid, String name) {
        if (effect != null) {
            final ResourceLocation e = effect.getPotion().getRegistryName();
            if (e != null) {
                return e.getNamespace().contentEquals(modid) && e.getPath().contentEquals(name);
            }
        }
        return false;
    }

    public static boolean isPotionEffectLazy(final PotionEffect effect, String modid, String name) {
        if (effect != null) {
            final ResourceLocation e = effect.getPotion().getRegistryName();
            final Potion potion = getModPotion(modid, name);
            if (potion != null && e != null && potion.getRegistryName() != null) {
                return e.compareTo(potion.getRegistryName()) == 0;
            }
        }
        return false;
    }

    public static boolean isPotionEffectLazy(final PotionEffect effect, String name) {
        if (effect != null) {
            final ResourceLocation e = effect.getPotion().getRegistryName();
            final Potion potion = Potion.getPotionFromResourceLocation(name);
            if (potion != null && e != null && potion.getRegistryName() != null) {
                return e.compareTo(potion.getRegistryName()) == 0;
            }
        }
        return false;
    }

    @Nullable
    public static Potion getPotionFromConfig(String string) {
        final String config = ConfigHelper.cleanConfigEntry(string);
        if (!config.isEmpty()) {
            String[] configString = config.split(":");
            String modID = configString[0];
            String pot = modID;
            if (configString.length > 1) {
                String effectID = configString[1];
                pot = modID + ":" + effectID;
            } else {
                pot = "minecraft:" + pot;
            }
            return Potion.getPotionFromResourceLocation(pot);
        }
        return null;
    }

    @Nullable
    public static PotionEffect getPotionEffectFromConfig(String string) {
        final String config = ConfigHelper.cleanConfigEntry(string);
        if (!config.isEmpty()) {
            String[] configString = config.split(":");
            String modID = configString[0];
            String pot = modID;
            if (configString.length > 1) {
                String effectID = configString[1];
                pot = modID + ":" + effectID;
            } else {
                pot = "minecraft:" + pot;
            }
            Potion potion = Potion.getPotionFromResourceLocation(pot);
            if (potion != null) {
                int duration = 300;
                int amplifier = 0;
                if (configString.length > 2) {
                    try {
                        String durationString = configString[2];
                        duration = Integer.parseInt(durationString);
                    } catch (Exception ignored) {

                    }
                    if (configString.length > 3) {
                        try {
                            String amplifierString = configString[3];
                            amplifier = Integer.parseInt(amplifierString);
                        } catch (Exception ignored) {

                        }
                    }
                }
                return new PotionEffect(potion, duration, amplifier, false, false);
            }
        }
        return null;
    }

    public static void addAllPotionEffectsFromConfig(EntityLivingBase entity, String... strings) {
        addAllPotionEffectsFromConfig(entity, false, false, strings);
    }

    public static void addAllPotionEffectsFromConfig(EntityLivingBase entity, boolean isPerm, String... strings) {
        addAllPotionEffectsFromConfig(entity, false, isPerm, strings);
    }

    public static void addAllPotionEffectsFromConfig(EntityLivingBase entity, boolean checkIfActive, boolean isPerm, String... strings) {
        if (entity == null) {
            return;
        }
        for (String string : strings) {
            final String cleanString = ConfigHelper.cleanConfigEntry(string);
            if (!cleanString.isEmpty()) {
                final PotionHolder potion = PotionHelper.getPotionHolder(ConfigHelper.cleanConfigEntry(cleanString));
                if (potion.getPotion() != null) {
                    if (!entity.world.isRemote) {
                        if (checkIfActive) {
                            if (entity.getActivePotionMap().isEmpty() || entity.isPotionActive(potion.getPotion())) {
                                entity.addPotionEffect(potion.getPotionEffect());
                            }
                        } else {
                            entity.addPotionEffect(potion.getPotionEffect());
                        }
                    } else {
                        if (isPerm && !entity.getActivePotionMap().isEmpty()) {
                            try {
                                PotionEffect pot = entity.getActivePotionEffect(potion.getPotion());
                                if (pot != null) {
                                    if (!pot.getIsPotionDurationMax()) {
                                        pot.setPotionDurationMax(true);
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
        }
    }

    public static void removeAllPotionEffectsFromConfig(EntityLivingBase entity, String... strings) {
        if (entity == null || entity.getActivePotionMap().isEmpty()) {
            return;
        }
        for (String string : strings) {
            final String cleanString = ConfigHelper.cleanConfigEntry(string);
            if (!cleanString.isEmpty()) {
                final PotionHolder potion = PotionHelper.getPotionHolder(cleanString);
                if (potion.getPotion() != null) {
                    if (entity.isPotionActive(potion.getPotion())) {
                        entity.removePotionEffect(potion.getPotion());
                    }
                }
            }
        }
    }
}
