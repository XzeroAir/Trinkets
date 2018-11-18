package xzeroair.trinkets.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.Minecraft;
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

public class CapDataToClientHandler implements IMessageHandler<CapDataMessage, IMessage> {
	// Do note that the default constructor is required, but implicitly defined in this case

	@Override public IMessage onMessage(CapDataMessage message, MessageContext ctx) {

		// This is the player the packet was sent to from the server
		Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
			if(Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID) != null) {
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

				//				player.sendMessage(new TextComponentString("You Recieved A Packet!"));
			}
		});
		// No response packet
		return null;
	}
}