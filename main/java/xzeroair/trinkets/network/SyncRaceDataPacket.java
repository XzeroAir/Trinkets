package xzeroair.trinkets.network;

import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.items.base.AccessoryBase;

public class SyncRaceDataPacket extends ThreadSafePacket {
	//	// A default constructor is always required
	public SyncRaceDataPacket() {

	}

	private int entityID;
	private NBTTagCompound tag;

	public SyncRaceDataPacket(EntityLivingBase entity, NBTTagCompound tag) {
		entityID = entity.getEntityId();
		this.tag = tag;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		final Minecraft mc = Minecraft.getMinecraft();
		final World world = mc.player.getEntityWorld();
		final Entity entity = world.getEntityByID(entityID);
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			prop.parseEquipped();
			if ((entity instanceof EntityLivingBase) && (prop.getEquippedItems() != null) && !prop.getEquippedItems().isEmpty()) {
				for (final Entry<String, ItemStack> entry : prop.getEquippedItems().entrySet()) {
					final ItemStack stack = entry.getValue();
					final TrinketProperties itemProp = Capabilities.getTrinketProperties(stack);
					if (!stack.isEmpty() && (itemProp != null)) {
						if (stack.getItem() instanceof AccessoryBase) {
							final AccessoryBase acc = ((AccessoryBase) stack.getItem());
							acc.initAbilities((EntityLivingBase) entity);
						}
					}
				}
			}
			prop.loadFromNBT(tag);
			prop.loadAbilitiesFromNBT(tag);
		}
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final Entity entity = server.player.getEntityWorld().getEntityByID(entityID);
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			prop.loadFromNBT(tag);
			prop.loadAbilitiesFromNBT(tag);
			prop.sendInformationToTracking();
		}
	}

}