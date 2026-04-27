package xzeroair.trinkets.network.keybinds;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.network.ThreadSafePacket;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;

public class KeybindPacket extends ThreadSafePacket {

    private int entityID;
    private boolean key;
    private boolean aux;
    private String ability;
    private int state;
    private int moveKey;

    public KeybindPacket() {
    }

    public KeybindPacket(EntityLivingBase entity, String ability, int moveKey, boolean key, boolean auxKey, int state) {
        this.entityID = entity.getEntityId();
        this.ability = ability;
        this.moveKey = moveKey;
        this.key = key;
        this.aux = auxKey;
        this.state = state;
    }

    public KeybindPacket(EntityLivingBase entity, String ability, boolean key, boolean auxKey, int state) {
        this(entity, ability, -1, key, auxKey, state);
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeInt(this.state);
        buffer.writeInt(this.moveKey);
        buffer.writeBoolean(this.key);
        buffer.writeBoolean(this.aux);
        ByteBufUtils.writeUTF8String(buffer, this.ability);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.state = buffer.readInt();
        this.moveKey = buffer.readInt();
        this.key = buffer.readBoolean();
        this.aux = buffer.readBoolean();
        this.ability = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        //		final Entity entity = Minecraft.getMinecraft().world.getEntityByID(entityID);

    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        final EntityLivingBase entity = server.player;
        Capabilities.getEntityProperties(entity, prop -> {
            try {
                final IAbilityInterface kbAbility = prop.getAbilityHandler().getAbility(this.ability);
                if ((kbAbility instanceof IKeyBindInterface)) {
                    final IKeyBindInterface keybind = ((IKeyBindInterface) kbAbility);
                    if (this.key) {
                        if (this.state == 0) {
                            keybind.onKeyPress(prop.getObject(), this.aux);
                        } else if (this.state == 1) {
                            keybind.onKeyDown(prop.getObject(), this.aux);
                        } else {

                        }
                    } else {
                        if (this.state == 2) {
                            keybind.onKeyRelease(prop.getObject(), this.aux);
                        } else {
                        }
                    }
                }
            } catch (final Exception e) {
                Trinkets.LOGGER.error("Trinkets had an Error with Ability:" + this.ability);
                e.printStackTrace();
            }
        });
    }
}
