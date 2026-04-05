package xzeroair.trinkets.traits.abilities.compat.firstaid;

import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import xzeroair.trinkets.traits.abilities.other.AbilityBlessing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility;

@Interface(modid = "firstaid", iface = "xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility", striprefs = true)
public class AbilityBlessingFirstAid extends AbilityBlessing implements IFirstAidAbility {

    public AbilityBlessingFirstAid() {
        super();
    }


    @Override
    @Method(modid = "firstaid")
    public boolean firstAidHit(EntityLivingBase entity, DamageSource source, float undistributedDmg, AbstractPlayerDamageModel before, AbstractPlayerDamageModel after) {
        if (TrinketsConfig.SERVER.MISC.Blessings.length == 0) {
            final int rand = this.CHANCE > 0 ? this.random.nextInt(this.CHANCE) : 0;
            if ((after.HEAD.currentHealth < 1) || (after.BODY.currentHealth < 1)) {
                if (rand == 0) {
                    if (this.sendMessageToPlayer(entity)) {
                        after.HEAD.currentHealth = after.HEAD.getMaxHealth();
                        after.BODY.currentHealth = after.BODY.getMaxHealth();
                        after.LEFT_ARM.currentHealth = after.LEFT_ARM.getMaxHealth();
                        after.RIGHT_ARM.currentHealth = after.RIGHT_ARM.getMaxHealth();
                        after.LEFT_LEG.currentHealth = after.LEFT_LEG.getMaxHealth();
                        after.RIGHT_LEG.currentHealth = after.RIGHT_LEG.getMaxHealth();
                        after.LEFT_FOOT.currentHealth = after.LEFT_FOOT.getMaxHealth();
                        after.RIGHT_FOOT.currentHealth = after.RIGHT_FOOT.getMaxHealth();
                        entity.setHealth(entity.getMaxHealth());
                        entity.world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.3F, 1F);
                        after.scheduleResync();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
