package xzeroair.trinkets.network.configsync;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.network.ThreadSafePacket;
import xzeroair.trinkets.util.TrinketsConfig;

public class PacketConfigSync extends ThreadSafePacket {

	Map<String, String> map;
	int size;

	public PacketConfigSync() {
	}

	public PacketConfigSync(Map<String, String> configMap) {
		if ((configMap != null) && !configMap.isEmpty()) {
			map = configMap;
			size = map.size();
		} else {
			map = new HashMap<>();
			size = 0;
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(size);
		for (final Entry<String, String> config : map.entrySet()) {
			ByteBufUtils.writeUTF8String(buffer, config.getKey());
			ByteBufUtils.writeUTF8String(buffer, config.getValue());
		}
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		size = buffer.readInt();
		if (size > 0) {
			map = new HashMap<>();
			for (int i = 0; i < size; i++) {
				final String key = ByteBufUtils.readUTF8String(buffer);
				final String value = ByteBufUtils.readUTF8String(buffer);
				map.put(key, value);
			}
		}
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		TrinketsConfig.readConfigMap(map);
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {

	}
}