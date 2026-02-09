package xzeroair.trinkets.client.gui;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.IGuiHandler;
import xzeroair.trinkets.container.TrinketInventoryContainer;
import xzeroair.trinkets.util.Reference;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world instanceof WorldClient) {
            switch (ID) {
                case Reference.GUI:
                    return new TrinketGui(player);
            }
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world instanceof WorldServer) {
            switch (ID) {
                case Reference.GUI:
                    return new TrinketInventoryContainer(player.inventory, !world.isRemote, player);
            }
        }
        return null;
    }
}