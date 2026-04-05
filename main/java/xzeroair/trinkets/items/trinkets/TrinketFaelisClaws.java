package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityViciousStrike;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.ConstantsResourceLocations;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigFaelisClaw;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import java.util.List;

public class TrinketFaelisClaws extends AccessoryBase {

    protected final ConfigFaelisClaw CONFIG = TrinketsConfig.SERVER.ITEMS.FAELIS_CLAW;
    protected final ClientConfigFaelisClaw CONFIG_CLIENT = TrinketsConfig.CLIENT.ITEMS.FAELIS_CLAW;

    public TrinketFaelisClaws(String name) {
        super(name);
        this.setUUID("4959ec73-142d-4b82-bd0d-cd6cd7431611");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        abilities.add(new AbilityViciousStrike(this.CONFIG.ABILITIES.VICIOUS_STRIKE));
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.AIR;
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
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
    }

    public static final ResourceLocation TEXTURE = new ResourceLocation(ConstantsResourceLocations.TEXTURES_CLAWS);

    @Override
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        if (!this.CONFIG_CLIENT.RENDER) {
            return;
        }
        boolean isFaelis = Capabilities.getEntityProperties(player, false, (prop, rtn) -> prop.getCurrentRace().compareRace(EntityRaces.faelis));
        if (isFaelis) {
            return;
        }
        int count = TrinketHelper.countAccessories(player, s -> !s.isEmpty() && (s.getItem().getRegistryName().compareTo(stack.getItem().getRegistryName()) == 0));
        final float offsetX = isSlim ? -12.4F : -18.6F;
        final float offsetY = 61F;
        final float offsetZ = -21F;
        final float bS = 0.16f;
        boolean flag = (this.CONFIG.COMPAT.BAUBLES.equip_multiple);
        boolean flag1 = !flag || count > 0;
        boolean flag2 = !flag || count > 1;
        if (flag1) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableCull();
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
            GlStateManager.pushMatrix();
            if (player.isSneaking()) {
                GlStateManager.translate(0F, 0.2F, 0F);
            }
            renderer.getMainModel().bipedLeftArm.postRender(scale);
            GlStateManager.scale(scale * bS, scale * bS, scale * bS);
            GlStateManager.translate(-offsetX, offsetY, offsetZ);
            GlStateManager.rotate(-90F, 0F, 1F, 0F);
            DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);
            GlStateManager.popMatrix();
        }
        if (flag2) {
            GlStateManager.pushMatrix();
            if (player.isSneaking()) {
                GlStateManager.translate(0F, 0.2F, 0F);
            }
            renderer.getMainModel().bipedRightArm.postRender(scale);
            GlStateManager.scale(scale * bS, scale * bS, scale * bS);
            GlStateManager.translate(offsetX, offsetY, offsetZ);
            GlStateManager.rotate(-90F, 0F, 1F, 0F);
            DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }

}
