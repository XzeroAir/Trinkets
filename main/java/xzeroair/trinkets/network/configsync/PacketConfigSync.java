package xzeroair.trinkets.network.configsync;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.network.ThreadSafePacket;
import xzeroair.trinkets.util.TrinketsConfig;

public class PacketConfigSync extends ThreadSafePacket {

    public PacketConfigSync() {
    }

    public PacketConfigSync(NBTTagCompound configMap) {
        if ((configMap != null) && !configMap.isEmpty()) {
            tag = configMap;
        } else {
            tag = new NBTTagCompound();
        }
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, tag);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        tag = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        TrinketsConfig.readConfigMap(tag);
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {

    }
}