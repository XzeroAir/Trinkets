package xzeroair.trinkets.util.helpers.raytrace;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.TargetingPredicates;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Flexible collision and beam traversal helper.
 *
 * This is deliberately separate from {@link RayTraceHelper}. New callers should build a
 * {@link RaytraceSettings}, use one of the generic trace methods, then use the typed convenience
 * methods when they only need the normal block/entity result collections.
 */
public final class RaytraceHelper {

    private static final double STEP_EPSILON = 0.0001D;
    private static final Predicate<Entity> DEFAULT_ENTITY_PREDICATE = TargetingPredicates.targetable();

    private RaytraceHelper() {
    }

    /**
     * Raytraces all entity hits in path order and lets the caller accumulate a value.
     */
    public static <R> R raytraceEntities(World world, @Nullable Entity source, Vec3d start, Vec3d end, Predicate<Entity> predicate, R initialValue, BiFunction<EntityHit, R, R> handler) {
        R result = initialValue;
        for (final EntityHit hit : findEntityHits(world, source, start, end, predicate)) {
            result = handler.apply(hit, result);
        }
        return result;
    }

    /**
     * Traces a beam with a caller-controlled remaining-energy budget. Callbacks own the gameplay
     * effect and energy consumed by each collision. The helper only owns collision order, path
     * segments, scattering, and stop conditions.
     */
    public static <R> BeamTraceResult<R> traceBeam(RaytraceSettings settings, double energy, Predicate<Entity> entityPredicate, R initialValue, BeamHitHandler<BlockHit, R> blockHandler, BeamHitHandler<EntityHit, R> entityHandler) {
        final List<TraceSegment> segments = new ArrayList<>();
        final Set<Entity> hitEntities = new HashSet<>();
        final Random random = new Random();
        Vec3d position = settings.getStart();
        Vec3d direction = settings.getDirection();
        double travelled = 0.0D;
        R value = initialValue;

        while ((energy > 0.0D) && (travelled < settings.getDistance())) {
            final double remainingDistance = settings.getDistance() - travelled;
            final Vec3d end = position.add(direction.scale(remainingDistance));
            final BlockHit blockHit = findNextBlock(settings, position, end, travelled);
            final double blockDistance = blockHit == null ? remainingDistance : position.distanceTo(blockHit.getHit().hitVec);
            final Vec3d entityEnd = position.add(direction.scale(blockDistance));
            final EntityHit entityHit = findClosestEntityHit(settings.getWorld(), settings.getSource(), position, entityEnd, entityPredicate, hitEntities, travelled);

            if (entityHit != null) {
                final BeamHitResult<R> step = entityHandler.apply(entityHit, energy, value);
                if (step == null) {
                    break;
                }
                value = step.getValue();
                segments.add(new TraceSegment(position, entityHit.getHit().hitVec, direction, energy, step.getEnergy(), null, entityHit));
                travelled += Math.min(remainingDistance, position.distanceTo(entityHit.getHit().hitVec) + STEP_EPSILON);
                position = entityHit.getHit().hitVec.add(direction.scale(STEP_EPSILON));
                energy = step.getEnergy();
                hitEntities.add(entityHit.getEntity());
                if (!step.shouldContinue()) {
                    break;
                }
                continue;
            }

            if (blockHit == null) {
                segments.add(new TraceSegment(position, end, direction, energy, energy, null, null));
                break;
            }

            final boolean transparent = settings.isTransparent(blockHit.getState());
            final boolean passThrough = settings.shouldPassThroughTransparentBlocks() && transparent;
            final BeamHitResult<R> step = blockHandler.apply(blockHit, energy, value);
            if (step == null) {
                break;
            }
            value = step.getValue();
            segments.add(new TraceSegment(position, blockHit.getHit().hitVec, direction, energy, step.getEnergy(), blockHit, null));
            travelled += Math.min(remainingDistance, blockDistance + STEP_EPSILON);
            position = blockHit.getHit().hitVec.add(direction.scale(STEP_EPSILON));
            energy = step.getEnergy();
            if (!step.shouldContinue() || !passThrough) {
                break;
            }
            if (settings.shouldScatterFluids() && blockHit.getState().getMaterial().isLiquid()) {
                direction = settings.getFluidScatter().apply(direction, blockHit, random).normalize();
            }
            if (settings.getTransparentInaccuracy() > 0.0D) {
                direction = addInaccuracy(direction, settings.getTransparentInaccuracy(), random);
            }
        }
        return new BeamTraceResult<>(value, energy, travelled, segments);
    }

