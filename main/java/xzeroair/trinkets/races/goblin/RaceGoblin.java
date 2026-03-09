package xzeroair.trinkets.races.goblin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.goblin.RaceGoblinRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.abilities.other.AbilityWolfMount;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.damage.DamageTypesConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;

public class RaceGoblin extends EntityRacePropertiesHandler {

    public static final GoblinConfig serverConfig = TrinketsConfig.SERVER.races.goblin;

    public RaceGoblin(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.goblin);
    }

    public RaceGoblin(@Nonnull EntityLivingBase e, Element element) {
        super(e, EntityRaces.goblin, element);
    }

    @Override
    public void startTransformation() {
        if (TrinketsConfig.getClientStore().CLIMBING_ENABLED) {
            this.addAbility(new AbilityClimbing());
        }
        if (serverConfig.rider) {
            this.addAbility(new AbilityWolfMount());
        }
    }

    @Override
    public void targetedByEnemy(EntityLivingBase enemy) {
        if (serverConfig.friendly_creepers) {
            if (enemy instanceof EntityCreeper) {
                ((EntityCreeper) enemy).setAttackTarget(null);
            }
        }
    }

    @Override
    public float isHurt(DamageSource source, float dmg) {
        if (serverConfig.natural_resistance) {
            if (source.isExplosion()) {
                if (!TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketDamageShield)) {
                    final float maxHP = entity.getHealth();
                    final float modifier = ((maxHP * 100) / 20F) * 0.01F;
                    final float clampModifier = MathHelper.clamp(modifier, 0.01F, 1F);
                    return dmg * clampModifier;
                }
            }
            if (source.isFireDamage()) {
                float modifier = 0.8F;
                if (source.getDamageType().equalsIgnoreCase("Lava")) {
                    modifier = 0.5f;
                }
                return dmg * modifier;
            }
        }
        return dmg;
    }

    @Override
    public boolean attackedEntity(EntityLivingBase target, DamageSource source, float dmg) {
        boolean attack = super.attackedEntity(target, source, dmg);
        if (serverConfig.creepers_explode) {
            if (attack) {
                if (target instanceof EntityCreeper) {
                    if (!((EntityCreeper) target).hasIgnited()) {
                        ((EntityCreeper) target).ignite();
                    }
                    if (((EntityCreeper) target).getCreeperState() == -1) {
                        ((EntityCreeper) target).setCreeperState(1);
                    }
                }
            }
        }
        return attack;
    }

    @Override
    public void whileTransformed() {
        super.whileTransformed();
        if (!entity.world.isRemote) {
            String[] potEffects = serverConfig.potEffects;
            for (final String potID : potEffects) {
                final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
                if (potion.getPotion() != null) {
                    entity.addPotionEffect(potion.getPotionEffect());
                }
            }
        }
    }

    @Override
    public boolean isAttacked(DamageSource source, float dmg) {
        DamageTypesConfig config = serverConfig.dmgType;
        if ((source.isFireDamage() && config.fire) || (source.isExplosion() && config.explosion) || (source.isMagicDamage() && config.magic) || (source.isProjectile() && config.projectile) || (source instanceof EntityDamageSourceIndirect && config.indirect)) {
            return true;
        }
        for (String type : config.damageTypes) {
            if (source.damageType.contentEquals(type)) {
                return true;
            }
        }
        return super.isAttacked(source, dmg);
    }

    @Override
    public void endTransformation() {
        String[] potEffects = serverConfig.potEffects;
        for (final String potID : potEffects) {
            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
            if (potion.getPotion() != null) {
                if (entity.isPotionActive(potion.getPotion())) {
                    entity.removePotionEffect(potion.getPotion());
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceGoblinRenderer(entity, this);
        }
        return RendererRace;
    }

}
