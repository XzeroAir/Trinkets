package xzeroair.trinkets.client.renderLayers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public final class RaceSelectionPreviewContext {

    @Nullable
    private static EntityPlayer previewPlayer;
    @Nullable
    private static EntityRacePropertiesHandler previewHandler;

    private RaceSelectionPreviewContext() {
    }

    public static void begin(EntityPlayer player, EntityRacePropertiesHandler handler) {
        previewPlayer = player;
        previewHandler = handler;
    }

    public static void end() {
        previewPlayer = null;
        previewHandler = null;
    }

    @Nullable
    public static EntityRacePropertiesHandler getHandler(EntityPlayer player) {
        return player == previewPlayer ? previewHandler : null;
    }
}