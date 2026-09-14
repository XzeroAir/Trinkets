package xzeroair.trinkets.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.entity.EntitySummonWrapper;
import xzeroair.trinkets.traits.abilities.AbilityLinkedVitality;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

import javax.annotation.Nullable;

public class EntityAIKnightTarget extends EntityAITarget {

    private static final String LINKED_VITALITY = "xat:linked_vitality";

    private final EntitySummonWrapper knight;
    private int revengeTimestamp;
    private int attackTimestamp;

    public EntityAIKnightTarget(EntitySummonWrapper knight) {
        super(knight, false);
        this.knight = knight;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        final EntityLivingBase summoner = this.knight.getSummoner();
        if (summoner == null || !summoner.isEntityAlive()) {
            return false;
        }

        final EntityLivingBase revengeTarget = summoner.getRevengeTarget();
        final int currentRevengeTimestamp = summoner.getRevengeTimer();
        if (currentRevengeTimestamp != this.revengeTimestamp && this.isSummonerTarget(summoner, revengeTarget)) {
            this.target = revengeTarget;
            this.revengeTimestamp = currentRevengeTimestamp;
            this.attackTimestamp = summoner.getLastAttackedEntityTime();
            return true;
        }

        final EntityLivingBase attackedTarget = summoner.getLastAttackedEntity();
        final int currentAttackTimestamp = summoner.getLastAttackedEntityTime();
        if (currentAttackTimestamp != this.attackTimestamp && this.isSummonerTarget(summoner, attackedTarget)) {
            this.target = attackedTarget;
            this.revengeTimestamp = summoner.getRevengeTimer();
            this.attackTimestamp = currentAttackTimestamp;
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        final EntityLivingBase summoner = this.knight.getSummoner();
        return summoner != null && summoner.getRevengeTimer() == this.revengeTimestamp && summoner.getLastAttackedEntityTime() == this.attackTimestamp && super.shouldContinueExecuting();
    }

    private boolean isSummonerTarget(EntityLivingBase summoner, @Nullable EntityLivingBase target) {
        if (target == summoner || !this.isSuitableTarget(target, false) || this.isLinkedPartner(summoner, target)) {
            return false;
        }
        return !(summoner instanceof EntityPlayer) || !(target instanceof EntityPlayer) || ((EntityPlayer) summoner).canAttackPlayer((EntityPlayer) target);
    }

    private boolean isLinkedPartner(EntityLivingBase summoner, @Nullable EntityLivingBase target) {
        if (target == null) {
            return false;
        }
        return Capabilities.getEntityProperties(summoner, false, (summonerProperties, result) -> {
            final IAbilityInterface summonerAbility = summonerProperties.getAbilityHandler().getAbility(LINKED_VITALITY);
            if (!(summonerAbility instanceof AbilityLinkedVitality) || !target.getUniqueID().equals(((AbilityLinkedVitality) summonerAbility).getLinkedPartnerUUID())) {
                return false;
            }
            return Capabilities.getEntityProperties(target, false, (targetProperties, targetResult) -> {
                final IAbilityInterface targetAbility = targetProperties.getAbilityHandler().getAbility(LINKED_VITALITY);
                return targetAbility instanceof AbilityLinkedVitality && summoner.getUniqueID().equals(((AbilityLinkedVitality) targetAbility).getLinkedPartnerUUID());
            });
        });
    }
}
