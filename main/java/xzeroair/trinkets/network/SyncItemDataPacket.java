package xzeroair.trinkets.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;

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
	private boolean equipped;

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

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
		World world = clientPlayer.getEntityWorld();
		try {
			final Entity entity = clientPlayer.getEntityWorld().getEntityByID(entityID);
			if (entity != null) {
				world = entity.getEntityWorld();
			}
			if ((entity instanceof EntityPlayer)) {
				if (handler == 1) {
					final ITrinketContainerHandler trinket = TrinketHelper.getTrinketHandler((EntityPlayer) entity);
					if (equipped) {
						if (syncStacks) {
							if (item != null) {
								if (!item.isEmpty()) {
									trinket.setStackInSlot(slot, item);
								}
							}
						}
						final ItemStack stack = trinket.getStackInSlot(slot);
						if (!stack.isEmpty()) {
							final TrinketProperties properties = Capabilities.getTrinketProperties(stack);
							if (properties != null) {
								properties.loadFromNBT(tag);
								//							properties.sendInformationToTracking((EntityLivingBase) entity);
							}
						}
					} else {
						if (syncStacks) {
							trinket.setStackInSlot(slot, ItemStack.EMPTY);
						}
					}
				} else if (handler == 2) {
					final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) entity);
					if (equipped) {
						if (syncStacks) {
							if (item != null) {
								if (!item.isEmpty()) {
									baubles.setStackInSlot(slot, item);
								}
							}
						}
						final ItemStack stack = baubles.getStackInSlot(slot);
						if (!stack.isEmpty()) {
							final TrinketProperties properties = Capabilities.getTrinketProperties(stack);
							if (properties != null) {
								properties.loadFromNBT(tag);
								//							properties.sendInformationToTracking((EntityLivingBase) entity);
							}
						}
					} else {
						if (syncStacks) {
							baubles.setStackInSlot(slot, ItemStack.EMPTY);
						}
					}
				} else {

				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final EntityPlayerMP serverPlayer = server.player;
		final WorldServer world = serverPlayer.getServerWorld();
		try {
			final Entity entity = world.getEntityByID(entityID);
			//			if (entity != null) {
			//				if (entity instanceof EntityPlayerMP) {
			//					world = ((EntityPlayerMP) entity).getServerWorld();
			//				}
			//			} else {
			//				entity = serverPlayer;
			//			}
			if ((entity instanceof EntityPlayer)) {
				if (handler == 1) {
					final ITrinketContainerHandler trinket = TrinketHelper.getTrinketHandler((EntityPlayer) entity);
					if (equipped) {
						if (syncStacks) {
							if (item != null) {
								if (!item.isEmpty()) {
									trinket.setStackInSlot(slot, item);
								}
							}
						}
						final ItemStack stack = trinket.getStackInSlot(slot);
						if (!stack.isEmpty()) {
							final TrinketProperties properties = Capabilities.getTrinketProperties(stack);
							if (properties != null) {
								properties.loadFromNBT(tag);
								properties.sendInformationToTracking((EntityLivingBase) entity);
							}
						}
					} else {
						if (syncStacks) {
							trinket.setStackInSlot(slot, ItemStack.EMPTY);
						}
					}
				} else if (handler == 2) {
					final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) entity);
					if (equipped) {
						if (syncStacks) {
							if (item != null) {
								if (!item.isEmpty()) {
									baubles.setStackInSlot(slot, item);
								}
							}
						}
						final ItemStack stack = baubles.getStackInSlot(slot);
						if (!stack.isEmpty()) {
							final TrinketProperties properties = Capabilities.getTrinketProperties(stack);
							if (properties != null) {
								properties.loadFromNBT(tag);
								properties.sendInformationToTracking((EntityLivingBase) entity);
							}
						}
					} else {
						if (syncStacks) {
							baubles.setStackInSlot(slot, ItemStack.EMPTY);
						}
					}
				} else {

				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}