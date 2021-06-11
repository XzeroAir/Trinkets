package xzeroair.trinkets.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.network.arcingorb.ArcingOrbAttackPacket;
import xzeroair.trinkets.network.arcingorb.ArcingOrbDodgePacket;
import xzeroair.trinkets.network.configsync.BlocklistSyncPacket;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.network.keybinds.KeyPressPacket;
import xzeroair.trinkets.network.mana.SyncManaCostToHudPacket;
import xzeroair.trinkets.network.mana.SyncManaHudPacket;
import xzeroair.trinkets.network.mana.SyncManaStatsPacket;
import xzeroair.trinkets.network.particles.LightningBoltPacket;
import xzeroair.trinkets.network.particles.LightningOrbPacket;
import xzeroair.trinkets.network.status.CombineStatusEffectPacket;
import xzeroair.trinkets.network.status.StatusEffectPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenDefaultInventory;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.network.vip.VipStatusPacket;
import xzeroair.trinkets.util.Reference;

public class NetworkHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID.toLowerCase());

	private static int ID = 0;

	private static int nextId() {
		return ID++;
	}

	/**
	 * Side.Client means sending to the Client Side.Server means sending to the
	 * Server
	 */
	public static void init() {

		INSTANCE.registerMessage(SizeDataPacket.Handler.class, SizeDataPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(SizeDataPacket.Handler.class, SizeDataPacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(ItemCapDataMessage.Handler.class, ItemCapDataMessage.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(ItemCapDataMessage.Handler.class, ItemCapDataMessage.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(PacketAccessorySync.Handler.class, PacketAccessorySync.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(PacketAccessorySync.Handler.class, PacketAccessorySync.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(PacketConfigSync.Handler.class, PacketConfigSync.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(PacketConfigSync.Handler.class, PacketConfigSync.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(BlocklistSyncPacket.Handler.class, BlocklistSyncPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(BlocklistSyncPacket.Handler.class, BlocklistSyncPacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(PolarizedStoneSyncPacket.Handler.class, PolarizedStoneSyncPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(PolarizedStoneSyncPacket.Handler.class, PolarizedStoneSyncPacket.class, nextId(), Side.CLIENT);

		//Handle VIP
		INSTANCE.registerMessage(VipStatusPacket.Handler.class, VipStatusPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(VipStatusPacket.Handler.class, VipStatusPacket.class, nextId(), Side.CLIENT);

		//Handle Health
		INSTANCE.registerMessage(HealthUpdatePacket.Handler.class, HealthUpdatePacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(HealthUpdatePacket.Handler.class, HealthUpdatePacket.class, nextId(), Side.CLIENT);

		//Handle Mana
		INSTANCE.registerMessage(SyncManaHudPacket.Handler.class, SyncManaHudPacket.class, nextId(), Side.CLIENT);
		INSTANCE.registerMessage(SyncManaCostToHudPacket.Handler.class, SyncManaCostToHudPacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(SyncManaStatsPacket.Handler.class, SyncManaStatsPacket.class, nextId(), Side.CLIENT);
		INSTANCE.registerMessage(SyncManaStatsPacket.Handler.class, SyncManaStatsPacket.class, nextId(), Side.SERVER);

		//Handle Trinkets Container
		INSTANCE.registerMessage(OpenTrinketGui.class, OpenTrinketGui.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(MoveHudMessage.class, MoveHudMessage.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(OpenDefaultInventory.class, OpenDefaultInventory.class, nextId(), Side.SERVER);

		//RACES
		INSTANCE.registerMessage(KeyPressPacket.class, KeyPressPacket.class, nextId(), Side.SERVER);

		//Handle Titan Reach
		INSTANCE.registerMessage(IncreasedAttackRangePacket.Handler.class, IncreasedAttackRangePacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(IncreasedReachPacket.Handler.class, IncreasedReachPacket.class, nextId(), Side.SERVER);

		INSTANCE.registerMessage(AlphaWolfPacket.Handler.class, AlphaWolfPacket.class, nextId(), Side.SERVER);

		INSTANCE.registerMessage(StatusEffectPacket.Handler.class, StatusEffectPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(CombineStatusEffectPacket.Handler.class, CombineStatusEffectPacket.class, nextId(), Side.SERVER);

		INSTANCE.registerMessage(ArcingOrbAttackPacket.Handler.class, ArcingOrbAttackPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(ArcingOrbAttackPacket.Handler.class, ArcingOrbAttackPacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(ArcingOrbDodgePacket.Handler.class, ArcingOrbDodgePacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(ArcingOrbDodgePacket.Handler.class, ArcingOrbDodgePacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(LightningBoltPacket.Handler.class, LightningBoltPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(LightningBoltPacket.Handler.class, LightningBoltPacket.class, nextId(), Side.CLIENT);
		INSTANCE.registerMessage(LightningOrbPacket.Handler.class, LightningOrbPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(LightningOrbPacket.Handler.class, LightningOrbPacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(AlphaWolfAttackPacket.Handler.class, AlphaWolfAttackPacket.class, nextId(), Side.SERVER);

		INSTANCE.registerMessage(UpdateThrowable.Handler.class, UpdateThrowable.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(UpdateThrowable.Handler.class, UpdateThrowable.class, nextId(), Side.CLIENT);

	}
}
