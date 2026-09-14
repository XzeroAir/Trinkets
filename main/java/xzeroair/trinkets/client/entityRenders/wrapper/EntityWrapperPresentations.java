package xzeroair.trinkets.client.entityRenders.wrapper;

import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class EntityWrapperPresentations {

    private static final ResourceLocation SLIME_SUMMON = new ResourceLocation(Reference.MODID, "slime_hop");
    private static final Map<ResourceLocation, EntityWrapperPresentation> PRESENTATIONS = new HashMap<>();

    static {
        PRESENTATIONS.put(SLIME_SUMMON, new SlimeSummonPresentation());
    }

    private EntityWrapperPresentations() {
    }

    @Nullable
    public static EntityWrapperPresentation get(@Nullable ResourceLocation templateId) {
        return templateId == null ? null : PRESENTATIONS.get(templateId);
    }
}
