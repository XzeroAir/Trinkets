package xzeroair.trinkets.races;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
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
	default void doRenderPlayerPre(RenderPlayerEvent.Pre event) {
	}

	@SideOnly(Side.CLIENT)
	default void doRenderPlayerPost(RenderPlayerEvent.Post event) {
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

	default void potionBeingApplied(PotionApplicableEvent event) {
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

	default void bowUsed(ItemStack stack, int charge) {
	}

	default void interact(PlayerInteractEvent event) {
	}

	default void interactWithEntity(PlayerInteractEvent.EntityInteract event) {
	}

	default void dismountedEntity() {
	}

	default void mountedEntity(EntityMountEvent event) {
	}

	default void targetedByEnemy(EntityLivingBase enemy) {
	}

	default float isHurt(DamageSource source, float dmg) {
		return dmg;
	}

	default float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		return dmg;
	}

	default void savedNBTData(NBTTagCompound compound) {
	}

	default void loadNBTData(NBTTagCompound compound) {
	}
}
