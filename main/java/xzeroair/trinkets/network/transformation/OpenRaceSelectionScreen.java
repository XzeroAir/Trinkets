package xzeroair.trinkets.network.transformation;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.network.ThreadSafePacket;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenRaceSelectionScreen extends ThreadSafePacket {

    private int size;
    List<String> blacklist;

    public OpenRaceSelectionScreen() {
        this(TrinketsConfig.SERVER.RACES.BLACKLIST);
    }

    public OpenRaceSelectionScreen(String... selectionBlacklist) {
        List<String> list = Arrays.asList(selectionBlacklist);
        this.blacklist = list;
        this.size = list.size();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.size);
        for (String s : this.blacklist) {
            ByteBufUtils.writeUTF8String(buf, s);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.size = buf.readInt();
        if (this.blacklist == null) {
            this.blacklist = new ArrayList<>();
        }
        for (int i = 0; i < this.size; i++) {
            this.blacklist.add(ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.player.getEntityWorld();
        TrinketsConfig.getClientStore().RACE_SELECTION_BLACKLIST = this.blacklist.toArray(new String[0]);
        mc.player.openGui(Trinkets.instance, Reference.GUI_RACE_SELECTION, world, 0, 0, 0);
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
    }
}