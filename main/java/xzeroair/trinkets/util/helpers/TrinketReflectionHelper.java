package xzeroair.trinkets.util.helpers;

import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class TrinketReflectionHelper {

	public static final Method ENTITY_SETSIZE = ObfuscationReflectionHelper.findMethod(Entity.class, "func_70105_a", void.class, float.class, float.class);

	public static List<LootPool> getPools(LootTable table) {
		// net.minecraft.world.storage.loot.LootTable field_186466_c # pools
		return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "pools");//"field_186466_c");
	}

	public static List<LootEntry> getLootEntries(LootPool pool) {
		// net.minecraft.world.storage.loot.LootPool field_186453_a # lootEntries
		return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "lootEntries");//"field_186453_a");
	}

}
