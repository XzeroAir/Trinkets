package xzeroair.trinkets.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.baubles.BaublesHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

import java.util.List;
import java.util.function.BiConsumer;

public class TrinketEventHandler {

    private boolean shouldHandleBaubles() {
        return Trinkets.MOD_COMPAT.Baubles;
    }

    private boolean shouldHandleTrinkets() {
        return !Trinkets.MOD_COMPAT.Baubles || TrinketsConfig.SERVER.GUI.ENABLED;
    }

    private void forEachAccessory(EntityLivingBase entity, BiConsumer<ItemStack, IAccessoryInterface> consumer) {
        if (this.shouldHandleBaubles()) {
            BaublesHelper.getBaublesHandler(entity, handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    final ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty() && (stack.getItem() instanceof IAccessoryInterface)) {
                        consumer.accept(stack, (IAccessoryInterface) stack.getItem());
                    }
                }
            });
        }
        if (this.shouldHandleTrinkets()) {
            TrinketHelper.getTrinketHandler(entity, handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    final ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty() && (stack.getItem() instanceof IAccessoryInterface)) {
                        consumer.accept(stack, (IAccessoryInterface) stack.getItem());
                    }
                }
            });
        }
    }

    private void forEachTrinketSlot(EntityLivingBase entity, BiConsumer<ItemStack, Integer> consumer) {
        TrinketHelper.getTrinketHandler(entity, handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                consumer.accept(handler.getStackInSlot(i), i);
            }
        });
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.END) {
            final EntityPlayer player = Minecraft.getMinecraft().player;
            if ((player == null) || !player.isEntityAlive() || (player.world == null)) {
                return;
            }
            this.forEachAccessory(player, (stack, item) -> item.eventClientTick(stack, player));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void PlayerLoggedInEvent(PlayerLoggedInEvent event) {
        final EntityPlayer player = event.player;
        final World world = player.getEntityWorld();
        if (world != null) {
            final boolean client = world.isRemote;
            this.forEachAccessory(player, (stack, item) -> {
                if (!client && (world instanceof WorldServer)) {
                    Capabilities.getTrinketProperties(stack, properties -> properties.itemEquipped(player));
                }
                item.eventPlayerLogin(stack, player);
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void EntityJoinWorld(EntityJoinWorldEvent event) {
        if (!this.shouldHandleTrinkets()) {
            return;
        }
        final Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP) {
            final EntityPlayerMP player = (EntityPlayerMP) entity;
            final World world = event.getWorld();
            if (world != null) {
                final boolean client = world.isRemote;
                this.forEachTrinketSlot(player, (stack, slot) -> {
                    if (!client && (world instanceof WorldServer)) {
                        if (stack.isEmpty()) {
                            final SyncItemDataPacket packet = new SyncItemDataPacket(player, stack, stack.getTagCompound(), slot, ItemHandlerType.TRINKETS, true, false);
                            NetworkHandler.sendTo(packet, player);
                        } else {
                            Capabilities.getTrinketProperties(stack, properties -> properties.sendInformationToPlayer(player));
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void PlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
        final EntityPlayer player = event.player;
        this.forEachAccessory(player, (stack, item) -> item.eventPlayerLogout(stack, player));
    }

    @SubscribeEvent
    public void playerStartTracking(PlayerEvent.StartTracking event) {
        final EntityPlayer player = event.getEntityPlayer();
        final Entity target = event.getTarget();
        final World world = player.getEntityWorld();
        if (!world.isRemote && (world instanceof WorldServer) && (target instanceof EntityPlayer)) {
            final EntityPlayer targetPlayer = (EntityPlayer) target;
            if (this.shouldHandleBaubles()) {
                BaublesHelper.getBaublesHandler(targetPlayer, handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        final ItemStack stack = handler.getStackInSlot(i);
                        if (stack.isEmpty()) {
                            final SyncItemDataPacket packet = new SyncItemDataPacket(targetPlayer, stack, stack.getTagCompound(), i, ItemHandlerType.BAUBLES, true, false);
                            NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
                        } else {
                            Capabilities.getTrinketProperties(stack, properties -> properties.sendInformationToPlayer(targetPlayer, player));
                        }
                    }
                });
            }
            if (this.shouldHandleTrinkets()) {
                this.forEachTrinketSlot(targetPlayer, (stack, slot) -> {
                    if (stack.isEmpty()) {
                        final SyncItemDataPacket packet = new SyncItemDataPacket(targetPlayer, stack, stack.getTagCompound(), slot, ItemHandlerType.TRINKETS, true, false);
                        NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
                    } else {
                        Capabilities.getTrinketProperties(stack, properties -> properties.sendInformationToPlayer(targetPlayer, player));
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void PlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
        final EntityPlayer player = event.player;
        this.forEachAccessory(player, (stack, item) -> item.eventPlayerChangedDimension(stack, player, event.fromDim, event.toDim));
    }

    @SubscribeEvent
    public void playerUpdate(TickEvent.PlayerTickEvent event) {
        final EntityPlayer player = event.player;
        if (player.isEntityAlive() && (event.phase == Phase.END)) {
            this.forEachAccessory(player, (stack, item) -> item.eventPlayerTick(stack, player));
        }
    }

    @SubscribeEvent
    public void LivingUpdate(LivingEvent.LivingUpdateEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer)) {
            this.forEachAccessory(entity, (stack, item) -> item.eventLivingUpdateTick(stack, entity));
        }
    }

    @SubscribeEvent
    public void potionApplicable(PotionApplicableEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        this.forEachAccessory(entity, (stack, item) -> item.eventPotionApplicable(stack, entity, event));
    }

    @SubscribeEvent
    public void livingJump(LivingJumpEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        this.forEachAccessory(entity, (stack, item) -> item.eventLivingJump(stack, entity));
    }

    @SubscribeEvent
    public void livingFall(LivingFallEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        this.forEachAccessory(entity, (stack, item) -> item.eventLivingFall(stack, entity, event));
    }

    @SubscribeEvent
    public void TargetEvent(LivingSetAttackTargetEvent event) {
        final EntityLivingBase entity = event.getTarget();
        this.forEachAccessory(entity, (stack, item) -> item.eventSetAttackTarget(stack, entity, event.getEntityLiving()));
    }

    @SubscribeEvent
    public void HurtEvent(LivingAttackEvent event) {
        final EntityLivingBase attacked = event.getEntityLiving();
        this.forEachAccessory(attacked, (stack, item) -> item.eventLivingAttacked(stack, attacked, event));
        if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
            final EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
            this.forEachAccessory(attacker, (stack, item) -> item.eventLivingAttacker(stack, attacker, event));
        }
    }

    @SubscribeEvent
    public void HurtEvent(LivingHurtEvent event) {
        final EntityLivingBase attacked = event.getEntityLiving();
        this.forEachAccessory(attacked, (stack, item) -> item.eventLivingHurtAttacked(stack, attacked, event));
        if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
            final EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
            this.forEachAccessory(attacker, (stack, item) -> item.eventLivingHurtAttacker(stack, attacker, event));
        }
    }

    @SubscribeEvent
    public void deathEvent(LivingDamageEvent event) {
        final EntityLivingBase attacked = event.getEntityLiving();
        this.forEachAccessory(attacked, (stack, item) -> item.eventLivingDamageAttacked(stack, attacked, event));
        if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
            final EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
            this.forEachAccessory(attacker, (stack, item) -> item.eventLivingDamageAttacker(stack, attacker, event));
        }
    }

    @SubscribeEvent
    public void experienceDropEvent(LivingExperienceDropEvent event) {
        final EntityPlayer player = event.getAttackingPlayer();
        this.forEachAccessory(player, (stack, item) -> item.eventLivingExperienceDrops(stack, player, event));
    }

    @SubscribeEvent
    public void ItemDropEvent(LivingDropsEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase) event.getSource().getTrueSource();
            this.forEachAccessory(entity, (stack, item) -> item.eventLivingDrops(stack, entity, event));
        }
    }

    @SubscribeEvent
    public void onAttemptToBreakBlock(BreakSpeed event) {
        final EntityPlayer player = event.getEntityPlayer();
        this.forEachAccessory(player, (stack, item) -> item.eventBreakSpeed(stack, player, event));
    }

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        final EntityPlayer player = event.getPlayer();
        this.forEachAccessory(player, (stack, item) -> item.eventBlockBreak(stack, player, event));
    }

    @SubscribeEvent
    public void onBlockDrops(BlockEvent.HarvestDropsEvent event) {
        final EntityPlayer player = event.getHarvester();
        this.forEachAccessory(player, (stack, item) -> item.eventBlockDrops(stack, player, event));
    }

    @SubscribeEvent
    public void playerDeath(PlayerDropsEvent event) {
        if (this.shouldHandleTrinkets() && (event.getEntity() instanceof EntityPlayer) && !event.getEntity().world.isRemote && !event.getEntity().world.getGameRules().getBoolean("keepInventory")) {
            this.dropItemsAt(event.getEntityPlayer(), event.getDrops(), event.getEntityPlayer());
        }
    }

    public void dropItemsAt(EntityPlayer player, List<EntityItem> drops, Entity entity) {
        TrinketHelper.getTrinketHandler(player, trinkets -> {
            for (int i = 0; i < trinkets.getSlots(); i++) {
                final ItemStack stack = trinkets.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    final EntityItem item = new EntityItem(entity.world, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, stack.copy());
                    item.setPickupDelay(40);
                    final float f1 = entity.world.rand.nextFloat() * 0.5F;
                    final float f2 = entity.world.rand.nextFloat() * (float) Math.PI * 2.0F;
                    item.motionX = -MathHelper.sin(f2) * f1;
                    item.motionZ = MathHelper.cos(f2) * f1;
                    item.motionY = 0.20000000298023224D;
                    drops.add(item);
                    trinkets.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        });
    }

    @SubscribeEvent
    public void PlayerCloneEvent(PlayerEvent.Clone event) {
        if (!this.shouldHandleTrinkets()) {
            return;
        }
        try {
            final TrinketContainerHandler original = (TrinketContainerHandler) event.getOriginal().getCapability(TrinketContainerProvider.containerCap, null);
            final NBTTagCompound nbt = original.serializeNBT();
            final TrinketContainerHandler clone = (TrinketContainerHandler) event.getEntityPlayer().getCapability(TrinketContainerProvider.containerCap, null);
            clone.deserializeNBT(nbt);
        } catch (final Exception e) {

        }
    }
}
