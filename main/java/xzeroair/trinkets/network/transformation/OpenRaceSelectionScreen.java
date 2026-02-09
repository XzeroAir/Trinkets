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
import xzeroair.trinkets.util.helpers.StringUtils;

public class OpenRaceSelectionScreen extends ThreadSafePacket {

    private String blacklist;

    public OpenRaceSelectionScreen() {
        this(TrinketsConfig.SERVER.races.selectionBlacklist);
    }

    public OpenRaceSelectionScreen(String[] selectionBlacklist) {
        this.init(selectionBlacklist != null && selectionBlacklist.length > 0 ? selectionBlacklist : new String[0]);
    }

    private void init(String[] selectionBlacklist) {
        this.blacklist = StringUtils.combineStringArray(selectionBlacklist);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, blacklist);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.player.getEntityWorld();
        // TODO don't merge Arrays into a single string.
        TrinketsConfig.getClientStore().RACE_SELECTION_BLACKLIST = StringUtils.deconstructStringArray(blacklist);
        mc.player.openGui(Trinkets.instance, Reference.GUI_RACE_SELECTION, world, 0, 0, 0);
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
    }
}