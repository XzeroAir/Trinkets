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
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

public class AbilityCacheSyncPacket extends ThreadSafePacket {


    // A default constructor is always required
    public AbilityCacheSyncPacket() {
    }

    public AbilityCacheSyncPacket(EntityLivingBase entity, NBTTagCompound tag) {
        this.entityID = entity.getEntityId();
        this.tag = tag;
    }

    @Override
    public void toBytes(ByteBuf buf) {
//        // Writes the int into the buf
        buf.writeInt(entityID);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
        tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.player.getEntityWorld();
        final Entity entity = world.getEntityByID(entityID);

        if (tag != null && !tag.isEmpty()) {
            String abilityName = tag.getString("Ability");
            Capabilities.getEntityProperties(entity, (prop) -> {
                IAbilityInterface ability = prop.getAbilityHandler().getAbility(abilityName);
                if (ability != null) {
                    ability.loadTagCacheFromNBT(tag);
                }
            });
        }

    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
//        final Entity entity = server.player.getEntityWorld().getEntityByID(entityID);
    }
}
