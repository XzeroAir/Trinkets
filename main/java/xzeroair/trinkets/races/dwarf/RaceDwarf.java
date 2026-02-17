package xzeroair.trinkets.races.dwarf;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.dwarf.RaceDwarfRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.AbilitySkilledMiner;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;

import javax.annotation.Nonnull;

public class RaceDwarf extends EntityRacePropertiesHandler {

    public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

    public RaceDwarf(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.dwarf);
    }

    public RaceDwarf(EntityLivingBase e, Element element) {
        super(e, EntityRaces.dwarf, element);
    }

    @Override
    public void startTransformation() {
        //		if (serverConfig.fortune) {
        //			this.addAbility(new AbilityPsudoFortune());
        //		}
//        if (serverConfig.skilled_miner) {
        this.addAbility(new AbilitySkilledMiner());
//        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceDwarfRenderer(entity, this);
        }
        return RendererRace;
    }
}
