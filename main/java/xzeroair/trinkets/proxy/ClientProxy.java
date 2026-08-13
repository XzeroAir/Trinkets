package xzeroair.trinkets.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.events.*;
import xzeroair.trinkets.client.gui.TrinketGui;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiAttributesScreen;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiRaceSelectionScreen;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.*;
import xzeroair.trinkets.client.renderLayers.TrinketsRenderLayer;
import xzeroair.trinkets.init.ModEntities;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsRenderEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class ClientProxy extends CommonProxy {

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        ModKeyBindings.init();
        super.preInit(e);
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
        ModEntities.registerEntityRenders();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        final Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        RenderPlayer render;
        render = skinMap.get("default");
        render.addLayer(new TrinketsRenderLayer(false, render));

        render = skinMap.get("slim");
        render.addLayer(new TrinketsRenderLayer(true, render));
        super.init(e);
        MinecraftForge.EVENT_BUS.register(ScreenOverlayEvents.instance);
        MinecraftForge.EVENT_BUS.register(new GuiScreenEvents());
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
        MinecraftForge.EVENT_BUS.register(new RenderEntitiesEvent());
        MinecraftForge.EVENT_BUS.register(new PlayerCameraSetupEvents());

        if (Trinkets.MOD_COMPAT.EnhancedVisuals) {
            MinecraftForge.EVENT_BUS.register(EnhancedVisualsRenderEvent.instance);
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public void renderEffect(int effectID, World world, double x, double y, double z, double x2, double y2, double z2, int color, float alpha, float intensity) {
        Particle effect = null;
        final Minecraft mc = Minecraft.getMinecraft();
        if (effectID == 1) {
            effect = new ParticleLightning(world, x, y, z, x2, y2, z2, color, alpha, false, intensity);
            mc.player.world.playSound(x, y, z, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 0.2F, 0.6F, true);
        } else if (effectID == 2) {
            effect = new ParticleLightningOrb(world, x, y, z, x2, y2, z2, color, alpha, false, intensity);
            mc.player.world.playSound(x, y, z, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 0.2F, 0.6F, true);
        } else if (effectID == 3) {
            world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, x, y, z, 0, 0, 0);
            //			effect = EnumParticleTypes.SWEEP_ATTACK;
        } else if (effectID == 4) {
            effect = new ParticleFireBreath(world, x, y, z, 0.0D, 0.0D, 0.0D, color, alpha);//, color, alpha);
//            effect.setParticleTextureIndex((int) alpha);
            //			world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0, 0);
        } else if (effectID == 5) {
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x, y, z, 0, 0, 0);
            mc.player.world.playSound(x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F)) * 0.7F, true);
        } else if (effectID == 6) {
            final double X = Reference.random.nextDouble() + x;
            final double Y = Reference.random.nextDouble() + y;
            final double Z = Reference.random.nextDouble() + z;
            effect = new ParticleGreed(world, new Vec3d(X, Y, Z), color, 1F, false);
        } else if (effectID == 7) {
            effect = new ParticleLightningOrb(world, x, y, z, x2, y2, z2, color, alpha, false, intensity);
        } else if (effectID == 8) {
            effect = new ShieldAuraEffect(world, new Vec3d(x, y, z), color, alpha, false);
        }
        if (effect != null) {
            mc.effectRenderer.addEffect(effect);
        }
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString(), id));
    }

    @Override
    public IThreadListener getThreadListener(@Nonnull final MessageContext context) {
        if (context.side.isClient()) {
            return Minecraft.getMinecraft();
        } else {
            return context.getServerHandler().player.getServerWorld();
        }
    }

    @Override
    public EntityPlayer getPlayer(@Nonnull final MessageContext context) {
        if (context.side.isClient()) {
            return Minecraft.getMinecraft().player;
        } else {
            return context.getServerHandler().player;
        }
    }

    @Override
    @Nullable
    public EntityLivingBase getEntityLivingBase(@Nonnull MessageContext context, int entityID) {
        final EntityPlayer player = context.side.isClient() ? Minecraft.getMinecraft().player : context.getServerHandler().player;
        final Entity entity = player.world.getEntityByID(entityID);
        return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world instanceof WorldClient) {
            switch (ID) {
                case Reference.GUI:
                    return new TrinketGui(player);
                case Reference.GUI_MANA:
                    return new ManaHud();
                case Reference.GUI_ENTITY:
                    return new GuiEntityProperties(player);
                case Reference.GUI_ATTRIBUTES:
                    return new GuiAttributesScreen(player);
                case Reference.GUI_RACE_SELECTION:
                    return new GuiRaceSelectionScreen(player);
            }
        }
        return null;
    }
}
