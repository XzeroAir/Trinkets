package xzeroair.trinkets.races.fairy.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;

//@formatter:off
//@Config(name = Reference.filePath+"Races/Fairy", modid = Reference.MODID)
public class FairyConfig {

		private final String name = "fairy";
		private final String PREFIX = Reference.MODID + ".config.races." + name;

		@Config.RequiresWorldRestart
		@Config.Comment("Creative Flight. Set to False to Disable. Default True")
		@Name("01. Creative Flight")
		@LangKey(PREFIX + ".flight")
		public boolean creative_flight = true;

		@Config.Comment("Fairy's Climbing Ability. Set to False to Disable. Default True")
		@Name("05. Climbing")
		@LangKey(PREFIX + ".climbing")
		public boolean climbing = true;

		@Name("06. Climable Blocks")
		@LangKey(PREFIX + ".climbing.blocks.list")
		public String[] allowedBlocks = new String[] {
			        "minecraft:dirt",
	                "minecraft:grass",
	                "minecraft:sand",
	                "minecraft:*cobblestone*",
	                "minecraft:*planks*",
	                "minecraft:*stairs",
	                "minecraft:*slab",
	                "minecraft:*slab2",
//	                "minecraft:cobblestone",
//	                "minecraft:mossy_cobblestone",
//	                "minecraft:oak_stairs",
//	                "minecraft:stone_stairs",
//	                "minecraft:brick_stairs",
//	                "minecraft:stone_brick_stairs",
//	                "minecraft:nether_brick_stairs",
//	                "minecraft:sandstone_stairs",
//	                "minecraft:spruce_stairs",
//	                "minecraft:birch_stairs",
//	                "minecraft:jungle_stairs",
//	                "minecraft:arcacia_stairs",
//	                "minecraft:quartz_stairs",
//	                "minecraft:dark_oak_stairs",
//	                "minecraft:red_sandstone_stairs",
//	                "minecraft:purpur_stairs",
//	                "minecraft:stone_slab",
//	                "minecraft:wooden_slab",
//	                "minecraft:stone_slab2",
//	                "minecraft:purpur_slab",
	                "minecraft:stone",
//	                "minecraft:planks",
	                "minecraft:gravel",
	                "minecraft:gold_ore",
	                "minecraft:iron_ore",
	                "minecraft:coal_ore",
	                "minecraft:log",
	                "minecraft:sponge",
	                "minecraft:glass",
	                "minecraft:lapis_ore",
	                "minecraft:lapis_block",
	                "minecraft:sandstone",
	                "minecraft:wool",
	                "minecraft:gold_block",
	                "minecraft:iron_block",
	                "minecraft:brick_block",
	                "minecraft:bookshelf",
	                "minecraft:obsidian",
	                "minecraft:diamond_ore",
	                "minecraft:diamond_block",
	                "minecraft:redstone_ore",
	                "minecraft:ice",
	                "minecraft:snow",
	                "minecraft:clay",
	                "minecraft:pumpkin",
	                "minecraft:netherrack",
	                "minecraft:soul_sand",
	                "minecraft:glowstone",
	                "minecraft:lit_pumpkin",
	                "minecraft:stained_glass",
	                "minecraft:stonebrick",
	                "minecraft:melon_block",
	                "minecraft:mycelium",
	                "minecraft:nether_brick",
	                "minecraft:end_stone",
	                "minecraft:emerald_ore",
	                "minecraft:emerald_block",
	                "minecraft:quartz_ore",
	                "minecraft:quartz_block",
	                "minecraft:stained_hardened_clay",
	                "minecraft:log2",
	                "minecraft:prismarine",
	                "minecraft:sea_lantern",
	                "minecraft:hay_block",
	                "minecraft:hardened_clay",
	                "minecraft:coal_block",
	                "minecraft:packed_ice",
	                "minecraft:red_sandstone",
	                "minecraft:purpur_block",
	                "minecraft:purpur_pillar",
	                "minecraft:end_bricks",
	                "minecraft:magma",
	                "minecraft:nether_wart_block",
	                "minecraft:red_nether_brick",
	                "minecraft:bone_block",
	                "minecraft:concrete",
	                "minecraft:concrete_powder",
	                "minecraft:leaves",
	                "minecraft:dispenser",
	                "minecraft:noteblock",
	                "minecraft:sticky_piston",
	                "minecraft:piston",
	                "minecraft:tnt",
	                "minecraft:chest",
	                "minecraft:crafting_table",
	                "minecraft:furnace",
	                "minecraft:cactus",
	                "minecraft:jukebox",
	                "minecraft:trapdoor",
	                "minecraft:iron_bars",
	                "minecraft:glass_pane",
	                "minecraft:enchanting_table",
	                "minecraft:end_portal_frame",
	                "minecraft:redstone_lamp",
	                "minecraft:ender_chest",
	                "minecraft:beacon",
	                "minecraft:anvil",
	                "minecraft:trapped_chest",
	                "minecraft:daylight_detector",
	                "minecraft:redstone_block",
	                "minecraft:hopper",
	                "minecraft:dropper",
	                "minecraft:stained_glass_pane",
	                "minecraft:leaves2",
	                "minecraft:iron_trapdoor",
	                "minecraft:slime",
	                "minecraft:end_rod",
	                "minecraft:chorus_plant",
	                "minecraft:chorus_flower",
	                "minecraft:observer",
					"minecraft:*_shulker_box",
//					"minecraft:white_shulker_box",
//					"minecraft:orange_shulker_box",
//					"minecraft:magenta_shulker_box",
//					"minecraft:light_blue_shulker_box",
//					"minecraft:yellow_shulker_box",
//					"minecraft:lime_shulker_box",
//					"minecraft:pink_shulker_box",
//					"minecraft:gray_shulker_box",
//					"minecraft:silver_shulker_box",
//					"minecraft:cyan_shulker_box",
//					"minecraft:purple_shulker_box",
//					"minecraft:blue_shulker_box",
//					"minecraft:brown_shulker_box",
//					"minecraft:green_shulker_box",
//					"minecraft:red_shulker_box",
//					"minecraft:black_shulker_box",
					"minecraft:*_glazed_terracotta",
//	                "minecraft:white_glazed_terracotta",
//	                "minecraft:orange_glazed_terracotta",
//	                "minecraft:magenta_glazed_terracotta",
//	                "minecraft:light_blue_glazed_terracotta",
//	                "minecraft:yellow_glazed_terracotta",
//	                "minecraft:lime_glazed_terracotta",
//	                "minecraft:pink_glazed_terracotta",
//	                "minecraft:gray_glazed_terracotta",
//	                "minecraft:silver_glazed_terracotta",
//	                "minecraft:cyan_glazed_terracotta",
//	                "minecraft:purple_glazed_terracotta",
//	                "minecraft:blue_glazed_terracotta",
//	                "minecraft:brown_glazed_terracotta",
//	                "minecraft:green_glazed_terracotta",
//	                "minecraft:red_glazed_terracotta",
//	                "minecraft:black_glazed_terracotta",
	                "minecraft:sign",
	                "minecraft:wooden_door",
	                "minecraft:iron_door",
	                "minecraft:bed",
	                "minecraft:flower_pot",
	                "minecraft:skull",
	                "minecraft:armor_stand",
	                "minecraft:spruce_door",
	                "minecraft:birch_door",
	                "minecraft:jungle_door",
	                "minecraft:dark_oak_door",
	                "minecraft:acacia_door"
		};

