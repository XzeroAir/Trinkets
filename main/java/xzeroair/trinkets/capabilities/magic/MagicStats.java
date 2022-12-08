package xzeroair.trinkets.capabilities.magic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import xzeroair.trinkets.attributes.MagicAttributes;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.capabilities.race.EntityProperties;
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
	private int bonusMana = 0;
	private int racialBonus = 100;
	private boolean sync = false;

	private int manaUpdateTickRate = 0;
	private int manaRegenTimeout = 0;

	public MagicStats(EntityLivingBase e) {
		super(e);
	}

	@Override
	public NBTTagCompound getTag() {
		final NBTTagCompound forgeData = object.getEntityData();
		if ((forgeData != null) && (object instanceof EntityPlayer)) {
			if (!forgeData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
				forgeData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			}

			final NBTTagCompound persistentData = forgeData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			final String capTag = Reference.MODID + ".magicstats";
			if (!persistentData.hasKey(capTag)) {
				persistentData.setTag(capTag, new NBTTagCompound());
			}
			return persistentData.getCompoundTag(capTag);
		}
		return super.getTag();
	}

	@Override
	public void onUpdate() {
		final boolean isCreative = (object instanceof EntityPlayer) && (((EntityPlayer) object).isCreative() || ((EntityPlayer) object).isSpectator());
		if (!TrinketsConfig.SERVER.mana.mana_enabled || isCreative) {
			this.refillMana();
			return;
		}
		if (object.getEntityWorld().isRemote) {
			sync = false;
			return;
		}
		if (manaRegenTimeout > 0) {
			manaRegenTimeout--;
			return;
		} else {
			manaRegenTimeout = 0;
		}
		if (this.getMana() > this.getMaxMana()) {
			this.setMana(this.getMaxMana());
		}
		if (this.getMana() < this.getMaxMana()) {
			manaUpdateTickRate++;
			if (manaUpdateTickRate > manaConfig.mana_update_ticks) {
				/*
				 * TODO Fix Affinity Maybe setup a field that determines the regen amount Maybe
				 * setup something to reduce the ticks needed to regen
				 */
				final IAttributeInstance attribute = object.getAttributeMap().getAttributeInstance(MagicAttributes.regen);
				if (attribute != null) {
					this.addMana((float) attribute.getAttributeValue());
				} else {
					this.addMana(1F);
				}
				manaUpdateTickRate = 0;
			}
		}
		if (sync) {
			sync = false;
			this.refillMana();
			//			this.sendManaToPlayer(object);
		}
		//		if (Trinkets.proxy.getSide() == Side.CLIENT) {
		//			ScreenOverlayEvents.instance.SyncMana(this.getMana(), this.getBonusMana(), this.getMaxMana());
		//		}
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
		final boolean isCreative = (object instanceof EntityPlayer) && (((EntityPlayer) object).isCreative() || ((EntityPlayer) object).isSpectator());
		boolean spend = true;
		if (isCreative || !TrinketsConfig.SERVER.mana.mana_enabled) {
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
		final float maxAffinityBonus = ((maxMana * (this.getRacialAffinity() * 0.01F)) - maxMana);
		final float maxBonus = maxMana + (10F * this.getBonusMana());
		final float max = maxAffinityBonus + maxBonus;
		if (manaConfig.mana_cap) {
			if (manaConfig.cap_affinity) {
				return MathHelper.clamp(max, 0F, manaConfig.mana_max);
			} else {
				return MathHelper.clamp(maxBonus, 0F, manaConfig.mana_max) + maxAffinityBonus;
			}
		} else {
			return max;
		}
	}

	public int getBonusMana() {
		return bonusMana;
	}

	public boolean needMana() {
		return mana < this.getMaxMana();
	}

	public void setBonusMana(int bonus) {
		if (bonus < 0) {
			bonusMana = 0;
		} else {
			bonusMana = bonus;
		}
		this.sendManaToPlayer(object);
	}

	public void setManaRegenTimeout() {
		this.setManaRegenTimeout(manaConfig.mana_regen_timeout);
	}

	public void setManaRegenTimeout(int timeout) {
		manaRegenTimeout = timeout;
	}

	public int getRacialAffinity() {
		// TODO Add Affinity Attribute Here
		final EntityProperties race = Capabilities.getEntityProperties(object);
		if (race != null) {
			racialBonus = race.getCurrentRace().getMagicAffinity();
		}
		return racialBonus;
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
		tag.setInteger("bonus_mana", bonusMana);
		return tag;
	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("mana")) {
			mana = tag.getFloat("mana");
		}
		if (tag.hasKey("bonus_mana")) {
			bonusMana = tag.getInteger("bonus_mana");
		}
	}

}
