package xzeroair.trinkets.items.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BasePotion;

public class ParalysisPotion extends BasePotion {

    public ParalysisPotion(String name, int duration, int color, boolean isBadEffect) {
        super(name, color, duration, isBadEffect);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketArcingOrb)) {
            entity.removePotionEffect(this);
            return;
        }
        entity.motionX = 0;
        if (entity.motionY > 0) {
            entity.motionY = 0;
        }
        entity.motionZ = 0;
        entity.velocityChanged = true;
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
