package xzeroair.trinkets.races.goblin;

import javax.annotation.Nonnull;
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
import net.minecraft.item.ItemFood;
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
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.GoblinEars;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceGoblin extends EntityRacePropertiesHandler {

	public static final GoblinConfig serverConfig = TrinketsConfig.SERVER.races.goblin;
	private int cooldown = 0;

	public RaceGoblin(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.goblin);
	}

	@Override
	public void startTransformation() {
		this.addAbility(Abilities.blockClimbing, new AbilityClimbing());
		try {
			final Entity mount = entity.getRidingEntity();
			if (mount instanceof AlphaWolf) {
				mount.dismountRidingEntity();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endTransformation() {
		try {
			final Entity mount = entity.getRidingEntity();
			if (mount instanceof AlphaWolf) {
				mount.dismountRidingEntity();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
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

	public void MountWolf(EntityWolf wolf) {
		if (wolf.isTamed() && wolf.isOwner(entity)) {
			final World world = entity.getEntityWorld();
			if (!world.isRemote) {
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
			}
		}
	}

	@Override
	public void interact(PlayerInteractEvent event) {
		final boolean canRide = serverConfig.rider;
		if (!canRide) {
			return;
		}
		if (entity.getRidingEntity() instanceof AlphaWolf) {
			if (event.getHand() == EnumHand.OFF_HAND) {
				double reachDistance = 5;
				final IAttributeInstance reachAttribe = entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
				if (reachAttribe != null) {
					reachDistance = reachAttribe.getAttributeValue();
				}

				final AlphaWolf wolf = (AlphaWolf) entity.getRidingEntity();
				wolf.MountedAttack(event.getEntityPlayer(), reachDistance);
			}
		}
	}

	@Override
	public void interactWithEntity(PlayerInteractEvent.EntityInteract event) {
		final boolean canRide = serverConfig.rider;
		if (!canRide) {
			return;
		}
		final Entity target = event.getTarget();
		if ((event.getHand() == EnumHand.OFF_HAND) && (target instanceof EntityWolf)) {
			final EntityWolf wolf = (EntityWolf) target;
			if (wolf.isChild()) {
				return;
			}
			final ItemStack mainStack = entity.getHeldItemMainhand();
			final ItemStack offStack = entity.getHeldItemOffhand();

			final boolean flag = !mainStack.isEmpty() && !(mainStack.getItem() instanceof ItemFood);
			final boolean flag1 = !offStack.isEmpty();
			final boolean hasBone = flag || flag1;
			if (mainStack.getItem().getRegistryName().getNamespace().equalsIgnoreCase("carryon") || offStack.getItem().getRegistryName().getNamespace().equalsIgnoreCase("carryon")) {
				return;
			}
			if (wolf.isTamed() && wolf.isOwner(entity) && hasBone) {
				this.MountWolf(wolf);
			}
		}
	}

	@Override
	public boolean mountEntity(Entity mount) {
		return super.mountEntity(mount);
	}

	@Override
	public boolean dismountedEntity(Entity mount) {
		//		if (mount instanceof AlphaWolf) {
		//			((AlphaWolf) mount).dismountEntity(entity);
		//			return true;
		//		} else {
		return super.dismountedEntity(mount);
		//		}
	}

	private final ModelBase ears = new GoblinEars();

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering || !properties.showTraits()) {
			return;
		}
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
		GlStateManager.color(properties.getTraitColorHandler().getRed(), properties.getTraitColorHandler().getGreen(), properties.getTraitColorHandler().getBlue());
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

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		if (entity.isRiding()) {
			if (!(entity.getRidingEntity() instanceof AlphaWolf)) {
				double t = 0;
				t = (100 - properties.getSize()) * 0.01D;
				final double t1 = (entity.height * 100) / (1.8 * 100);
				final double t2 = (1.8 * 100) / (entity.height * 100);
				t *= t2 * 0.5D;
				GlStateManager.translate(0, t, 0);
			}
		}
	}

}
