package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;

public class SelectRacePacket extends ThreadSafePacket {

    private int raceId;
    private int primaryElementId;

    public SelectRacePacket() {
    }

    public SelectRacePacket(EntityRace race, Element primaryElement) {
        this.raceId = EntityRace.getIdFromRace(race);
        this.primaryElementId = Element.getIdFromElement(primaryElement);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.raceId);
        buf.writeInt(this.primaryElementId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.raceId = buf.readInt();
        this.primaryElementId = buf.readInt();
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        // Client to Server selection request; this handler is unreachable by registration.
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        final EntityRace race = EntityRace.getRaceById(this.raceId);
        final Element primaryElement = Element.getById(this.primaryElementId);
        if (!this.isValidSelection(race, primaryElement)) {
            return;
        }
        final EntityPlayerMP player = server.player;
        Capabilities.getEntityProperties(player, properties -> {
            if (!TrinketsConfig.SERVER.RACES.SELECTION_MENU && !properties.isRaceSelectionAuthorized()) {
                return;
            }
            properties.setOriginalRaceCache(new RaceCache(race, primaryElement));
            properties.consumeRaceSelectionAuthorization();
            properties.scheduleResync();
        });
    }

    private boolean isValidSelection(EntityRace race, Element primaryElement) {
        if ((race == null) || race.isNone() || (primaryElement == null)) {
            return false;
        }
        for (final String blockedRace : TrinketsConfig.SERVER.RACES.BLACKLIST) {
            if (race.getName().equalsIgnoreCase(blockedRace) || race.getRegistryName().toString().equalsIgnoreCase(blockedRace)) {
                return false;
            }
        }
        return true;
    }
}
