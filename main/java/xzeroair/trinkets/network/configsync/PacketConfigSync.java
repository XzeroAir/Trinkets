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
            this.tag = configMap;
        } else {
            this.tag = new NBTTagCompound();
        }
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, this.tag);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.tag = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        TrinketsConfig.readConfigMap(this.tag);
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {

    }
}