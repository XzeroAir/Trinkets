package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.entity.camera.EntityCamera;
import xzeroair.trinkets.util.Reference;

public class ModEntities {

	public static final List<Entity> Entity = new ArrayList<>();

	public static final Map<Integer, EntityEggInfo> entityEggs = Maps.<Integer, EntityEggInfo>newLinkedHashMap();
	public static final Map<Integer, String> idToBEEntityName = Maps.<Integer, String>newLinkedHashMap();

	private static int nextEntityId = 1;

	public static void init() {
		registerEntity("camera", EntityCamera.class, 64, 3, true);
	}

	// register an entity
	public static int registerEntity(String name, Class<? extends Entity> entity, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates) {

		int EntityId = nextEntityId;
		nextEntityId++;
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, EntityId,
				Main.instance, trackingRange, updateFrequency, true);
		idToBEEntityName.put(EntityId, name);
		return EntityId;
	}

	public static Entity createEntityByID(int tanEntityId, World worldIn) {
		Entity entity = null;
		ModContainer mc = FMLCommonHandler.instance().findContainerFor(Main.instance);
		EntityRegistration er = EntityRegistry.instance().lookupModSpawn(mc, tanEntityId);
		if (er != null) {
			Class<? extends Entity> clazz = er.getEntityClass();
			try {
				if (clazz != null) {
					entity = clazz.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		if (entity == null) {
		}
		return entity;
	}
}
