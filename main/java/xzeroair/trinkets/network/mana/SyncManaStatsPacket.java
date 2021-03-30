package xzeroair.trinkets.network.mana;

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
import xzeroair.trinkets.capabilities.race.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class SyncManaStatsPacket implements IMessage {

	public SyncManaStatsPacket() {
	}

	private int entityID;
	private NBTTagCompound tag;
	private float mana;
	private int bonusMana;

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		mana = buf.readFloat();
		bonusMana = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeFloat(mana);
		buf.writeInt(bonusMana);
		ByteBufUtils.writeTag(buf, tag);
	}

	public SyncManaStatsPacket(EntityLivingBase entity, MagicStats magic) {
		entityID = entity.getEntityId();
		tag = new NBTTagCompound();
		mana = magic.getMana();
		bonusMana = magic.getBonusMana();
		magic.saveToNBT(tag);
	}

	public static class Handler implements IMessageHandler<SyncManaStatsPacket, IMessage> {
		@Override
		public IMessage onMessage(SyncManaStatsPacket message, MessageContext ctx) {
			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof EntityLivingBase) {
						if (entity.hasCapability(Capabilities.ENTITY_RACE, null)) {
							EntityProperties cap = Capabilities.getEntityRace((EntityLivingBase) entity);
							if (cap != null) {
								cap.getMagic().loadFromNBT(message.tag);
								cap.getMagic().setMana(message.mana);
								cap.getMagic().setBonusMana(message.bonusMana);
							}
						}
					}
				}
			});
			return null;
		}

	}

}
