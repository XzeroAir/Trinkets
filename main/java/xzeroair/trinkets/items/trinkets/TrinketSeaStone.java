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
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWaterAffinity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityParasitesImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityThirstImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigSeaStone;
import xzeroair.trinkets.util.config.trinkets.ConfigSeaStone;

import java.util.List;

public class TrinketSeaStone extends AccessoryBase {

    public static final ConfigSeaStone serverConfig = TrinketsConfig.SERVER.Items.SEA_STONE;
    public static final ClientConfigSeaStone clientConfig = TrinketsConfig.CLIENT.items.SEA_STONE;

    public TrinketSeaStone(String name) {
        super(name);
        this.setUUID("6029aecd-318e-4b45-8c36-2ddd7f481e36");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        abilities.add(new AbilityWaterAffinity());
        final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
        final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
        final boolean tan = tanEnabled || sdEnabled;
        if (tan && serverConfig.compat.tan.prevent_thirst) {
            abilities.add(new AbilityThirstImmunity());
            if (sdEnabled) {
                abilities.add(new AbilityParasitesImmunity());
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        if (!clientConfig.doRender) {
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
    public Element getPrimaryElement() {
        return Elements.WATER;
    }

    @Override
    public boolean ItemEnabled() {
        return serverConfig.enabled;
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
