package xzeroair.trinkets.races.faelis;

import java.util.UUID;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.client.model.FaelisEars;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class RaceFaelis extends EntityRacePropertiesHandler {

	public static FaelisConfig serverConfig = TrinketsConfig.SERVER.races.faelis;
	protected UpdatingAttribute movement, jump;

	public RaceFaelis(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.faelis);
		movement = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), SharedMonsterAttributes.MOVEMENT_SPEED);
		jump = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), JumpAttribute.Jump);
	}

	@Override
	public void startTransformation() {
		//		this.addAbility(Abilities.blockClimbing, new AbilityClimbing());
	}

	@Override
	public void whileTransformed() {
		if (entity.world.isRemote) {
			return;
		}
		boolean hasMilkBuff = false;
		if (TrinketsConfig.SERVER.races.faelis.penalties) {
			double amount = 0;
			final double weight = serverConfig.penalty_amount;//0.0125;
			final StatusHandler status = Capabilities.getStatusHandler(entity);
			if (status != null) {
				if (status.getActiveEffects().containsKey(StatusEffectsEnum.Invigorated.getName())) {
					hasMilkBuff = true;
				} else {
					hasMilkBuff = false;
				}
			}
			try {
				for (final ItemStack stack : entity.getArmorInventoryList()) {
					if (!stack.isEmpty() && (stack.getItem() instanceof ItemArmor)) {
						final double w = ConfigHelper.parseItemArmor(stack, serverConfig.heavyArmor);
						amount -= w == 0 ? weight : w;
					}
				}
			} catch (final Exception e) {
			}

			if ((amount != 0) && !hasMilkBuff) {
				movement.addModifier(entity, amount, 2);
				jump.addModifier(entity, amount, 2);
			} else {
				movement.removeModifier(entity);
				jump.removeModifier(entity);
			}
		} else {
			movement.removeModifier(entity);
			jump.removeModifier(entity);
		}
	}

	@Override
	public void endTransformation() {
		AttributeHelper.removeAttributes(entity, UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"));
	}

	@Override
	public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if (!((source instanceof EntityDamageSourceIndirect) || source.isExplosion() || source.isMagicDamage() || source.isProjectile())) {
			if ((dmg > 0)) {
				final ItemStack stack1 = entity.getHeldItemMainhand();
				final ItemStack stack2 = entity.getHeldItemOffhand();
				if ((stack1.isEmpty() && stack2.isEmpty())) {
					dmg += serverConfig.bonus;
				}
			}
		}
		return dmg;
	}

	private FaelisEars ears = new FaelisEars();

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

}