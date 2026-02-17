package xzeroair.trinkets.client.races.human;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.client.races.RaceEmptyRenderer;
import xzeroair.trinkets.races.human.RaceHuman;

public class RaceHumanRenderer extends RaceEmptyRenderer<RaceHumanRenderer, RaceHuman> {
    public RaceHumanRenderer(EntityLivingBase entity, RaceHuman raceHuman) {
        super(entity, raceHuman);
    }
}