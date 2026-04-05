package xzeroair.trinkets.races;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.IRenderRaceHandler;

public interface IRaceHandler {

    default void startTransformation() {
    }

    default void endTransformation() {
    }

    default void whileTranforming() {

    }

    default void whileTransformed() {

    }

    default void jump() {
    }

    default void fall(LivingFallEvent event) {
    }

    default boolean potionBeingApplied(PotionEffect effect) {
        return false;
    }

    default void breakingBlock(BreakSpeed event) {
    }

    default void blockBroken(BreakEvent event) {
    }

    default void blockDrops(HarvestDropsEvent event) {
    }

    default void bowNocked(ArrowNockEvent event) {
    }

    default void bowDrawing(ItemStack stack, int charge) {
    }

    default void bowUsed(ArrowLooseEvent event) {
    }

    default void interact(PlayerInteractEvent event) {
    }

    default void interactWithEntity(PlayerInteractEvent.EntityInteract event) {
    }

    default boolean dismountedEntity(Entity mount) {
        return true;
    }

    default boolean mountEntity(Entity mount) {
        return true;
    }

    /**
     * @param enemy {@link EntityLiving#setAttackTarget(EntityLivingBase)}
     */
    default void targetedByEnemy(EntityLivingBase enemy) {
    }

    default boolean isAttacked(DamageSource source, float dmg) {
        return true;
    }

    default float isHurt(DamageSource source, float dmg) {
        return dmg;
    }

    default float isDamaged(DamageSource source, float dmg) {
        return dmg;
    }

    default boolean attackedEntity(EntityLivingBase target, DamageSource source, float dmg) {
        return true;
    }

    default float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
        return dmg;
    }

    default float damagedEntity(EntityLivingBase target, DamageSource source, float dmg) {
        return dmg;
    }

    default float onHeal(float healAmount) {
        return healAmount;
    }

    default NBTTagCompound savedNBTData(NBTTagCompound compound) {
        return compound;
    }

    default void loadNBTData(NBTTagCompound compound) {
    }

    @SideOnly(Side.CLIENT)
    IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer();

}
