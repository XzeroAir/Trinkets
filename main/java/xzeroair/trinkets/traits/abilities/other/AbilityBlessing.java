package xzeroair.trinkets.traits.abilities.other;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;

public class AbilityBlessing extends Ability implements IAttackAbility {

    protected final int CHANCE;

    public AbilityBlessing() {
        super("blessing_of_life");
        this.CHANCE = 100;
    }

    @Override
    public float damaged(@Nonnull EntityLivingBase attacked, DamageSource source, float dmg) {
        if (TrinketsConfig.SERVER.MISC.Blessings.length == 0) {
            if ((attacked.getHealth() - dmg) <= 0F) {
                if (this.random.nextInt(this.CHANCE) == 0) {
                    if (this.sendMessageToPlayer(attacked)) {
                        attacked.heal(attacked.getMaxHealth());
                        attacked.world.playSound((EntityPlayer) null, attacked.posX, attacked.posY, attacked.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.3F, 1F);
                        return 0F;
                    }
                }
            }
        }
        return dmg;
    }

    @Override
    public boolean sendMessageToPlayer(Entity entity) {
        if ((entity instanceof EntityPlayer)) {
            final boolean client = entity.world.isRemote;
            if (!client) {
                StringUtils.sendStatusMessageToPlayer(entity, TranslationHelper.INSTANCE.gold + "Don't give up!", true);
                return true;
            }
        }
        return false;
    }
}
