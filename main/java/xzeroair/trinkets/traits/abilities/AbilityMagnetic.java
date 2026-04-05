package xzeroair.trinkets.traits.abilities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.*;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityMagnetic;
import xzeroair.trinkets.util.helpers.Kinetics;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import javax.annotation.Nonnull;
import java.util.List;

public class AbilityMagnetic extends Ability implements ITickableAbility, IHeldAbility, ITickableInventoryAbility, IToggleAbility, IKeyBindInterface {

    protected final ConfigAbilityMagnetic CONFIG;

    protected boolean toggled;
    protected int mode;

    public AbilityMagnetic() {
        this(TrinketsConfig.SERVER.ABILITIES.MAGNETIC);
    }

    public AbilityMagnetic(ConfigAbilityMagnetic config) {
        super(TrinketsRegistryNames.ModAbilities.MAGNETIC);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.toggled = false;
        this.mode = -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = this.getTranslationKey();
        final KeyEntry key1 = new LangEntry(langKey, "collect");
        final KeyEntry key2 = new LangEntry(langKey, "collectxp", this.CONFIG.PICKUP_XP);
        final KeyEntry key4 = new OptionEntry("collecttoggle", this.CONFIG.PICKUP_XP, helper.toggleCheckTranslation(this.isAbilityToggled()));
        final KeyEntry key6 = new KeyBindEntry("magnetkb", ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName());
        final KeyEntry key7 = new KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
        return helper.formatAddVariables(key, renderID, key1, key2, key4, key6, key7);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (this.isAbilityToggled() && !entity.world.isRemote) {
            this.collectDrops(entity);
        }
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean inHand) {
        if (stack.getItem() instanceof TrinketPolarized) {
            Capabilities.getTrinketProperties(stack, prop -> {
                if (this.isAbilityToggled() != prop.mainAbility()) {
                    this.toggleAbility(prop.mainAbility());
                    this.sendMessageToPlayer(entity);
                }
            });
        }
    }

