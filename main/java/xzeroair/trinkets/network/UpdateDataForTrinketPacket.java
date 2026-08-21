package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;

public class UpdateDataForTrinketPacket extends ThreadSafePacket {

    private int action;
    private int slot;
    private int handler;

    public UpdateDataForTrinketPacket() {
    }

    public UpdateDataForTrinketPacket(EntityLivingBase player, NBTTagCompound tag, int slot, ItemHandlerType handler, int action) {
        this(player, tag, slot, handler.getId(), action);
    }

    public UpdateDataForTrinketPacket(EntityLivingBase player, NBTTagCompound tag, int slot, int handler, int action) {
        this.slot = slot;
        this.handler = handler;
        this.tag = tag == null ? new NBTTagCompound() : tag;
        this.action = action;
        this.entityID = player.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
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

    private void handlePacket(EntityLivingBase entity) {
        if ((this.action != 1) || (this.tag == null)) {
            return;
        }
        final ItemHandlerType typeHandler = ItemHandlerType.byID(this.handler);
        final ItemStack stack = TrinketHelper.getStackFromHandler(entity, typeHandler, this.slot);
        if (!stack.isEmpty()) {
            this.updateProperties(stack, typeHandler);
        }
    }

    private void updateProperties(ItemStack stack, ItemHandlerType typeHandler) {
        Capabilities.getTrinketProperties(stack, properties -> {
            if (properties.updateSlotInfo(properties.getItemStack(), this.slot, typeHandler)) {
                properties.getSlotInfo().setChanged(true);
            }
            properties.loadFromNBT(this.tag);
        });
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        if (clientPlayer == null) {
            return;
        }
        final World world = clientPlayer.getEntityWorld();
        final Entity entity = world == null ? null : world.getEntityByID(this.entityID);
        if (entity instanceof EntityLivingBase) {
            this.handlePacket((EntityLivingBase) entity);
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        // Server to Client item update packet; this handler is unreachable by registration.
    }
}
