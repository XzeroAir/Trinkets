package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.entity.EntitySummonWrapper;
import xzeroair.trinkets.traits.abilities.base.AbilityRaceSpecific;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySummonSlime;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class AbilitySummonSlime extends AbilityRaceSpecific implements ITickableAbility {

    private static final ResourceLocation SLIME_SOURCE = new ResourceLocation("minecraft", "slime");
    private static final ResourceLocation SLIME_HOP_TEMPLATE = new ResourceLocation(Reference.MODID, "slime_hop");
    private static final String SUMMONS_TAG = "SlimeSummons";
    private static final String SUMMON_ID_TAG = "SummonId";
    private static final String WRAPPER_TAG = "Wrapper";
    private static final String CURRENT_HEALTH_TAG = "CurrentHealth";
    private static final String FOLLOW_DIMENSION_TAG = "FollowDimension";
    private static final String ACTIVE_SUMMON_SLOTS_TAG = "ActiveSummonSlots";
    private static final String FREE_SUMMON_SLOTS_TAG = "FreeSummonSlots";
    private static final int COLLISION_GRACE_TICKS = 20 * 10;
    private final List<NBTTagCompound> summons = new ArrayList<>();
    private int activeSummonSlots;
    private int freeSummonSlots;
    private final ConfigAbilitySummonSlime CONFIG;

    public AbilitySummonSlime() {
        this(TrinketsConfig.SERVER.RACES.SLIME.ABILITIES.SUMMON_SLIME);
    }

    public AbilitySummonSlime(ConfigAbilitySummonSlime config) {
        super(TrinketsRegistryNames.ModAbilities.SUMMON_SLIME);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry raceKey = new TranslationHelper.KeyBindEntry("racekb", this.getKey());
        final TranslationHelper.KeyEntry auxKey = new TranslationHelper.KeyBindEntry("auxkb", this.getAuxKey());
        final TranslationHelper.KeyEntry healthCost = new TranslationHelper.OptionEntry("healthcost", true, this.CONFIG.HEALTH_COST_PERCENTAGE * 100F);
        final TranslationHelper.KeyEntry summonLimit = new TranslationHelper.OptionEntry("summonlimit", true, this.CONFIG.MAXIMUM_ACTIVE_SUMMONS);
        final TranslationHelper.KeyEntry activeSummons = new TranslationHelper.OptionEntry("activesummons", true, this.getActiveSummonSlots());
        final TranslationHelper.KeyEntry freeSummonSlots = new TranslationHelper.OptionEntry("freesummons", true, this.getFreeSummonSlots());
        final TranslationHelper.KeyEntry healthPerKill = new TranslationHelper.OptionEntry("healthperkill", true, this.CONFIG.HEALTH_PER_KILL);
        final TranslationHelper.KeyEntry maximumHealth = new TranslationHelper.OptionEntry("maximumhealth", true, this.CONFIG.MAXIMUM_SUMMON_HEALTH);
        final TranslationHelper.KeyEntry foodHealing = new TranslationHelper.OptionEntry("foodhealing", true, this.CONFIG.SLIMEBALL_HEALING);
        return helper.formatAddVariables(key, renderID, raceKey, auxKey, healthCost, summonLimit, activeSummons, freeSummonSlots, healthPerKill, maximumHealth, foodHealing);
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        if (!(entity instanceof EntityLivingBase) || entity.world.isRemote) {
            return true;
        }
        if (Aux) {
            this.dismissSummons((EntityLivingBase) entity);
            return true;
        }

        return this.summonSlime((EntityLivingBase) entity);
    }

    public boolean summonSlime(EntityLivingBase summoner) {
        if (!(summoner instanceof EntityPlayer)) {
            return false;
        }
        final EntityPlayer player = (EntityPlayer) summoner;
        final NBTTagCompound dormantSummon = this.findDormantSummon(player);
        if (dormantSummon == null && this.summons.size() >= this.CONFIG.MAXIMUM_ACTIVE_SUMMONS) {
            return false;
        }
        final float healthCost = summoner.getMaxHealth() * this.CONFIG.HEALTH_COST_PERCENTAGE;
        if (summoner.getHealth() <= healthCost || !FirstAidCompat.canSpendSummonHealth(summoner, healthCost)) {
            return false;
        }

        if (dormantSummon != null) {
            if (!this.restoreSummon(player, dormantSummon)) {
                return false;
            }
            if (!FirstAidCompat.spendSummonHealth(summoner, healthCost)) {
                summoner.setHealth(summoner.getHealth() - healthCost);
            }
            return true;
        }

        final World world = summoner.world;
        if (!EntityList.isRegistered(SLIME_SOURCE)) {
            return false;
        }

        final Entity source = EntityList.createEntityByIDFromName(SLIME_SOURCE, world);
        if (!(source instanceof EntityLivingBase)) {
            return false;
        }

        final EntitySummonWrapper wrapper = new EntitySummonWrapper(world);
        if (!wrapper.setSource((EntityLivingBase) source)) {
            return false;
        }
        wrapper.setSummoner(summoner);
        wrapper.setTemporarySummon(false);
        wrapper.setPersistentSummonId(UUID.randomUUID());
        wrapper.setSummonHealth(healthCost);
        wrapper.setCollisionGraceTicks(COLLISION_GRACE_TICKS);
        if (!wrapper.setBehaviorTemplate(SLIME_HOP_TEMPLATE)) {
            return false;
        }
        wrapper.setPresentationColor(Capabilities.getEntityProperties(summoner, 0, (properties, color) -> properties.getRaceHandler()
                .getPrimaryTraitColor()));

        wrapper.setLocationAndAngles(summoner.posX, summoner.posY, summoner.posZ, summoner.rotationYaw, 0F);
        if (!world.spawnEntity(wrapper)) {
            return false;
        }
        this.storeSummon(wrapper);

        if (!FirstAidCompat.spendSummonHealth(summoner, healthCost)) {
            summoner.setHealth(summoner.getHealth() - healthCost);
        }
        return true;
    }

    private void dismissSummons(EntityLivingBase summoner) {
        if (!(summoner instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer) summoner;
        for (NBTTagCompound summon : this.summons) {
            final EntitySummonWrapper wrapper = this.findSummon(player, summon.getUniqueId(SUMMON_ID_TAG));
            if (wrapper != null) {
                this.storeAndDismiss(wrapper, summoner);
            }
        }
        this.getAbilityHolder().getHandler().saveAbilityStorage(summoner, this);
        this.setChanged(true);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer) || entity.world.isRemote) {
            return;
        }
        final EntityPlayer player = (EntityPlayer) entity;
        for (NBTTagCompound summon : this.summons) {
            final UUID summonId = summon.getUniqueId(SUMMON_ID_TAG);
            final EntitySummonWrapper wrapper = this.findSummon(player, summonId);
            if (wrapper != null) {
                this.writeSummon(wrapper, summon);
            }
        }
        this.updateSummonSlotCounter(player);
    }

    public int getActiveSummonSlots() {
        return this.activeSummonSlots;
    }

    public int getFreeSummonSlots() {
        return this.freeSummonSlots;
    }

    private void updateSummonSlotCounter(EntityPlayer player) {
        int activeSlots = 0;
        for (NBTTagCompound summon : this.summons) {
            if (this.findSummon(player, summon.getUniqueId(SUMMON_ID_TAG)) != null) {
                activeSlots++;
            }
        }
        final int freeSlots = Math.max(0, this.CONFIG.MAXIMUM_ACTIVE_SUMMONS - this.summons.size());
        if (this.activeSummonSlots != activeSlots || this.freeSummonSlots != freeSlots) {
            this.activeSummonSlots = activeSlots;
            this.freeSummonSlots = freeSlots;
            this.setChanged(true);
        }
    }

    public void storeAndDismiss(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        this.storeAndDismiss(wrapper, summoner, false);
    }

    private void storeAndDismiss(EntitySummonWrapper wrapper, EntityLivingBase summoner, boolean followDimension) {
        final UUID summonId = wrapper.getPersistentSummonId();
        if (summonId == null) {
            return;
        }
        for (NBTTagCompound summon : this.summons) {
            if (summonId.equals(summon.getUniqueId(SUMMON_ID_TAG))) {
                this.writeSummon(wrapper, summon);
                if (followDimension) {
                    summon.setBoolean(FOLLOW_DIMENSION_TAG, true);
                } else {
                    summon.removeTag(FOLLOW_DIMENSION_TAG);
                }
                this.getAbilityHolder().getHandler().saveAbilityStorage(summoner, this);
                wrapper.setDead();
                this.setChanged(true);
                return;
            }
        }
    }

    public void removeSummon(UUID summonId) {
        for (Iterator<NBTTagCompound> iterator = this.summons.iterator(); iterator.hasNext(); ) {
            if (summonId.equals(iterator.next().getUniqueId(SUMMON_ID_TAG))) {
                iterator.remove();
                this.setChanged(true);
                return;
            }
        }
    }

    private void storeSummon(EntitySummonWrapper wrapper) {
        final NBTTagCompound summon = new NBTTagCompound();
        summon.setUniqueId(SUMMON_ID_TAG, wrapper.getPersistentSummonId());
        this.writeSummon(wrapper, summon);
        this.summons.add(summon);
        this.setChanged(true);
    }

    private void writeSummon(EntitySummonWrapper wrapper, NBTTagCompound summon) {
        final NBTTagCompound wrapperData = new NBTTagCompound();
        wrapper.writeToNBT(wrapperData);
        wrapperData.removeTag("ActiveEffects");
        summon.setFloat(CURRENT_HEALTH_TAG, wrapper.getHealth());
        summon.setTag(WRAPPER_TAG, wrapperData);
    }

    private EntitySummonWrapper findSummon(EntityPlayer player, UUID summonId) {
        for (Entity entity : player.world.loadedEntityList) {
            if (entity instanceof EntitySummonWrapper && summonId.equals(((EntitySummonWrapper) entity).getPersistentSummonId())) {
                return (EntitySummonWrapper) entity;
            }
        }
        return null;
    }

    @Nullable
    private NBTTagCompound findDormantSummon(EntityPlayer player) {
        for (NBTTagCompound summon : this.summons) {
            if (this.findSummon(player, summon.getUniqueId(SUMMON_ID_TAG)) == null) {
                return summon;
            }
        }
        return null;
    }

    private boolean restoreSummon(EntityPlayer player, NBTTagCompound summon) {
        final NBTTagCompound wrapperData = summon.getCompoundTag(WRAPPER_TAG);
        if (wrapperData.isEmpty()) {
            return false;
        }
        final EntitySummonWrapper wrapper = new EntitySummonWrapper(player.world);
        wrapper.readFromNBT(wrapperData);
        if (summon.hasKey(CURRENT_HEALTH_TAG, 5)) {
            wrapper.setHealth(Math.min(summon.getFloat(CURRENT_HEALTH_TAG), wrapper.getMaxHealth()));
        }
        wrapper.setTemporarySummon(false);
        wrapper.setPersistentSummonId(summon.getUniqueId(SUMMON_ID_TAG));
        wrapper.setSummoner(player);
        wrapper.setCollisionGraceTicks(COLLISION_GRACE_TICKS);
        wrapper.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, 0F);
        if (player.world.spawnEntity(wrapper)) {
            this.writeSummon(wrapper, summon);
            this.getAbilityHolder().getHandler().saveAbilityStorage(player, this);
            this.setChanged(true);
            return true;
        }
        return false;
    }

    @Override
    public void loadStorage(NBTTagCompound compound) {
        this.summons.clear();
        final NBTTagList savedSummons = compound.getTagList(SUMMONS_TAG, 10);
        for (int index = 0; index < savedSummons.tagCount() && this.summons.size() < this.CONFIG.MAXIMUM_ACTIVE_SUMMONS; ++index) {
            final NBTTagCompound summon = savedSummons.getCompoundTagAt(index);
            if (summon.hasUniqueId(SUMMON_ID_TAG) && summon.hasKey(WRAPPER_TAG, 10)) {
                this.summons.add(summon.copy());
            }
        }
    }

    @Override
    public NBTTagCompound saveStorage(NBTTagCompound compound) {
        final NBTTagList savedSummons = new NBTTagList();
        for (NBTTagCompound summon : this.summons) {
            savedSummons.appendTag(summon.copy());
        }
        compound.setTag(SUMMONS_TAG, savedSummons);
        return compound;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        NBTHelper.hasInteger(tag, ACTIVE_SUMMON_SLOTS_TAG, (value) -> this.activeSummonSlots = value);
        NBTHelper.hasInteger(tag, FREE_SUMMON_SLOTS_TAG, (value) -> this.freeSummonSlots = value);
    }

    @Override
    public NBTTagCompound sendAbilityData() {
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(ACTIVE_SUMMON_SLOTS_TAG, this.activeSummonSlots);
        tag.setInteger(FREE_SUMMON_SLOTS_TAG, this.freeSummonSlots);
        return tag;
    }

    public static void storePersistentSummon(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        if (!(summoner instanceof EntityPlayer)) {
            return;
        }
        Capabilities.getEntityProperties(summoner, properties -> {
            for (xzeroair.trinkets.traits.AbilityHandler.AbilityHolder holder : properties.getAbilityHandler()
                    .getActiveAbilities()
                    .values()) {
                if (holder.getAbility() instanceof AbilitySummonSlime) {
                    ((AbilitySummonSlime) holder.getAbility()).storeAndDismiss(wrapper, summoner);
                }
            }
        });
    }

    public static void storeDimensionFollowingSummon(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        if (!(summoner instanceof EntityPlayer)) {
            return;
        }
        Capabilities.getEntityProperties(summoner, properties -> {
            for (xzeroair.trinkets.traits.AbilityHandler.AbilityHolder holder : properties.getAbilityHandler()
                    .getActiveAbilities()
                    .values()) {
                if (holder.getAbility() instanceof AbilitySummonSlime) {
                    ((AbilitySummonSlime) holder.getAbility()).storeAndDismiss(wrapper, summoner, true);
                }
            }
        });
    }

    public static void restoreDimensionFollowingSummons(EntityPlayer player) {
        Capabilities.getEntityProperties(player, properties -> {
            for (xzeroair.trinkets.traits.AbilityHandler.AbilityHolder holder : properties.getAbilityHandler()
                    .getActiveAbilities()
                    .values()) {
                if (holder.getAbility() instanceof AbilitySummonSlime) {
                    ((AbilitySummonSlime) holder.getAbility()).restorePendingDimensionFollowingSummons(player);
                }
            }
        });
    }

    private void restorePendingDimensionFollowingSummons(EntityPlayer player) {
        boolean restored = false;
        for (NBTTagCompound summon : this.summons) {
            if (summon.getBoolean(FOLLOW_DIMENSION_TAG) && this.restoreSummon(player, summon)) {
                summon.removeTag(FOLLOW_DIMENSION_TAG);
                restored = true;
            }
        }
        if (restored) {
            this.getAbilityHolder().getHandler().saveAbilityStorage(player, this);
            this.setChanged(true);
        }
    }

    public static void removePersistentSummon(EntitySummonWrapper wrapper) {
        final EntityLivingBase summoner = wrapper.getSummoner();
        final UUID summonId = wrapper.getPersistentSummonId();
        if (!(summoner instanceof EntityPlayer) || summonId == null) {
            return;
        }
        Capabilities.getEntityProperties(summoner, properties -> {
            for (xzeroair.trinkets.traits.AbilityHandler.AbilityHolder holder : properties.getAbilityHandler()
                    .getActiveAbilities()
                    .values()) {
                if (holder.getAbility() instanceof AbilitySummonSlime) {
                    ((AbilitySummonSlime) holder.getAbility()).removeSummon(summonId);
                }
            }
        });
    }
}
