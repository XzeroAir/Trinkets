package xzeroair.trinkets.items.trinkets;

import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.model.BipedJsonModel;
import xzeroair.trinkets.client.model.EnderQueenCrownModel;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityEnderQueen;
import xzeroair.trinkets.traits.abilities.compat.enhancedvisuals.AbilityEnhancedVisualsStatic;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsCompat;
import xzeroair.trinkets.util.compat.mobends.MoBendsCompat;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigEnderCrown;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketEnderTiara extends AccessoryBase {

    protected final ConfigEnderCrown CONFIG = TrinketsConfig.SERVER.ITEMS.ENDER_CROWN;
    protected final ClientConfigEnderCrown clientConfig = TrinketsConfig.CLIENT.ITEMS.ENDER_CROWN;

    public TrinketEnderTiara(String name) {
        super(name);
        this.setUUID("a45dbc1c-17e9-40b4-b6a3-09dea74355b7");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilityEnderQueen(this.CONFIG.ABILITIES.ENDER_QUEEN));
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
        if (EnhancedVisualsCompat.isModActive()) {
            abilities.add(new AbilityEnhancedVisualsStatic(this.CONFIG.ABILITIES.EXTERNAL.ENDER_EYES));
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, @Nonnull ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot(@Nonnull ItemStack stack) {
        //super.getEquipmentSlot(stack);
        return EntityEquipmentSlot.HEAD;
    }

    @Override
    public boolean isValidArmor(@Nonnull ItemStack stack, @Nonnull EntityEquipmentSlot armorType, @Nonnull Entity entity) {
        boolean alreadyEquipped = (entity instanceof EntityLivingBase) && TrinketHelper.AccessoryCheck((EntityLivingBase) entity, this);
        return !alreadyEquipped && super.isValidArmor(stack, armorType, entity);
    }

    @Override
    public boolean canEquipAccessory(ItemStack stack, EntityLivingBase player) {
        return TrinketHelper.getHead(player, (s) -> s.isItemEqual(stack)).isEmpty() && super.canEquipAccessory(stack, player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(@Nonnull EntityLivingBase entityLiving, @Nonnull ItemStack itemStack, @Nonnull EntityEquipmentSlot armorSlot, @Nonnull ModelBiped _default) {
        if (this.clientConfig.RENDER && this.clientConfig.RENDER_HELMET && !itemStack.isEmpty()) {
            BipedJsonModel model = EnderQueenCrownModel.INSTANCE.getHelmet();
            model.setModelAttributes(_default);
            model.isSneak = _default.isSneak;
            model.isRiding = _default.isRiding;
            model.isChild = _default.isChild;
            model.rightArmPose = _default.rightArmPose;
            model.leftArmPose = _default.leftArmPose;
            return model;
        }
        return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        if (!this.clientConfig.RENDER) {
            return;
        }
        GlStateManager.pushMatrix();
        BipedJsonModel model = EnderQueenCrownModel.INSTANCE.getAccessory();
        double sneakOffset = player.isSneaking() ? 0.2 : 0;
        boolean hasHelmet = player.hasItemInSlot(EntityEquipmentSlot.HEAD);
        double helmetOffsetY = hasHelmet ? 0.07 : 0;
        double helmetOffsetZ = hasHelmet ? -0.04 : 0;
        GlStateManager.translate(0, sneakOffset, 0);
        if (MoBendsCompat.isModEnabled()) {
            renderer.getMainModel().bipedHead.postRender(scale);
        }
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        GlStateManager.translate(-8F * scale, 1.8F * scale, -4.8F * scale);
        GlStateManager.translate(0.0F, helmetOffsetY, helmetOffsetZ);
//        float f = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
//        float f1 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
//        float f2 = f1 - f;
        float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        float swing = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
        model.render(player, swing, Math.min(player.limbSwingAmount, 1.0F), (float) player.ticksExisted + partialTicks, yaw, pitch, scale, -1);
        GlStateManager.popMatrix();
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.VOID;
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
    public void registerModels() {
        final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation model = new ModelResourceLocation(this.getRegistryName().toString() + "_model", "inventory");
        ModelBakery.registerItemVariants(this, normal, model);
        ModelLoader.setCustomMeshDefinition(this, stack -> Capabilities.getTrinketProperties(stack, normal, (prop, tex) -> {
            if (prop.getSlotInfo().getHandlerType().equals(ItemHandlerType.HEAD)) {
                return model;
            } else {
                return tex;
            }
        }));
    }
}
