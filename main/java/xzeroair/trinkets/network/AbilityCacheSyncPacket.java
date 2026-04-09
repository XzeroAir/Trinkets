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
        if (mc.player != null) {
            final World world = mc.player.getEntityWorld();
            final Entity entity = world != null ? world.getEntityByID(this.entityID) : null;
            if (entity != null) {
                Capabilities.getEntityProperties(entity, (prop) -> {
                    if (this.tag.hasKey(ABILITY_TAG)) {
                        String abilityName = this.tag.getString(ABILITY_TAG);
                        boolean enabled = this.tag.hasKey("ENABLED");
                        boolean disabled = this.tag.hasKey("DISABLED");
                        if (enabled || disabled) {
                            String source = this.tag.getString("Source");
                            if (!source.isEmpty()) {
                                if (enabled) {
                                    prop.getAbilityHandler().removeKillOrder(source, abilityName);
                                } else {
                                    prop.getAbilityHandler().addKillOrder(source, abilityName);
                                }
                            }
                        } else {
                            IAbilityInterface ability = prop.getAbilityHandler().getAbility(abilityName);
                            if (ability != null) {
                                if (this.tag.hasKey(abilityName)) {
                                    ability.loadStorage(this.tag.getCompoundTag(abilityName));
                                }
                                if (this.tag.hasKey(DATA_TAG)) {
                                    ability.loadDataCache(this.tag.getCompoundTag(DATA_TAG));
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
    }
}
