package xzeroair.trinkets.network.mana;

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
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.network.ThreadSafePacket;

import javax.annotation.Nonnull;

public class SyncManaStatsPacket extends ThreadSafePacket {

    public SyncManaStatsPacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        ByteBufUtils.writeTag(buf, this.tag);
    }

    public SyncManaStatsPacket(@Nonnull EntityLivingBase entity, @Nonnull MagicStats magic) {
        this(entity, magic.saveToNBT(new NBTTagCompound()));
    }

    public SyncManaStatsPacket(@Nonnull EntityLivingBase entity, @Nonnull NBTTagCompound tag) {
        this.entityID = entity.getEntityId();
        this.tag = tag;
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.player.getEntityWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        Capabilities.getMagicStats(entity, (magic) -> magic.loadFromNBT(this.tag));
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
    }

}
