package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.util.compat.baubles.BaublesHelper;

public class UpdateDataForTrinketPacket extends ThreadSafePacket {

    // A default constructor is always required
    public UpdateDataForTrinketPacket() {
    }

    int action;
    int slot;
    int handler;

    public UpdateDataForTrinketPacket(EntityLivingBase player, NBTTagCompound tag, int slot, TrinketHelper.SlotInformation.ItemHandlerType handler, int action) {
        this(player, tag, slot, handler.getId(), action);
    }

    public UpdateDataForTrinketPacket(EntityLivingBase player, NBTTagCompound tag, int slot, int handler, int action) {
        this.slot = slot;
        this.handler = handler;
        if (tag == null) {
            this.tag = new NBTTagCompound();
        } else {
            this.tag = tag;
        }
        this.action = action;
        this.entityID = player.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Writes the int into the buf
        buf.writeInt(this.action);
        buf.writeInt(this.slot);
        buf.writeInt(this.handler);
        buf.writeInt(this.entityID);
        ByteBufUtils.writeTag(buf, this.tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.action = buf.readInt();
        this.slot = buf.readInt();
        this.handler = buf.readInt();
        this.entityID = buf.readInt();
        this.tag = ByteBufUtils.readTag(buf);
    }

    private void handlePacket(EntityLivingBase entity, Side side) {
        try {
            final TrinketHelper.SlotInformation.ItemHandlerType typeHandler = TrinketHelper.SlotInformation.ItemHandlerType.byID(this.handler);
            if (typeHandler.equals(TrinketHelper.SlotInformation.ItemHandlerType.TRINKETS)) {
                try {
                    TrinketHelper.getTrinketHandler(entity, trinkets -> {
                        Capabilities.getTrinketProperties(trinkets.getStackInSlot(this.slot), properties -> {
                            this.handleTrinkets(entity, properties, typeHandler, side);
                        });
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else if (typeHandler.equals(TrinketHelper.SlotInformation.ItemHandlerType.BAUBLES)) {
                try {
                    BaublesHelper.getBaublesHandler(entity, baubles -> {
                        Capabilities.getTrinketProperties(baubles.getStackInSlot(this.slot), properties -> {
                            this.handleBaubles(entity, properties, typeHandler, side);
                        });
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (entity instanceof EntityPlayer) {
                    if (this.slot >= 0) {
                        EntityPlayer player = (EntityPlayer) entity;
                        Capabilities.getTrinketProperties(player.inventory.getStackInSlot(this.slot), properties -> {
                            this.handlePlayerInventory(player, properties, typeHandler, side);
                        });
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePlayerInventory(final EntityPlayer player, TrinketProperties properties, TrinketHelper.SlotInformation.ItemHandlerType typeHandler, final Side side) {
        this.handleAction(player, properties, typeHandler, side);
    }

    private void handleTrinkets(final EntityLivingBase entity, TrinketProperties properties, TrinketHelper.SlotInformation.ItemHandlerType typeHandler, final Side side) {
        this.handleAction(entity, properties, typeHandler, side);
    }

    private void handleBaubles(final EntityLivingBase entity, TrinketProperties properties, TrinketHelper.SlotInformation.ItemHandlerType typeHandler, final Side side) {
        this.handleAction(entity, properties, typeHandler, side);
    }

    private void handleAction(EntityLivingBase player, TrinketProperties properties, TrinketHelper.SlotInformation.ItemHandlerType typeHandler, Side side) {
        if (side.isClient()) {
            if (this.action == 1) {
                if (properties.updateSlotInfo(properties.getItemStack(), this.slot, typeHandler)) {
                    properties.getSlotInfo().setChanged(true);
                }
                properties.loadFromNBT(this.tag);
            }
        } else {
//    NotifyPlayerAboutAbilities packet = new NotifyPlayerAboutAbilities(player, this.getTag(), itemSlot, type, 1);
//    NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
        }
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        final World world = clientPlayer.getEntityWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        if ((entity instanceof EntityLivingBase)) {
            this.handlePacket((EntityLivingBase) entity, Side.CLIENT);
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        final EntityPlayerMP serverPlayer = server.player;
        final WorldServer world = serverPlayer.getServerWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        if ((entity instanceof EntityLivingBase)) {
            this.handlePacket((EntityLivingBase) entity, Side.SERVER);
        }
    }
}