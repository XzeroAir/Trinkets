package xzeroair.trinkets.network.keybinds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class KeybindPacket implements IMessage, IMessageHandler<KeybindPacket, IMessage> {

	private int entityID;
	private NBTTagCompound tag;

	public KeybindPacket() {
	}

	public KeybindPacket(EntityLivingBase entity, NBTTagCompound tag) {
		entityID = entity.getEntityId();
		this.tag = tag;
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(entityID);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		entityID = buffer.readInt();
		tag = ByteBufUtils.readTag(buffer);
	}

	@Override
	public IMessage onMessage(KeybindPacket message, MessageContext ctx) {

		Trinkets.proxy.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof EntityLivingBase) {
						EntityLivingBase living = (EntityLivingBase) entity;
						if (living.hasCapability(Capabilities.ENTITY_RACE, null)) {
							EntityProperties cap = Capabilities.getEntityRace(living);
							//							if (message.KeyCode >= 0) {
							//								cap.KeyPressed(message.KeyCode);
							//								cap.AuxKeyPressed(message.AuxCode);
							//							}
						}
					}
				}
			}
		});
		return null;
	}
}