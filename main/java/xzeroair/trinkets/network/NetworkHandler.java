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

		INSTANCE.registerMessage(CapDataToServerHandler.class, CapDataMessage.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(CapDataToClientHandler.class, CapDataMessage.class, nextId(), Side.CLIENT);

		INSTANCE.registerMessage(MobCapDataToClientHandler.class, MobCapDataMessage.class, nextId(), Side.CLIENT);
		INSTANCE.registerMessage(MobCapDataToServerHandler.class, MobCapDataMessage.class, nextId(), Side.SERVER);

		INSTANCE.registerMessage(ItemCapDataToServerHandler.class, ItemCapDataMessage.class, nextId(), Side.SERVER);

		INSTANCE.registerMessage(PacketBaubleSync.Handler.class, PacketBaubleSync.class, nextId(), Side.CLIENT);

	}
}
