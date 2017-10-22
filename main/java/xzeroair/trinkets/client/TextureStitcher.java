package xzeroair.trinkets.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.util.Reference;

public class TextureStitcher
{
	@SubscribeEvent
	public void textureStitchEventPre(TextureStitchEvent.Pre event)
	{
		ResourceLocation particle_default = new ResourceLocation(Reference.MODID,"textures/particle/default");
		ResourceLocation greed = new ResourceLocation(Reference.MODID, "textures/particle/greed");
		ResourceLocation heartbeat = new ResourceLocation("textures/particle/heartbeat");
		
		event.getMap().registerSprite(particle_default);
		event.getMap().registerSprite(greed);
		event.getMap().registerSprite(heartbeat);
	}
}