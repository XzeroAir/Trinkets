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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.ElfEars;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.traits.abilities.AbilityChargedShot;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceElf extends EntityRacePropertiesHandler {

	public static final ElfConfig serverConfig = TrinketsConfig.SERVER.races.elf;

	protected UpdatingAttribute bonusSpeed, bonusAtkSpeed;

	public RaceElf(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.elf);
		bonusSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.MOVEMENT_SPEED);
		bonusAtkSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.ATTACK_SPEED);
	}

	@Override
	public void startTransformation() {
		if (serverConfig.charge_shot) {
			this.addAbility(Abilities.chargedShot, new AbilityChargedShot());
		}
	}

	@Override
	public void whileTransformed() {
		if (entity.world.getBiome(entity.getPosition()) != null) {
			final Set<Type> biomeType = BiomeDictionary.getTypes(entity.world.getBiome(entity.getPosition()));
			if (biomeType.contains(Type.FOREST)) {
				bonusSpeed.addModifier(entity, 0.2, 2);
				bonusAtkSpeed.addModifier(entity, 0.5, 2);
			} else {
				bonusSpeed.removeModifier();
				bonusAtkSpeed.removeModifier();
			}
		}
	}

	@Override
	public void endTransformation() {
		bonusSpeed.removeModifier();
		bonusAtkSpeed.removeModifier();
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
