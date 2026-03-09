package xzeroair.trinkets.races.dwarf;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.dwarf.RaceDwarfRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.AbilitySkilledMiner;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.damage.DamageTypesConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;

public class RaceDwarf extends EntityRacePropertiesHandler {

    public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

    public RaceDwarf(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.dwarf);
    }

    public RaceDwarf(EntityLivingBase e, Element element) {
        super(e, EntityRaces.dwarf, element);
    }

    @Override
    public void startTransformation() {
        //		if (serverConfig.fortune) {
        //			this.addAbility(new AbilityPsudoFortune());
        //		}
//        if (serverConfig.skilled_miner) {
        this.addAbility(new AbilitySkilledMiner());
//        }
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
            this.RendererRace = new RaceDwarfRenderer(entity, this);
        }
        return RendererRace;
    }
}
