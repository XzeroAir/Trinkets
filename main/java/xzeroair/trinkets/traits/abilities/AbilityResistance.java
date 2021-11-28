package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Shield;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;
import xzeroair.trinkets.util.handlers.TickHandler;

public class AbilityResistance extends AbilityBase implements ITickableAbility, IAttackAbility, IPotionAbility {

	protected static final Shield clientConfig = TrinketsConfig.CLIENT.items.DAMAGE_SHIELD;
	protected static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;

	int hitCount;

	public AbilityResistance() {
		hitCount = 0;
	}

	public int getHitCount() {
		return hitCount;
	}

	public AbilityResistance setHitCount(int hitCount) {
		this.hitCount = hitCount;
		return this;
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		final int duration = 30;
		Potion resistance = Potion.getPotionFromResourceLocation(serverConfig.potionEffect);
		if (resistance == null) {
			resistance = MobEffects.RESISTANCE;
		}
		if (!entity.isPotionActive(resistance)) {
			entity.addPotionEffect(new PotionEffect(resistance, duration, serverConfig.resistance_level, false, false));
			this.removeCounter("CheckResist");
		} else {
			final int dur = entity.getActivePotionEffect(resistance).getDuration();
			final int amp = entity.getActivePotionEffect(resistance).getAmplifier();
			final TickHandler counter = this.getCounter("CheckResist", true);
			final boolean trigger = counter.Tick();
			if (trigger) {
				this.removeCounter("CheckResist");
			}
			if ((dur > duration) && serverConfig.resistance_stacks) {
				//Assume it was given by something else
				if ((amp <= serverConfig.resistance_level)) {
					entity.addPotionEffect(new PotionEffect(resistance, duration, amp + 1, false, false));
				} else {
					if (trigger && ((amp + 1) < 3)) {
						entity.addPotionEffect(new PotionEffect(resistance, dur, amp + 1, false, false));
					}
				}
			} else {
				if ((amp <= serverConfig.resistance_level)) {
					entity.addPotionEffect(new PotionEffect(resistance, duration, serverConfig.resistance_level, false, false));
				}
			}
		}
	}

	@Override
	public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
		if (serverConfig.damage_ignore) {
			if (!attacked.world.isRemote) {
				if (dmg > 1) {
					final boolean indirect = this.isIndirectDamage(source);
					if (indirect) {
						this.countHit(attacked, indirect);
						if (this.trigger(attacked, indirect)) {
							return true;
						}
					}
				}
			}
		}
		return cancel;
	}

	@Override
	public float hurt(EntityLivingBase attacked, DamageSource source, float dmg) {
		if (serverConfig.damage_ignore) {
			if (dmg > 1) {
				final boolean indirect = this.isIndirectDamage(source);
				if (!indirect) {
					this.countHit(attacked, indirect);
					if (this.trigger(attacked, indirect)) {
						dmg = 0;
					}
				}
			}
		}
		if (serverConfig.explosion_resist) {
			if (source.isExplosion()) {
				if (dmg > 1f) {
					return dmg * serverConfig.explosion_amount;
				}
			}
		}
		return dmg;
	}

	private void countHit(EntityLivingBase attacked, boolean indirect) {
		//		if (hitCount < serverConfig.hits) {
		hitCount++;
		if (attacked instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) attacked;
			final ItemStack shield = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDamageShield);
			final TrinketProperties cap = Capabilities.getTrinketProperties(shield);
			if ((cap != null)) {
				cap.setCount(hitCount);
				cap.sendInformationToPlayer(player, player);
			}
		}
		final EntityProperties prop = Capabilities.getEntityRace(attacked);
		final NBTTagCompound tag = new NBTTagCompound();
		this.saveStorage(tag);
		prop.sendInformationToPlayer(attacked, tag);
		//		}
	}

	private boolean trigger(EntityLivingBase attacked, boolean indirect) {
		//		final boolean client = attacked.getEntityWorld().isRemote;
		if ((hitCount > serverConfig.hits)) {
			hitCount = 0;
			if (attacked instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) attacked;
				final ItemStack shield = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDamageShield);
				final TrinketProperties cap = Capabilities.getTrinketProperties(shield);
				if (cap != null) {
					cap.setCount(hitCount);
					cap.sendInformationToPlayer(player, player);
				}
				if (serverConfig.special) {
					String string = "Ow!";
					final VipStatus vip = Capabilities.getVipStatus(attacked);
					if (vip != null) {
						final String quote = vip.getRandomQuote();
						if (!quote.isEmpty()) {
							string = quote;
						}
					}
					final TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
					player.sendStatusMessage(message, true);

					if (clientConfig.effectVolume > 0) {
						player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, (float) clientConfig.effectVolume, 1F);
					}
				}
			}
			final EntityProperties prop = Capabilities.getEntityRace(attacked);
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveStorage(tag);
			prop.sendInformationToPlayer(attacked, tag);
			return true;
		}
		return false;
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		Potion resistance = Potion.getPotionFromResourceLocation(serverConfig.potionEffect);
		if (resistance == null) {
			resistance = MobEffects.RESISTANCE;
		}
		if (effect.getPotion().equals(resistance)) {
			final TickHandler counter = this.getCounter("CheckResist", true);
			counter.setLength(effect.getDuration() - 1).setCountdown(true);
		}
		return cancel;
	}

	@Override
	public void removeAbility(EntityLivingBase entity) {
		Potion resistance = Potion.getPotionFromResourceLocation(serverConfig.potionEffect);
		if (resistance == null) {
			resistance = MobEffects.RESISTANCE;
		}
		if (entity.isPotionActive(resistance)) {
			entity.removeActivePotionEffect(resistance);
			entity.addPotionEffect(new PotionEffect(resistance, 10, serverConfig.resistance_level, false, false));
		}
	}

	@Override
	public void loadStorage(NBTTagCompound nbt) {
		if (nbt.hasKey("xat.hitCount")) {
			hitCount = nbt.getInteger("xat.hitCount");
		}
	}

	@Override
	public void saveStorage(NBTTagCompound nbt) {
		nbt.setInteger("xat.hitCount", hitCount);
	}

}
