package xzeroair.trinkets.network.keybinds;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
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
		entityID = entity.getEntityId();
		this.ability = ability;
		this.moveKey = moveKey;
		this.key = key;
		aux = auxKey;
		this.state = state;
	}

	public KeybindPacket(EntityLivingBase entity, String ability, boolean key, boolean auxKey, int state) {
		this(entity, ability, -1, key, auxKey, state);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(entityID);
		buffer.writeInt(state);
		buffer.writeInt(moveKey);
		buffer.writeBoolean(key);
		buffer.writeBoolean(aux);
		ByteBufUtils.writeUTF8String(buffer, ability);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		entityID = buffer.readInt();
		state = buffer.readInt();
		moveKey = buffer.readInt();
		key = buffer.readBoolean();
		aux = buffer.readBoolean();
		ability = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		//		final Entity entity = Minecraft.getMinecraft().world.getEntityByID(entityID);

	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final Entity entity = server.player.getEntityWorld().getEntityByID(entityID);
		Capabilities.getEntityProperties(entity, prop -> {
			try {
				final IAbilityInterface kbAbility = prop.getAbilityHandler().getAbility(ability);
				if ((kbAbility != null) && (kbAbility instanceof IKeyBindInterface)) {
					final IKeyBindInterface keybind = ((IKeyBindInterface) kbAbility);
					if (key) {
						if (state == 0) {
							keybind.onKeyPress(entity, aux);
						} else if (state == 1) {
							keybind.onKeyDown(entity, aux);
						} else {

						}
					} else {
						if (state == 2) {
							keybind.onKeyRelease(entity, aux);
						} else {
						}
					}
				}
			} catch (final Exception e) {
				Trinkets.log.error("Trinkets had an Error with Ability:" + ability);
				e.printStackTrace();
			}
		});
	}
}