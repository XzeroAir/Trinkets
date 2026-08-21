package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class IncreasedReachPacket extends ThreadSafePacket {

    public IncreasedReachPacket() {
    }

    private int targetEntityID;
    private int hand;
    private double x;
    private double y;
    private double z;

    public IncreasedReachPacket(EntityLivingBase entity, EnumHand hand, Entity targetEntity, Vec3d vec) {
        this(entity, hand, targetEntity, vec.x, vec.y, vec.z);
    }

    public IncreasedReachPacket(EntityLivingBase entity, EnumHand hand, Entity targetEntity, double x, double y, double z) {
        this.entityID = entity.getEntityId();
        this.targetEntityID = targetEntity.getEntityId();
        this.hand = hand == EnumHand.MAIN_HAND ? 0 : 1;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeInt(this.hand);
        buf.writeInt(this.targetEntityID);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.hand = buf.readInt();
        this.targetEntityID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {

    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        if (!TrinketsConfig.SERVER.MISC.REACH) {
            return;
        }
        final EntityPlayer player = server.player;
        final World world = player.getEntityWorld();
        final Entity interacted = world.getEntityByID(this.targetEntityID);
        if (!this.isValidReachTarget(player, interacted)) {
            return;
        }
        if (this.hand == 1) {
            interacted.processInitialInteract(player, EnumHand.OFF_HAND);
        } else {
            player.attackTargetEntityWithCurrentItem(interacted);
        }
    }

    private boolean isValidReachTarget(EntityPlayer player, Entity target) {
        if ((target == null) || (target == player)) {
            return false;
        }
        final IAttributeInstance reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
        if ((reach == null) || (reach.getAttributeValue() <= 5.0D)) {
            return false;
        }
        final RayTraceResult result = RayTraceHelper.rayTrace(player, reach.getAttributeValue() * 0.8D);
        return (result != null) && (result.typeOfHit == Type.ENTITY) && (result.entityHit == target);
    }
}
