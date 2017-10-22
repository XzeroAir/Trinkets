package xzeroair.trinkets.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import xzeroair.trinkets.particles.ParticleGreed;

@EventBusSubscriber
public class CommonProxy {
	
	public void spawnParticle(EnumParticleTypes Particle, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int i) {

	}
	
	public void registerItemRenderer(Item item, int meta, String id) {
		
	}
	
}