    protected void collectDrops(EntityLivingBase entity) {
        final Predicate<EntityLivingBase> filter = Predicates.and(EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE);
        final boolean flag = filter.apply(entity);
        if (flag) {
            if (this.CONFIG.COST > 0F) {
                final MagicStats magic = Capabilities.getMagicStats(entity);
                if (magic != null) {
                    if (magic.getMana() < this.CONFIG.COST) {
                        this.toggleAbility(false);
                        return;
                    }
                    if (entity.ticksExisted % this.CONFIG.FREQUENCY == 0 && !magic.spendMana(this.CONFIG.COST)) {
                        this.toggleAbility(false);
                        return;
                    }
                }
            }
            final AxisAlignedBB bBox = entity.getEntityBoundingBox();
//            final List<String> cfg = Arrays.asList(CONFIG.WHITELIST);
            final Predicate<Entity> lootPredicate = Predicates.and(EntitySelectors.IS_ALIVE, ent -> (ent instanceof EntityItem) || ((ent instanceof EntityXPOrb) && this.CONFIG.PICKUP_XP));
            final Predicate<EntityPlayer> otherPlayerPredicate = Predicates.and(EntitySelectors.IS_ALIVE, ent -> {
                if ((ent == null) || (entity == ent) || (entity.getEntityId() == ent.getEntityId())) {
                    return false;
                }
                return Capabilities.getEntityProperties(ent, false, (prop, rtn) -> {
                    IAbilityInterface ability = prop.getAbilityHandler().getAbility(Reference.MODID + ":" + TrinketsRegistryNames.ModAbilities.MAGNETIC);
                    return ability != null && ability instanceof IToggleAbility && ability.isAbilityEnabled();
                });
            });
            final List<Entity> Loot = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(this.CONFIG.RANGE.RANGE_HORIZONTAL, this.CONFIG.RANGE.RANGE_VERTICAL, this.CONFIG.RANGE.RANGE_HORIZONTAL), lootPredicate);
            final List<EntityPlayer> others = entity.world.getEntitiesWithinAABB(EntityPlayer.class, bBox.grow(this.CONFIG.RANGE.RANGE_HORIZONTAL, this.CONFIG.RANGE.RANGE_VERTICAL, this.CONFIG.RANGE.RANGE_HORIZONTAL), otherPlayerPredicate);
            for (final Entity loot : Loot) {
                final double distance = loot.getDistance(entity.posX, entity.posY, entity.posZ);
                boolean someonesCloser = false;
                for (final EntityPlayer otherP : others) {
                    final float dist = loot.getDistance(otherP);
                    if (dist < distance) {
                        someonesCloser = true;
                    }
                }
                if (!someonesCloser) {
                    this.handleLoot(entity, loot);
                }
            }
        }
    }

    protected void handleLoot(Entity entity, Entity drop) {
        if ((drop instanceof EntityItem) || (this.CONFIG.PICKUP_XP && (drop instanceof EntityXPOrb))) {
            if ((entity instanceof EntityPlayer)) {
                final EntityPlayer player = (EntityPlayer) entity;
                if (!player.world.isRemote) {
                    if (drop instanceof EntityItem) {
                        if (this.CONFIG.PICKUP_INSTANT) {
                            this.pickupItem(player, drop);
                        } else {
                            Kinetics.applyForce(entity, drop, true, this.CONFIG.FORCE, 0.8D, 0.85D);
//                            this.applyForce(drop, entity.getPositionVector(), true);
//                            this.pull(drop, entity.posX, entity.posY, entity.posZ);
                        }
                    } else if (drop instanceof EntityXPOrb) {
                        if (this.CONFIG.PICKUP_INSTANT_XP) {
                            this.pickupXP(player, drop);
                        } else {
                            Kinetics.applyForce(entity, drop, true, this.CONFIG.FORCE, 0.8D, 0.85D);
//                            this.applyForce(drop, entity.getPositionVector(), true);
//                            this.pull(drop, entity.posX, entity.posY, entity.posZ);
                        }
                    }
                }
            } else {
                Kinetics.applyForce(entity, drop, true, this.CONFIG.FORCE, 0.8D, 0.85D);
//                this.applyForce(drop, entity.getPositionVector(), true);
//                            this.pull(drop, entity.posX, entity.posY, entity.posZ);
            }
        }
    }

    protected void pickupItem(EntityPlayer player, Entity itemEntity) {
        if (itemEntity instanceof EntityItem) {
            final EntityItem item = (EntityItem) itemEntity;
            if (!(item.getItem().getItem() instanceof TrinketPolarized)) {
                item.onCollideWithPlayer(player);
            }
        }
    }

    protected void pickupXP(EntityPlayer player, Entity xpOrb) {
        if (xpOrb instanceof EntityXPOrb) {
            player.xpCooldown = 0;
            final EntityXPOrb xp = (EntityXPOrb) xpOrb;
            xp.delayBeforeCanPickup = 0;
            xp.onCollideWithPlayer(player);
        }
    }

    protected void applyForce(Entity targetEntity, Vec3d to, boolean isPull) {
        if (!targetEntity.onGround) {
            return;
        }
        boolean test = false;
        if (test) {
            Vec3d from = targetEntity.getPositionVector();

            double lerp = 0.2D; // config (0.05–0.2 is typical)

            targetEntity.setPositionAndUpdate(from.x + (to.x - from.x) * lerp, from.y + (to.y - from.y) * lerp, from.z + (to.z - from.z) * lerp);
        }
        if (!test) {
            Vec3d from = targetEntity.getPositionVector();

            Vec3d delta = to.subtract(from);
            double distance = delta.length();

            if (distance > 0.001D) {
                Vec3d dir = delta.normalize();

                // Configurable values
                double force = this.CONFIG.FORCE;     // base strength
                double maxSpeed = 0.8D;   // clamp to prevent jitter/explosions
                double damping = 0.85D;   // stabilizer

                // Pull = +force, Push = -force
                double signedForce = isPull ? force : -force;

                // Optional: scale by distance (feels more "magnetic")
                double scaledForce = signedForce * Math.min(distance, 1.0D);

                targetEntity.motionX += dir.x * scaledForce;
                targetEntity.motionY += dir.y * scaledForce;
                targetEntity.motionZ += dir.z * scaledForce;

                // Damping to reduce oscillation
                targetEntity.motionX *= damping;
                targetEntity.motionY *= damping;
                targetEntity.motionZ *= damping;

                // Clamp velocity
                double speed = Math.sqrt(targetEntity.motionX * targetEntity.motionX + targetEntity.motionY * targetEntity.motionY + targetEntity.motionZ * targetEntity.motionZ);

                if (speed > maxSpeed) {
                    double scale = maxSpeed / speed;
                    targetEntity.motionX *= scale;
                    targetEntity.motionY *= scale;
                    targetEntity.motionZ *= scale;
                }
                targetEntity.velocityChanged = true;
            }
        }
    }

    @Override
    public boolean isAbilityToggled() {
        return this.toggled;
    }

    @Override
    public int getToggleMode() {
        return this.mode;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        if (this.toggled != enabled) {
            this.toggled = enabled;
            this.setChanged(true);
        }
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        if (this.mode != value) {
            this.mode = value;
            this.setChanged(true);
        }
        return this.toggleAbility(value > 0);
    }

    @Override
    public boolean sendMessageToPlayer(@Nonnull Entity entity) {
        final boolean client = entity.world.isRemote;
        if (!client && (entity instanceof EntityPlayer)) {
            final TranslationHelper helper = TranslationHelper.INSTANCE;
            final String magnetMode = new TextComponentTranslation(this.getTranslationKey() + ".magnetmode").getFormattedText();
            final KeyEntry key = new OptionEntry("collecttoggle", this.CONFIG.PICKUP_XP, helper.toggleCheckTranslation(this.isAbilityToggled()));
            StringUtils.sendStatusMessageToPlayer(entity, helper.formatAddVariables(magnetMode, key), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        if (!Aux) {
            final boolean enabled = this.isAbilityToggled();
            this.toggleAbility(!enabled);
            this.sendMessageToPlayer(entity);
            AbilityHolder holder = this.getAbilityHolder();
            if ((entity instanceof EntityLivingBase)) {
                final ItemStack stack = holder.getInfo().getStackFromHandler((EntityLivingBase) entity);
                if (!stack.isEmpty() && stack.getItem() instanceof TrinketPolarized) {
                    Capabilities.getTrinketProperties(stack, cap -> {
                        cap.toggleMainAbility(this.isAbilityToggled());
                        cap.sendInformationToPlayer(((EntityPlayer) entity));
                    });
                }
            }
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }

}
