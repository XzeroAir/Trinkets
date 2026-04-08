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

    protected TreeMap<String, ConfigHelper.ConfigEquipmentObject> BowWeights = new TreeMap<>();

    protected ConfigAbilitySkilledArcher CONFIG;

    private final int MAX_SHOT_WINDOW = 10;
    protected boolean drawnBow = false;
    protected boolean drawingBow = false;
    protected boolean isUsingABow = false;
    protected boolean released = false;
    protected boolean hitPending = false;
    protected boolean crit = false;
    protected boolean explosion = false;
    protected boolean usedMana = false;
    protected boolean usedFullCost = false;
    protected boolean isSneaking = false;

    protected int heldTicks, waitTicks, distanceTicks, defaultDrawTime;
    protected float bowWeight, drawWeight, defaultDrawWeight, damageMultiplier;

    private static final String TAG_MULT = "chargedShotMultiplier";
    private static final String TAG_WEIGHT = "chargedShotWeight";
    private static final String TAG_CRIT = "chargedShotCrit";
    private static final String TAG_FULL = "chargedShotFullCost";

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

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) return;
//        if (shotPending) {
//            shotTicks++;
//            if (shotTicks > MAX_SHOT_WINDOW) {
//                shotPending = false;
//                reset();
//            }
//        }
//        ItemStack stack = entity.getActiveItemStack();
//        boolean usingBow = entity.isHandActive() && !stack.isEmpty();
//        if (!usingBow) {
//            reset();
//            return;
//        }
//        boolean isBow = isUsingABow;
//        if (!isBow) {
//            reset();
//            return;
//        }
//

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
//            reset();
            return duration;
        }
        if (!this.isUsingABow) {
            this.isUsingABow = true;
        }

        float charge = this.getChargeCurve(72000 - duration);

        if (this.isSneaking) {
            final float ManaCost = this.CONFIG.CHARGE_SHOT_COST;
            final MagicStats magic = Capabilities.getMagicStats(entity);
            if (magic != null) {
                final float Cost = MathHelper.clamp(ManaCost * (charge * 10), 0, magic.getMana());
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
//        if (!charging) return;
        this.isUsingABow = false;
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
        float charge = this.getChargeCurve(72000 - chargeTicks);
        float c = this.getChargeCurve(chargeTicks);
        this.drawWeight = pounds * c;
        this.crit = c >= 1F;

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

                        /* Base bonus from mana usage */
                        scale += ratio;

                        if (magic.spendMana(manaSpent)) {
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


//        event.setCanceled(true);


//        spawnChargedArrow(player, world, event.getBow(), chargeTicks);
//        reset();
    }

//    private void spawnChargedArrow(EntityPlayer player, @Nonnull World world, ItemStack bow, int chargeTicks) {
//
//        if (world.isRemote || CHARGE_SHOT_MAX_DRAW_WEIGHT <= 0F) return;
//
//        boolean infinite = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow) > 0;
//
//        ItemStack ammo = findAmmo(player);
//
//        if (ammo.isEmpty() && !infinite) return;
//
//        if (ammo.isEmpty()) {
//            ammo = new ItemStack(Items.ARROW);
//        }
//
//        ItemArrow itemarrow = (ItemArrow) (ammo.getItem() instanceof ItemArrow ? ammo.getItem() : Items.ARROW);
//
//        EntityArrow arrow = itemarrow.createArrow(world, ammo, player);
//        arrow.shootingEntity = player;
//
//        this.applyArrowVelocity(arrow, player, chargeTicks, CHARGE_SHOT_MAX_DRAW_WEIGHT);
//
//        if (critReady) {
//            arrow.setIsCritical(true);
//        }
//
//        int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);
//        if (power > 0) {
//            arrow.setDamage(arrow.getDamage() + power * 0.5D + 0.5D);
//        }
//
//        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);
//        if (punch > 0) {
//            arrow.setKnockbackStrength(punch);
//        }
//
//        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) {
//            arrow.setFire(100);
//        }
//
//        NBTTagCompound data = arrow.getEntityData();
//        data.setFloat(TAG_MULT, damageMultiplier);
//        data.setFloat(TAG_WEIGHT, drawWeight);
//        data.setBoolean(TAG_CRIT, critReady);
//        data.setBoolean(TAG_FULL, fullManaBoost);
//
//        bow.damageItem(1, player);
//
//        if (!infinite) {
//            ammo.shrink(1);
//            if (ammo.isEmpty()) {
//                player.inventory.deleteStack(ammo);
//            }
//        }
//
//        world.spawnEntity(arrow);
//    }

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

//    private float getVanillaVelocity(int charge) {
//        float f = charge / 20.0F;
//        f = (f * f + f * 2.0F) / 3.0F;
//
//        if (f > 1.0F) {
//            f = 1.0F;
//        }
//
//        return f;
//    }

//    private ItemStack findAmmo(@Nonnull EntityPlayer player) {
//
//        if (isArrow(player.getHeldItem(EnumHand.OFF_HAND))) return player.getHeldItem(EnumHand.OFF_HAND);
//        if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) return player.getHeldItem(EnumHand.MAIN_HAND);
//
//        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
//            ItemStack itemstack = player.inventory.getStackInSlot(i);
//
//            if (isArrow(itemstack)) {
//                return itemstack;
//            }
//        }
//
//        return ItemStack.EMPTY;
//    }

//    private boolean isArrow(@Nonnull ItemStack stack) {
//        return stack.getItem() instanceof ItemArrow;
//    }

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

        float mult = this.damageMultiplier;
        float weight = this.drawWeight;
        float scaledDamage = this.applyArrowDamage(dmg * this.damageMultiplier, this.drawWeight);

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
        return scale * this.CONFIG.CHARGE_SHOT_DAMAGE_MULTI;
    }

    public float getExplosionStrength(float drawWeight) {
        float factor = this.getWeightFactor(drawWeight);
        float base = 1.5F;
        float maxBonus = 2.0F;
        return base + factor * maxBonus;
    }

//    public void applyArrowVelocity(EntityArrow arrow, EntityPlayer player, int chargeTicks, float drawWeight) {
//        float vanillaVelocity = getVanillaVelocity(chargeTicks);
//        float velocityScale = getVelocityScale(drawWeight);
//        float finalVelocity = vanillaVelocity * 3.0F * velocityScale;
//        arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, finalVelocity, 1.0F);
//    }

    public float applyArrowDamage(float baseDamage, float drawWeight) {
        float damageScale = this.getDamageScale(drawWeight);
        return baseDamage * damageScale;
    }

//    public float getArmorPenetration(float drawWeight) {
//        float factor = getWeightFactor(drawWeight);
//        return Math.min(0.35F, factor * 0.35F);
//    }

    public BowScalingMode getScalingMode() {
        return this.CONFIG.SCALING_MODE;
    }

    private boolean arrowReleased() {
        return false;
    }

    private void reset() {
        this.heldTicks = 0;
        this.waitTicks = 0;
        this.distanceTicks = 0;
        this.bowWeight = 0F;
        this.drawWeight = 0F;
        this.damageMultiplier = 0F;
        this.drawnBow = false;
        this.drawingBow = false;
        this.isUsingABow = false;
        this.released = false;
        this.hitPending = false;
        this.crit = false;
        this.explosion = false;
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
