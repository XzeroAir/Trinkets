package xzeroair.trinkets.client.races;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.races.EmptyHandler;

public class RaceEmptyRenderer<T extends RaceEmptyRenderer, H extends EmptyHandler> extends RaceDefaultRenderer<T, H> {

    public RaceEmptyRenderer(EntityLivingBase entity, H emptyHandler) {
        super(entity, emptyHandler);
    }

}
