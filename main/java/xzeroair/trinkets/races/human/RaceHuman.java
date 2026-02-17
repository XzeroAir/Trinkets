package xzeroair.trinkets.races.human;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.human.RaceHumanRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EmptyHandler;
import xzeroair.trinkets.traits.elements.Element;

import javax.annotation.Nonnull;

public class RaceHuman extends EmptyHandler {

    public RaceHuman(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.human);
    }

    public RaceHuman(@Nonnull EntityLivingBase e, Element element) {
        super(e, EntityRaces.human, element);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceHumanRenderer(entity, this);
        }
        return RendererRace;
    }

}
