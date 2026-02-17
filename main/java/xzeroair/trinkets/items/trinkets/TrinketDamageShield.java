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
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityResistance;
import xzeroair.trinkets.traits.abilities.compat.firstaid.AbilityIgnoreHeadshot;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDamageShield;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketDamageShield extends AccessoryBase {

    public static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;
    public static final ClientConfigDamageShield clientConfig = TrinketsConfig.CLIENT.items.DAMAGE_SHIELD;

    public TrinketDamageShield(String name) {
        super(name);
        this.setUUID("c0885371-20dd-4c56-86eb-78f24d9fe777");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        abilities.add(new AbilityResistance());
        if (Trinkets.MOD_COMPAT.FirstAid && serverConfig.compat.firstaid.chance_ignore) {
            abilities.add(new AbilityIgnoreHeadshot());
        }
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.LIGHT;
    }

    @Override
    public void onAccessoryEquipped(ItemStack stack, EntityLivingBase entity) {
        super.onAccessoryEquipped(stack, entity);
        if (TrinketsConfig.SERVER.misc.retrieveVIP) {
            Capabilities.getVipStatus(entity, status -> {
                Capabilities.getTrinketProperties(stack, prop -> prop.setVariant(status.getStatus()));
            });
        }
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        Capabilities.getTrinketProperties(stack, prop -> prop.setVariant(0));
    }

    @Override
    public void onAccessoryUnequipped(ItemStack stack, EntityLivingBase entity) {
        super.onAccessoryUnequipped(stack, entity);
        Capabilities.getTrinketProperties(stack, prop -> prop.setVariant(0));
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
                case 0:
                    return normal;
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
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        if (!clientConfig.doRender) {
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
    public boolean ItemEnabled() {
        return serverConfig.enabled;
    }
}