package xzeroair.trinkets.capabilities;

import java.util.concurrent.Callable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerStorage;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.manaCap.ManaStats;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.container.TrinketContainerHandler;

public class Capabilities {

	public static void init() {
		CapabilityManager.INSTANCE.register(ITrinketContainerHandler.class, TrinketContainerStorage.storage, new TrinketContainerFactory());

		CapabilityManager.INSTANCE.register(ManaStats.class, new Capability.IStorage<ManaStats>() {
			@Override
			public NBTBase writeNBT(Capability<ManaStats> capability, ManaStats instance, EnumFacing side) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void readNBT(Capability<ManaStats> capability, ManaStats instance, EnumFacing side, NBTBase nbt) {
				throw new UnsupportedOperationException();
			}
		}, () -> null);

		CapabilityManager.INSTANCE.register(RaceProperties.class, new Capability.IStorage<RaceProperties>() {
			@Override
			public NBTBase writeNBT(Capability<RaceProperties> capability, RaceProperties instance, EnumFacing side) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void readNBT(Capability<RaceProperties> capability, RaceProperties instance, EnumFacing side, NBTBase nbt) {
				throw new UnsupportedOperationException();
			}
		}, () -> null);

		CapabilityManager.INSTANCE.register(TrinketProperties.class, new Capability.IStorage<TrinketProperties>() {
			@Override
			public NBTBase writeNBT(Capability<TrinketProperties> capability, TrinketProperties instance, EnumFacing side) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void readNBT(Capability<TrinketProperties> capability, TrinketProperties instance, EnumFacing side, NBTBase nbt) {
				throw new UnsupportedOperationException();
			}
		}, () -> null);
	}

	@CapabilityInject(ManaStats.class)
	public static Capability<ManaStats> PLAYER_MANA;

	@CapabilityInject(RaceProperties.class)
	public static Capability<RaceProperties> ENTITY_RACE;

	@CapabilityInject(TrinketProperties.class)
	public static Capability<TrinketProperties> ITEM_TRINKET;

	public static RaceProperties getEntityRace(EntityLivingBase entity) {
		return entity.getCapability(ENTITY_RACE, null);
	}

	public static ManaStats getPlayerMana(EntityPlayer player) {
		return player.getCapability(PLAYER_MANA, null);
	}

	public static TrinketProperties getTrinketProperties(ItemStack stack) {
		return stack.getCapability(ITEM_TRINKET, null);
	}

	private static class TrinketContainerFactory implements Callable<ITrinketContainerHandler> {
		@Override
		public ITrinketContainerHandler call() throws Exception {
			return new TrinketContainerHandler();
		}
	}
}
