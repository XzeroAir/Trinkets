package xzeroair.trinkets.events;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.entity.ai.EnderAiEdit;
import xzeroair.trinkets.entity.ai.EnderMoveAI;
import xzeroair.trinkets.entity.ai.EnderQueensKnightAI;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import java.util.List;

public class EnderQueenHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void soundEvent(PlaySoundEvent event) {
        if (!event.getSound().getSoundLocation().toString().contentEquals(Reference.MINECRAFT_ENDER_MAN_SCREAM)) {
            return;
        }
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
        if ((player == null) || (player.world == null)) {
            return;
        }
        boolean hasAbility = TrinketHelper.entityHasAbility(player, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN);
        if (hasAbility) {
            event.setResultSound(null);
            return;
        }
        boolean hasArmorEquipped = !(TrinketHelper.getHead(player, stack -> (stack.getItem().getRegistryName().toString().compareTo(Reference.MODID + ":" + TrinketsRegistryNames.ModItems.ENDER_TIARA) == 0)).isEmpty());
        if (hasArmorEquipped) {
            event.setResultSound(null);
            return;
        }
        if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)) {
            event.setResultSound(null);
        }
    }

    @SubscribeEvent
    public void EndermanJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote || !(event.getEntity() instanceof EntityEnderman)) {
            return;
        }

        final EntityEnderman enderman = (EntityEnderman) event.getEntity();
        final NBTTagCompound data = enderman.getEntityData();
        data.removeTag(EnderQueensKnightAI.FOLLOWING_TAG);
        if (!TrinketsConfig.SERVER.ITEMS.ENDER_CROWN.ENABLED || !TrinketsConfig.SERVER.ITEMS.ENDER_CROWN.ABILITIES.ENDER_QUEEN.ENABLED) {
            return;
        }

        boolean hasEnderAiEdit = false;
        boolean hasKnightAi = false;
        for (final Object taskEntry : enderman.targetTasks.taskEntries.toArray()) {
            final EntityAIBase ai = ((EntityAITaskEntry) taskEntry).action;
            hasEnderAiEdit |= ai instanceof EnderAiEdit;
            hasKnightAi |= ai instanceof EnderQueensKnightAI;
            if (ai.toString().startsWith("net.minecraft.entity.monster.EntityEnderman$AIFindPlayer")) {
                enderman.targetTasks.removeTask(ai);
            }
        }
        if (!hasEnderAiEdit) {
            enderman.targetTasks.addTask(1, new EnderAiEdit(enderman));
        }
        if (!hasKnightAi) {
            enderman.targetTasks.addTask(2, new EnderQueensKnightAI(enderman));
        }
        if (TrinketsConfig.SERVER.ITEMS.ENDER_CROWN.ABILITIES.ENDER_QUEEN.ENDERMAN_FOLLOW) {
            boolean hasFollowAi = false;
            for (final Object taskEntry : enderman.tasks.taskEntries.toArray()) {
                hasFollowAi |= ((EntityAITaskEntry) taskEntry).action instanceof EnderMoveAI;
            }
            if (!hasFollowAi) {
                enderman.tasks.addTask(3, new EnderMoveAI(enderman));
            }
        }
    }

    @SubscribeEvent
    public void EnderTeleportEvent(EnderTeleportEvent event) {
        if (!TrinketsConfig.SERVER.ITEMS.ENDER_CROWN.ABILITIES.ENDER_QUEEN.BLOCK_TELEPORTATION) {
            return;
        }
        final Entity entity = event.getEntity();
        if ((entity == null) || entity.isInWater()) {
            return;
        }

        if (this.blockEntityTeleport(entity) || (this.blockPlayerTeleport(entity))) {
            final AxisAlignedBB bBox = entity.getEntityBoundingBox().grow(16, 4, 16);
            //@formatter:off
            final List<EntityLivingBase> entLivList = entity.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, bBox,
                    Predicates.and(EntitySelectors.NOT_SPECTATING, e -> e != entity &&
                    (TrinketHelper.isEntityBoss(e) ||
                    !(TrinketHelper.getHead(e, stack ->
                    (stack.getItem().getRegistryName().toString().compareTo(Reference.MODID + ":" + TrinketsRegistryNames.ModItems.ENDER_TIARA) == 0))
                    .isEmpty()) ||
                    TrinketHelper.entityHasAbility(e, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN) ||
                    TrinketHelper.AccessoryCheck(e, ModItems.trinkets.TrinketEnderTiara))));
            //@formatter:on
            if (!entLivList.isEmpty()) {
                event.setCanceled(true);
            }
        }
    }

    protected boolean blockPlayerTeleport(Entity entity) {
        if (entity instanceof EntityPlayer) {
            try {
                if (entity instanceof EntityPlayerMP) {
                    return ((EntityPlayerMP) entity).getServer().isPVPEnabled();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected boolean blockEntityTeleport(Entity entity) {
        if (!(entity instanceof EntityPlayer) && (entity instanceof EntityLivingBase)) {
            return true;
        }
        return false;
    }

}
