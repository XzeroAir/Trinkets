package xzeroair.trinkets.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

// The params of the IMessageHandler are <REQ, REPLY>
// This means that the first param is the packet you are receiving, and the second is the packet you are returning.
// The returned packet can be used as a "response" from a sent packet.
public class CapDataToServerHandler implements IMessageHandler<CapDataMessage, IMessage> {

	@Override public IMessage onMessage(CapDataMessage message, MessageContext ctx) {

		Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
			if(Main.proxy.getPlayer(ctx) != null) {
				EntityPlayer player = (EntityPlayer) Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID);
				ICap cap = player.getCapability(CapPro.sizeCapability, null);

				cap.setSize(message.size);
				cap.setTrans(message.trans);
				cap.setTarget(message.target);
				cap.setWidth(message.width);
				cap.setHeight(message.height);
				cap.setDefaultWidth(message.defaultWidth);
				cap.setDefaultHeight(message.defaultHeight);

				player.eyeHeight = message.eyeHeight;

				Main.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Sent A Packet!"));
			}
		});
		// No response packet
		return null;
	}
}