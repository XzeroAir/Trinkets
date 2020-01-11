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

	@Config.RequiresMcRestart
	@Config.Comment("Should Trinkets and Baubles use ArtemisLib to handle Height Adjustments to the player")
	@Config.Name("ArtemisLib")
	public boolean artemislib = true;

	//	@Config.RequiresMcRestart
	//	@Config.Comment("Should Trinkets and Baubles Interact with First Aid")
	//	@Config.Name("First Aid")
	//	public boolean firstaid = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should Trinkets and Baubles Interact with Tough as Nails")
	@Config.Name("Tough as Nails")
	public boolean toughasnails = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should Trinkets and Baubles prevent morphing while transformed")
	@Config.Name("Morph")
	public boolean morph = true;

	//	@Config.RequiresMcRestart
	//	@Config.Comment("Should Trinkets and Baubles not have an emc value")
	//	@Config.Name("ProjectE")
	//	public boolean projecte = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should Trinkets and Baubles prevent some of the Visuals in Enhanced Visuals")
	@Config.Name("Enhanced Visuals")
	public boolean enhancedvisuals = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should Trinkets and Baubles Stone of the Sea work Infinite Oxygen work with Better Diving")
	@Config.Name("Better Diving")
	public boolean betterdiving = true;

}
