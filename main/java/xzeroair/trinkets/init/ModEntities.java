package xzeroair.trinkets.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.entityRenders.RenderAlphaWolf;
import xzeroair.trinkets.client.entityRenders.RenderThrownProjectile;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.entity.MovingThrownProjectile;
import xzeroair.trinkets.util.Reference;

public class ModEntities {

	public static void registerEntities() {
		registerEntity("AlphaWolf", AlphaWolf.class, 120, 50);
		registerEntity("DragonBreath", MovingThrownProjectile.class, 121, 50);
		//		EntityEntryBuilder.create().
	}

	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2) {
		EntityRegistry.registerModEntity(
				new ResourceLocation(Reference.MODID + ":" + name), entity,
				name, id, Trinkets.instance, range, 1, true, color1, color2
		);
	}

	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
		EntityRegistry.registerModEntity(
				new ResourceLocation(Reference.MODID + ":" + name), entity,
				name, id, Trinkets.instance, range, 1, true
		);
	}

	@SideOnly(Side.CLIENT)
	public static void registerEntityRenders() {
		RenderingRegistry.registerEntityRenderingHandler(AlphaWolf.class, RenderAlphaWolf.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(MovingThrownProjectile.class, RenderThrownProjectile.FACTORY);
	}

}
