package xzeroair.trinkets.races.elf;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.ElfEars;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceElf extends EntityRacePropertiesHandler {

	public static final ElfConfig serverConfig = TrinketsConfig.SERVER.races.elf;

	protected boolean critReady = false;
	protected float chargeMultiplier = 0;
	protected UpdatingAttribute bonusSpeed, bonusAtkSpeed;

	public RaceElf(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.elf);
		bonusSpeed = new UpdatingAttribute(e, UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.MOVEMENT_SPEED);
		bonusAtkSpeed = new UpdatingAttribute(e, UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.ATTACK_SPEED);
	}

	@Override
	public void whileTransformed() {
		//		System.out.println(entity.world.getBiome(entity.getPosition()).);
		if (entity.world.getBiome(entity.getPosition()) != null) {
			Set<Type> biomeType = BiomeDictionary.getTypes(entity.world.getBiome(entity.getPosition()));
			if (biomeType.contains(Type.FOREST)) {
				bonusSpeed.addModifier(0.5, 2);
				bonusAtkSpeed.addModifier(0.5, 2);
			}
		}
	}

	@Override
	public void endTransformation() {
		bonusSpeed.removeModifier();
		bonusAtkSpeed.removeModifier();
	}

	@Override
	public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if (serverConfig.charge_shot) {
			if (source.isProjectile() && (dmg > 0) && (chargeMultiplier > 1.5f)) {
				return dmg * chargeMultiplier;
			}
			chargeMultiplier = 0;
		}
		return dmg;
	}

	//TODO Maybe add a special effect for the charge shot?
	@Override
	public void bowDrawing(ItemStack stack, int charge) {
		if (serverConfig.charge_shot) {
			float ManaCost = serverConfig.CS_Cost;
			if (entity.isSneaking()) {
				float tension = 1.5f + (((charge * 100) / 100) * 0.01F);
				float clamp = MathHelper.clamp(tension, 1f, magic.getMana() / ManaCost);
				chargeMultiplier = clamp;
				magic.syncToManaCostToHud(clamp * ManaCost);
			} else {
				magic.syncToManaCostToHud(0);
				chargeMultiplier = 0;
			}
		}
	}

	@Override
	public void bowNocked(ArrowNockEvent event) {
		//		System.out.println("Trigger?");
	}

	// TODO Make Sure Charged Shot is properly working
	@Override
	public void bowUsed(ItemStack itemStack, int charge) {
		if (serverConfig.charge_shot) {
			float multiplier = chargeMultiplier;
			float cost = serverConfig.CS_Cost * multiplier;
			if ((cost > 0) && (magic.getMana() >= cost)) {
				magic.spendMana(cost);
			}
			magic.syncToManaCostToHud(0);
		}
	}

	@Override
	public void jump() {
		if (properties.isTransformed()) {
			entity.motionY *= 1.2F;
			if (entity.isSprinting()) {
				float f = entity.rotationYaw * 0.017453292F;
				entity.motionX -= MathHelper.sin(f) * 0.05F;
				entity.motionZ += MathHelper.cos(f) * 0.05F;
			}
		}
	}

	@Override
	public void fall(LivingFallEvent event) {
		if (properties.isTransformed()) {
			final float base = event.getDistance();
			final float distance = (base * 0.25F);
			event.setDistance(base - distance);
		}
	}

	private ModelBase ears = new ElfEars();

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering || !properties.showTraits()) {
			return;
		}
		ears = new ElfEars();
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
		GlStateManager.color(properties.getTraitColorHandler().getRed(), properties.getTraitColorHandler().getGreen(), properties.getTraitColorHandler().getBlue(), properties.getTraitOpacity());
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		ears.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1F);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();

	}
}
