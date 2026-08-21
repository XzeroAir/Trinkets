package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;

public class UpdateRaceProfilePacket extends ThreadSafePacket {

    private NBTTagCompound profile;

    public UpdateRaceProfilePacket() {
    }

    public UpdateRaceProfilePacket(EntityRacePropertiesHandler properties) {
        this.profile = properties.saveProfileData(new NBTTagCompound());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.profile);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.profile = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        // Client to Server race profile request; this handler is unreachable by registration.
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        if (this.profile == null) {
            return;
        }
        Capabilities.getEntityProperties(server.player, properties -> {
            final EntityRacePropertiesHandler raceProperties = properties.getRaceHandler();
            if (raceProperties.loadProfileData(this.profile)) {
                properties.scheduleResync();
            }
        });
    }
}
