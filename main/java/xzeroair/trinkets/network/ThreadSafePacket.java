package xzeroair.trinkets.network;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class ThreadSafePacket extends BasicPacket {
	
	public ThreadSafePacket() {
		super();
	}
	public ThreadSafePacket(int entityID, NBTTagCompound tag) {
		super(entityID, tag);
	}

	@Override
	public final IMessage handleClient(final NetHandlerPlayClient client) {
		FMLCommonHandler.instance().getWorldThread(client).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				handleClientSafe(client);
			}
		});
		return null; 
	}

	@Override
	public final IMessage handleServer(final NetHandlerPlayServer server) {
		FMLCommonHandler.instance().getWorldThread(server).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				handleServerSafe(server);
			}
		});
		return null;
	}

	public abstract void handleClientSafe(NetHandlerPlayClient client);

	public abstract void handleServerSafe(NetHandlerPlayServer server);

}
