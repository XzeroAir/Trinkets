package xzeroair.trinkets.dev;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties.LinkType;
import xzeroair.trinkets.traits.abilities.AbilityLinkedVitality;
import xzeroair.trinkets.util.TrinketsConfig;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 * Development-only Bonded HUD partner support. Remove this package before a release build.
 */
public final class DevBondedHudTools {

    private static final String COMMAND = "xatdev";
    private static final String PARTNER_NAME = "Bonded Partner";
    private static final String PARTNER_OWNER_TAG = "xat.dev.bondedHudOwner";

    private DevBondedHudTools() {
    }

    public static void register(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDevBondedHud());
        Trinkets.LOGGER.info("Registered Bonded HUD development command: /xatdev bondedhud <spawn|remove>");
    }

    private static final class CommandDevBondedHud extends CommandBase {

        @Override
        public String getName() {
            return COMMAND;
        }

        @Override
        public String getUsage(@Nonnull ICommandSender sender) {
            return "/xatdev bondedhud <spawn|remove>";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public java.util.List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, BlockPos targetPos) {
            return args.length == 1 ? getListOfStringsMatchingLastWord(args, "bondedhud") : args.length == 2 ? getListOfStringsMatchingLastWord(args, "spawn", "remove") : Collections.emptyList();
        }

        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            if (!(sender.getCommandSenderEntity() instanceof EntityPlayerMP) || args.length != 2 || !"bondedhud".equalsIgnoreCase(args[0])) {
                throw new CommandException(this.getUsage(sender));
            }
            final EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
            if ("spawn".equalsIgnoreCase(args[1])) {
                spawnPartner(player);
                sender.sendMessage(new TextComponentString("Spawned Bonded Partner. Use a complete Linking Locket on it, then equip your left half to test the HUD."));
            } else if ("remove".equalsIgnoreCase(args[1])) {
                removePartner(player);
                sender.sendMessage(new TextComponentString("Removed Bonded Partner."));
            } else {
                throw new CommandException(this.getUsage(sender));
            }
        }
    }

    public static boolean isPartnerFor(EntityPlayer player, EntityLivingBase target) {
        return target instanceof EntityCow && player.getUniqueID()
                .toString()
                .equals(target.getEntityData().getString(PARTNER_OWNER_TAG));
    }

    public static boolean isTestLocket(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("xat.dev", 10) && stack.getTagCompound()
                .getCompoundTag("xat.dev")
                .getBoolean("bondedHudLocket");
    }

    public static AbilityLinkedVitality createLinkedVitality() {
        return new DevLinkedVitality();
    }

    public static boolean linkTestPartner(ItemStack stack, EntityPlayer player, EntityLivingBase target) {
        if (player.world.isRemote || !isPartnerFor(player, target) || stack.getMetadata() != 0) {
            return false;
        }
        Capabilities.getTrinketProperties(stack, properties -> {
            if (!properties.hasLinkedEntity()) {
                properties.linkEntity(target, LinkType.PLAYER);
                stack.getOrCreateSubCompound("xat.dev").setBoolean("bondedHudLocket", true);
                stack.setItemDamage(1);
            }
        });
        return true;
    }

    private static void spawnPartner(EntityPlayerMP player) {
        removePartner(player);
        final EntityCow partner = new EntityCow(player.getServerWorld());
        partner.setPositionAndRotation(player.posX + 2.0D, player.posY, player.posZ, player.rotationYaw + 180.0F, 0.0F);
        partner.setCustomNameTag(PARTNER_NAME);
        partner.setAlwaysRenderNameTag(true);
        partner.setNoAI(true);
        partner.setNoGravity(true);
        partner.getEntityData().setString(PARTNER_OWNER_TAG, player.getUniqueID().toString());
        player.getServerWorld().spawnEntity(partner);
    }

    private static void removePartner(EntityPlayerMP player) {
        for (final net.minecraft.entity.Entity entity : player.getServerWorld().loadedEntityList) {
            if (entity instanceof EntityLivingBase && isPartnerFor(player, (EntityLivingBase) entity)) {
                entity.setDead();
            }
        }
    }

    private static final class DevLinkedVitality extends AbilityLinkedVitality {

        private DevLinkedVitality() {
            super(TrinketsConfig.SERVER.ITEMS.LINKING_LOCKET.ABILITIES.LINKED_VITALITY);
        }

        @Override
        public void tickAbility(EntityLivingBase entity) {
            if (!entity.world.isRemote) {
                this.syncLinkedPartnerInfo(entity);
            }
            super.tickAbility(entity);
        }

        @Override
        protected boolean linkedPartnerHasAbility(EntityLivingBase wearer, EntityLivingBase partner) {
            return wearer instanceof EntityPlayer && this.linkedPartnerUUID != null && this.linkedPartnerUUID.equals(partner.getUniqueID()) && isPartnerFor((EntityPlayer) wearer, partner);
        }
    }
}
