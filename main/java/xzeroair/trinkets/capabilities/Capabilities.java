package xzeroair.trinkets.capabilities;

import java.util.concurrent.Callable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerStorage;
import xzeroair.trinkets.capabilities.TileEntityCap.ManaEssenceProperties;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.container.TrinketContainerHandler;

public class Capabilities {

	public static void init() {
		CapabilityManager.INSTANCE.register(ITrinketContainerHandler.class, TrinketContainerStorage.storage, new TrinketContainerFactory());

		CapabilityManager.INSTANCE.register(EntityProperties.class, new Capability.IStorage<EntityProperties>() {
			@Override
			public NBTBase writeNBT(Capability<EntityProperties> capability, EntityProperties instance, EnumFacing side) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void readNBT(Capability<EntityProperties> capability, EntityProperties instance, EnumFacing side, NBTBase nbt) {
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

		CapabilityManager.INSTANCE.register(VipStatus.class, new Capability.IStorage<VipStatus>() {
			@Override
			public NBTBase writeNBT(Capability<VipStatus> capability, VipStatus instance, EnumFacing side) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void readNBT(Capability<VipStatus> capability, VipStatus instance, EnumFacing side, NBTBase nbt) {
				throw new UnsupportedOperationException();
			}
		}, () -> null);

		CapabilityManager.INSTANCE.register(ManaEssenceProperties.class, new Capability.IStorage<ManaEssenceProperties>() {
			@Override
			public NBTBase writeNBT(Capability<ManaEssenceProperties> capability, ManaEssenceProperties instance, EnumFacing side) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void readNBT(Capability<ManaEssenceProperties> capability, ManaEssenceProperties instance, EnumFacing side, NBTBase nbt) {
				throw new UnsupportedOperationException();
			}
		}, () -> null);
	}

	@CapabilityInject(EntityProperties.class)
	public static Capability<EntityProperties> ENTITY_RACE;

	@CapabilityInject(TrinketProperties.class)
	public static Capability<TrinketProperties> ITEM_TRINKET;

	@CapabilityInject(VipStatus.class)
	public static Capability<VipStatus> VIP_STATUS;

	@CapabilityInject(ManaEssenceProperties.class)
	public static Capability<ManaEssenceProperties> TILE_ENTITY_MANA_ESSENCE;

	public static ManaEssenceProperties getTileEntityManaProperties(TileEntity te) {
		return te.getCapability(TILE_ENTITY_MANA_ESSENCE, null);
	}

	public static EntityProperties getEntityRace(EntityLivingBase entity) {
		return entity.getCapability(ENTITY_RACE, null);
	}

	public static TrinketProperties getTrinketProperties(ItemStack stack) {
		return stack.getCapability(ITEM_TRINKET, null);
	}

	public static VipStatus getVipStatus(EntityLivingBase player) {
		return player.getCapability(VIP_STATUS, null);
	}

	private static class TrinketContainerFactory implements Callable<ITrinketContainerHandler> {
		@Override
		public ITrinketContainerHandler call() throws Exception {
			return new TrinketContainerHandler();
		}
	}
}
