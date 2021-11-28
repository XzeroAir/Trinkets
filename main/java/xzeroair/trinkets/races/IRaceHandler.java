package xzeroair.trinkets.races;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRaceHandler {

	@SideOnly(Side.CLIENT)
	default void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
	}

	@SideOnly(Side.CLIENT)
	default void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
	}

	@SideOnly(Side.CLIENT)
	default void doRenderPlayerPost(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
	}

	@SideOnly(Side.CLIENT)
	default void doRenderLivingSpecialsPre(RenderLivingEvent.Specials.Pre event) {
	}

	@SideOnly(Side.CLIENT)
	default void doRenderLivingSpecialsPost(RenderLivingEvent.Specials.Post event) {
	}

	@SideOnly(Side.CLIENT)
	default void doRenderLivingPre(RenderLivingEvent.Pre event) {
	}

	@SideOnly(Side.CLIENT)
	default void doRenderLivingPost(RenderLivingEvent.Post event) {
	}

	@SideOnly(Side.CLIENT)
	default void onClientTick() {
	}

	default void startTransformation() {
	}

	default void endTransformation() {
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

	default ActionResult<ItemStack> bowNocked(World world, ItemStack bow, EnumHand hand, ActionResult<ItemStack> result, boolean hasAmmo) {
		return result;
	}

	default void bowDrawing(ItemStack stack, int charge) {
	}

	default void bowUsed(ItemStack stack, int charge) {
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

	default void savedNBTData(NBTTagCompound compound) {
	}

	default void loadNBTData(NBTTagCompound compound) {
	}
}
