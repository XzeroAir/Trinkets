package xzeroair.trinkets.races.faelis;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.client.model.FaelisEars;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ArmorEntry;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;
import java.util.UUID;

public class RaceFaelis extends EntityRacePropertiesHandler {

    public static FaelisConfig serverConfig = TrinketsConfig.SERVER.races.faelis;
    protected UpdatingAttribute movement, jump;

    public RaceFaelis(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.faelis);
        movement = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        jump = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    @Override
    public void startTransformation() {
        if (TrinketsConfig.getClientStore().CLIMBING_ENABLED) {
            this.addAbility(new AbilityClimbing());
        }
    }

    @Override
    public void whileTransformed() {
        if (entity.world.isRemote) {
            return;
        }
        boolean hasMilkBuff = false;
        if (TrinketsConfig.SERVER.races.faelis.penalties) {
            double amount = 0;
            final StatusHandler status = Capabilities.getStatusHandler(entity);
            if (status != null) {
                if (status.getActiveEffects().containsKey(StatusEffectsEnum.Invigorated.getName())) {
                    hasMilkBuff = true;
                } else {
                    hasMilkBuff = false;
                }
            }
            if (TrinketsConfig.SERVER.races.faelis.penalties) {
                try {
                    for (final ItemStack stack : entity.getEquipmentAndArmor()) {
                        final Item item = stack.getItem();
                        final String regName = item.getRegistryName().toString();
                        final String itemType = ConfigHelper.ArmorEntry.getItemType(stack);
                        if (!itemType.isEmpty()) {
                            final String ItemMaterial = ConfigHelper.ArmorEntry.getItemMaterial(stack).toLowerCase();
                            if (item instanceof ItemArmor) {
                                final ItemArmor armor = ((ItemArmor) item);
                                final String armorType = armor.armorType.getName();
                                ArmorEntry entry = ConfigHelper.TrinketConfigStorage.getEquipmentEntry(regName + ":" + armorType, regName, "ObjectMaterial:" + ItemMaterial + ":" + armorType, "ObjectMaterial:" + ItemMaterial);
                                if (entry != null) {
                                    amount -= entry.getEquipmentWeight();
                                }
                            } else {
                                final String hand = stack.isItemEqual(entity.getHeldItemMainhand()) ? "mainhand" : stack.isItemEqual(entity.getHeldItemOffhand()) ? "offhand" : "hand";
                                String[] mS = new String[]{regName + ":" + hand, regName, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + hand, "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                                final ArmorEntry main = ConfigHelper.TrinketConfigStorage.getEquipmentEntry((k, v) -> v.doesItemMatchEntry(stack), mS);
                                double mW = main == null ? 0 : main.getEquipmentWeight();
                                amount -= mW;
                            }
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
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
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/ears.png");
    public static final ResourceLocation TEXTURE_INNER = new ResourceLocation(Reference.MODID + ":" + "textures/inner_ears.png");
    public static final ResourceLocation TEXTURE_OUTER = new ResourceLocation(Reference.MODID + ":" + "textures/outer_ears.png");

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!TrinketsConfig.CLIENT.rendering || !this.showTraits()) {
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
        final float[] rgb = ColorHelper.getRGBColor(this.getTraitVariant() == 1 ? this.getAltTraitColor() : this.getTraitColor());
        final float[] rgb2 = ColorHelper.getRGBColor(this.getTraitVariant() == 1 ? this.getTraitColor() : this.getAltTraitColor());
        final float fscale = 0.30F;
        GlStateManager.scale(fscale, fscale, fscale);
        final double x = 0.0;
        final double y = -2.5;
        final double z = -0.72;
        final double height = 1;
        final double width = 1;
        final float u = 32;
        final float v = 0;
        final int uWidth = 16;
        final int vHeight = 16;
        final float tileWidth = 64;
        final float tileHeight = 32;
        final double xR = -0.3;
        final double xL = -xR;
        float rot = 26F;
        final int solidVariant = 2;
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.rotate(-rot, 0, 1, 0);
        if (this.getTraitVariant() == solidVariant) {
            DrawingHelper.Draw(TEXTURE, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xR, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
        } else {
            DrawingHelper.Draw(TEXTURE_INNER, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xR, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
        }
        GlStateManager.rotate(rot * 2, 0, 1, 0);
        if (this.getTraitVariant() == solidVariant) {
            DrawingHelper.Draw(TEXTURE, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xL, y, z + 0.0001, u, v + 16, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
        } else {
            DrawingHelper.Draw(TEXTURE_INNER, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xL, y, z + 0.0001, u, v + 16, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

}