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

import javax.annotation.Nonnull;

public class AbilityCacheSyncPacket extends ThreadSafePacket {

    private static final String ABILITY_TAG = "Ability";
    private static final String DATA_TAG = "data";

    // A default constructor is always required
    public AbilityCacheSyncPacket() {
    }

    public AbilityCacheSyncPacket(@Nonnull EntityLivingBase entity, NBTTagCompound tag) {
        this.entityID = entity.getEntityId();
        this.tag = tag;
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeInt(this.entityID);
        ByteBufUtils.writeTag(buf, this.tag);
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        this.entityID = buf.readInt();
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        if (this.tag == null || this.tag.isEmpty()) {
            return;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            return;
        }
        final World world = mc.player.getEntityWorld();
        if (world == null) {
            return;
        }
        final Entity entity = world.getEntityByID(this.entityID);
        if (entity == null) {
            return;
        }

        Capabilities.getEntityProperties(entity, (prop) -> {
            if (this.tag.hasKey(ABILITY_TAG)) {
                String abilityName = this.tag.getString(ABILITY_TAG);
                if (this.tag.hasKey("ENABLED")) {
                    prop.getAbilityHandler().removeKillOrder(abilityName);
                } else if (this.tag.hasKey("DISABLED")) {
                    prop.getAbilityHandler().addKillOrder(abilityName);
                } else {
                    IAbilityInterface ability = prop.getAbilityHandler().getAbility(abilityName);
                    if (ability != null) {
                        if (this.tag.hasKey(abilityName)) {
                            NBTTagCompound data = this.tag.getCompoundTag(abilityName);
                            ability.loadStorage(data);
                        }
                        if (this.tag.hasKey(DATA_TAG)) {
                            NBTTagCompound data = this.tag.getCompoundTag(DATA_TAG);
                            ability.loadDataCache(data);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
    }
}
