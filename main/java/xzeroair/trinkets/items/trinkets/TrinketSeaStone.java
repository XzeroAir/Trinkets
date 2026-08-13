package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilitySkilledSwimmer;
import xzeroair.trinkets.traits.abilities.compat.enhancedvisuals.AbilityEnhancedVisualsSplash;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityThirstAbsorption;
import xzeroair.trinkets.traits.abilities.elements.water.AbilityWaterAffinity;
import xzeroair.trinkets.traits.abilities.elements.water.AbilityWaterImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsCompat;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigSeaStone;
import xzeroair.trinkets.util.config.trinkets.ConfigSeaStone;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketSeaStone extends AccessoryBase {

    protected final ConfigSeaStone CONFIG = TrinketsConfig.SERVER.ITEMS.SEA_STONE;
    protected final ClientConfigSeaStone clientConfig = TrinketsConfig.CLIENT.ITEMS.SEA_STONE;

    public TrinketSeaStone(String name) {
        super(name);
        this.setUUID("6029aecd-318e-4b45-8c36-2ddd7f481e36");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilityWaterAffinity(this.CONFIG.ABILITIES.WATER_AFFINITY));
        abilities.add(new AbilityWaterImmunity(this.CONFIG.ABILITIES.WATER_IMMUNITY));
        abilities.add(new AbilitySkilledSwimmer(this.CONFIG.ABILITIES.SKILLED_SWIMMER));
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
        if (SurvivalCompat.isSurvivalModsActive()) {
            abilities.add(new AbilityThirstAbsorption(this.CONFIG.ABILITIES.EXTERNAL.WATER_ABSORPTION));
        }
        if (EnhancedVisualsCompat.isModActive()) {
            abilities.add(new AbilityEnhancedVisualsSplash(this.CONFIG.ABILITIES.EXTERNAL.CLEAR_VISION));
        }
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.WATER;
    }

    @Override
    public String[] getAttributeConfig() {
        return this.CONFIG.ATTRIBUTES;
    }

    @Override
    public String[] getEffectsToRemove() {
        return this.CONFIG.EFFECTS_TO_REMOVE;
    }

    @Override
    public String[] getEffectsToAdd() {
        return this.CONFIG.EFFECTS_TO_ADD;
    }

    @Override
    public String[] getDamageTypesToIgnoreConfig() {
        return this.CONFIG.DAMAGE_TYPES_TO_IGNORE;
    }

    @Override
    public boolean ItemEnabled() {
        return this.CONFIG.ENABLED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        if (!this.clientConfig.RENDER) {
            return;
        }
        final float offsetY = 0.16F;
        final float offsetZ = 0.14F;
        GlStateManager.pushMatrix();
        if (player.isSneaking()) {
            GlStateManager.translate(0F, 0.2F, 0F);
        }
        renderer.getMainModel().bipedBody.postRender(scale);
        GlStateManager.rotate(180F, 1F, 0F, 0F);
        GlStateManager.translate(0F, -offsetY, offsetZ);
        if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(0F, 0, -(offsetZ - 0.2F));
        }
        final float bS = 3f;
        GlStateManager.scale(scale * bS, scale * bS, scale * bS);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
//		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation worn = new ModelResourceLocation(this.getRegistryName().toString() + "_worn", "inventory");
//		ModelBakery.registerItemVariants(this, normal, worn);
        ModelBakery.registerItemVariants(this, worn);
        ModelLoader.setCustomMeshDefinition(this, stack -> worn);
    }

}
