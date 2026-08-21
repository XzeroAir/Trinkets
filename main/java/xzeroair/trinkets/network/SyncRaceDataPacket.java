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

    public SyncRaceDataPacket() {
    }

    public SyncRaceDataPacket(EntityLivingBase entity, NBTTagCompound tag) {
        this.entityID = entity.getEntityId();
        this.tag = tag;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        ByteBufUtils.writeTag(buf, this.tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            return;
        }
        final World world = mc.player.getEntityWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        if (entity instanceof EntityLivingBase) {
            Capabilities.getEntityProperties(entity, prop -> prop.loadFromNBT(this.tag));
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        // Server to Client race sync packet; this handler is unreachable by registration.
    }
}
