package xzeroair.trinkets.races.human;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.human.RaceHumanRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.human.config.HumanConfig;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.damage.DamageTypesConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;

public class RaceHuman extends EntityRacePropertiesHandler {

    public static final HumanConfig serverConfig = TrinketsConfig.SERVER.races.human;

    public RaceHuman(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.human);
        setMaxTraitVariant(0);
    }

    public RaceHuman(@Nonnull EntityLivingBase e, Element element) {
        super(e, EntityRaces.human, element);
        setMaxTraitVariant(0);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
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
            this.RendererRace = new RaceHumanRenderer(entity, this);
        }
        return RendererRace;
    }

}