    /**
     * Raytraces block hits in path order. The return value and block behavior belong entirely to the
     * supplied callback.
     */
    public static <R> R raytraceBlocks(RaytraceSettings settings, R initialValue, BiFunction<BlockHit, R, BlockTraceStep<R>> handler) {
        R value = initialValue;
        for (final BlockHit hit : findBlocks(settings)) {
            final BlockTraceStep<R> step = handler.apply(hit, value);
            if (step == null) {
                break;
            }
            value = step.getValue();
            if (!step.shouldContinue()) {
                break;
            }
        }
        return value;
    }

    // Typed convenience methods begin here.

    public static List<BlockHit> findBlocks(RaytraceSettings settings) {
        final List<BlockHit> hits = new ArrayList<>();
        final Vec3d direction = settings.getDirection();
        Vec3d position = settings.getStart();
        double travelled = 0.0D;
        while (travelled < settings.getDistance()) {
            final Vec3d end = position.add(direction.scale(settings.getDistance() - travelled));
            final BlockHit hit = findNextBlock(settings, position, end, travelled);
            if (hit == null) {
                break;
            }
            hits.add(hit);
            final boolean passThrough = settings.shouldPassThroughTransparentBlocks() && settings.isTransparent(hit.getState());
            if (!passThrough) {
                break;
            }
            final double stepDistance = position.distanceTo(hit.getHit().hitVec);
            travelled += Math.min(settings.getDistance() - travelled, stepDistance + STEP_EPSILON);
            position = hit.getHit().hitVec.add(direction.scale(STEP_EPSILON));
        }
        return hits;
    }

    public static List<EntityHit> findEntityHits(World world, @Nullable Entity source, Vec3d start, Vec3d end, @Nullable Predicate<Entity> predicate) {
        return findEntityHits(world, source, start, end, predicate, Collections.emptySet(), 0.0D);
    }

    public static List<Pair<Entity, Double>> findEntityDistances(World world, @Nullable Entity source, Vec3d start, Vec3d end, @Nullable Predicate<Entity> predicate) {
        final List<Pair<Entity, Double>> entities = new ArrayList<>();
        for (final EntityHit hit : findEntityHits(world, source, start, end, predicate)) {
            entities.add(Pair.of(hit.getEntity(), hit.getDistance()));
        }
        return entities;
    }

    @Nullable
    public static EntityHit findClosestEntityHit(World world, @Nullable Entity source, Vec3d start, Vec3d end, @Nullable Predicate<Entity> predicate, Set<Entity> excluding, double distanceOffset) {
        final AxisAlignedBB bounds = new AxisAlignedBB(start, end).grow(1.0D);
        final Predicate<Entity> effectivePredicate = entity -> !excluding.contains(entity) && (predicate == null ? DEFAULT_ENTITY_PREDICATE.apply(entity) : predicate.apply(entity));
        EntityHit nearest = null;
        for (final Entity entity : world.getEntitiesInAABBexcluding(source, bounds, effectivePredicate)) {
            final AxisAlignedBB box = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
            if (box.contains(start)) {
                return new EntityHit(entity, new RayTraceResult(entity, start), distanceOffset);
            }
            final RayTraceResult hit = box.calculateIntercept(start, end);
            if (hit == null) {
                continue;
            }
            final double distance = distanceOffset + start.distanceTo(hit.hitVec);
            if ((nearest == null) || (distance < nearest.getDistance())) {
                nearest = new EntityHit(entity, hit, distance);
            }
        }
        return nearest;
    }

    private static List<EntityHit> findEntityHits(World world, @Nullable Entity source, Vec3d start, Vec3d end, @Nullable Predicate<Entity> predicate, Set<Entity> excluding, double distanceOffset) {
        final AxisAlignedBB bounds = new AxisAlignedBB(start, end).grow(1.0D);
        final Predicate<Entity> effectivePredicate = entity -> !excluding.contains(entity) && (predicate == null ? DEFAULT_ENTITY_PREDICATE.apply(entity) : predicate.apply(entity));
        final List<Entity> candidates = world.getEntitiesInAABBexcluding(source, bounds, effectivePredicate);
        final List<EntityHit> hits = new ArrayList<>();
        for (final Entity entity : candidates) {
            final AxisAlignedBB box = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
            if (box.contains(start)) {
                hits.add(new EntityHit(entity, new RayTraceResult(entity, start), distanceOffset));
                continue;
            }
            final RayTraceResult hit = box.calculateIntercept(start, end);
            if (hit != null) {
                hits.add(new EntityHit(entity, hit, distanceOffset + start.distanceTo(hit.hitVec)));
            }
        }
        hits.sort(Comparator.comparingDouble(EntityHit::getDistance));
        return hits;
    }

