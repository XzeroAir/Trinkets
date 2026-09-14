package xzeroair.trinkets.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.entityRenders.RenderAlphaWolf;
import xzeroair.trinkets.client.entityRenders.RenderAreaEffect;
import xzeroair.trinkets.client.entityRenders.RenderRangedAttack;
import xzeroair.trinkets.client.entityRenders.RenderSummonWrapper;
import xzeroair.trinkets.entity.EntityAlphaWolf;
import xzeroair.trinkets.entity.EntityAreaEffect;
import xzeroair.trinkets.entity.EntityRangedAttack;
import xzeroair.trinkets.entity.EntitySummonWrapper;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsRegistryNames;

public class ModEntities {

    public static void registerEntities() {
        registerEntity(TrinketsRegistryNames.ModEntities.ALPHA_WOLF, EntityAlphaWolf.class, 120, 50);
        registerEntity(TrinketsRegistryNames.ModEntities.DRAGON_BREATH, EntityRangedAttack.class, 121, 50);
        registerEntity(TrinketsRegistryNames.ModEntities.AREA_EFFECT, EntityAreaEffect.class, 122, 50);
        registerEntity(TrinketsRegistryNames.ModEntities.ENTITY_WRAPPER, EntitySummonWrapper.class, 123, 80);
        //		EntityEntryBuilder.create().
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Trinkets.instance, range, 1, true, color1, color2);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Trinkets.instance, range, 1, true);
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityAlphaWolf.class, RenderAlphaWolf.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityRangedAttack.class, RenderRangedAttack.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityAreaEffect.class, RenderAreaEffect.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntitySummonWrapper.class, RenderSummonWrapper.FACTORY);
    }

}
