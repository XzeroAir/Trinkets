package xzeroair.trinkets.client;

import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.ConstantsResourceLocations;
import xzeroair.trinkets.util.Reference;

public class ConstantsTextureResourceLocation {

    private static final String MODID = Reference.MODID;

    public static final ResourceLocation HUMAN = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_HUMAN + ".png");
    public static final ResourceLocation DWARF = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_DWARF + ".png");
    public static final ResourceLocation ELF = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_ELF + ".png");
    public static final ResourceLocation FAIRY = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_FAIRY + ".png");
    public static final ResourceLocation GOBLIN = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_GOBLIN + ".png");
    public static final ResourceLocation FAELIS = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_FAELIS + ".png");
    public static final ResourceLocation TITAN = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_TITAN + ".png");
    public static final ResourceLocation DRAGON = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_DRAGON + ".png");
    public static final ResourceLocation DRAGON_FIRE = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_DRAGON_FIRE + ".png");
    public static final ResourceLocation DRAGON_ICE = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_DRAGON_ICE + ".png");
    public static final ResourceLocation DRAGON_LIGHTNING = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_DRAGON_LIGHTNING + ".png");
    public static final ResourceLocation TAURUS = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PATH_POTIONS_RACES_TAURUS + ".png");

    public static final ResourceLocation PARTICLE_GREED = new ResourceLocation(MODID, ConstantsResourceLocations.TEXTURES_PARTICLES_PATH + "/greed.png");

    public static ResourceLocation getPotionIconForRace(EntityRace race, Element element) {
        switch (race.getName().toLowerCase()) {
            case "dwarf":
                return DWARF;
            case "elf":
                return ELF;
            case "fairy":
                return FAIRY;
            case "goblin":
                return GOBLIN;
            case "faelis":
                return FAELIS;
            case "titan":
                return TITAN;
            case "dragon":
                switch (element.getName().toLowerCase()) {
                    case "fire":
                        return DRAGON_FIRE;
                    case "ice":
                        return DRAGON_ICE;
                    case "lightning":
                        return DRAGON_LIGHTNING;
                    default:
                        return DRAGON;
                }
            case "taurus":
                return TAURUS;
            case "human":
            default:
                return HUMAN;

        }
    }


}