    @Nullable
    private static BlockHit findNextBlock(RaytraceSettings settings, Vec3d start, Vec3d end, double distanceOffset) {
        final RayTraceResult hit = settings.getWorld().rayTraceBlocks(start, end, settings.shouldUseLiquids(), settings.shouldIgnoreBlocksWithoutBoundingBox(), false);
        if ((hit == null) || (hit.typeOfHit != RayTraceResult.Type.BLOCK)) {
            return null;
        }
        final BlockPos pos = hit.getBlockPos();
        final IBlockState state = settings.getWorld().getBlockState(pos);
        if (state.getCollisionBoundingBox(settings.getWorld(), pos) == Block.NULL_AABB) {
            return null;
        }
        return new BlockHit(pos, state, hit, distanceOffset + start.distanceTo(hit.hitVec));
    }

    private static Vec3d addInaccuracy(Vec3d direction, double degrees, Random random) {
        final double yaw = Math.toRadians((((random.nextDouble() * 2.0D) - 1.0D) * degrees));
        final double pitch = Math.toRadians((((random.nextDouble() * 2.0D) - 1.0D) * degrees));
        final double cosYaw = Math.cos(yaw);
        final double sinYaw = Math.sin(yaw);
        final double cosPitch = Math.cos(pitch);
        final double sinPitch = Math.sin(pitch);
        final double x = (direction.x * cosYaw) - (direction.z * sinYaw);
        final double z = (direction.x * sinYaw) + (direction.z * cosYaw);
        return new Vec3d(x, (direction.y * cosPitch) - (z * sinPitch), (direction.y * sinPitch) + (z * cosPitch)).normalize();
    }

    public interface BeamHitHandler<T, R> {
        BeamHitResult<R> apply(T hit, double energy, R value);
    }

    public interface FluidScatter {
        Vec3d apply(Vec3d direction, BlockHit hit, Random random);
    }

    public static class BlockTraceStep<R> {
        private final R value;
        private final boolean continueTrace;

        public BlockTraceStep(R value, boolean continueTrace) {
            this.value = value;
            this.continueTrace = continueTrace;
        }

        public R getValue() {
            return this.value;
        }

        public boolean shouldContinue() {
            return this.continueTrace;
        }
    }

    public static class BeamHitResult<R> extends BlockTraceStep<R> {
        private final double energy;

        public BeamHitResult(R value, double energy, boolean continueTrace) {
            super(value, continueTrace);
            this.energy = energy;
        }

        public double getEnergy() {
            return this.energy;
        }
    }

    public static class RaytraceSettings {
        private final World world;
        private final Entity source;
        private final Vec3d start;
        private final Vec3d direction;
        private final double distance;
        private boolean useLiquids;
        private boolean ignoreBlocksWithoutBoundingBox = true;
        private boolean passThroughTransparentBlocks;
        private Predicate<IBlockState> transparentPredicate = state -> false;
        private double transparentInaccuracy;
        private boolean scatterFluids;
        private FluidScatter fluidScatter = (direction, hit, random) -> direction;

        public RaytraceSettings(World world, @Nullable Entity source, Vec3d start, Vec3d direction, double distance) {
            this.world = world;
            this.source = source;
            this.start = start;
            this.direction = direction.normalize();
            this.distance = distance;
        }

        public World getWorld() {
            return this.world;
        }

        @Nullable
        public Entity getSource() {
            return this.source;
        }

        public Vec3d getStart() {
            return this.start;
        }

        public Vec3d getDirection() {
            return this.direction;
        }

        public double getDistance() {
            return this.distance;
        }

        public boolean shouldUseLiquids() {
            return this.useLiquids;
        }

        public RaytraceSettings setUseLiquids(boolean useLiquids) {
            this.useLiquids = useLiquids;
            return this;
        }

        public boolean shouldIgnoreBlocksWithoutBoundingBox() {
            return this.ignoreBlocksWithoutBoundingBox;
        }

        public RaytraceSettings setIgnoreBlocksWithoutBoundingBox(boolean ignoreBlocksWithoutBoundingBox) {
            this.ignoreBlocksWithoutBoundingBox = ignoreBlocksWithoutBoundingBox;
            return this;
        }

