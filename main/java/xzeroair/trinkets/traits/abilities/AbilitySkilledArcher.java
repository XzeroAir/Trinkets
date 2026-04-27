package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.enums.BowScalingMode;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IBowAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySkilledArcher;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class AbilitySkilledArcher extends Ability implements ITickableAbility, IBowAbility, IAttackAbility {

    private static final boolean DEBUG_LOGGING = false;

    protected TreeMap<String, ConfigHelper.ConfigEquipmentObject> BowWeights = new TreeMap<>();

    protected ConfigAbilitySkilledArcher CONFIG;

    private final int MAX_SHOT_WINDOW = 10;
    protected boolean drawnBow = false;
    protected boolean drawingBow = false;
    protected boolean released = false;
    protected boolean hitPending = false;
    protected boolean crit = false;
    protected boolean usedMana = false;
    protected boolean usedFullCost = false;
    protected boolean isSneaking = false;

    protected int waitTicks, defaultDrawTime;
    protected float drawWeight, defaultDrawWeight, damageMultiplier;

    public AbilitySkilledArcher() {
        this(TrinketsConfig.SERVER.ABILITIES.SKILLED_ARCHER);
    }

    public AbilitySkilledArcher(ConfigAbilitySkilledArcher config) {
        super(TrinketsRegistryNames.ModAbilities.SKILLED_ARCHER);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.defaultDrawWeight = config.DEFAULT_WEIGHT;
        this.defaultDrawTime = config.CHARGE_SHOT_TIME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

    private float getChargeCurve(int ticks) {
        float charge = ticks / (float) this.defaultDrawTime;
        charge = (charge * charge + charge * 2.0F) / 3.0F;
        if (charge > 1F) charge = 1F;
        return charge;
    }

    private void logUseTick(EntityLivingBase entity, int duration, float charge, float baseCost, float hudCost, float mana) {
        if (!DEBUG_LOGGING) {
            return;
        }
        Trinkets.LOGGER.info(
                "[SkilledArcherDebug][UseTick][{}] duration={}, ticksUsed={}, chargeCurve={}, baseCost={}, hudCost={}, mana={}, sneaking={}, remote={}",
                entity.getName(),
                duration,
                72000 - duration,
                charge,
                baseCost,
                hudCost,
                mana,
                this.isSneaking,
                entity.world.isRemote
        );
    }

    private void logLooseArrowPre(EntityPlayer player, int chargeTicks, float charge, float pounds) {
        if (!DEBUG_LOGGING) {
            return;
        }
        Trinkets.LOGGER.info(
                "[SkilledArcherDebug][LooseArrow-Pre][{}] eventCharge={}, releaseChargeCurve={}, bowWeight={}, sneaking={}, remote={}",
                player.getName(),
                chargeTicks,
                charge,
                pounds,
                player.isSneaking(),
                player.world.isRemote
        );
    }

    private void logLooseArrowMana(EntityPlayer player, int chargeTicks, float manaCost, float mana, float ratio, float manaSpent, float charge) {
        if (!DEBUG_LOGGING) {
            return;
        }
        Trinkets.LOGGER.info(
                "[SkilledArcherDebug][LooseArrow-Mana][{}] eventCharge={}, manaCost={}, manaBefore={}, ratio={}, manaSpent={}, releaseCharge={}, drawWeight={}, crit={}, remote={}",
                player.getName(),
                chargeTicks,
                manaCost,
                mana,
                ratio,
                manaSpent,
                charge,
                this.drawWeight,
                this.crit,
                player.world.isRemote
        );
    }

    private void logLooseArrowSpend(EntityPlayer player, boolean spent, float manaAfter, boolean fullCost) {
        if (!DEBUG_LOGGING) {
            return;
        }
        Trinkets.LOGGER.info(
                "[SkilledArcherDebug][LooseArrow-Spend][{}] spent={}, manaAfter={}, usedFullCost={}",
                player.getName(),
                spent,
                manaAfter,
                fullCost
        );
    }

    private void logDamage(DamageSource source, EntityLivingBase target, float baseDamage, float drawScale, float shotMultiplier, float finalMultiplier, float finalDamage) {
        if (!DEBUG_LOGGING) {
            return;
        }
        Trinkets.LOGGER.info(
                "[SkilledArcherDebug][Damage][{} -> {}] baseDamage={}, drawWeight={}, drawScale={}, shotMultiplier={}, finalMultiplier={}, finalDamage={}, crit={}, usedMana={}, usedFullCost={}",
                source.getTrueSource() != null ? source.getTrueSource().getName() : "unknown",
                target.getName(),
                baseDamage,
                this.drawWeight,
                drawScale,
                shotMultiplier,
                finalMultiplier,
                finalDamage,
                this.crit,
                this.usedMana,
                this.usedFullCost
        );
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) return;

        if (entity.isSneaking()) {
            if (!this.isSneaking) {
                this.isSneaking = true;
            }
        } else {
            if (this.isSneaking) {
                final MagicStats magic = Capabilities.getMagicStats(entity);
                if (magic != null) {
                    magic.syncToManaCostToHud(0);
                }
                this.isSneaking = false;
            }
        }
        if (this.hitPending) {
            this.waitTicks++;
            if (this.waitTicks > this.MAX_SHOT_WINDOW) {
                this.hitPending = false;
                this.reset();
            }
        }
    }

    @Override
    public void knockArrow(ArrowNockEvent event) {
        ItemStack stack = event.getBow();
//        this.CHARGE_SHOT_MAX_DRAW_WEIGHT = getBowWeight(stack);
//        if (this.CHARGE_SHOT_MAX_DRAW_WEIGHT > 0) {
        this.drawingBow = true;
//        }
    }

    @Override
    public int onItemUseTick(EntityLivingBase entity, ItemStack stack, int duration) {
        if (!this.drawingBow) {
            return duration;
        }

        float charge = this.getChargeCurve(72000 - duration);

        if (this.isSneaking) {
            final float ManaCost = this.CONFIG.CHARGE_SHOT_COST;
            final MagicStats magic = Capabilities.getMagicStats(entity);
            if (magic != null) {
                final float Cost = MathHelper.clamp(ManaCost * (charge * 10), 0, magic.getMana());
                this.logUseTick(entity, duration, charge, ManaCost, Cost, magic.getMana());
                magic.syncToManaCostToHud(Cost);
            }
        }
        if (charge >= 1F && !this.drawnBow) {
            this.drawnBow = true;
            if (duration % 5 == 0 && entity.world.isRemote && (entity instanceof EntityPlayer)) {
                entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, SoundCategory.PLAYERS, 0.3F, 0.5F);
            }
        } else {
            if (entity.world.isRemote && (entity instanceof EntityPlayer)) {
                entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.BLOCK_NOTE_GUITAR, SoundCategory.PLAYERS, 0.1F, charge + 0.5F);
            }
        }
        return duration;

    }

    @Override
    public void onItemUseStop(EntityLivingBase entity, ItemStack stack, int duration) {
        this.drawingBow = false;
        this.drawnBow = false;
    }

    @Override
    public void looseArrow(@Nonnull ArrowLooseEvent event) {
        float pounds = this.getBowWeight(event.getBow());
        if (pounds <= 0) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        int chargeTicks = event.getCharge();
        float charge = this.getChargeCurve(chargeTicks);
        this.logLooseArrowPre(player, chargeTicks, charge, pounds);
        this.drawWeight = pounds * charge;
        this.crit = charge >= 1F;

        float scale = this.CONFIG.CHARGE_SHOT_DAMAGE_MULTI;
        if (!this.released) {
            boolean sneaking = player.isSneaking();
            if (sneaking) {
                float manaCost = this.CONFIG.CHARGE_SHOT_COST * charge * 10F;
                if (manaCost > 0F) {
                    MagicStats magic = Capabilities.getMagicStats(player);
                    if (magic != null) {
                        magic.syncToManaCostToHud(0);
                        float mana = magic.getMana();
                        float ratio = Math.min(1F, mana / manaCost);
                        float manaSpent = manaCost * ratio;
                        this.logLooseArrowMana(player, chargeTicks, manaCost, mana, ratio, manaSpent, charge);

                        /* Base bonus from mana usage */
                        scale += ratio;

                        boolean spent = magic.spendMana(manaSpent);
                        this.logLooseArrowSpend(player, spent, magic.getMana(), ratio >= 1F);
                        if (spent) {
                            this.usedMana = true;
                            this.usedFullCost = ratio >= 1F;
                        }

                        /* Extra bonus for full mana charge */
                        if (this.usedFullCost) {
                            scale += this.CONFIG.CHARGE_SHOT_MANA_DAMAGE_MULTI;
                        }
                    }
                }
            }
            if (scale < this.CONFIG.CHARGE_SHOT_MIN_DAMAGE_MULTI) {
                scale = this.CONFIG.CHARGE_SHOT_MIN_DAMAGE_MULTI;
            }
            this.damageMultiplier = scale;
            this.hitPending = true;
            this.waitTicks = 0;
            this.released = true;
        }
    }

    public float getBowWeight(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (!(item instanceof ItemArmor)) {
                String regName = item.getRegistryName().toString();
                String itemType = ConfigHelper.ConfigEquipmentObject.getItemType(stack);
                if (!itemType.isEmpty()) {
                    final String ItemMaterial = ConfigHelper.ConfigEquipmentObject.getItemMaterial(stack).toLowerCase();

                    String[] mS = new String[]{regName, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial};
                    final ConfigHelper.ConfigEquipmentObject main = ConfigHelper.TrinketConfigStorage.getListEntry(ConfigHelper.TrinketConfigStorage.BowWeights, (k, v) -> v.doesItemMatchEntry(stack), mS);
                    if (main != null) {
                        float wt = (float) main.getEquipmentWeight();
                        if (wt <= 1F) {
                            return this.CONFIG.DEFAULT_WEIGHT;
                        } else {
                            return wt;
                        }
                    } else {
                        return 0F;
                    }
                }
            }
        }
        return this.CONFIG.DEFAULT_WEIGHT;
    }

    @Override
    public void arrowImpact(@Nonnull ProjectileImpactEvent.Arrow event) {
        EntityArrow arrow = event.getArrow();
        RayTraceResult result = event.getRayTraceResult();
        if (result != null && result.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
            if (this.drawWeight > 0 && this.crit && this.usedFullCost && this.CONFIG.CHARGE_SHOT_EXPLODES) {
                float strength = this.getExplosionStrength(this.drawWeight);
                arrow.world.createExplosion(arrow, arrow.posX, arrow.posY, arrow.posZ, strength, false);
            }
            this.reset();
        }
    }

    @Override
    public float hurtEntity(EntityLivingBase target, @Nonnull DamageSource source, float dmg) {
        if (!(source.getImmediateSource() instanceof EntityArrow)) return dmg;
        EntityArrow arrow = (EntityArrow) source.getImmediateSource();

        float weight = this.drawWeight;
        float drawScale = this.getDamageScale(this.drawWeight);
        float shotMultiplier = Math.max(this.damageMultiplier, this.CONFIG.CHARGE_SHOT_MIN_DAMAGE_MULTI);
        float finalMultiplier = drawScale * shotMultiplier;
        float scaledDamage = dmg * finalMultiplier;
        this.logDamage(source, target, dmg, drawScale, shotMultiplier, finalMultiplier, scaledDamage);

        if (scaledDamage > 0 && this.crit && this.usedFullCost && this.CONFIG.CHARGE_SHOT_EXPLODES) {
            float strength = this.getExplosionStrength(weight);
            arrow.world.createExplosion(arrow, arrow.posX, arrow.posY, arrow.posZ, strength, false);
        }
        this.reset();
        return scaledDamage;
    }

    private float getWeightFactor(float drawWeight) {
        if (this.defaultDrawWeight <= 0F) return 0F;
        return drawWeight / this.defaultDrawWeight;
    }

    public int getScaledDrawTime(float drawWeight) {
        float factor = this.getWeightFactor(drawWeight);
        return Math.max(5, (int) (this.defaultDrawTime * factor));
    }

    public float getVelocityScale(float drawWeight) {

        float factor = this.getWeightFactor(drawWeight);

        switch (this.getScalingMode()) {

            case LINEAR:
                return factor;
            case SOFT:
                return (float) Math.pow(factor, 0.75);
            case KINETIC:
            default:
                return (float) Math.sqrt(factor);
        }
    }

    public float getDamageScale(float drawWeight) {
        float factor = this.getWeightFactor(drawWeight);
        float scale;
        switch (this.getScalingMode()) {
            case LINEAR:
                scale = factor;
                break;
            case SOFT:
                scale = (float) Math.pow(factor, 0.75);
                break;
            case KINETIC:
            default:
                /* kinetic energy ~ velocity^2 */
                float velocity = (float) Math.sqrt(factor);
                scale = velocity * velocity;
                break;
        }
        return Math.max(scale, this.CONFIG.CHARGE_SHOT_MIN_DAMAGE_MULTI);
    }

    public float getExplosionStrength(float drawWeight) {
        float factor = this.getWeightFactor(drawWeight);
        float base = 1.5F;
        float maxBonus = 2.0F;
        return base + factor * maxBonus;
    }

    public float applyArrowDamage(float baseDamage, float drawWeight) {
        float damageScale = this.getDamageScale(drawWeight);
        return baseDamage * damageScale;
    }

    public float getFinalDamageMultiplier(float drawWeight, float shotMultiplier) {
        return this.getDamageScale(drawWeight) * Math.max(shotMultiplier, this.CONFIG.CHARGE_SHOT_MIN_DAMAGE_MULTI);
    }

    public BowScalingMode getScalingMode() {
        return this.CONFIG.SCALING_MODE;
    }

    private void reset() {
        this.waitTicks = 0;
        this.drawWeight = 0F;
        this.damageMultiplier = 0F;
        this.drawnBow = false;
        this.drawingBow = false;
        this.released = false;
        this.hitPending = false;
        this.crit = false;
        this.usedMana = false;
        this.usedFullCost = false;
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        Capabilities.getMagicStats(entity, magic -> magic.syncToManaCostToHud(0));
    }


    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = this.generateConfigTag(ConfigHelper.TrinketConfigStorage.BowWeights);
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setFloat("defaultDrawWeight", this.defaultDrawWeight);
        tag.setInteger("defaultDrawTime", this.defaultDrawTime);
        return tag;
    }

    protected NBTTagCompound generateConfigTag(LinkedHashMap<String, ConfigHelper.ConfigEquipmentObject> collection) {
        if (collection != null && !collection.isEmpty()) {
            NBTTagCompound tag = new NBTTagCompound();
            int index = 0;
            for (Map.Entry<String, ConfigHelper.ConfigEquipmentObject> e : collection.entrySet()) {
                NBTTagCompound config = new NBTTagCompound();
                config.setString("key", e.getKey());
                config.setString("config", e.getValue().getOriginalEntry());
                tag.setTag(index + "", config);
                index++;
            }
            return tag;
        }
        return null;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        if (!this.BowWeights.isEmpty()) {
            this.BowWeights.clear();
        }
        NBTHelper.hasFloat(tag, "defaultDrawWeight", (value) -> this.defaultDrawWeight = value);
        NBTHelper.hasInteger(tag, "defaultDrawTime", (value) -> this.defaultDrawTime = value);
        int index = 0;
        if (tag != null && !tag.isEmpty()) {
            for (int i = 0; i < tag.getSize(); i++) {
                NBTTagCompound Bows = tag.getCompoundTag(i + "");
                if (!Bows.isEmpty()) {
                    if (Bows.hasKey("config")) {
                        String key = Bows.hasKey("key") ? Bows.getString("key") : "" + index++;
                        this.BowWeights.put(key, new ConfigHelper.ConfigEquipmentObject(Bows.getString("config")));
                    }
                }
            }
        }
    }
}
