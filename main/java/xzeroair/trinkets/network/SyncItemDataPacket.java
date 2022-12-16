package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.util.compat.baubles.BaublesHelper;

public class SyncItemDataPacket extends ThreadSafePacket {
	// A default constructor is always required
	public SyncItemDataPacket() {
	}

	NBTTagCompound tag;
	int slot;
	int handler;
	int entityID;
	ItemStack item;
	boolean syncStacks;
	boolean equipped;

	public SyncItemDataPacket(EntityLivingBase player, ItemStack stack, NBTTagCompound tag, int slot, ItemHandlerType handler, boolean syncSlot, boolean equipped) {
		this(player, stack, tag, slot, handler.getId(), syncSlot, equipped);
	}

	public SyncItemDataPacket(EntityLivingBase player, ItemStack stack, NBTTagCompound tag, int slot, int handler, boolean syncSlot, boolean equipped) {
		this.slot = slot;
		this.handler = handler;
		if (tag == null) {
			this.tag = new NBTTagCompound();
		} else {
			this.tag = tag;
		}
		syncStacks = syncSlot;
		this.equipped = equipped;
		if (syncSlot) {
			item = stack;
		}
		entityID = player.getEntityId();
	}

	public SyncItemDataPacket(EntityLivingBase player, ItemStack stack, NBTTagCompound tag, int slot, int handler) {
		this(player, stack, tag, slot, handler, false, true);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(slot);
		buf.writeInt(handler);
		buf.writeInt(entityID);
		buf.writeBoolean(syncStacks);
		buf.writeBoolean(equipped);
		ByteBufUtils.writeTag(buf, tag);
		if (syncStacks) {
			ByteBufUtils.writeItemStack(buf, item);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
		handler = buf.readInt();
		entityID = buf.readInt();
		syncStacks = buf.readBoolean();
		equipped = buf.readBoolean();
		tag = ByteBufUtils.readTag(buf);
		if (syncStacks) {
			item = ByteBufUtils.readItemStack(buf);
		}
	}

	private void handlePacket(EntityLivingBase entity, Side side) {
		try {
			final ItemHandlerType typeHandler = ItemHandlerType.byID(handler);
			if (typeHandler.equals(ItemHandlerType.TRINKETS)) {
				try {
					this.handleTrinkets(entity, side);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else if (typeHandler.equals(ItemHandlerType.BAUBLES)) {
				try {
					this.handleBaubles(entity, side);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else {

			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void handleTrinkets(final EntityLivingBase entity, final Side side) {
		TrinketHelper.getTrinketHandler(entity, trinkets -> {
			if (equipped) {
				if (syncStacks) {
					if ((item != null) && !item.isEmpty()) {
						trinkets.setStackInSlot(slot, item);
					}
				}
				final ItemStack stack = trinkets.getStackInSlot(slot);
				Capabilities.getTrinketProperties(stack, properties -> {
					if (side.isClient()) {
						properties.itemEquipped(stack, entity);
						properties.loadFromNBT(tag);
					} else {
						properties.scheduleResync();
					}
				});
			} else {
				if (syncStacks) {
					trinkets.setStackInSlot(slot, ItemStack.EMPTY);
				}
			}
		});
	}

	private void handleBaubles(final EntityLivingBase entity, final Side side) {
		BaublesHelper.getBaublesHandler(entity, baubles -> {
			if (equipped) {
				if (syncStacks) {
					if ((item != null) && !item.isEmpty()) {
						baubles.setStackInSlot(slot, item);
					}
				}
				final ItemStack stack = baubles.getStackInSlot(slot);
				Capabilities.getTrinketProperties(stack, properties -> {
					if (side.isClient()) {
						properties.itemEquipped(stack, entity);
						properties.loadFromNBT(tag);
					} else {
						properties.scheduleResync();
					}
				});
			} else {
				if (syncStacks) {
					baubles.setStackInSlot(slot, ItemStack.EMPTY);
				}
			}
		});
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
		final World world = clientPlayer.getEntityWorld();
		final Entity entity = world.getEntityByID(entityID);
		if ((entity instanceof EntityLivingBase)) {
			this.handlePacket((EntityLivingBase) entity, Side.CLIENT);
		}
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final EntityPlayerMP serverPlayer = server.player;
		final WorldServer world = serverPlayer.getServerWorld();
		final Entity entity = world.getEntityByID(entityID);
		if ((entity instanceof EntityLivingBase)) {
			this.handlePacket((EntityLivingBase) entity, Side.SERVER);
		}
	}
}