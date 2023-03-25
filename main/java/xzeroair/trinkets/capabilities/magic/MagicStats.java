package xzeroair.trinkets.capabilities.magic;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import xzeroair.trinkets.attributes.MagicAttributes;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.mana.SyncManaCostToHudPacket;
import xzeroair.trinkets.network.mana.SyncManaStatsPacket;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.mana.EntityManaConfig;

public class MagicStats extends CapabilityBase<MagicStats, EntityLivingBase> {

	private static final EntityManaConfig manaConfig = TrinketsConfig.SERVER.mana;

	private float mana = 100f;
	private float maxMana = 100f;
	private double bonusMana = 0;
	private boolean sync = false;

	private double manaUpdateTickRate = 0;
	private double manaRegenTimeout = 0;

	private UpdatingAttribute MANA_BONUS;

	public MagicStats(EntityLivingBase e) {
		super(e);
		MANA_BONUS = new UpdatingAttribute("BonusMax", UUID.fromString("a3b8802c-e521-45c0-b126-eb45692f68eb"), MagicAttributes.MAX_MANA).setSavedInNBT(true);
	}

	@Override
	public NBTTagCompound getTag() {
		NBTTagCompound tag = object.getEntityData();
		if (tag != null) {
			final NBTTagCompound persistentData;
			if (object instanceof EntityPlayer) {
				if (!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
					tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
				}
				persistentData = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			} else {
				persistentData = tag;
			}
			final String capTag = Reference.MODID + ".magicstats";
			if (!persistentData.hasKey(capTag)) {
				persistentData.setTag(capTag, new NBTTagCompound());
			}
			return persistentData.getCompoundTag(capTag);
		}
		return super.getTag();
	}

	public boolean onRegenCooldown() {
		if (manaRegenTimeout > 0) {
			manaRegenTimeout--;
			return true;
		} else {
			manaRegenTimeout = 0;
			return false;
		}
	}

