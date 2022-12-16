package xzeroair.trinkets.events;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.entity.ai.EnderAiEdit;
import xzeroair.trinkets.entity.ai.EnderMoveAI;
import xzeroair.trinkets.entity.ai.EnderQueensKnightAI;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;

public class EnderQueenHandler {

	private ConfigEnderCrown serverConfig = TrinketsConfig.SERVER.Items.ENDER_CROWN;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void soundEvent(PlaySoundEvent event) {
		final EntityPlayerSP player = Minecraft.getMinecraft().player;
		if ((player != null) && (player.world != null)) {
			if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)
					|| TrinketHelper.entityHasAbility(Abilities.enderQueen.toString(), player)) {
				if (event.getSound().getSoundLocation().toString().contentEquals("minecraft:entity.endermen.stare")) {
					event.setResultSound(null);
				}
			}
		}
	}

	@SubscribeEvent
	public void EndermanJoinWorld(EntityJoinWorldEvent event) {
		//Add Tiara AI to Enderman
		if (event.getEntity() instanceof EntityEnderman) {
			final EntityEnderman ender = (EntityEnderman) event.getEntity();

			for (final Object a : ender.targetTasks.taskEntries.toArray()) {
				final EntityAIBase ai = ((EntityAITaskEntry) a).action;
				if (ai.toString().startsWith("net.minecraft.entity.monster.EntityEnderman$AIFindPlayer")) {
					ender.targetTasks.removeTask(ai);
				}
			}
			ender.targetTasks.addTask(1, new EnderAiEdit(ender));
			ender.targetTasks.addTask(2, new EnderQueensKnightAI(ender));
			if (serverConfig.Follow) {
				ender.targetTasks.addTask(3, new EnderMoveAI(ender));
			}
		}
	}

	@SubscribeEvent
	public void EnderTeleportEvent(EnderTeleportEvent event) {
		if (!TrinketsConfig.SERVER.Items.ENDER_CROWN.teleport) {
			return;
		}
		final Entity entity = event.getEntity();
		if (entity == null) {
			return;
		}
		final boolean isPlayer = entity instanceof EntityPlayer;
		boolean pvpEnabled = false;
		if (isPlayer) {
			try {
				if (entity instanceof EntityPlayerMP) {
					pvpEnabled = ((EntityPlayerMP) entity).getServer().isPVPEnabled();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		if ((entity instanceof EntityEnderman) || (isPlayer && pvpEnabled)) {
			final AxisAlignedBB bBox = entity.getEntityBoundingBox().grow(16, 4, 16);
			//			final List<EntityPlayer> entLivList = event.getEntity().getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, bBox);
			final List<EntityPlayer> entLivList = entity.getEntityWorld().getEntitiesWithinAABB(
					EntityPlayer.class, bBox,
					Predicates.and(
							EntitySelectors.NOT_SPECTATING,
							player -> ((player != null) && TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara))
									|| TrinketHelper.entityHasAbility(Abilities.enderQueen.toString(), player)
					)
			);
			if (!entLivList.isEmpty() && !entLivList.contains(entity)) {
				if (!entity.isInWater()) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
	}

	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
	}

}
