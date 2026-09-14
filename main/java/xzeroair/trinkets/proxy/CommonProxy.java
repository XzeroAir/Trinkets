package xzeroair.trinkets.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.container.TrinketInventoryContainer;
import xzeroair.trinkets.events.*;
import xzeroair.trinkets.init.ModEntities;
import xzeroair.trinkets.init.ModSounds;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.OreDictionaryCompat;
import xzeroair.trinkets.util.compat.elenaidodge.ElenaiDodgeCompat;
import xzeroair.trinkets.util.compat.firstaid.FirstAidDamageEvent;

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
        ModEntities.registerEntities();
        //Other Mod Compatibility

    }

    public void init(FMLInitializationEvent e) {
        OreDictionaryCompat.registerOres();

        ModSounds.init();

        MinecraftForge.EVENT_BUS.register(OnWorldJoinHandler.instance);

        MinecraftForge.EVENT_BUS.register(PlayerEventMC.instance);

        MinecraftForge.EVENT_BUS.register(EventHandler.instance);

        MinecraftForge.EVENT_BUS.register(EnderQueenHandler.instance);

        MinecraftForge.EVENT_BUS.register(CombatHandler.instance);
        MinecraftForge.EVENT_BUS.register(EntityWrapperLifecycleHandler.instance);

        MinecraftForge.EVENT_BUS.register(MovementHandler.instance);

        MinecraftForge.EVENT_BUS.register(BlockBreakEvents.instance);
        MinecraftForge.EVENT_BUS.register(GuideAdvancementHandler.instance);

        MinecraftForge.EVENT_BUS.register(TrinketEventHandler.instance);
        if (Trinkets.MOD_COMPAT.FirstAid) {
            MinecraftForge.EVENT_BUS.register(FirstAidDamageEvent.instance);
        }
        if (Trinkets.MOD_COMPAT.ElenaiDodge1 && TrinketsConfig.getClientStore().MOD_COMPAT_ELENAI_DODGE) {
            MinecraftForge.EVENT_BUS.register(ElenaiDodgeCompat.instance);
        }
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    public void renderEffect(int effectID, World world, double x, double y, double z, double x2, double y2, double z2, int color, float alpha, float intensity) {
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

}
