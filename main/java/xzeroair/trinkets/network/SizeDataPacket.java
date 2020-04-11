package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceProperties;

public class SizeDataPacket implements IMessage {
	// A default constructor is always required
	public SizeDataPacket() {
	}

	public int size = 100;
	public boolean trans = false;
	public int target = 100;
	public String food = "none";
	public float width = 0.6F;
	public float height = 1.8F;
	public float defaultWidth = 0.6F;
	public float defaultHeight = 1.8F;
	public float eyeHeight = 1.62F;
	public int entityID = 0;

	public SizeDataPacket(EntityLivingBase entity, int size, boolean trans, int target, String food) {

		this.entityID = entity.getEntityId();
		this.size = size;
		this.trans = trans;
		this.target = target;
		this.food = food;
		this.width = entity.width;
		this.height = entity.height;
		this.defaultWidth = entity.width;
		this.defaultHeight = entity.height;
		this.eyeHeight = entity.getEyeHeight();
	}

	public SizeDataPacket(EntityLivingBase entity, RaceProperties cap) {
		this.entityID = entity.getEntityId();
		this.size = cap.getSize();
		this.trans = cap.getTrans();
		this.target = cap.getTarget();
		this.food = cap.getFood();
		this.width = entity.width;
		this.height = entity.height;
		this.defaultWidth = entity.width;
		this.defaultHeight = entity.height;
		this.eyeHeight = entity.getEyeHeight();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(this.size);
		buf.writeBoolean(this.trans);
		buf.writeInt(this.target);
		ByteBufUtils.writeUTF8String(buf, this.food);
		buf.writeFloat(this.width);
		buf.writeFloat(this.height);
		buf.writeFloat(this.defaultWidth);
		buf.writeFloat(this.defaultHeight);
		buf.writeFloat(this.eyeHeight);
		buf.writeInt(this.entityID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.size = buf.readInt();
		this.trans = buf.readBoolean();
		this.target = buf.readInt();
		ByteBufUtils.readUTF8String(buf);
		this.width = buf.readFloat();
		this.height = buf.readFloat();
		this.defaultWidth = buf.readFloat();
		this.defaultHeight = buf.readFloat();
		this.eyeHeight = buf.readFloat();
		this.entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<SizeDataPacket, IMessage> {

		@Override
		public IMessage onMessage(SizeDataPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					// final EntityPlayer entity = Trinkets.proxy.getPlayer(ctx);
					final Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof EntityLivingBase) {
						if (entity.hasCapability(Capabilities.ENTITY_RACE, null)) {
							RaceProperties cap = Capabilities.getEntityRace((EntityLivingBase) entity);
							if (cap != null) {
								cap.setSize(message.size);
								cap.setTrans(message.trans);
								cap.setTarget(message.target);
								cap.setFood(message.food);
								cap.setWidth(message.width);
								cap.setHeight(message.height);
								cap.setDefaultWidth(message.defaultWidth);
								cap.setDefaultHeight(message.defaultHeight);
							}
						}
//						if (ctx.side == Side.CLIENT) {
//							System.out.println("Packet Recieved");
//							Trinkets.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Recieved A Packet!"));
//						} else {
//							System.out.println("Packet Sent");
//							Trinkets.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Sent A Packet!"));
//						}
					}
				}
			});
			return null;
		}
	}

}