package xzeroair.trinkets.traits.abilities.elements.light;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.entity.EntityRangedAttack;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.traits.abilities.elements.AbilityBreathBase;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;
import xzeroair.trinkets.util.helpers.SoundHelper;

import javax.annotation.Nonnull;

public class AbilityLightBreath extends AbilityBreathBase {

    private static final float PROJECTILE_SPEED = 4.0F;
    private static final float ENERGY_DECAY_PER_TICK = 0.04F;
    private static final float ENERGY_FIZZLE_THRESHOLD = 0.01F;
    private static final float ENTITY_CONTACT_ENERGY_COST = 1.4F;
    private static final float TRANSPARENT_BLOCK_ENERGY_COST = 0.08F;
    private static final int RECAST_DELAY_TICKS = 10;

    private int recastDelayTicks;

    public AbilityLightBreath() {
        this(new ConfigAbilityBreath(20));
    }

    public AbilityLightBreath(ConfigAbilityBreath config) {
        super(TrinketsRegistryNames.ModAbilities.BREATH_LIGHT, config);
    }

    @Override
    protected boolean DragonBreath(Entity entity) {
        if (this.isSpectator(entity) || !(entity instanceof EntityLivingBase)) {
            return false;
        }
        if (this.recastDelayTicks > 0) {
            this.recastDelayTicks--;
            return true;
        }
        if (!entity.world.isRemote && (BREATH_STAGE <= 0 || BREATH_STAGE % 20 == 0)) {
            this.renderCharge((EntityLivingBase) entity);
        }
        this.BREATH_STAGE++;
        if (this.BREATH_STAGE < this.BREATH_FREQUENCY) {
            if ((this.BREATH_STAGE % 2 == 0)) {
                final float progress = Math.min(1.0F, (float) this.BREATH_STAGE / (float) BREATH_FREQUENCY);
                SoundHelper.playSound(entity, SoundEvents.BLOCK_NOTE_BASS, SoundCategory.PLAYERS, 0.2F, 0.55F + (0.45F * progress));
            }
            return true;
        }
        this.BREATH_STAGE = 0;

        final MagicStats magic = Capabilities.getMagicStats(entity);
        if ((magic != null) && !magic.spendMana(this.COST)) {
            return false;
        }
        if (!entity.world.isRemote) {
            this.castBeam((EntityLivingBase) entity);
        }
        this.recastDelayTicks = RECAST_DELAY_TICKS;
        return true;
    }

    private void castBeam(@Nonnull EntityLivingBase caster) {
        final int color = this.getBreathColor(caster);
        final int lifetimeTicks = Math.max(1, MathHelper.ceil((float) 24 / PROJECTILE_SPEED));
        final EntityRangedAttack beam = new EntityRangedAttack(caster.world, caster, color).setColor(color)
                                                                                           .setElement(Elements.LIGHT)
                                                                                           .setDamage(this.DAMAGE)
                                                                                           .setEffects(this.EFFECTS)
                                                                                           .setAllowTerrainInteraction(this.INTERACT_WITH_TERRAIN)
                                                                                           .setVisualType(EntityRangedAttack.VISUAL_LASER)
                                                                                           .setPhysicsTemplate(new EntityRangedAttack.PhysicsTemplate().setGravity(0.0F)
                                                                                                                                                       .setDrag(Material.AIR, 1.0F)
                                                                                                                                                       .setLifetimeTicks(lifetimeTicks)
                                                                                                                                                       .setEntityCollisionResponse(EntityRangedAttack.ContactResponse.PASS_THROUGH)
                                                                                                                                                       .setTransparentBlockProfile(EntityRangedAttack.TransparentBlockProfile.NON_OPAQUE))
                                                                                           .setEnergyEnabled(true)
                                                                                           .setEnergyDecayPerTick(ENERGY_DECAY_PER_TICK)
                                                                                           .setEnergyFizzleThreshold(ENERGY_FIZZLE_THRESHOLD)
                                                                                           .setEnergyDamageScaling(true)
                                                                                           .setDefaultCollisionBehavior(EntityRangedAttack.defaultCollisionBehavior()
                                                                                                                                          .setEntityContactEnergyCost(ENTITY_CONTACT_ENERGY_COST)
                                                                                                                                          .setTransparentBlockEnergyCost(TRANSPARENT_BLOCK_ENERGY_COST));
        final Vec3d mouth = this.getMouthPosition(caster);
        beam.setVisualOrigin(mouth);
        beam.setPosition(mouth.x, mouth.y, mouth.z);
        beam.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0.0F, PROJECTILE_SPEED, 0.0F);
        SoundHelper.playSound(caster, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, SoundCategory.PLAYERS, 0.6F, 1F);
        caster.world.spawnEntity(beam);
    }

    private Vec3d getMouthPosition(EntityLivingBase caster) {
        return caster.getPositionEyes(1.0F).add(caster.getLookVec().scale(0.7D)).add(0.0D, -0.15D, 0.0D);
    }

    private void renderCharge(EntityLivingBase caster) {
        if (!(caster.world instanceof WorldServer)) {
            return;
        }
        final WorldServer world = (WorldServer) caster.world;
        final Vec3d mouth = this.getMouthPosition(caster);
        final int color = this.getBreathColor(caster);
        NetworkHandler.sendToAllAround(new EffectsRenderPacket(caster, mouth.x, mouth.y, mouth.z, mouth.x, mouth.y, mouth.z, color, Reference.EFFECT_LIGHT_CHARGE, 0.95F, 32.0F), new NetworkRegistry.TargetPoint(world.provider.getDimension(), mouth.x, mouth.y, mouth.z, 64.0D));
    }
}