		@Name("07. Climbing Whitelist")
		@LangKey(PREFIX + ".climbing.whitelist")
		public boolean whitelistClimbables = true;

		@Config.Comment("If the player can Mount Entities at all")
		@Name("08. Able to Mount Entities")
		@LangKey(PREFIX + ".mount.enabled")
		public boolean canMount = true;

		@Config.Comment("If the player can Control Boats")
		@Name("09. Can Control Boats")
		@LangKey(PREFIX + ".mount.boat.control")
		public boolean canControlBoats = false;

		@Config.Comment("Should the Mount Blacklist to a Whitelist")
		@Name("10. Toggle Whitelist")
		@LangKey(PREFIX + ".mount.whitelist")
		public boolean whitelist = false;

		@Config.Comment("Entities that can not be mounted, or can only be mounted")
		@Name("11. Mount Blacklist")
		@LangKey(PREFIX + ".mount.blacklist")
		public String[] mountBlacklist = new String[] {
				"quark:seat",
				"sit:entity_sit"
		};

		@Config.Comment("Mana Cost per second while flying")
		@Name("12. Flight Cost")
		@LangKey(PREFIX + ".flight.cost")
		public float flight_cost = 0F;

		@Name("Compatability Settings")
		@LangKey(Reference.MODID + ".config.compatability")
		private final Compatability compat = new Compatability();
		public class Compatability {

			@Name("Tough as Nails Compatability")
			@LangKey(Reference.MODID + ".config.toughasnails")
			private final TANCompat tan = new TANCompat();
			public class TANCompat {

			}
		}

		@Name("Size")
		@LangKey(Reference.MODID + ".config.race.size")
		public final RaceSizeConfig size = new RaceSizeConfig(25, 25);

		@Config.Comment({"For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
		@Name("Attributes")
		@LangKey(Reference.MODID + ".config.attributes")
		public String[] attributes = {
				"Name:generic.maxHealth, Amount:-0.6, Operation:2",
				"Name:generic.knockbackResistance; Amount:0; Operation:0",
				"Name:generic.movementSpeed, Amount:-0.25, Operation:2",
				"Name:generic.attackDamage, Amount:-0.75, Operation:2",
				"Name:generic.attackSpeed, Amount:0, Operation:0",
				"Name:generic.armor, Amount:-0.5, Operation:2",
				"Name:generic.armorToughness, Amount:-0.25, Operation:2",
				"Name:generic.luck, Amount:0, Operation:0",
				"Name:generic.reachDistance, Amount:-0.35, Operation:2",
				"Name:forge.swimSpeed, Amount:-0.25, Operation:2",
				"Name:xat.entityMagic.regen, Amount:0, Operation:0",
				"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
				"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
				"Name:xat.jump, Amount:-0.25, Operation:2",
				"Name:xat.stepheight, Amount:-0.35, Operation:0",
				"Name:xat.flyspeed, Amount:-0.6, Operation:2"
		};
}
