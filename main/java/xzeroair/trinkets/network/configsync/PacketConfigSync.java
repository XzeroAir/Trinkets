package xzeroair.trinkets.network.configsync;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class PacketConfigSync implements IMessage {

	int playerId;
	boolean setting;
	boolean setting2;
	boolean setting3;
	boolean setting4;
	boolean setting5;
	boolean setting6;
	boolean setting7;
	boolean setting8;
	boolean setting9;
	boolean setting10;
	boolean setting11;
	boolean setting12;
	//	boolean setting13;
	int rows;
	int length;
	double value;

	public PacketConfigSync() {}

	public PacketConfigSync(EntityPlayer p,
			boolean setting,
			boolean setting2,
			boolean setting3,
			boolean setting4,
			boolean setting5,
			double value,
			boolean setting6,
			int rows,
			int length,
			boolean setting7,
			boolean setting8,
			boolean setting9,
			boolean setting10,
			boolean setting11,
			boolean setting12
			//			boolean setting13,
			) {
		this.setting = setting;
		this.setting2  = setting2;
		this.setting3  = setting3;
		this.setting4  = setting4;
		this.setting5  = setting5;
		this.setting6  = setting6;
		this.setting7  = setting7;
		this.setting8  = setting8;
		this.setting9  = setting9;
		this.setting10 = setting10;
		this.setting11 = setting11;
		this.setting12 = setting12;
		//		this.setting13 = setting13;
		this.rows = rows;
		this.length = length;
		this.value = value;
		playerId = p.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(playerId);
		buffer.writeBoolean(setting);
		buffer.writeBoolean(setting2);
		buffer.writeBoolean(setting3);
		buffer.writeBoolean(setting4);
		buffer.writeBoolean(setting5);
		buffer.writeDouble(value);
		buffer.writeBoolean(setting6);
		buffer.writeInt(rows);
		buffer.writeInt(length);
		buffer.writeBoolean(setting7);
		buffer.writeBoolean(setting8);
		buffer.writeBoolean(setting9);
		buffer.writeBoolean(setting10);
		buffer.writeBoolean(setting11);
		buffer.writeBoolean(setting12);
		//		buffer.writeBoolean(setting13);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		playerId = buffer.readInt();
		setting = buffer.readBoolean();
		setting2 = buffer.readBoolean();
		setting3 = buffer.readBoolean();
		setting4 = buffer.readBoolean();
		setting5 = buffer.readBoolean();
		value = buffer.readDouble();
		setting6 = buffer.readBoolean();
		rows = buffer.readInt();
		length = buffer.readInt();
		setting7 = buffer.readBoolean();
		setting8 = buffer.readBoolean();
		setting9 = buffer.readBoolean();
		setting10 = buffer.readBoolean();
		setting11 = buffer.readBoolean();
		setting12 = buffer.readBoolean();
		//		setting13 = buffer.readBoolean();
	}

	public static class Handler implements IMessageHandler<PacketConfigSync, IMessage>
	{
		@Override
		public IMessage onMessage(PacketConfigSync message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getPlayer(ctx) != null) {

					TrinketsConfig.SERVER.FAIRY_RING.creative_flight = message.setting2;
					TrinketsConfig.SERVER.DRAGON_EYE.oreFinder = message.setting3;
					TrinketsConfig.SERVER.GUI.guiEnabled = message.setting4;
					TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed = message.setting5;
					TrinketsConfig.SERVER.FAIRY_RING.flight_speed = message.value;
					TrinketsConfig.SERVER.GUI.guiEnabled = message.setting6;
					TrinketsConfig.SERVER.GUI.guiSlotsRows = message.rows;
					TrinketsConfig.SERVER.GUI.guiSlotsRowLength = message.length;
					TrinketsConfig.compat.artemislib = message.setting7;
					TrinketsConfig.compat.baubles = message.setting8;
					TrinketsConfig.compat.enhancedvisuals = message.setting9;
					TrinketsConfig.compat.morph = message.setting10;
					TrinketsConfig.compat.toughasnails = message.setting11;
				}
			});
			return null;
		}
	}
}