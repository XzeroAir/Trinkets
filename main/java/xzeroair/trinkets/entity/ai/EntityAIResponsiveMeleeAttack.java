package xzeroair.trinkets.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import xzeroair.trinkets.entity.EntitySummonWrapper;

public class EntityAIResponsiveMeleeAttack extends EntityAIAttackMelee {

    private final EntitySummonWrapper wrapper;
    private final double speed;

    public EntityAIResponsiveMeleeAttack(EntitySummonWrapper wrapper, double speed) {
        super(wrapper, speed, true);
        this.wrapper = wrapper;
        this.speed = speed;
    }

    @Override
    public boolean shouldExecute() {
        return !this.wrapper.suppressMovement() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.wrapper.suppressMovement() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        final EntityLivingBase target = this.wrapper.getAttackTarget();
        if (target != null && target.isEntityAlive() && this.wrapper.getDistanceSq(target) > 4.0D && (!this.wrapper.getNavigator()
                                                                                                                   .tryMoveToEntityLiving(target, this.speed) || !this.reachesTarget(target))) {
            this.wrapper.pathFailed(target);
        }
    }

    private boolean reachesTarget(EntityLivingBase target) {
        final Path path = this.wrapper.getNavigator().getPath();
        final PathPoint finalPoint = path == null ? null : path.getFinalPathPoint();
        return finalPoint != null && target.getDistanceSq(finalPoint.x + 0.5D, finalPoint.y, finalPoint.z + 0.5D) <= 1.0D;
    }
}
