package xzeroair.trinkets.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import xzeroair.trinkets.particles.ParticleGreed;
import xzeroair.trinkets.particles.ParticleHeartBeat;

public class ClientProxy extends CommonProxy {
	
	Minecraft MINECRAFT = Minecraft.getMinecraft();
	@Override
	public void spawnParticle(EnumParticleTypes Particle, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int i) {
		
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleGreed(MINECRAFT.world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed));
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleHeartBeat(MINECRAFT.world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed));
	}
		
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
		
	}
}
