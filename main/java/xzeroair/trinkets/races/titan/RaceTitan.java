package xzeroair.trinkets.races.titan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.titan.RaceTitanRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.other.AbilityHeavy;
import xzeroair.trinkets.traits.abilities.other.AbilityLargeHands;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.damage.DamageTypesConfig;
import xzeroair.trinkets.util.helpers.EntityHelper;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RaceTitan extends EntityRacePropertiesHandler {

    public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;

    //	private UpdatingAttribute speed, attack;

    public RaceTitan(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.titan);
    }

    public RaceTitan(@Nonnull EntityLivingBase e, Element element) {
        super(e, EntityRaces.titan, element);
    }

    @Override
    public void startTransformation() {
        this.addAbility(new AbilityLargeHands());
        if (serverConfig.sink) {
            this.addAbility(new AbilityHeavy());
        }
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
        if (!entity.world.isRemote && entity.isRiding()) {
            final Entity mount = entity.getRidingEntity();
            if ((mount != null) && !this.mountEntity(mount)) {
                entity.dismountRidingEntity();
            }
        }
    }

    @Override
    public boolean mountEntity(Entity mount) {
        if (EntityHelper.isCreative(entity)) {
            return true;
        } else if (!serverConfig.canMount) {
            return false;
        } else if (serverConfig.mountBlacklist.length > 0) {
            List<String> disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);
            try {
                final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
                final String modID = regName.getNamespace();
                final String entityID = regName.getPath();
                final boolean doesWildcardExist = disallowedMounts.contains(modID + ":*");
                final boolean exists = disallowedMounts.contains(regName.toString());
                if (doesWildcardExist || exists) {
                    return serverConfig.whitelist;
                } else {
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return !serverConfig.whitelist;
        } else {
            return true;
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
            this.RendererRace = new RaceTitanRenderer(entity, this);
        }
        return RendererRace;
    }

}
