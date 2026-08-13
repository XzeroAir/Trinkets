package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;

public class SyncRaceDataPacket extends ThreadSafePacket {
    //	// A default constructor is always required
    public SyncRaceDataPacket() {
    }

    private boolean sync;

    public SyncRaceDataPacket(EntityLivingBase entity, NBTTagCompound tag) {
        this(entity, tag, true);
    }

    public SyncRaceDataPacket(EntityLivingBase entity, NBTTagCompound tag, boolean sync) {
        this.entityID = entity.getEntityId();
        this.tag = tag;
        this.sync = sync;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.sync);
        ByteBufUtils.writeTag(buf, this.tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.sync = buf.readBoolean();
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.player.getEntityWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        Capabilities.getEntityProperties(entity, prop -> {
            prop.loadFromNBT(this.tag);
        });
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        final Entity entity = server.player;
        Capabilities.getEntityProperties(entity, prop -> {
            prop.loadFromNBT(this.tag);
            if (this.sync) {
                prop.sendInformationToTracking(this.tag);
            }
        });
    }

}