	@Override
	public void onUpdate() {
		if (object.getEntityWorld().isRemote) {
			sync = false;
			return;
		}
		if (MANA_BONUS != null) {
			float bonusPerPoint = TrinketsConfig.SERVER.mana.bonus;
			if (bonusPerPoint > 0) {
				double bonus = this.getBonusMana();
				float maxBonus = TrinketsConfig.SERVER.mana.cap_bonus;
				double amount = maxBonus > 0 ? Math.min(bonusPerPoint * bonus, maxBonus) : bonusPerPoint * bonus;
				MANA_BONUS.addModifier(object, amount, 0);
			} else {
				MANA_BONUS.removeModifier(object);
			}
		}
		final boolean isCreative = (object instanceof EntityPlayer) && (((EntityPlayer) object).isCreative());
		if (!TrinketsConfig.SERVER.mana.mana_enabled || isCreative) {
			this.refillMana();
			return;
		}
		if (this.onRegenCooldown()) {
			return;
		}
		if (this.getMana() > this.getMaxMana()) {
			this.setMana(this.getMaxMana());
		}
		if (this.getMana() < this.getMaxMana()) {
			manaUpdateTickRate++;
			final IAttributeInstance cooldown = object.getAttributeMap().getAttributeInstance(MagicAttributes.regenCooldown);
			double cooldownMulti = cooldown != null ? cooldown.getAttributeValue() : 1D;
			if (manaUpdateTickRate > (manaConfig.mana_update_ticks * cooldownMulti)) {
				/*
				 * TODO Fix Affinity Maybe setup a field that determines the regen amount Maybe
				 * setup something to reduce the ticks needed to regen
				 */
				final IAttributeInstance attribute = object.getAttributeMap().getAttributeInstance(MagicAttributes.regen);
				this.addMana(attribute != null ? (float) attribute.getAttributeValue() : 1F);
				manaUpdateTickRate = 0;
			}
		}
		if (sync) {
			sync = false;
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
		return mana;
	}

	public void setMana(float mana) {
		//		System.out.println(this.mana + "|" + mana);
		if (!object.world.isRemote) {
			if (mana > this.getMaxMana()) {
				this.mana = this.getMaxMana();
			} else if (mana < 0) {
				this.mana = 0;
			} else {
				this.mana = mana;
			}
			//		sync = true;
			this.sendManaToPlayer(object);
		}
	}

	public void addMana(float mana) {
		this.setMana(this.mana + mana);
	}

	public boolean spendMana(float cost) {
		final boolean isCreative = (object instanceof EntityPlayer) && (((EntityPlayer) object).isCreative());
		boolean spend = true;
		if (isCreative || !TrinketsConfig.SERVER.mana.mana_enabled) {
			return true;
		} else if (cost <= 0) {
			return true;
		} else if (cost <= this.getMana()) {
			spend = true;
		} else {
			spend = false;
			if ((object instanceof EntityPlayer) && object.world.isRemote) {
				final String Message = "No MP";
				((EntityPlayer) object).sendStatusMessage(new TextComponentString(Message), true);
			}
		}

		if (spend == true) {
			this.setMana(mana - cost);
			this.setManaRegenTimeout();
		}
		return spend;
	}

	public float getMaxMana() {
		IAttributeInstance maxMana = object.getEntityAttribute(MagicAttributes.MAX_MANA);
		if (maxMana != null) {
			final float max = (float) maxMana.getAttributeValue();
			final float maxAffinityBonus = (float) ((maxMana.getBaseValue() * (this.getRacialAffinity() * 0.01F)) - maxMana.getBaseValue());
			if (manaConfig.mana_cap) {
				if (manaConfig.cap_affinity) {
					return MathHelper.clamp(max, 0F, manaConfig.mana_max);
				} else {
					return MathHelper.clamp(max, 0F, manaConfig.mana_max) + maxAffinityBonus;
				}
			}
			return Math.max(max + maxAffinityBonus, 0);
		} else {
			return 100F;
		}
	}

	public double getBonusMana() {
		return bonusMana;
	}

	public boolean needMana() {
		return mana < this.getMaxMana();
	}

	public void setBonusMana(double bonus) {
		if (bonus < 0) {
			bonusMana = 0;
		} else {
			bonusMana = bonus;
		}
		this.sendManaToPlayer(object);
	}

	public void setManaRegenTimeout() {
		final IAttributeInstance attribute = object.getAttributeMap().getAttributeInstance(MagicAttributes.regenCooldown);
		double cooldownMulti = attribute != null ? attribute.getAttributeValue() : 1D;
		this.setManaRegenTimeout((int) (manaConfig.mana_regen_timeout * cooldownMulti));
		//		this.setManaRegenTimeout(manaConfig.mana_regen_timeout);
	}

	public void setManaRegenTimeout(int timeout) {
		manaRegenTimeout = timeout;
	}

	public int getMagicAffinity() {
		IAttributeInstance affinity = object.getEntityAttribute(MagicAttributes.affinity);
		if (affinity != null) {
			int amount = (int) affinity.getAttributeValue();
			amount += (this.getRacialAffinity() - 100);
			return amount;
		} else {
			return 100;
		}
	}

	public int getRacialAffinity() {
		return Capabilities.getEntityProperties(object, EntityRaces.none, (prop, r) -> prop.getCurrentRace()).getMagicAffinity();
	}

	/**
	 * Send Mana from Server to a Client player
	 */

	public void sendManaToPlayer(EntityLivingBase e) {
		if ((e instanceof EntityPlayerMP)) {
			NetworkHandler.sendTo(new SyncManaStatsPacket(object, this), (EntityPlayerMP) e);
		}
	}

	public void syncToManaCostToHud(float cost) {
		if ((object instanceof EntityPlayer) && !object.world.isRemote) {
			NetworkHandler.sendTo(new SyncManaCostToHudPacket(cost), (EntityPlayerMP) object);
		}
	}

	/**
	 * Handle NBT
	 */
	@Override
	public void copyFrom(MagicStats stats, boolean wasDeath, boolean keepInv) {
		bonusMana = stats.bonusMana;
		if (wasDeath) {
			if (keepInv) {
				mana = stats.mana;
			} else {
				mana = this.getMaxMana();
			}
		} else {
			mana = stats.mana;
		}
		sync = true;
	}

	@Override
	public NBTTagCompound saveToNBT(NBTTagCompound tag) {
		tag.setFloat("mana", mana);
		tag.setDouble("bonus_mana", bonusMana);
		return tag;
	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("mana")) {
			mana = tag.getFloat("mana");
		}
		if (tag.hasKey("bonus_mana")) {
			bonusMana = tag.getDouble("bonus_mana");
		}
	}

}
