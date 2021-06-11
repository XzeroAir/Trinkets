package xzeroair.trinkets.proxy;

import java.util.Map;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.gui.TrinketGui;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.ParticleFireBreath;
import xzeroair.trinkets.client.particles.ParticleLightning;
import xzeroair.trinkets.client.particles.ParticleLightningOrb;
import xzeroair.trinkets.client.renderLayers.TrinketsRenderLayer;
import xzeroair.trinkets.util.registry.EventRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		ModKeyBindings.init();
		super.preInit(e);
		EventRegistry.clientPreInit();
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
		EventRegistry.clientInit();
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		EventRegistry.clientPostInit();
	}

	@Override
	public void spawnParticle(int effectID, World world, double x, double y, double z, double motX, double motY, double motZ, int color, float alpha) {
		Particle effect = null;
		if (effectID == 3) {
			effect = new ParticleFireBreath(world, x, y, z, 0.0D, 0.0D, 0.0D, color, alpha);//, color, alpha);
			effect.setParticleTextureIndex((int) alpha);
			//			effect = new ParticleFireBreath(world, x, y, z, motX, motY, motZ, color, 0.25F);//, color, alpha);
		} else {

		}
		if (effect != null) {
			Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		}
	}

	@Override
	public void renderEffect(int effectID, World world, double x, double y, double z, double x2, double y2, double z2, int color, float alpha, float intensity) {
		Particle effect = null;
		if (effectID == 1) {
			effect = new ParticleLightning(world, x, y, z, x2, y2, z2, color, alpha, false, intensity);
		} else if (effectID == 2) {
			effect = new ParticleLightningOrb(world, x, y, z, x2, y2, z2, color, alpha, false, intensity);
		} else if (effectID == 3) {
			//			effect = new ParticleFireBreath(world, x, y, z, x2, y2, z2, color, alpha, false, intensity);
		} else {

		}
		if (effect != null) {
			Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		}
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString(), id));
	}

	@Override
	public IThreadListener getThreadListener(final MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft();
		} else {
			return context.getServerHandler().player.getServerWorld();
		}
	}

	@Override
	public EntityPlayer getPlayer(final MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft().player;
		} else {
			return context.getServerHandler().player;
		}
	}

	@Override
	@Nullable
	public EntityLivingBase getEntityLivingBase(MessageContext context, int entityID) {
		final EntityPlayer player = context.side.isClient() ? Minecraft.getMinecraft().player : context.getServerHandler().player;
		final Entity entity = player.world.getEntityByID(entityID);
		return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (world instanceof WorldClient) {
			switch (ID) {
			case Trinkets.GUI:
				return new TrinketGui(player);
			case 1:
				return new ManaHud();
			case 2:
				return new GuiEntityProperties(player);
			}
		}
		return null;
	}
}
