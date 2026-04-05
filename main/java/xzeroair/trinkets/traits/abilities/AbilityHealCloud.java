package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.traits.abilities.base.AbilityRaceSpecific;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class AbilityHealCloud extends AbilityRaceSpecific {

    protected float COST;

    public AbilityHealCloud() {
        super("restoration_field");
        this.COST = 0F;
    }

    public boolean castHealCloud(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
//        final float cfgCost = this.COST;
//        final float maxMP = magic.getMaxMana();
//        final float mp = magic.getMana();
        final double maxDist = 15D;
        final Vec3d start = entity.getPositionEyes(1F);
        final Vec3d lookVec = entity.getLookVec();
        final Vec3d end = start.add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
        final RayTraceResult result = RayTraceHelper.rayTrace(entity, maxDist);
        Vec3d hitLoc = end;
        if (result != null) {
            if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
                final Entity hit = result.entityHit;
                hitLoc = result.hitVec.add(0, hit.height * 0.5F, 0);
            } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                hitLoc = result.hitVec;
            }
        }
        if (Trinkets.proxy.getSide() == Side.CLIENT) {
//            NetworkHandler.sendToServer(new EffectsRenderPacket(entity, start.x, start.y, start.z, hitLoc.x, hitLoc.y, hitLoc.z, 2515356, 1, 0.9F, 3F));
            NetworkHandler.sendToServer(new EffectsRenderPacket(entity, hitLoc.x, hitLoc.y, hitLoc.z, hitLoc.x, hitLoc.y, hitLoc.z, 2515356, 8, 0.9F, 3F));
        }
        if (!entity.getEntityWorld().isRemote) {
            EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(entity.world, hitLoc.x, hitLoc.y, hitLoc.z);
            entityareaeffectcloud.setOwner(entity);
            entityareaeffectcloud.setRadius(3.0F);
//            entityareaeffectcloud.setRadiusOnUse(-0.5F);
            entityareaeffectcloud.setWaitTime(10);
            entityareaeffectcloud.setDuration(120);
//            entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
//                        entityareaeffectcloud.setPotion(type);
//minecraft:weakness:20:1
//            for (String str : EFFECTS) {
            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder("minecraft:instant_health:60:0");
            if (potion.getPotion() != null) {
                entityareaeffectcloud.addEffect(potion.getPotionEffect());
            }
//            }
            entityareaeffectcloud.setColor(12514535);
            entity.world.spawnEntity(entityareaeffectcloud);
//            boolean pvpEnabled = false;
//            try {
//                if (entity instanceof EntityPlayerMP) {
//                    pvpEnabled = ((EntityPlayerMP) entity).getServer().isPVPEnabled();
//                }
//        } catch( final Exception e){
//            e.printStackTrace();
//        }
//            final AxisAlignedBB bb1 = new AxisAlignedBB(new BlockPos(hitLoc)).grow(1);

//            final List<Entity> splash = entity.world.getEntitiesInAABBexcluding(entity, bb1, Targets);
//            for (final Entity e : splash) {
//                if ((e instanceof EntityPlayer) && !pvpEnabled) {
//
//                } else {
//                    if ((multi > 0.33F) && (e instanceof EntityCreeper)) {
//                        final EntityCreeper creep = (EntityCreeper) e;
//                        final EntityLightningBolt bolt = new EntityLightningBolt(entity.getEntityWorld(), creep.posX, creep.posY, creep.posZ, true);
//                        creep.onStruckByLightning(bolt);
//                        creep.extinguish();
//                    }
//                    e.attackEntityFrom(new EntityDamageSource(MINECRAFT_LIGHTNING, entity), finalDamage);
//                }
//            }
        }
        return true;
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        final MagicStats magic = Capabilities.getMagicStats(entity);
        if (magic != null) {
            return magic.getMana() >= this.COST;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(Entity entity, boolean Aux) {
        final MagicStats magic = Capabilities.getMagicStats(entity);
        if (magic != null) {
            return magic.spendMana(this.COST);
        }
        return false;
    }

    @Override
    public boolean onKeyRelease(Entity entity, boolean Aux) {
        final MagicStats magic = Capabilities.getMagicStats(entity);
        if (magic != null) {
            this.castHealCloud(magic.getEntity(), magic, true, Aux);
        }
        return true;
    }
}

