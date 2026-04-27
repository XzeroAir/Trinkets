package xzeroair.trinkets.items.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.util.Reference;

public class InvigoratedPotion extends BasePotion {

    public InvigoratedPotion(String name, int duration, int color, boolean isBadEffect) {
        super(Reference.MODID, name, color, duration, isBadEffect, -1, -1, null);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return false;
    }

    @Override
    public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return false;
    }
}
