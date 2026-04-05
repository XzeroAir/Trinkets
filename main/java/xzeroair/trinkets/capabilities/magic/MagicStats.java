package xzeroair.trinkets.capabilities.magic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.attributes.MagicAttributes;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityEntityBase;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.mana.SyncManaCostToHudPacket;
import xzeroair.trinkets.network.mana.SyncManaStatsPacket;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.mana.EntityManaConfig;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.StringUtils;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MagicStats extends CapabilityEntityBase<MagicStats, EntityLivingBase> {

    private final String TAG_KEY = Reference.MODID + ":magic";
    private final EntityManaConfig manaConfig = TrinketsConfig.SERVER.MAGIC;

    private float mana = 100f;
    private double bonusMana = 0;
    private boolean sync = false;

    private double manaUpdateTickRate = 0;
    private double manaRegenTimeout = 0;

    private final UpdatingAttribute MANA_BONUS;

    public MagicStats(EntityLivingBase e) {
        super(e);
        this.MANA_BONUS = new UpdatingAttribute("BonusMax", UUID.fromString("a3b8802c-e521-45c0-b126-eb45692f68eb"), MagicAttributes.MAX_MANA).setSavedInNBT(true);
    }

    @Override
    public NBTTagCompound getTag() {
        final NBTTagCompound tag = NBTHelper.getEntityTag(this.getEntity());
        if (tag != null) {
            if (!tag.hasKey(this.TAG_KEY)) {
                tag.setTag(this.TAG_KEY, new NBTTagCompound());
            }
            return tag.getCompoundTag(this.TAG_KEY);
        } else {
            return super.getTag();
        }
    }

    public boolean onRegenCooldown() {
        boolean manaEnabled = TrinketsConfig.SERVER.MAGIC.mana_enabled;
        if (!manaEnabled || (this.manaRegenTimeout <= 0)) {
            this.manaRegenTimeout = 0;
            return false;
        }
        this.manaRegenTimeout--;
        return true;
    }

    @Override
    public void onUpdate() {
        if (this.MANA_BONUS != null) {
            float bonusPerPoint = TrinketsConfig.SERVER.MAGIC.bonus;
            if (bonusPerPoint > 0) {
                double bonus = this.getBonusMana();
                double amount = bonusPerPoint * bonus;
                this.MANA_BONUS.addModifier(this.getEntity(), amount, 0);
            } else {
                this.MANA_BONUS.removeModifier(this.getEntity());
            }
        }
        if (this.getEntity().world.isRemote) {
            this.sync = false;
            return;
        }
        if (!TrinketsConfig.SERVER.MAGIC.mana_enabled || this.isCreativePlayer()) {
            this.refillMana();
            return;
        }
        if (this.onRegenCooldown()) {
            return;
        }
        if (this.getMana() > this.getMaxMana()) {
            this.setMana(this.getMaxMana());
        } else if (this.getMana() < this.getMaxMana()) {
            this.manaUpdateTickRate++;
            final IAttributeInstance cooldown = this.getEntity().getAttributeMap().getAttributeInstance(MagicAttributes.regenCooldown);
            double cooldownMulti = cooldown != null ? cooldown.getAttributeValue() : 1D;
            if (this.manaUpdateTickRate > (this.manaConfig.mana_update_ticks * cooldownMulti)) {
                /*
                 * TODO Fix Affinity Maybe setup a field that determines the regen amount Maybe
                 * setup something to reduce the ticks needed to regen
                 */
                final IAttributeInstance attribute = this.getEntity().getAttributeMap().getAttributeInstance(MagicAttributes.regen);
                this.addMana(attribute != null ? (float) attribute.getAttributeValue() : 1F);
                this.manaUpdateTickRate = 0;
            }
        }
        if (this.sync) {
            this.sync = false;
            this.refillMana(); // this only triggers when changing dimension from the end to the overworld, or when the player dies
        }
    }

    public void refillMana() {
        if (this.getMana() != this.getMaxMana()) {
            this.setMana(this.getMaxMana());
        }
    }

    /**
     * Getters, Setters and Booleans
     */

    public float getMana() {
        return this.mana;
    }

    public void setMana(float mana) {
        if (!this.getEntity().world.isRemote) {
            float amount = Math.min(Math.max(mana, 0), this.getMaxMana());
            if (this.mana != amount) {
                this.mana = amount;
                this.sendInformationToPlayer();
            }
        }
    }

    public void addMana(float mana) {
        this.setMana(this.mana + mana);
    }

    public boolean spendMana(float cost) {
        boolean isCreative = (this.getEntity() instanceof EntityPlayer) && ((EntityPlayer) this.getEntity()).isCreative();
        boolean manaEnabled = TrinketsConfig.SERVER.MAGIC.mana_enabled;

        if (!manaEnabled || isCreative) {
            return true;
        }
        if (cost <= 0 && !this.getEntity().world.isRemote) {
            return true;
        } else if ((cost > 0) && (cost <= this.getMana())) {
            this.setMana(this.mana - cost);
            this.setManaRegenTimeout();
            return true;
        } else if ((cost > this.getMana())) {
            StringUtils.sendStatusMessageToPlayer(this.getEntity(), "No MP", true);
        }
        return false;
    }

    public float getMaxMana() {
        IAttributeInstance maxMana = this.getEntity().getEntityAttribute(MagicAttributes.MAX_MANA);
        if (maxMana != null) {
            final float max = (float) maxMana.getAttributeValue();
            final float maxAffinityBonus = (float) ((maxMana.getBaseValue() * (this.getMagicAffinity() * 0.01F)) - maxMana.getBaseValue());
            return Math.max(max + maxAffinityBonus, 0);
        } else {
            return 100F;
        }
    }

    public double getBonusMana() {
        return this.bonusMana;
    }

    public boolean needMana() {
        return this.getMana() < this.getMaxMana();
    }

    public void setBonusMana(double bonus) {
        if (bonus < 0) {
            this.bonusMana = 0;
        } else {
            this.bonusMana = bonus;
        }
        if (this.bonusMana > this.manaConfig.bonus_max) {
            this.bonusMana = this.manaConfig.bonus_max;
        }
        this.sendInformationToPlayer(this.getEntity());
    }

    public void setManaRegenTimeout() {
        final IAttributeInstance attribute = this.getEntity().getAttributeMap().getAttributeInstance(MagicAttributes.regenCooldown);
        double cooldownMulti = attribute != null ? attribute.getAttributeValue() : 1D;
        this.setManaRegenTimeout((int) (this.manaConfig.mana_regen_timeout * cooldownMulti));
        //		this.setManaRegenTimeout(manaConfig.mana_regen_timeout);
    }

    public void setManaRegenTimeout(int timeout) {
        this.manaRegenTimeout = timeout;
    }

    public int getMagicAffinity() {
        IAttributeInstance affinity = this.getEntity().getEntityAttribute(MagicAttributes.affinity);
        if (affinity != null) {
            int amount = (int) affinity.getAttributeValue();
            amount += (this.getRacialAffinity());
            return amount;
        } else {
            return 0;
        }
    }

    public int getRacialAffinity() {
        return Capabilities.getEntityProperties(this.getEntity(), EntityRaces.none, (prop, r) -> prop.getCurrentRace().getRace()).getMagicAffinity();
    }

    @Override
    public void onJoinWorld() {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(this.getEntity());
        }
    }

    @Override
    public void onLogin() {
    }

    @Override
    public void onLogoff() {
    }

    @Override
    public void onChangedDimension(int from, int to) {

    }

    public void sendInformationToPlayer() {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(this.getEntity(), this.saveToNBT(new NBTTagCompound()));
        }
    }

    public void sendInformationToPlayer(EntityLivingBase receiver) {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(receiver, this.saveToNBT(new NBTTagCompound()));
        }
    }

    public void sendInformationToPlayer(EntityLivingBase receiver, NBTTagCompound tag) {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote && (receiver instanceof EntityPlayerMP)) {
            NetworkHandler.sendTo(new SyncManaStatsPacket(this.getEntity(), tag), (EntityPlayerMP) receiver);
        }
    }

    public void sendInformationToTracking(NBTTagCompound tag) {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote && (world instanceof WorldServer)) {
            final WorldServer w = (WorldServer) world;
            NetworkHandler.sendToClients(w, this.getEntity().getPosition(), new SyncManaStatsPacket(this.getEntity(), tag));
        }
    }

    public void syncToManaCostToHud(float cost) {
        if ((this.getEntity() instanceof EntityPlayer) && !this.getEntity().world.isRemote) {
            NetworkHandler.sendTo(new SyncManaCostToHudPacket(cost), (EntityPlayerMP) this.getEntity());
        }
    }

    /**
     * Handle NBT
     */
    @Override
    public void copyFrom(@Nonnull MagicStats stats, boolean wasDeath, boolean keepInv) {
        this.bonusMana = stats.bonusMana;
        if (wasDeath) {
            if (keepInv) {
                this.mana = stats.mana;
            } else {
                this.mana = this.getMaxMana();
            }
        } else {
            this.mana = stats.mana;
        }
        this.sync = true;
    }

    @Override
    public NBTTagCompound saveToNBT(@Nonnull NBTTagCompound tag) {
        tag.setFloat("mana", this.getMana());
        tag.setDouble("bonus_mana", this.getBonusMana());
        return tag;
    }

    @Override
    public void loadFromNBT(@Nonnull NBTTagCompound tag) {
        if (tag.hasKey("mana")) {
            this.mana = tag.getFloat("mana");
        }
        if (tag.hasKey("bonus_mana")) {
            this.bonusMana = tag.getDouble("bonus_mana");
        }
    }

}
