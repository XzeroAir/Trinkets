package xzeroair.trinkets.events;

import java.util.List;

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
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;

public class EnderQueenHandler {

	private ConfigEnderCrown serverConfig = TrinketsConfig.SERVER.Items.ENDER_CROWN;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void soundEvent(PlaySoundEvent event) {
		if (!event.getSound().getSoundLocation().toString().contentEquals("minecraft:entity.endermen.stare")) {
			return;
		}
		final EntityPlayerSP player = Minecraft.getMinecraft().player;
		if ((player == null) || (player.world == null)) {
			return;
		}
		if (TrinketHelper.entityHasAbility(Abilities.enderQueen.toString(), player) ||
				(TrinketHelper.getSlotInfoForArmor(
						player,
						stack -> !stack.isEmpty() &&
								stack.getItem().getRegistryName().toString().contentEquals(ModItems.trinkets.TrinketEnderTiara.toString())
				) != null) ||
				TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)) {
			event.setResultSound(null);
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
		if (TrinketsConfig.SERVER.Items.ENDER_CROWN.teleport) {
			return;
		}
		final Entity entity = event.getEntity();
		if ((entity == null) || entity.isInWater()) {
			return;
		}

		if (this.blockEntityTeleport(entity) || (this.blockPlayerTeleport(entity))) {
			final AxisAlignedBB bBox = entity.getEntityBoundingBox().grow(16, 4, 16);
			final List<EntityLivingBase> entLivList = entity.getEntityWorld().getEntitiesWithinAABB(
					EntityLivingBase.class, bBox,
					Predicates.and(
							EntitySelectors.NOT_SPECTATING,
							player -> TrinketHelper.entityHasAbility(Abilities.enderQueen.toString(), player) ||
									(TrinketHelper.getSlotInfoForArmor(
											player,
											stack -> !stack.isEmpty() &&
													stack.getItem().getRegistryName().toString().contentEquals(ModItems.trinkets.TrinketEnderTiara.toString())
									) != null) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara)
					)
			);
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
