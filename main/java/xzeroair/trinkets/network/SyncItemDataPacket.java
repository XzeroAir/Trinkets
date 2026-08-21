package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.util.compat.baubles.BaublesHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SyncItemDataPacket extends ThreadSafePacket {
    // A default constructor is always required
    public SyncItemDataPacket() {
    }

    private int slot;
    private int handler;
    private ItemStack item;
    private boolean syncStacks;
    private boolean equipped;

    public SyncItemDataPacket(EntityLivingBase player, ItemStack stack, @Nullable NBTTagCompound tag, int slot, @Nonnull ItemHandlerType handler, boolean syncSlot, boolean equipped) {
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
        this.syncStacks = syncSlot;
        this.equipped = equipped;
        if (syncSlot) {
            this.item = stack;
        }
        this.entityID = player.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Writes the int into the buf
        buf.writeInt(this.slot);
        buf.writeInt(this.handler);
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.syncStacks);
        buf.writeBoolean(this.equipped);
        ByteBufUtils.writeTag(buf, this.tag);
        if (this.syncStacks) {
            ByteBufUtils.writeItemStack(buf, this.item);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slot = buf.readInt();
        this.handler = buf.readInt();
        this.entityID = buf.readInt();
        this.syncStacks = buf.readBoolean();
        this.equipped = buf.readBoolean();
        this.tag = ByteBufUtils.readTag(buf);
        if (this.syncStacks) {
            this.item = ByteBufUtils.readItemStack(buf);
        }
    }

    private void handlePacket(EntityLivingBase entity) {
        final ItemHandlerType typeHandler = ItemHandlerType.byID(this.handler);
        if (typeHandler.equals(ItemHandlerType.TRINKETS)) {
            this.handleTrinkets(entity);
        } else if (typeHandler.equals(ItemHandlerType.BAUBLES)) {
            this.handleBaubles(entity);
        } else if (entity instanceof EntityPlayer) {
            this.handlePlayerInventory((EntityPlayer) entity);
        }
    }

    private void handlePlayerInventory(final EntityPlayer player) {
        if ((this.slot < 0) || (this.slot >= player.inventory.getSizeInventory())) {
            return;
        }
        final ItemStack stack = player.inventory.getStackInSlot(this.slot);
        if (!stack.isEmpty()) {
            Capabilities.getTrinketProperties(stack, properties -> properties.loadFromNBT(this.tag));
        }
    }

    private void handleTrinkets(final EntityLivingBase entity) {
        TrinketHelper.getTrinketHandler(entity, trinkets -> {
            if ((this.slot < 0) || (this.slot >= trinkets.getSlots())) {
                return;
            }
            if (this.equipped && this.syncStacks && (this.item != null) && !this.item.isEmpty()) {
                trinkets.setStackInSlot(this.slot, this.item);
            } else if (!this.equipped && this.syncStacks) {
                trinkets.setStackInSlot(this.slot, ItemStack.EMPTY);
                return;
            }
            final ItemStack stack = trinkets.getStackInSlot(this.slot);
            Capabilities.getTrinketProperties(stack, properties -> {
                if (this.equipped) {
                    properties.itemEquipped(entity);
                    properties.loadFromNBT(this.tag);
                }
            });
        });
    }

    private void handleBaubles(final EntityLivingBase entity) {
        BaublesHelper.getBaublesHandler(entity, baubles -> {
            if ((this.slot < 0) || (this.slot >= baubles.getSlots())) {
                return;
            }
            if (this.equipped && this.syncStacks && (this.item != null) && !this.item.isEmpty()) {
                baubles.setStackInSlot(this.slot, this.item);
            } else if (!this.equipped && this.syncStacks) {
                baubles.setStackInSlot(this.slot, ItemStack.EMPTY);
                return;
            }
            final ItemStack stack = baubles.getStackInSlot(this.slot);
            Capabilities.getTrinketProperties(stack, properties -> {
                if (this.equipped) {
                    properties.itemEquipped(entity);
                    properties.loadFromNBT(this.tag);
                }
            });
        });
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        if (clientPlayer == null) {
            return;
        }
        final World world = clientPlayer.getEntityWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        if (entity instanceof EntityLivingBase) {
            this.handlePacket((EntityLivingBase) entity);
        }
    }

    @Override
    public void handleServerSafe(@Nonnull NetHandlerPlayServer server) {
        // Server to Client sync packet; this handler is unreachable by registration.
    }
}
