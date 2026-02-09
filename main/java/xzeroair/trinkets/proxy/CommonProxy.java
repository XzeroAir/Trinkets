package xzeroair.trinkets.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.container.TrinketInventoryContainer;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.OreDictionaryCompat;
import xzeroair.trinkets.util.registry.EventRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EventBusSubscriber
public class CommonProxy implements IGuiHandler {

    public Side getSide() {
        return Side.SERVER;
    }

    public void preInit(FMLPreInitializationEvent e) {

        //Register Mod Stuff

        //Event Handlers
        EventRegistry.preInit();
        //Other Mod Compatibility
        EventRegistry.modCompatPreInit();
    }

    public void init(FMLInitializationEvent e) {
        OreDictionaryCompat.registerOres();

        EventRegistry.serverInit();

        EventRegistry.init();
        EventRegistry.modCompatInit();
    }

    public void postInit(FMLPostInitializationEvent e) {
        EventRegistry.postInit();
        EventRegistry.modCompatPostInit();
    }

    public void renderEffect(int effectID, World world, double x, double y, double z, double x2, double y2, double z2, int color, float alpha, float intensity) {
    }

    public void playSound(int effectID, World world, double x, double y, double z, float volume, float pitch) {
        if (effectID == 1) {
            world.playSound(((EntityPlayer) null), x, y, z, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.PLAYERS, volume, pitch);
        }
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    public IThreadListener getThreadListener(@Nonnull final MessageContext context) {
        if (context.side.isServer()) {
            return context.getServerHandler().player.getServerWorld();
        } else {
            throw new WrongSideException("Tried to get the IThreadListener from a client-side MessageContext on the dedicated server");
        }
    }

    public EntityPlayer getPlayer(@Nonnull final MessageContext context) {
        if (context.side.isServer()) {
            return context.getServerHandler().player;
        } else {
            throw new WrongSideException("Tried to get the player from a client-side MessageContext on the dedicated server");
        }
    }

    @Nullable
    public EntityLivingBase getEntityLivingBase(@Nonnull MessageContext context, int entityID) {
        if (context.side.isServer()) {
            final Entity entity = context.getServerHandler().player.world.getEntityByID(entityID);
            return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
        }
        throw new WrongSideException("Tried to get the player from a client-side MessageContext on the dedicated server");
    }

    class WrongSideException extends RuntimeException {
        public WrongSideException(final String message) {
            super(message);
        }

        public WrongSideException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world instanceof WorldServer) {
            switch (ID) {
                case Reference.GUI:
                    return new TrinketInventoryContainer(player.inventory, !world.isRemote, player);
                default:
                    return null;
            }
        }
        return null;
    }

    public void spawnParticle(int effectID, World world, double x, double y, double z, double motX, double motY, double motZ, int color, float alpha) {

    }
}
