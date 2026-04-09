package xzeroair.trinkets.items.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.traits.elements.Element;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class TransformationPotion extends BasePotion {

    // I assume before the packet syncs to the client, the client removes the attribute or something, in the empty race handler

    protected RaceCache race;
    protected Element primary, secondary;

    public TransformationPotion(String modid, String name, int color, int duration, @Nonnull RaceCache cache, ResourceLocation texture) {
        super(modid, name, color, duration, false, -1, -1, texture);
        this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, cache.getRace().getUUID().toString(), 1, 0);
        this.race = cache;
        this.primary = cache.getPrimaryElement();
        this.secondary = cache.getSecondaryElement();
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, @Nonnull AbstractAttributeMap attributeMapIn, int amplifier) {
        List<Potion> removal = new ArrayList<>();
        for (Entry<Potion, PotionEffect> effect : entity.getActivePotionMap().entrySet()) {
            if (effect.getKey() instanceof TransformationPotion) {
                TransformationPotion pot = (TransformationPotion) effect.getKey();
                boolean matches = this.getRaceCache().compare(pot.getRaceCache());
                if (!matches) {
                    removal.add(pot);
                }
            }
        }
        for (Potion pot : removal) {
            entity.removePotionEffect(pot);
        }
        RaceAttribute.removeAllModifiers(entity);
        Capabilities.getEntityProperties(entity, prop -> {
            prop.setPotionRaceCache(this.getRaceCache());
        });
        super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
    }

    @Override
    public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {

    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
    }

    @Override
    public void removeAttributesModifiersFromEntity(@Nonnull EntityLivingBase entity, @Nonnull AbstractAttributeMap attributeMapIn, int amplifier) {
        super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
        Capabilities.getEntityProperties(entity, prop -> {
            prop.setPotionRaceCache(null);
        });
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;//super.isReady(duration, amplifier);
    }

    @Override
    public boolean isInstant() {
        return false;//super.isInstant();
    }

    @Override
    public boolean isBadEffect() {
        return super.isBadEffect();
    }

    @Override
    public boolean isBeneficial() {
        return super.isBeneficial();
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        List<ItemStack> cures = new ArrayList<>();
        return cures;
    }

    public RaceCache getRaceCache() {
        return this.race;
    }

    public String getRaceUUID() {
        return this.getRaceCache().getRace().getUUID().toString();
    }
}