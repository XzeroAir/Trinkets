package xzeroair.trinkets.races.dragon.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;

//@formatter:off
public class DragonConfig {
    private final String name = "dragon";
    private final String PREFIX = Reference.MODID + ".config.races." + name;

    @Config.RequiresWorldRestart
    @Config.Comment("Creative Flight. Set to False to Disable. Default True")
    @Name("00. Creative Flight")
    @LangKey(PREFIX + ".flight")
    public boolean creative_flight = true;

    @Config.Comment("Mana Cost per second while flying")
    @Name("05. Flight Cost")
    @LangKey(PREFIX + ".flight.cost")
    public float flight_cost = 5F;

    @Name("Compatability Settings")
    @LangKey(Reference.MODID + ".config.compatibility")
    public Compatability compat = new Compatability();

    public class Compatability {

        @Name("Tough as Nails Compatability")
        @LangKey(Reference.MODID + ".config.toughasnails")
        public TANCompat tan = new TANCompat();

        public class TANCompat {
            @Config.Comment("If Tough as Nails is Installed, Should the player be immune to Heat")
            @Name("00. Immune to Heat")
            @LangKey(Reference.MODID + ".config." + ModItems.DragonsEye + ".toughasnails.immunity.heat")
            public boolean immuneToHeat = true;

            @Config.Comment("If Tough as Nails is Installed, Should the player be immune to Cold")
            @Name("01. Immune to Cold")
            @LangKey(Reference.MODID + ".config." + ModItems.DragonsEye + ".toughasnails.immunity.cold")
            public boolean immuneToCold = true;
        }

        @Name("Fire Resistance Tiers")
        @LangKey(Reference.MODID + ".config.fire_resistance_tiers")
        public FireResistanceTiers FRTiers = new FireResistanceTiers();

        public class FireResistanceTiers {

            public int amplifier = 0;
        }

    }

    @Name("Element Settings")
    @LangKey(Reference.MODID + ".config.element")
    public ElementConfig elementConfig = new ElementConfig();

    public class ElementConfig {

        @Name("Fire Settings")
        @LangKey(Reference.MODID + ".config.element.fire")
        public FireElementConfig fire = new FireElementConfig();

        public class FireElementConfig {

            @Config.Comment("Does the breath attack effect terrain")
            @Name("00. Breath effects terrain")
            @LangKey(PREFIX + ".breath.terrain")
            public boolean terrain = true;

            @Config.Comment("How much damage per second the dragon breath does")
            @Name("01. Dragon Breath Damage")
            @LangKey(PREFIX + ".breath.damage")
            public float breath_damage = 1F;

            @Config.Comment("The Mana Cost per tick when using dragons breath")
            @Name("02. Dragon Breath Cost")
            @LangKey(PREFIX + ".breath.cost")
            public float breath_cost = 10F;

            @Config.Comment("What effects does the breath attack apply to targets")
            @Name("03. Dragon Breath Effects")
            @LangKey(PREFIX + ".breath.effects")
            public String[] effects = {};

            @Config.Comment("What effects are dragons immune to")
            @Name("04. Dragon Resistances")
            @LangKey(PREFIX + ".resistances")
            public String[] resistances = {};

        }

        @Name("Ice Settings")
        @LangKey(Reference.MODID + ".config.element.ice")
        public IceElementConfig ice = new IceElementConfig();

        public class IceElementConfig {

            @Config.Comment("Does the breath attack effect terrain")
            @Name("00. Breath effects terrain")
            @LangKey(PREFIX + ".breath.terrain")
            public boolean terrain = true;

            @Config.Comment("How much damage per second the dragon breath does")
            @Name("01. Dragon Breath Damage")
            @LangKey(PREFIX + ".breath.damage")
            public float breath_damage = 1F;

            @Config.Comment("The Mana Cost per tick when using dragons breath")
            @Name("02. Dragon Breath Cost")
            @LangKey(PREFIX + ".breath.cost")
            public float breath_cost = 10F;

            @Config.Comment("What effects does the breath attack apply to targets")
            @Name("03. Dragon Breath Effects")
            @LangKey(PREFIX + ".breath.effects")
            public String[] effects = {
                    "minecraft:slowness:100:2",
            };

            @Config.Comment("What effects are dragons immune to")
            @Name("04. Dragon Resistances")
            @LangKey(PREFIX + ".resistances")
            public String[] resistances = {};

        }

        @Name("Lightning Settings")
        @LangKey(Reference.MODID + ".config.element.lightning")
        public LightningElementConfig lightning = new LightningElementConfig();

        public class LightningElementConfig {

            @Config.Comment("Does the breath attack effect terrain")
            @Name("00. Breath effects terrain")
            @LangKey(PREFIX + ".breath.terrain")
            public boolean terrain = true;

            @Config.Comment("How much damage per second the dragon breath does")
            @Name("01. Dragon Breath Damage")
            @LangKey(PREFIX + ".breath.damage")
            public float breath_damage = 1F;

            @Config.Comment("The Mana Cost per tick when using dragons breath")
            @Name("02. Dragon Breath Cost")
            @LangKey(PREFIX + ".breath.cost")
            public float breath_cost = 10F;

            @Config.Comment("What effects does the breath attack apply to targets")
            @Name("03. Dragon Breath Effects")
            @LangKey(PREFIX + ".breath.effects")
            public String[] effects = {
                    "minecraft:slowness:20:4",
                    "minecraft:weakness:20:1"
            };

            @Config.Comment("What effects are dragons immune to")
            @Name("04. Dragon Resistances")
            @LangKey(PREFIX + ".resistances")
            public String[] resistances = {};

        }
    }

    @Name("Size")
    @LangKey(Reference.MODID + ".config.race.size")
    public final RaceSizeConfig size = new RaceSizeConfig(120, 120);

    @Config.Comment({"For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
    @Name("Attributes")
    @LangKey(Reference.MODID + ".config.attributes")
    public String[] attributes = {
            "Name:generic.maxHealth, Amount:0.25, Operation:1",
            "Name:generic.knockbackResistance; Amount:0; Operation:0",
            "Name:generic.movementSpeed, Amount:0, Operation:0",
            "Name:generic.attackDamage, Amount:0.5, Operation:1",
            "Name:generic.attackSpeed, Amount:0, Operation:0",
            "Name:generic.armor, Amount:0, Operation:0",
            "Name:generic.armorToughness, Amount:0.5, Operation:1",
            "Name:generic.luck, Amount:0, Operation:0",
            "Name:generic.reachDistance, Amount:0, Operation:0",
            "Name:forge.swimSpeed, Amount:0, Operation:0",
            "Name:xat.entityMagic.regen, Amount:0, Operation:0",
            "Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
            "Name:xat.entityMagic.affinity, Amount:0, Operation:0",
            "Name:xat.jump, Amount:0, Operation:0",
            "Name:xat.stepheight, Amount:0, Operation:0",
            "Name:xat.flyspeed, Amount:-0.6, Operation:2"
    };
}
