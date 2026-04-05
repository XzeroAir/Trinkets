package xzeroair.trinkets.network.vip;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.network.ThreadSafePacket;

public class VipStatusPacket extends ThreadSafePacket {

    public VipStatusPacket() {
        super();
    }

    public VipStatusPacket(EntityPlayer entity, NBTTagCompound tag) {
        super(entity.getEntityId(), tag);
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
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        final World world = clientPlayer.getEntityWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        Capabilities.getVipStatus(entity, vip -> {
            vip.loadFromNBT(this.tag);
        });
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
    }

}