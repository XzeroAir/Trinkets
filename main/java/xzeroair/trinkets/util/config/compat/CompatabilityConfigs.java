package xzeroair.trinkets.util.config.compat;

import net.minecraftforge.common.config.Config;

public class CompatabilityConfigs {

	@Config.RequiresMcRestart
	@Config.Comment({
			"Should Items that implement IBauble be allowed in the Trinkets Gui",
			"WARNING.",
			"This is Very Experimental, Baubles equipped might only partially work, not at all, or Potentially cause a crash."
	})
	@Config.Name("Baubles")
	public boolean baubles = false;

	@Config.Comment("Should Trinkets & Baubles Items only work in the T&B Container")
	@Config.Name("Accessories Use T&B Container Only")
	public boolean xatItemsInTrinketGuiOnly = false;

	//	@Config.RequiresMcRestart
	@Config.RequiresWorldRestart
	@Config.Comment("Should Trinkets and Baubles use ArtemisLib to handle Height Adjustments to the player")
	@Config.Name("ArtemisLib")
	public boolean artemislib = false;

	@Config.RequiresMcRestart
	@Config.Name("ElenaiDodge")
	public boolean elenaiDodge = true;

	//	@Config.RequiresMcRestart
	//	@Config.Comment("Should Trinkets and Baubles Interact with First Aid")
	//	@Config.Name("First Aid")
	//	public boolean firstaid = true;

	//	@Config.RequiresMcRestart
	@Config.RequiresWorldRestart
	@Config.Comment("Should Trinkets and Baubles Interact with Tough as Nails")
	@Config.Name("Tough as Nails")
	public boolean toughasnails = true;

	//	@Config.RequiresMcRestart
	@Config.RequiresWorldRestart
	@Config.Comment("Should Trinkets and Baubles Interact with Simple Difficulty")
	@Config.Name("Simple Difficulty")
	public boolean simpledifficulty = true;

	//	@Config.RequiresMcRestart
	//	@Config.Comment("Should Trinkets and Baubles prevent morphing while transformed")
	//	@Config.Name("Morph")
	//	public boolean morph = true;

	//	@Config.RequiresMcRestart
	//	@Config.Comment("Should Trinkets and Baubles not have an emc value")
	//	@Config.Name("ProjectE")
	//	public boolean projecte = true;

	@Config.RequiresWorldRestart
	@Config.Comment("Should Trinkets and Baubles Interact with Lycanites Mobs")
	@Config.Name("Lycanites Mobs")
	public boolean lycanites = true;

	@Config.RequiresWorldRestart
	@Config.Comment("Should Trinkets and Baubles Interact with Defiled Lands")
	@Config.Name("Defiled Lands")
	public boolean defiledlands = true;

	//	@Config.RequiresMcRestart
	@Config.RequiresWorldRestart
	@Config.Comment("Should Trinkets and Baubles prevent some of the Visuals in Enhanced Visuals")
	@Config.Name("Enhanced Visuals")
	public boolean enhancedvisuals = true;

	//	@Config.RequiresMcRestart
	@Config.RequiresWorldRestart
	@Config.Comment("Should Trinkets and Baubles Stone of the Sea work Infinite Oxygen work with Better Diving")
	@Config.Name("Better Diving")
	public boolean betterdiving = true;

}
