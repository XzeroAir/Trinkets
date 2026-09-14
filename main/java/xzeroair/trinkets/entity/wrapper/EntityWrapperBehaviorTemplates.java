package xzeroair.trinkets.entity.wrapper;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class EntityWrapperBehaviorTemplates {

    private static final Map<ResourceLocation, EntityWrapperBehaviorTemplate> TEMPLATES = new HashMap<>();

    static {
        register(TemplateSlimeSummon.INSTANCE);
    }

    private EntityWrapperBehaviorTemplates() {
    }

    private static void register(EntityWrapperBehaviorTemplate template) {
        TEMPLATES.put(template.getId(), template);
    }

    @Nullable
    public static EntityWrapperBehaviorTemplate get(ResourceLocation id) {
        return TEMPLATES.get(id);
    }
}
