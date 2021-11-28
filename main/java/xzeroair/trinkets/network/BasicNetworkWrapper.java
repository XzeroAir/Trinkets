package xzeroair.trinkets.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BasicNetworkWrapper {

	public final SimpleNetworkWrapper network;
	protected final PacketHandler handler;
	private int id = 0;

	public BasicNetworkWrapper(String channelName) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		handler = new PacketHandler();
	}

	/**
	 * Packet will be received on both client and server side.
	 */
	public void registerPacket(Class<? extends BasicPacket> packetClazz) {
		this.registerPacketClient(packetClazz);
		this.registerPacketServer(packetClazz);
	}

	public void registerPacketClient(Class<? extends BasicPacket> packetClazz) {
		this.registerPacketImpl(packetClazz, Side.CLIENT);
	}

	public void registerPacketServer(Class<? extends BasicPacket> packetClazz) {
		this.registerPacketImpl(packetClazz, Side.SERVER);
	}

	private void registerPacketImpl(Class<? extends BasicPacket> packetClazz, Side side) {
		network.registerMessage(handler, packetClazz, id++, side);
	}

	public static class PacketHandler implements IMessageHandler<BasicPacket, IMessage> {

		@Override
		public IMessage onMessage(BasicPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				return message.handleServer(ctx.getServerHandler());
			} else {
				return message.handleClient(ctx.getClientHandler());
			}
		}
	}
}
