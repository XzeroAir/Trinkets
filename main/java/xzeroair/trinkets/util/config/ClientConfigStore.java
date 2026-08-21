package xzeroair.trinkets.util.config;

import xzeroair.trinkets.util.TrinketsConfig;

public class ClientConfigStore {

    public static final ClientConfigStore INSTANCE = new ClientConfigStore();

    /**
     * Trinket Container
     */
    public boolean TRINKET_CONTAINER_ENABLED = TrinketsConfig.SERVER.GUI.ENABLED;
    /**
     * Race Selection
     */
    public String[] RACE_SELECTION_BLACKLIST = TrinketsConfig.SERVER.RACES.BLACKLIST;

    /**
     * Mod Compatibility.
     */
    public boolean MOD_COMPAT_BETTER_DIVING = TrinketsConfig.compat.BETTER_DIVING;
    public boolean MOD_COMPAT_ELENAI_DODGE = TrinketsConfig.compat.ELENAI_DODGE;
    public boolean MOD_COMPAT_ENHANCED_VISUALS = TrinketsConfig.compat.ENHANCED_VISUALS;
    public boolean MOD_COMPAT_TOUGHASNAILS = TrinketsConfig.compat.TOUGH_AS_NAILS;
    public boolean MOD_COMPAT_SIMPLEDIFFICULTY = TrinketsConfig.compat.SIMPLE_DIFFICULTY;
    public boolean MOD_COMPAT_ICE_AND_FIRE = TrinketsConfig.compat.ICE_AND_FIRE;
    public boolean MOD_COMPAT_IDO_SWIMMING = TrinketsConfig.compat.IDO_SWIMMING;
    public boolean MOD_COMPAT_TROPICRAFT_SWIMMING = TrinketsConfig.compat.TROPICRAFT_SWIMMING;

    /**
     * MISC
     */
    public boolean BLOCK_MOVEMENT = TrinketsConfig.SERVER.MISC.MOVEMENT;
    public boolean REACH_FIX = TrinketsConfig.SERVER.MISC.REACH;

}
