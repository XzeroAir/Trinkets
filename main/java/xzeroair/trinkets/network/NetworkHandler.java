package xzeroair.trinkets.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.network.keybinds.KeybindPacket;
import xzeroair.trinkets.network.mana.SyncManaCostToHudPacket;
import xzeroair.trinkets.network.mana.SyncManaStatsPacket;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.network.vip.VipStatusPacket;
import xzeroair.trinkets.util.Reference;

public class NetworkHandler extends BasicNetworkWrapper {

    public NetworkHandler() {
        super(Reference.MODID.toLowerCase());
    }

    public static final NetworkHandler INSTANCE = new NetworkHandler();

    /**
     * Side.Client means sending to the Client Side.Server means sending to the
     * Server
     */
    public void init() {
        // PLAYER RACE
        this.registerPacketClient(SyncRaceDataPacket.class);
        this.registerPacketServer(SelectRacePacket.class);
        this.registerPacketServer(UpdateRaceProfilePacket.class);

        // ITEM DATA
        this.registerPacketClient(SyncItemDataPacket.class);

        // MAGIC STATS
        this.registerPacketClient(SyncManaStatsPacket.class);
        this.registerPacketClient(SyncManaCostToHudPacket.class);

        // KEY BINDS
        this.registerPacket(KeybindPacket.class);

        this.registerPacketServer(IncreasedReachPacket.class);

        // PARTICLES
        this.registerPacketClient(EffectsRenderPacket.class);

        // MISC
        this.registerPacketClient(VipStatusPacket.class);

        this.registerPacket(OpenTrinketGui.class);

        this.registerPacketClient(PacketConfigSync.class);
        // TODO Make sure to do something with the Chat Message Packet, its probably unnecessary
        this.registerPacketClient(GenericChatMessage.class);
        this.registerPacketClient(AbilityCacheSyncPacket.class);
        this.registerPacketClient(UpdateDataForTrinketPacket.class);

    }

    public static void sendPacket(Entity player, Packet<?> packet) {
        if ((player instanceof EntityPlayerMP) && (((EntityPlayerMP) player).connection != null)) {
            ((EntityPlayerMP) player).connection.sendPacket(packet);
        }
    }

    public static void sendToAll(BasicPacket packet) {
        INSTANCE.network.sendToAll(packet);
    }

    public static void sendTo(BasicPacket packet, EntityPlayerMP player) {
        INSTANCE.network.sendTo(packet, player);
    }

    public static void sendToAllAround(BasicPacket packet, NetworkRegistry.TargetPoint point) {
        INSTANCE.network.sendToAllAround(packet, point);
    }

    public static void sendToDimension(BasicPacket packet, int dimensionId) {
        INSTANCE.network.sendToDimension(packet, dimensionId);
    }

    public static void sendToServer(BasicPacket packet) {
        INSTANCE.network.sendToServer(packet);
    }

    public static void sendToTracking(BasicPacket packet, Entity entity) {
        INSTANCE.network.sendToAllTracking(packet, entity);

    }

    public static void sendToClients(WorldServer world, BlockPos pos, BasicPacket packet) {
        final Chunk chunk = world.getChunk(pos);
        for (final EntityPlayer player : world.playerEntities) {
            if (!(player instanceof EntityPlayerMP)) {
                continue;
            }
            final EntityPlayerMP playerMP = (EntityPlayerMP) player;
            if (world.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, chunk.x, chunk.z)) {
                sendTo(packet, playerMP);
            }
        }
    }
}
