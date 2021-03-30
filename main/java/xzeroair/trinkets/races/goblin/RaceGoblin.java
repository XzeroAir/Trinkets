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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.GoblinEars;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.AlphaWolfPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.ClimbHandler;
import xzeroair.trinkets.util.helpers.ColorHelper;

public class RaceGoblin extends EntityRacePropertiesHandler {

	public static final GoblinConfig serverConfig = TrinketsConfig.SERVER.races.goblin;
	private ClimbHandler climbing;
	private int cooldown = 0;

	public RaceGoblin(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.goblin);
		color = new ColorHelper().setColor(properties.getTraitColor()).setAlpha(properties.getTraitOpacity());
		climbing = new ClimbHandler(e, e.world);
	}

	@Override
	public void endTransformation() {
		if (entity.getRidingEntity() instanceof EntityWolf) {
			this.DismountWolf((EntityWolf) entity.getRidingEntity());
		}
	}

	@Override
	public void whileTransformed() {
		if (entity instanceof EntityPlayer) { // Start Player Effects
			EntityPlayer player = (EntityPlayer) entity;

		} else { // End player effects

		}
		if (entity.getRidingEntity() instanceof AlphaWolf) {
			//			if (entity.isSneaking()) {
			//				this.DismountWolf((EntityWolf) entity.getRidingEntity());
			//			}
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
				float maxHP = entity.getHealth();
				float modifier = ((maxHP * 100) / 20F) * 0.01F;
				float clampModifier = MathHelper.clamp(modifier, 0.01F, 1F);
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
		//TODO FIx this, It's taking MP when Wolf is untamed because of how I have the spend mana set up
		if (!wolf.isTamed() || entity.isSneaking() || (magic.spendMana(magic.getMaxMana() * 0.6F) == false)) {
			return;
		}
		if (wolf.isTamed() && (wolf.getOwner() == entity)) {
			if (!entity.world.isRemote) {
				AlphaWolf newWolf = new AlphaWolf(entity.world);
				NBTTagCompound tag = new NBTTagCompound();
				wolf.writeToNBT(tag);
				tag.setString("id", EntityList.getKey(wolf.getClass()).toString());
				newWolf.storeOldWolf(tag);
				newWolf.setCustomNameTag(wolf.getCustomNameTag());
				newWolf.setLocationAndAngles(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, 0F);
				newWolf.setTamedBy(entity);
				newWolf.setHealth(wolf.getHealth());
				entity.world.spawnEntity(newWolf);
				entity.startRiding(newWolf);
				wolf.setDead();
			}
		}
	}

	//Client Side, sending to server
	public void DismountWolf(EntityWolf wolf) {
		if (entity.getEntityWorld().isRemote) {
			NetworkHandler.INSTANCE.sendToServer(new AlphaWolfPacket(wolf));
		}
		entity.dismountRidingEntity();
	}

	@Override
	public void interact(PlayerInteractEvent event) {
		if (event.getEntityPlayer().getRidingEntity() instanceof AlphaWolf) {
			if (event.getHand() == EnumHand.OFF_HAND) {
				IAttributeInstance reach = event.getEntityPlayer().getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
				AlphaWolf wolf = (AlphaWolf) event.getEntityPlayer().getRidingEntity();
				wolf.MountedAttack(event.getEntityPlayer(), reach.getAttributeValue());
			}
		}
	}

	@Override
	public void interactWithEntity(PlayerInteractEvent.EntityInteract event) {
		Entity target = event.getTarget();
		if ((event.getHand() == EnumHand.OFF_HAND) && (target instanceof EntityWolf)) {
			EntityWolf wolf = (EntityWolf) target;
			this.MountWolf(wolf);
		}
	}

	private ModelBase ears = new GoblinEars();

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering || (!properties.showTraits())) {
			return;
		}
		if (color == null) {
			color = new ColorHelper().setColor(properties.getTraitColor()).setAlpha(properties.getTraitOpacity());
		} else {
			if (color.getDecimal() != color.setColor(properties.getTraitColor()).getDecimal()) {
				color = new ColorHelper().setColor(properties.getTraitColor()).setAlpha(properties.getTraitOpacity());
			}
		}
		GlStateManager.pushMatrix();
		if (entity.isSneaking()) {
			GlStateManager.translate(0, 0.2, 0);
		}
		if (renderer instanceof RenderPlayer) {
			RenderPlayer rend = (RenderPlayer) renderer;
			rend.getMainModel().bipedHead.postRender(scale);
		}
		if (entity.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
			GlStateManager.translate(0.0F, -0.02F, -0.045F);
			GlStateManager.scale(1.1F, 1.1F, 1.1F);
		}
		GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), properties.getTraitOpacity());
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		ears.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1F);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(RenderPlayerEvent.Pre event) {
		if (entity.isRiding()) {
			if (!(entity.getRidingEntity() instanceof AlphaWolf)) {
				double t = 0;
				t = (100 - properties.getSize()) * 0.01D;
				double t1 = (entity.height * 100) / (1.8 * 100);
				double t2 = (1.8 * 100) / (entity.height * 100);
				t *= t2 * 0.5D;
				GlStateManager.translate(0, t, 0);
			}
		}
	}

}
