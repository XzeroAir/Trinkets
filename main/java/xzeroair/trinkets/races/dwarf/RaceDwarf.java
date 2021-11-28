package xzeroair.trinkets.races.dwarf;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.AbilityPsudoFortune;
import xzeroair.trinkets.traits.abilities.AbilitySkilledMiner;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceDwarf extends EntityRacePropertiesHandler {

	public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

	public RaceDwarf(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.dwarf);
	}

	@Override
	public void startTransformation() {
		if (serverConfig.fortune) {
			this.addAbility(Abilities.psudoFortune, new AbilityPsudoFortune());
		}
		this.addAbility(Abilities.skilledMiner, new AbilitySkilledMiner());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		if (entity.isRiding()) {
			double t = 0;
			t = (100 - properties.getSize()) * 0.01D;
			final double t1 = (entity.height * 100) / (1.8 * 100);
			final double t2 = (1.8 * 100) / (entity.height * 100);
			t *= t2 * 0.5D;
			GlStateManager.translate(0, t, 0);
		}
	}

}
