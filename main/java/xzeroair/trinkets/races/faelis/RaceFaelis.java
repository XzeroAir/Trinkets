package xzeroair.trinkets.races.faelis;

import java.util.UUID;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.GenericAttribute;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.FaelisEars;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class RaceFaelis extends EntityRacePropertiesHandler {

	public static FaelisConfig serverConfig = TrinketsConfig.SERVER.races.faelis;

	public RaceFaelis(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.faelis);
	}

	@Override
	public void whileTransformed() {
		if (TrinketsConfig.SERVER.races.faelis.penalties) {
			ItemStack Head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			ItemStack Chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			ItemStack Legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
			ItemStack Feet = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);

			double amount = 0;
			double weight = serverConfig.penalty_amount;//0.0125;

			if (!(Head.isEmpty()) && !(Head.getItem() == Items.LEATHER_HELMET)) {
				amount -= weight;
			}

			if (!(Chest.isEmpty()) && !((Chest.getItem() == Items.LEATHER_CHESTPLATE) || (Chest.getItem() == Items.ELYTRA))) {
				amount -= weight;
			}

			if (!(Legs.isEmpty()) && !(Legs.getItem() == Items.LEATHER_LEGGINGS)) {
				amount -= weight;
			}

			if (!(Feet.isEmpty()) && !(Feet.getItem() == Items.LEATHER_BOOTS)) {
				amount -= weight;
			}

			if (amount != 0) {
				GenericAttribute armor = new GenericAttribute(entity, amount, UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), 2, SharedMonsterAttributes.MOVEMENT_SPEED);
				if (properties.getStatusHandler().getActiveEffects().containsKey(StatusEffectsEnum.Invigorated.getName())) {
					amount = 0;
					armor.removeModifier();
				} else {
					armor.addModifier();
				}
			}
		}
	}

	@Override
	public void endTransformation() {
		AttributeHelper.removeAttributes(entity, UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"));
	}

	@Override
	public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if ((dmg > 0)) {
			ItemStack stack1 = entity.getHeldItemMainhand();
			ItemStack stack2 = entity.getHeldItemOffhand();
			if ((stack1.isEmpty() && stack2.isEmpty())) {
				dmg += serverConfig.bonus;
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