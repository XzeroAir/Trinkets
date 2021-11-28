package xzeroair.trinkets.races.titan;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.IncreasedReachPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.other.AbilityHeavy;
import xzeroair.trinkets.traits.abilities.other.AbilityLargeHands;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class RaceTitan extends EntityRacePropertiesHandler {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;
	public static List<String> disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);

	//	private UpdatingAttribute speed, attack;

	public RaceTitan(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.titan);
		disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);
	}

	@Override
	public void startTransformation() {
		this.addAbility(Abilities.largeHands, new AbilityLargeHands());
		if (serverConfig.sink) {
			this.addAbility(Abilities.heavy, new AbilityHeavy());
		}
	}

	@Override
	public void onTick() {
		super.onTick();
		if (cooldown > 0) {
			cooldown--;
		} else {
			cooldown = 0;
		}
	}

	@Override
	public void whileTransformed() {
		if (!entity.world.isRemote && entity.isRiding()) {
			final Entity mount = entity.getRidingEntity();
			if ((mount != null) && !this.mountEntity(mount)) {
				entity.dismountRidingEntity();
			}
		}
	}

	@Override
	public boolean mountEntity(Entity mount) {
		if (this.isCreativePlayer()) {
			return true;
		} else if (!serverConfig.canMount) {
			return false;
		} else if (!disallowedMounts.isEmpty()) {
			try {
				final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
				final String modID = regName.getNamespace();
				final String entityID = regName.getPath();
				final boolean doesWildcardExist = disallowedMounts.contains(modID + ":*");
				final boolean exists = disallowedMounts.contains(regName.toString());
				if (doesWildcardExist || exists) {
					return serverConfig.whitelist;
				} else {
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return !serverConfig.whitelist;
		} else {
			return true;
		}
	}

	private float cooldown = 0;

	@Override
	public void interact(PlayerInteractEvent event) {
		final EntityPlayer player = event.getEntityPlayer();
		if (Trinkets.proxy.getSide() == Side.CLIENT) {
			if ((cooldown != 0)) {
				return;
			}
			final KeyBinding lClick = Minecraft.getMinecraft().gameSettings.keyBindAttack;
			final KeyBinding rClick = Minecraft.getMinecraft().gameSettings.keyBindUseItem;
			final IAttributeInstance reach = player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
			if ((reach.getAttributeValue() > 5)) {
				final RayTraceResult result = RayTraceHelper.rayTrace(player, reach.getAttributeValue() * 0.8);
				if ((result != null) && (result.typeOfHit == Type.ENTITY)) {
					final Entity entity = result.entityHit;
					final Vec3d vec = result.hitVec;
					if (lClick.isKeyDown()) {
						NetworkHandler.sendToServer(new IncreasedReachPacket(player, EnumHand.MAIN_HAND, entity, vec.x, vec.y, vec.z));
						cooldown = player.getCooldownPeriod();
						Trinkets.proxy.renderEffect(3, player.getEntityWorld(), result.hitVec.x, result.hitVec.y + (entity.height * 0.5F), result.hitVec.z, 0, 0, 0, 0, 1, 1);
					} else if (rClick.isKeyDown()) {
						NetworkHandler.sendToServer(new IncreasedReachPacket(player, EnumHand.OFF_HAND, entity, vec.x, vec.y, vec.z));
					}
				}
			}
		}

	}

	float ls, pls = 0F;

	// TODO See if I can slow the player movement animation
	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		//		ModelPlayer model = renderer.getMainModel();
		//
		//		if (entity.noClip) {
		//			return;
		//		}
		//
		//		if ((Minecraft.getMinecraft().gameSettings.thirdPersonView >= 1)) {
		//			GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		//			GlStateManager.translate(0.0F, 0.0F, -0.7F);
		//
		//			float swing = entity.limbSwing / 3;
		//
		//			// Head
		//			model.bipedHead.rotateAngleX = -0.95f;
		//			model.bipedHeadwear.rotateAngleX = -0.95f;
		//
		//			// Make sure to rotate both the arm and the arm jacket texture
		//			model.bipedLeftArm.rotateAngleX = swing;
		//			model.bipedRightArm.rotateAngleX = swing;
		//			model.bipedLeftArm.rotateAngleY = swing;
		//			model.bipedRightArm.rotateAngleY = -swing;
		//			model.bipedLeftArmwear.rotateAngleX = swing;
		//			model.bipedRightArmwear.rotateAngleX = swing;
		//			model.bipedLeftArmwear.rotateAngleY = swing;
		//			model.bipedRightArmwear.rotateAngleY = -swing;
		//		}
		//		event.getEntityPlayer().limbSwingAmount *= 0.95F;
		//		//		event.getEntityPlayer().prevLimbSwingAmount = 0;
		//		float ls = event.getEntityPlayer().limbSwingAmount;
		//		float pls = event.getEntityPlayer().prevLimbSwingAmount;
		//		System.out.println(ls + " | " + pls);
		if (entity.isRiding()) {
			double t = 0;
			t = (100 - properties.getSize()) * 0.01D;
			final double t1 = (entity.height * 100) / (1.8 * 100);
			final double t2 = (1.8 * 100) / (entity.height * 100);
			t *= t2 * 0.5D;
			GlStateManager.translate(0, t, 0);
		}
	}

}
