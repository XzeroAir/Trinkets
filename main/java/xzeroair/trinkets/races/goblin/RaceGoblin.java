package xzeroair.trinkets.races.goblin;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.client.model.GoblinEars;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;

public class RaceGoblin extends EntityRacePropertiesHandler {

	public static final GoblinConfig serverConfig = TrinketsConfig.SERVER.races.goblin;
	private int cooldown = 0;

	public RaceGoblin(@Nonnull EntityLivingBase e) {
		super(e, EntityRaces.goblin);
	}

	@Override
	public void startTransformation() {
		this.addAbility(new AbilityClimbing());
	}

	@Override
	public void whileTransformed() {
		if (entity.getRidingEntity() instanceof AlphaWolf) {
			if (!entity.isPotionActive(MobEffects.STRENGTH)) {
				entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100, 0, false, false));
			}
			if (!entity.isPotionActive(MobEffects.REGENERATION)) {
				entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0, false, false));
			}
		}
		if (cooldown > 0) {
			cooldown--;
		}
	}

	@Override
	public void targetedByEnemy(EntityLivingBase enemy) {
		if (enemy instanceof EntityCreeper) {
			((EntityCreeper) enemy).setAttackTarget(null);
			((EntityCreeper) enemy).setCreeperState(-1);
		}
	}

	@Override
	public float isHurt(DamageSource source, float dmg) {
		if (source.isExplosion()) {
			if (!TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketDamageShield)) {
				final float maxHP = entity.getHealth();
				final float modifier = ((maxHP * 100) / 20F) * 0.01F;
				final float clampModifier = MathHelper.clamp(modifier, 0.01F, 1F);
				return dmg * clampModifier;
			}
		}
		if (source.isFireDamage()) {
			float modifier = 0.8F;
			if (source.getDamageType().equalsIgnoreCase("Lava")) {
				modifier = 0.5f;
			}
			return dmg * modifier;
		}
		return dmg;
	}

	public boolean MountWolf(EntityWolf wolf) {
		final World world = entity.getEntityWorld();
		final boolean canRide = serverConfig.rider;
		final boolean isOwner = (wolf.isTamed() && wolf.isOwner(entity));
		if (!canRide || !isOwner || wolf.isChild()) {
			return false;
		}
		final ItemStack mainStack = entity.getHeldItemMainhand();
		final ItemStack offStack = entity.getHeldItemOffhand();
		if (mainStack.getItem().getRegistryName().getNamespace().equalsIgnoreCase("carryon") ||
				offStack.getItem().getRegistryName().getNamespace().equalsIgnoreCase("carryon")) {
			return false;
		}
		boolean flag = mainStack.isEmpty();
		if (entity instanceof EntityPlayer) {
			if (mainStack.getItem().onLeftClickEntity(mainStack, (EntityPlayer) entity, wolf)) {
				flag = false;
			}
		}
		if (flag) {
			if (!world.isRemote) {
				if (!entity.isRiding()) {
					final AlphaWolf newWolf = new AlphaWolf(world);
					final NBTTagCompound tag = new NBTTagCompound();
					wolf.writeToNBT(tag);
					tag.setString("id", EntityList.getKey(wolf.getClass()).toString());
					newWolf.storeOldWolf(tag);
					newWolf.setCustomNameTag(wolf.getCustomNameTag());
					newWolf.setLocationAndAngles(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, 0F);
					newWolf.setTamedBy(entity);
					newWolf.setHealth(wolf.getHealth());
					world.spawnEntity(newWolf);
					entity.startRiding(newWolf);
					wolf.setDead();
					return true;
				} else {
					//					int mountID = entity.getRidingEntity().getEntityId();
					//					int attackedID = wolf.getEntityId();
					//					System.out.println(mountID + "|" + attackedID);
					//					if (mountID == attackedID) {
					//						return true;
					//					}
				}
			}
		}
		return false;
	}

	@Override
	public void interact(PlayerInteractEvent event) {
		final boolean canRide = serverConfig.rider;
		if (!canRide) {
			return;
		}
		if (entity.isRiding() && (entity.getRidingEntity() instanceof AlphaWolf)) {
			if ((cooldown == 0) && (event.getHand() == EnumHand.OFF_HAND)) {
				double reachDistance = 5;
				final IAttributeInstance reachAttribe = entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
				if (reachAttribe != null) {
					reachDistance = reachAttribe.getAttributeValue();
				}

				final AlphaWolf wolf = (AlphaWolf) entity.getRidingEntity();
				wolf.MountedAttack(event.getEntityPlayer(), reachDistance);
				cooldown = 30;
			}
		}
	}

	@Override
	public void interactWithEntity(PlayerInteractEvent.EntityInteract event) {
	}

	@Override
	public boolean attackedEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if (target instanceof EntityWolf) {
			final EntityWolf wolf = (EntityWolf) target;
			if (this.MountWolf(wolf)) {
				// TODO Hurts Wolf before spawn for some weird reason
				//				System.out.println("Mounted");
				return false;
			}
		}
		//		System.out.println(dmg + "");
		return super.attackedEntity(target, source, dmg);
	}

	private final ModelBase ears = new GoblinEars();

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering) {
			return;
		}
		if (this.showTraits()) {
			GlStateManager.pushMatrix();
			if (entity.isSneaking()) {
				GlStateManager.translate(0, 0.2, 0);
			}
			if (renderer instanceof RenderPlayer) {
				final RenderPlayer rend = (RenderPlayer) renderer;
				rend.getMainModel().bipedHead.postRender(scale);
			}
			if (entity.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
				GlStateManager.translate(0.0F, -0.02F, -0.045F);
				GlStateManager.scale(1.1F, 1.1F, 1.1F);
			}
			final float[] rgb = ColorHelper.getRGBColor(this.getTraitColor());
			GlStateManager.color(
					rgb[0],
					rgb[1],
					rgb[2]
			);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			ears.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1F);
			GlStateManager.enableLighting();
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.popMatrix();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		//		super.doRenderPlayerPre(entity, x, y, z, renderer, partialTick);
		final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if ((entity == Minecraft.getMinecraft().player) && (screen != null) && !((screen instanceof GuiChat) || (screen instanceof GuiEntityProperties) || (screen instanceof ManaHud))) {
			return;
		}
		if ((this.isTransforming() || this.isTransformed()) && !properties.isNormalSize()) {
			final double hScale = properties.getHeightValue() * 0.01D;
			final double wScale = properties.getWidthValue() * 0.01D;
			final double xLoc = (x / wScale) - x;
			final double yLoc = (y / hScale) - y;
			final double zLoc = (z / wScale) - z;

			final double yOffset = entity.getYOffset();
			final Entity mount = entity.getRidingEntity();
			//			double vanillaOffset = mount.posY + mount.getMountedYOffset() + entity.getYOffset();// + 0.15 * prevRearingAmount
			final double mountedOffset = entity.isRiding() && (mount != null) ? (mount.getMountedYOffset()) : 0;
			//			final double offsetDifference = entity.isRiding() && (mount != null) ? (mount.height - mountedOffset) : 0;
			//						final double retMountedOffset = mountedOffset - ((offsetDifference) * 0.66D);
			final double retMountedOffset = -(mountedOffset + yOffset) - 0.1D;
			if (entity.isRiding() && !(mount instanceof AlphaWolf)) {
				GlStateManager.translate(0, mountedOffset, 0);
				GlStateManager.translate(0, -yOffset, 0);
				GlStateManager.translate(0, retMountedOffset, 0);
			}
			GlStateManager.scale(wScale, hScale, wScale);
			if (entity.isRiding() && !(mount instanceof AlphaWolf)) {
				GlStateManager.translate(0, -retMountedOffset, 0);
				GlStateManager.translate(0, yOffset, 0);
				GlStateManager.translate(0, -mountedOffset, 0);
			}
			GlStateManager.translate(xLoc, yLoc, zLoc);
		}
	}

}
