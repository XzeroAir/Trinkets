package xzeroair.trinkets.network.particles;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.network.ThreadSafePacket;

public class EffectsRenderPacket extends ThreadSafePacket {

    public EffectsRenderPacket() {
    }

    double x;
    double y;
    double z;
    double x2;
    double y2;
    double z2;
    private int effectID;
    private int color;
    private float alpha;
    private float intensity;

    public EffectsRenderPacket(Entity entity, double x, double y, double z, double x2, double y2, double z2, int color, int effectID, float alpha, float intensity) {
        this.entityID = entity.getEntityId();
        this.effectID = effectID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.color = color;
        this.alpha = alpha;
        this.intensity = intensity;
    }

    public EffectsRenderPacket(Entity entity, double x, double y, double z, float intensity, int effectID) {
        this(entity, x, y, z, 0, 0, 0, 0, effectID, 1F, intensity);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeInt(this.effectID);
        buf.writeInt(this.color);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeDouble(this.x2);
        buf.writeDouble(this.y2);
        buf.writeDouble(this.z2);
        buf.writeFloat(this.alpha);
        buf.writeFloat(this.intensity);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.effectID = buf.readInt();
        this.color = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.x2 = buf.readDouble();
        this.y2 = buf.readDouble();
        this.z2 = buf.readDouble();
        this.alpha = buf.readFloat();
        this.intensity = buf.readFloat();
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        World world = clientPlayer.getEntityWorld();
        try {
            final Entity entity = clientPlayer.getEntityWorld().getEntityByID(this.entityID);
            if (entity != null) {
                world = entity.getEntityWorld();
            }
            Trinkets.proxy.renderEffect(this.effectID, world, this.x, this.y, this.z, this.x2, this.y2, this.z2, this.color, this.alpha, this.intensity);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        // Server to Client particle packet; this handler is unreachable by registration.
    }
}
