package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class GenericChatMessage extends ThreadSafePacket {
    // A default constructor is always required
    public GenericChatMessage() {
    }

    private String stringMsg;
    private boolean onScreen;

    public GenericChatMessage(EntityPlayer player, String msg, boolean onScreen) {
        this.entityID = player.getEntityId();
        this.stringMsg = msg;
        this.onScreen = onScreen;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Writes the int into the buf
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.onScreen);
        ByteBufUtils.writeUTF8String(buf, this.stringMsg);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.onScreen = buf.readBoolean();
        this.stringMsg = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        final World world = clientPlayer.getEntityWorld();
        //		final Entity entity = world.getEntityByID(entityID);
        //		if (entity instanceof EntityPlayer) {
        //			((EntityPlayer) entity).sendStatusMessage(new TextComponentString(stringMsg), onScreen);
        //		}
        if (world != null) {
            clientPlayer.sendStatusMessage(new TextComponentString(this.stringMsg), this.onScreen);
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {

    }
}