package xzeroair.trinkets.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.util.Reference;

public class NetworkHandler {

	public static SimpleNetworkWrapper INSTANCE;

	private static int ID = 0;

	private static int nextId() {
		return ID++;
	}

	public static void init(){
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

		INSTANCE.registerMessage(CapDataMessage.Handler.class, CapDataMessage.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(CapDataMessage.Handler.class, CapDataMessage.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(MobCapDataMessage.Handler.class, MobCapDataMessage.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(MobCapDataMessage.Handler.class, MobCapDataMessage.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(ItemCapDataMessage.Handler.class, ItemCapDataMessage.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(ItemCapDataMessage.Handler.class, ItemCapDataMessage.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(PacketBaubleSync.Handler.class, PacketBaubleSync.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(PacketBaubleSync.Handler.class, PacketBaubleSync.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(PacketConfigSync.Handler.class, PacketConfigSync.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(PacketConfigSync.Handler.class, PacketConfigSync.class, nextId(), Side.CLIENT);

	}
}
