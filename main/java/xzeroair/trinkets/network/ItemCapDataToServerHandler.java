package xzeroair.trinkets.network;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.ItemCap.ItemProvider;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

public class ItemCapDataToServerHandler implements IMessageHandler<ItemCapDataMessage, IMessage> {

	@Override public IMessage onMessage(ItemCapDataMessage message, MessageContext ctx) {

		Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
			if(Main.proxy.getPlayer(ctx) != null) {
				ICap cap = Main.proxy.getPlayer(ctx).getCapability(CapPro.sizeCapability, null);
				ItemStack baubleCheck = TrinketHelper.getBaubleTypeStack(Main.proxy.getPlayer(ctx), TrinketType.head);
				ItemCap itemNBT = baubleCheck.getCapability(ItemProvider.itemCapability, null);
				itemNBT.setOreType(message.type);
				itemNBT.nightVisionOn(message.on);
				//				Main.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Sent A Packet!"));

			}
		});
		// No response packet
		return null;
	}
}