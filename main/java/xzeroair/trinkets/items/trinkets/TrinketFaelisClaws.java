package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.races.faelis.RaceFaelisClaws;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.trinkets.cosmetic.TrinketCosmetic;
import xzeroair.trinkets.enums.RenderCosmeticFeature;
import xzeroair.trinkets.traits.abilities.AbilityViciousStrike;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigFaelisClaw;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;

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

    @Override
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        if (!this.CONFIG_CLIENT.RENDER) {
            return;
        }
        final boolean isFaelis = Capabilities.getEntityProperties(player, false, (prop, rtn) -> prop.getCurrentRaceCache().compareRace(EntityRaces.faelis));
        if (isFaelis) {
            return;
        }
        final ItemStack firstClaw = TrinketHelper.getAccessory(player, s -> !s.isEmpty() && (s.getItem().getRegistryName().compareTo(this.getRegistryName()) == 0));
        if (firstClaw.isEmpty() || (firstClaw != stack)) {
            return;
        }
        final boolean cosmeticFaelisClaws = !TrinketHelper.getAccessory(player, s -> {
            if (s.isEmpty() || !(s.getItem() instanceof TrinketCosmetic)) {
                return false;
            }
            return RenderCosmeticFeature.cosmetic(s.getMetadata()) == RenderCosmeticFeature.FAELIS;
        }).isEmpty();
        if (cosmeticFaelisClaws) {
            return;
        }
        final int count = TrinketHelper.countAccessories(player, s -> !s.isEmpty() && (s.getItem().getRegistryName().compareTo(this.getRegistryName()) == 0));
        final boolean renderLeft = count > 0;
        final boolean renderRight = count > 1;
        RaceFaelisClaws.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale, 0, 0, 0, renderLeft, renderRight);
    }

}