        public boolean shouldPassThroughTransparentBlocks() {
            return this.passThroughTransparentBlocks;
        }

        public RaytraceSettings setPassThroughTransparentBlocks(boolean passThroughTransparentBlocks) {
            this.passThroughTransparentBlocks = passThroughTransparentBlocks;
            return this;
        }

        public RaytraceSettings setTransparentPredicate(Predicate<IBlockState> transparentPredicate) {
            this.transparentPredicate = transparentPredicate == null ? state -> false : transparentPredicate;
            return this;
        }

        public boolean isTransparent(IBlockState state) {
            return this.transparentPredicate.apply(state);
        }

        public double getTransparentInaccuracy() {
            return this.transparentInaccuracy;
        }

        public RaytraceSettings setTransparentInaccuracy(double transparentInaccuracy) {
            this.transparentInaccuracy = Math.max(0.0D, transparentInaccuracy);
            return this;
        }

        public boolean shouldScatterFluids() {
            return this.scatterFluids;
        }

        public RaytraceSettings setScatterFluids(boolean scatterFluids) {
            this.scatterFluids = scatterFluids;
            return this;
        }

        public FluidScatter getFluidScatter() {
            return this.fluidScatter;
        }

        public RaytraceSettings setFluidScatter(FluidScatter fluidScatter) {
            this.fluidScatter = fluidScatter == null ? (direction, hit, random) -> direction : fluidScatter;
            return this;
        }
    }

    public static class BlockHit {
        private final BlockPos pos;
        private final IBlockState state;
        private final RayTraceResult hit;
        private final double distance;

        public BlockHit(BlockPos pos, IBlockState state, RayTraceResult hit, double distance) {
            this.pos = pos;
            this.state = state;
            this.hit = hit;
            this.distance = distance;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public IBlockState getState() {
            return this.state;
        }

        public RayTraceResult getHit() {
            return this.hit;
        }

        public double getDistance() {
            return this.distance;
        }
    }

    public static class EntityHit {
        private final Entity entity;
        private final RayTraceResult hit;
        private final double distance;

        public EntityHit(Entity entity, RayTraceResult hit, double distance) {
            this.entity = entity;
            this.hit = hit;
            this.distance = distance;
        }

        public Entity getEntity() {
            return this.entity;
        }

        public RayTraceResult getHit() {
            return this.hit;
        }

        public double getDistance() {
            return this.distance;
        }
    }

    public static class TraceSegment {
        private final Vec3d start;
        private final Vec3d end;
        private final Vec3d direction;
        private final double energyBefore;
        private final double energyAfter;
        private final BlockHit blockHit;
        private final EntityHit entityHit;

        public TraceSegment(Vec3d start, Vec3d end, Vec3d direction, double energyBefore, double energyAfter, @Nullable BlockHit blockHit, @Nullable EntityHit entityHit) {
            this.start = start;
            this.end = end;
            this.direction = direction;
            this.energyBefore = energyBefore;
            this.energyAfter = energyAfter;
            this.blockHit = blockHit;
            this.entityHit = entityHit;
        }

        public Vec3d getStart() {
            return this.start;
        }

        public Vec3d getEnd() {
            return this.end;
        }

        public Vec3d getDirection() {
            return this.direction;
        }

        public double getEnergyBefore() {
            return this.energyBefore;
        }

        public double getEnergyAfter() {
            return this.energyAfter;
        }

        @Nullable
        public BlockHit getBlockHit() {
            return this.blockHit;
        }

        @Nullable
        public EntityHit getEntityHit() {
            return this.entityHit;
        }
    }

    public static class BeamTraceResult<R> {
        private final R value;
        private final double remainingEnergy;
        private final double travelledDistance;
        private final List<TraceSegment> segments;

        public BeamTraceResult(R value, double remainingEnergy, double travelledDistance, List<TraceSegment> segments) {
            this.value = value;
            this.remainingEnergy = remainingEnergy;
            this.travelledDistance = travelledDistance;
            this.segments = Collections.unmodifiableList(new ArrayList<>(segments));
        }

        public R getValue() {
            return this.value;
        }

        public double getRemainingEnergy() {
            return this.remainingEnergy;
        }

        public double getTravelledDistance() {
            return this.travelledDistance;
        }

        public List<TraceSegment> getSegments() {
            return this.segments;
        }
    }
}
