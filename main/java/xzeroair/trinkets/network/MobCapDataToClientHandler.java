package xzeroair.trinkets.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;

public class MobCapDataToClientHandler implements IMessageHandler<MobCapDataMessage, IMessage> {
	// Do note that the default constructor is required, but implicitly defined in this case

	@Override public IMessage onMessage(MobCapDataMessage message, MessageContext ctx) {

		// This is the player the packet was sent to from the server
		Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
			if(Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID) != null) {
				EntityLiving entity = (EntityLiving) Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID);
				ICap cap = entity.getCapability(CapPro.sizeCapability, null);

				cap.setSize(message.size);
				cap.setTrans(message.trans);
				cap.setTarget(message.target);
				cap.setWidth(message.width);
				cap.setHeight(message.height);
				entity.width = message.width;
				entity.height = message.height;
				cap.setDefaultWidth(message.defaultWidth);
				cap.setDefaultHeight(message.defaultHeight);

				//						Main.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Recieved A Packet!"));
			}
		});
		// No response packet
		return null;
	}
}