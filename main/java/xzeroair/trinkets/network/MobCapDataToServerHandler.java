package xzeroair.trinkets.network;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;

// The params of the IMessageHandler are <REQ, REPLY>
// This means that the first param is the packet you are receiving, and the second is the packet you are returning.
// The returned packet can be used as a "response" from a sent packet.
public class MobCapDataToServerHandler implements IMessageHandler<MobCapDataMessage, IMessage> {

	@Override public IMessage onMessage(MobCapDataMessage message, MessageContext ctx) {

		Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
			if(Main.proxy.getPlayer(ctx) != null) {
				if(Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID) instanceof EntityLiving) {
					EntityLiving entity = (EntityLiving) Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID);
					ICap cap = entity.getCapability(CapPro.sizeCapability, null);

					cap.setSize(message.size);
					cap.setTrans(message.trans);
					cap.setTarget(message.target);
					cap.setWidth(message.width);
					cap.setHeight(message.height);
					cap.setDefaultWidth(message.defaultWidth);
					cap.setDefaultHeight(message.defaultHeight);

					//				Main.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Sent A Packet!"));
				}
			}
		});
		// No response packet
		return null;
	}
}