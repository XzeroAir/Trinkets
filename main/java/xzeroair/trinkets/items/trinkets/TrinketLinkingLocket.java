package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties.LinkType;
import xzeroair.trinkets.dev.DevBondedHudTools;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityLinkedVitality;
import xzeroair.trinkets.traits.abilities.compat.firstaid.AbilityLinkedVitalityFirstAid;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigLinkingLocket;
import xzeroair.trinkets.util.config.trinkets.ConfigLinkingLocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TrinketLinkingLocket extends AccessoryBase {

    public static final int COMPLETE = 0;
    public static final int LEFT_HALF = 1;
    public static final int RIGHT_HALF = 2;

    protected final ConfigLinkingLocket CONFIG = TrinketsConfig.SERVER.ITEMS.LINKING_LOCKET;
    protected final ClientConfigLinkingLocket clientConfig = TrinketsConfig.CLIENT.ITEMS.LINKING_LOCKET;

    public TrinketLinkingLocket(String name) {
        super(name);
        this.setUUID("03f24a17-cc14-447f-87b7-3c40c1d8498a");
        this.setHasSubtypes(true);
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        if (DevBondedHudTools.isTestLocket(stack)) {
            abilities.add(DevBondedHudTools.createLinkedVitality());
        } else if (FirstAidCompat.isModEnabled()) {
            abilities.add(new AbilityLinkedVitalityFirstAid(this.CONFIG.ABILITIES.LINKED_VITALITY));
        } else {
            abilities.add(new AbilityLinkedVitality(this.CONFIG.ABILITIES.LINKED_VITALITY));
        }
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
    public boolean itemInteractionForEntity(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull EntityLivingBase target, @Nonnull EnumHand hand) {
        if (DevBondedHudTools.isPartnerFor(player, target)) {
            DevBondedHudTools.linkTestPartner(stack, player, target);
            return true;
        }
        if (player.world.isRemote || target == player || !(target instanceof EntityPlayer) || stack.getMetadata() != COMPLETE) {
            return true;
        }
        Capabilities.getTrinketProperties(stack, properties -> {
            if (properties.hasLinkedEntity()) {
                return;
            }
            final EntityPlayer partner = (EntityPlayer) target;
            final ItemStack partnerLocket = new ItemStack(this, 1, RIGHT_HALF);
            Capabilities.getTrinketProperties(partnerLocket, partnerProperties -> {
                partnerProperties.onCrafted(player.world, player);
                properties.linkEntity(partner, LinkType.PLAYER);
                partnerProperties.linkEntity(player, LinkType.PLAYER);
            });
            stack.setItemDamage(LEFT_HALF);
            if (!partner.inventory.addItemStackToInventory(partnerLocket)) {
                partner.dropItem(partnerLocket, false);
            }
        });
        return true;
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
            GlStateManager.translate(0F, 0F, -(offsetZ - 0.2F));
        }
        final float itemScale = 3F;
        GlStateManager.scale(scale * itemScale, scale * itemScale, scale * itemScale);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltips, @Nonnull ITooltipFlag flagIn) {
        Capabilities.getTrinketProperties(stack, properties -> {
            if (properties.hasLinkedEntity()) {
                tooltips.add(I18n.format("item.linking_locket.tooltip.partner", properties.getLinkedEntityName()));
            }
        });
        super.addInformation(stack, world, tooltips, flagIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        final ModelResourceLocation unbound = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation boundLeft = new ModelResourceLocation(this.getRegistryName().toString() + "_left", "inventory");
        final ModelResourceLocation boundRight = new ModelResourceLocation(this.getRegistryName().toString() + "_right", "inventory");
        ModelBakery.registerItemVariants(this, unbound, boundLeft, boundRight);
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            if (stack.getMetadata() == LEFT_HALF) {
                return boundLeft;
            }
            if (stack.getMetadata() == RIGHT_HALF) {
                return boundRight;
            }
            return unbound;
        });
    }
}
