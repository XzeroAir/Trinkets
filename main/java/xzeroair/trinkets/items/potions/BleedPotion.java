package xzeroair.trinkets.items.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.TrinketsDamageSource;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BasePotion;

public class BleedPotion extends BasePotion {

    public BleedPotion(String name, int duration, int color, boolean isBadEffect) {
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
        if (TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketFaelisClaw)) {
            entity.removePotionEffect(this);
            return;
        }
        if (!entity.world.isRemote && (entity.ticksExisted % 20) == 0) {
            entity.attackEntityFrom(TrinketsDamageSource.bleeding, 0.5F * (amplifier + 1));
        }
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
