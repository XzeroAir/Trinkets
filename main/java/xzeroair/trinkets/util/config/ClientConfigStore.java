package xzeroair.trinkets.util.config;

import xzeroair.trinkets.util.TrinketsConfig;

public class ClientConfigStore {

    public static final ClientConfigStore INSTANCE = new ClientConfigStore();

    /**
     * Trinket Container
     */
    public boolean TRINKET_CONTAINER_ENABLED = TrinketsConfig.SERVER.GUI.guiEnabled;
    /**
     * Race Selection
     */
    public String[] RACE_SELECTION_BLACKLIST = TrinketsConfig.SERVER.races.selectionBlacklist;

    /**
     * Dragon's Eye settings.
     */
    public boolean DRAGON_EYE_OF_ENABLED = TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder;

    /**
     * Climbable Blocks
     */
    public boolean CLIMBING_ENABLED = TrinketsConfig.SERVER.races.fairy.climbing;
    public String[] CLIMB_BLOCKS = TrinketsConfig.SERVER.races.fairy.allowedBlocks;

    /**
     * Better Diving Compat.
     */
    public boolean MOD_COMPAT_BETTER_DIVING = TrinketsConfig.compat.betterdiving;
    public boolean MOD_COMPAT_ELENAI_DODGE = TrinketsConfig.compat.elenaiDodge;
    public boolean MOD_COMPAT_ENHANCED_VISUALS = TrinketsConfig.compat.enhancedvisuals;
    public boolean MOD_COMPAT_TOUGHASNAILS = TrinketsConfig.compat.toughasnails;
    public boolean MOD_COMPAT_SIMPLEDIFFICULTY = TrinketsConfig.compat.simpledifficulty;

    /**
     * MISC
     */
    public boolean BLOCK_MOVEMENT = TrinketsConfig.SERVER.misc.movement;
    public boolean REACH_FIX = TrinketsConfig.SERVER.misc.reach;

}
