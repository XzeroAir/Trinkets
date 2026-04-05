package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilitySafeGuard;
import xzeroair.trinkets.traits.abilities.compat.enhancedvisuals.AbilityEnhancedVisualsBlur;
import xzeroair.trinkets.traits.abilities.compat.firstaid.AbilityHardHead;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsCompat;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDamageShield;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketDamageShield extends AccessoryBase {

    protected final ConfigDamageShield CONFIG = TrinketsConfig.SERVER.ITEMS.DAMAGE_SHIELD;
    protected final ClientConfigDamageShield clientConfig = TrinketsConfig.CLIENT.ITEMS.DAMAGE_SHIELD;

    public TrinketDamageShield(String name) {
        super(name);
        this.setUUID("c0885371-20dd-4c56-86eb-78f24d9fe777");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilitySafeGuard(this.CONFIG.ABILITIES.SAFE_GUARD));
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
        if (FirstAidCompat.isModEnabled()) {
            abilities.add(new AbilityHardHead(this.CONFIG.ABILITIES.EXTERNAL.HARD_HEAD));
        }
        if (EnhancedVisualsCompat.isModActive()) {
            abilities.add(new AbilityEnhancedVisualsBlur(this.CONFIG.ABILITIES.EXTERNAL.CLEAR_VISION));
        }
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.LIGHT;
    }

    @Override
    public void onAccessoryEquipped(ItemStack stack, @Nonnull EntityLivingBase entity) {
        super.onAccessoryEquipped(stack, entity);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }

    @Override
    public void onAccessoryUnequipped(ItemStack stack, @Nonnull EntityLivingBase entity) {
        super.onAccessoryUnequipped(stack, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        if (!this.clientConfig.RENDER) {
            return;
        }
        final float offsetX = 0.17F;
        final float offsetY = 0.22F;
        final float offsetZ = 0.16F;
        GlStateManager.pushMatrix();
        if (player.isSneaking()) {
            GlStateManager.translate(0F, 0.2F, 0F);
        }
        renderer.getMainModel().bipedBody.postRender(scale);
        GlStateManager.rotate(180F, 1F, 0F, 0F);
        GlStateManager.translate(offsetX, -offsetY, offsetZ);
        if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(offsetX - 0.14F, 0, -(offsetZ - 0.2F));
        }
        final float bS = 3f;
        GlStateManager.scale(scale * bS, scale * bS, scale * bS);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation bro = new ModelResourceLocation(this.getRegistryName().toString() + "_bro", "inventory");
        final ModelResourceLocation panda = new ModelResourceLocation(this.getRegistryName().toString() + "_panda", "inventory");
        final ModelResourceLocation vip = new ModelResourceLocation(this.getRegistryName().toString() + "_vip", "inventory");
        final ModelResourceLocation artsy = new ModelResourceLocation(this.getRegistryName().toString() + "_artsy", "inventory");
        final ModelResourceLocation twilight = new ModelResourceLocation(this.getRegistryName().toString() + "_twilight", "inventory");
        ModelBakery.registerItemVariants(this, normal, bro, panda, vip, artsy, twilight);
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            int variant = Capabilities.getTrinketProperties(stack, 0, (prop, var) -> prop.getVariant());
            switch (variant) {
                case 1:
                    return vip;
                case 2:
                    return bro;
                case 3:
                    return panda;
                case 4:
                    return artsy;
                case 5:
                    return twilight;
                default:
                    return normal;
            }
        });
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

}