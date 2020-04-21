package xzeroair.trinkets.network;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.network.configsync.BlocklistSyncPacket;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.network.trinketcontainer.OpenDefaultInventory;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.firstaid.FirstAidSyncHPPacket;

public class NetworkHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID.toLowerCase());

	private static int ID = 0;

	private static int nextId() {
		return ID++;
	}

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

		INSTANCE.registerMessage(FirstAidSyncHPPacket.Handler.class, FirstAidSyncHPPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(FirstAidSyncHPPacket.Handler.class, FirstAidSyncHPPacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(PolarizedStoneSyncPacket.Handler.class, PolarizedStoneSyncPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(PolarizedStoneSyncPacket.Handler.class, PolarizedStoneSyncPacket.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(OpenTrinketGui.class, OpenTrinketGui.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(OpenDefaultInventory.class, OpenDefaultInventory.class, nextId(), Side.SERVER);

		INSTANCE.registerMessage(IncreasedAttackRangePacket.Handler.class, IncreasedAttackRangePacket.class, nextId(), Side.SERVER);

	}

	// Item Data
	public static void sendItemDataTo(EntityLivingBase send, ItemStack stack, TrinketProperties capSend, boolean isTrinket, EntityPlayerMP recieve) {
		NetworkHandler.INSTANCE.sendTo(new ItemCapDataMessage(send, stack, capSend, isTrinket), recieve);
	}

	public static void sendItemDataTracking(EntityLivingBase entity, ItemStack stack, TrinketProperties capSend, boolean isTrinket) {
		NetworkHandler.INSTANCE.sendToAllTracking(new ItemCapDataMessage(entity, stack, capSend, isTrinket), entity);
	}

	public static void sendItemDataAll(EntityLivingBase entity, ItemStack stack, TrinketProperties capSend, boolean isTrinket) {
		NetworkHandler.INSTANCE.sendToAll(new ItemCapDataMessage(entity, stack, capSend, isTrinket));
	}

	public static void sendItemDataServer(EntityLivingBase send, ItemStack stack, TrinketProperties capSend, boolean isTrinket) {
		NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(send, stack, capSend, isTrinket));
	}

	//	// Player Data
	//	public static void sendPlayerDataTo(EntityLivingBase send, RaceProperties capSend, EntityPlayerMP recieve) {
	//		NetworkHandler.INSTANCE.sendTo(new SizeDataPacket(send, capSend), recieve);
	//	}
	//
	//	public static void sendPlayerDataTracking(EntityLivingBase entity, RaceProperties cap) {
	//		NetworkHandler.INSTANCE.sendToAllTracking(new SizeDataPacket(entity, cap), entity);
	//	}
	//
	//	// If Entity Instanceof EntityPlayerMP
	//	public static void sendPlayerDataAll(EntityLivingBase entity, RaceProperties cap) {
	//		NetworkHandler.INSTANCE.sendToAll(new SizeDataPacket(entity, cap));
	//	}
	//
	//	public static void sendPlayerDataServer(EntityLivingBase entity, RaceProperties cap) {
	//		NetworkHandler.INSTANCE.sendToServer(new SizeDataPacket(entity, cap));
	//	}
}
