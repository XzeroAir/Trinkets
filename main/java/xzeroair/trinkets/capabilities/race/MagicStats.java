package xzeroair.trinkets.capabilities.race;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.mana.SyncManaCostToHudPacket;
import xzeroair.trinkets.network.mana.SyncManaHudPacket;
import xzeroair.trinkets.network.mana.SyncManaStatsPacket;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.mana.EntityManaConfig;

public class MagicStats {

	private static final EntityManaConfig manaConfig = TrinketsConfig.SERVER.mana;

	private EntityLivingBase entity;
	private EntityProperties properties;

	private float mana = 100f;
	private float maxMana = 100f;
	private int bonusMana = 0;

	private int manaUpdateTickRate;
	private int manaRegenTimeout;

	public MagicStats(EntityLivingBase e, EntityProperties properties) {
		entity = e;
		this.properties = properties;
	}

	public void regenMana() {
		if (!TrinketsConfig.SERVER.mana.mana_enabled || ((entity instanceof EntityPlayer) && ((EntityPlayer) entity).isCreative())) {
			this.refillMana();
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
				this.addMana(1F);
				manaUpdateTickRate = 0;
			}
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
		if (mana > this.getMaxMana()) {
			this.mana = this.getMaxMana();
		} else if (mana < 0) {
			this.mana = 0;
		} else {
			this.mana = mana;
		}
		this.sendManaToPlayer(entity);
		this.syncToManaHud();
	}

	public void addMana(float mana) {
		this.setMana(this.mana + mana);
	}

	public boolean spendMana(float cost) {
		boolean spend = true;
		if (((entity instanceof EntityPlayer) && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator())) || !TrinketsConfig.SERVER.mana.mana_enabled) {
			return true;
		} else if (cost <= this.getMana()) {
			spend = true;
		} else {
			spend = false;
		}

		if (spend == true) {
			this.setMana(mana - cost);
			this.setManaRegenTimeout();
		}
		return spend;
	}

	public float getMaxMana() {
		float maxAffinityBonus = ((maxMana * (properties.getCurrentRace().getMagicAffinity() * 0.01F)) - maxMana);
		float maxBonus = maxMana + (10F * this.getBonusMana());
		float max = maxAffinityBonus + maxBonus;
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
		bonusMana = bonus;
	}

	public void setManaRegenTimeout() {
		manaRegenTimeout = manaConfig.mana_regen_timeout;
	}

	/**
	 * Send Mana from Server to a Client player
	 */

	public void sendManaToPlayer(EntityLivingBase e) {
		if ((e instanceof EntityPlayer) && !e.world.isRemote) {
			NetworkHandler.INSTANCE.sendTo(new SyncManaStatsPacket(entity, this), (EntityPlayerMP) e);
		}
	}

	public void sendManaToServer() {
		if (entity.world.isRemote) {
			NetworkHandler.INSTANCE.sendToServer(new SyncManaStatsPacket(entity, this));
		}
	}

	public void syncToManaHud() {
		if ((entity instanceof EntityPlayer)) {
			if (!entity.world.isRemote) {
				NetworkHandler.INSTANCE.sendTo(new SyncManaHudPacket(this.getMana(), this.getBonusMana(), this.getMaxMana()), (EntityPlayerMP) entity);
			} else {
				//				ScreenOverlayEvents.instance.SyncMana(this.getMana(), this.getBonusMana(), this.getMaxMana());
			}
		}
	}

	public void syncToManaCostToHud(float cost) {
		// I Might not need this
		if ((entity instanceof EntityPlayer) && !entity.world.isRemote) {
			NetworkHandler.INSTANCE.sendTo(new SyncManaCostToHudPacket(cost), (EntityPlayerMP) entity);
		}
	}

	/**
	 * Handle NBT
	 */

	public void copyFrom(MagicStats stats) {
		mana = stats.mana;
		bonusMana = stats.bonusMana;
	}

	public void saveToNBT(NBTTagCompound tag) {
		tag.setFloat("mana", mana);
		tag.setInteger("bonus_mana", bonusMana);
	}

	public void loadFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("mana")) {
			mana = tag.getFloat("mana");
		}
		if (tag.hasKey("bonus_mana")) {
			bonusMana = tag.getInteger("bonus_mana");
		}
	}

}
