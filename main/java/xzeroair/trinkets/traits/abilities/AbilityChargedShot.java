<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IBowAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;

public class AbilityChargedShot extends Ability implements ITickableAbility, IBowAbility, IAttackAbility {

	public static final ElfConfig serverConfig = TrinketsConfig.SERVER.races.elf;

	public AbilityChargedShot() {
		super(Abilities.chargedShot);
	}

	protected boolean critReady, hit, doDamage, useMP, doAbility, pullingBow, release, cancel, doExplosion = false;
	protected int v1, v2, v3, poundage, tension, resistance = 0;
	protected float chargeMultiplier, damageMultiplier, pounds, f1, f2, f3 = 0;
	protected int heldDuration = 0;
	//	protected int maxChargeTime = serverConfig.ChargeTime;//1200;

	//REskillable Check skill levels, do stuff based on them?
	@Override
	public void tickAbility(EntityLivingBase entity) {
		final float ManaCost = serverConfig.CS_Cost;
		boolean blacklistedBow = false;
		if ((entity.getActiveItemStack() != null) && !entity.getActiveItemStack().isEmpty()) {
			try {
				final String[] configList = serverConfig.bowBlacklist;
				for (final String s : configList) {
					//					if (ConfigHelper.checkItemInConfig(entity.getActiveItemStack(), s)) {
					ConfigObject object = new ConfigObject(s);
					if (object.doesItemMatchEntry(entity.getActiveItemStack())) {
						blacklistedBow = true;
						break;
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		final boolean bow = entity.isHandActive() && (entity.getActiveItemStack().getItem() instanceof ItemBow)
				&&
				!blacklistedBow
		//				!entity.getActiveItemStack().getItem().getRegistryName().getPath().toLowerCase().replace("_", "").contains("crossbow")
		;
		final MagicStats magic = Capabilities.getMagicStats(entity);
		final boolean sneaking = entity.isSneaking();

		if (magic == null)
			return;
		if (sneaking) {
			if (bow) {
				if (!pullingBow) {
					this.cancel();
					heldDuration = serverConfig.ChargeTime / 10;
				} else {
					heldDuration++;
				}
				doAbility = true;
				pullingBow = true;
			} else {
				if (pullingBow) {
					magic.syncToManaCostToHud(0);
					this.cancel();
					pullingBow = false;
				}
				return;
			}
		} else {
			if (doAbility || pullingBow) {
				magic.syncToManaCostToHud(0);
				this.reset();
			}
			return;
		}

		if (serverConfig.ChargeTime < 12) {
			serverConfig.ChargeTime = 12;
		}
		if (heldDuration > serverConfig.ChargeTime) {
			heldDuration = serverConfig.ChargeTime;
		}
		// 1200 ticks is 60 seconds, Max Use Time is 72000 which is an Hour

		final float mana = magic.getMana();
		float scale = (float) (((heldDuration * 100) / serverConfig.ChargeTime) * 0.01);//(f) / configDuration;
		final float testCost = (ManaCost * (scale * 10));
		if ((testCost > 0) && (testCost > mana)) {
			scale = mana / testCost;
			cancel = true;
		} else {
			cancel = false;
		}

		if (scale <= 0F) {

		} else if ((scale > 0.0F) && (scale <= 0.25F)) {
			//			System.out.println("Small Game");
		} else if ((scale > 0.25F) && (scale <= 0.41F)) {
			//			System.out.println("Medium Game");
		} else if ((scale > 0.41F) && (scale <= 0.65F)) {
			//			System.out.println("Large Game");
		} else if ((scale > 0.65F) && (scale <= 1F)) {
			//			System.out.println("Monster Game");
		} else if ((scale > 1F)) {
			//			System.out.println("Wimp Sauce");
		}

		if (scale > 0) {
			if (!cancel) {
				if ((scale < 1F)) {
					if (entity.world.isRemote && (entity instanceof EntityPlayer)) {
						entity.world.playSound(
								(EntityPlayer) entity,
								entity.getPosition(),
								SoundEvents.BLOCK_NOTE_GUITAR,
								SoundCategory.PLAYERS,
								0.1F,
								scale + 0.5F
						);
					}
					critReady = false;
				} else {
					if (!critReady) {
						if (entity.world.isRemote && (entity instanceof EntityPlayer)) {
							entity.world.playSound(
									(EntityPlayer) entity,
									entity.getPosition(),
									SoundEvents.EVOCATION_ILLAGER_CAST_SPELL,
									SoundCategory.PLAYERS,
									1F,
									0.5F
							);
						}
						critReady = true;
					}
				}
			} else {
			}
			final float Cost = MathHelper.clamp(ManaCost * (scale * 10), 0, magic.getMana());
			magic.syncToManaCostToHud(Cost);
		}
	}

	// Default Action Result is null
	@Override
	public void knockArrow(ArrowNockEvent event) {
		if (!event.getWorld().isRemote) {
			//			pullingBow = false;
			doAbility = false;
			release = false;
			hit = false;
			doDamage = false;
			critReady = false;
			doExplosion = false;
			useMP = false;
			//			heldDuration = 0;
			//			damageMultiplier = 0;
		}
	}

	// Client and Server
	@Override
	public int onItemUseTick(EntityLivingBase entity, ItemStack stack, int duration) {
		//		if (!entity.world.isRemote) {
		//			pullingBow = true;
		//		}
		return duration;
	}

	// Client and Server
	// Run Before Loose Arrow
	@Override
	public void onItemUseStop(EntityLivingBase entity, ItemStack stack, int duration) {
		//		if (!entity.world.isRemote) {
		//			pullingBow = false;
		//		}
		if (!entity.world.isRemote && doAbility) {
			if (!release) {
				final float ManaCost = serverConfig.CS_Cost;
				final MagicStats magic = Capabilities.getMagicStats(entity);
				//		final float test = ((((mana - cost) * 100) / maxMana) * 0.01F);
				final float mana = magic.getMana();
				float scale = (float) (((heldDuration * 100) / serverConfig.ChargeTime) * 0.01);//(f) / configDuration;
				final float testCost = (ManaCost * (scale * 10));
				if ((testCost > 0) && (testCost > mana)) {
					scale = mana / testCost;
				}
				final float Cost = MathHelper.clamp(ManaCost * (scale * 10), 0, magic.getMana());
				if (magic.spendMana(Cost)) {
					damageMultiplier = scale;
					useMP = true;
				} else {
					damageMultiplier = 0;
					this.cancel();
				}
				magic.syncToManaCostToHud(0);
			}
			pullingBow = false;
			release = true;
		}
	}

	// Client and Server
	@Override
	public void looseArrow(ArrowLooseEvent event) {

	}

	@Override
	public void arrowImpact(ProjectileImpactEvent.Arrow event) {
		final World world = event.getEntity().getEntityWorld();
		RayTraceResult rayTraceResult = event.getRayTraceResult();
		EntityArrow arrow = event.getArrow();
		if (!world.isRemote) {
			if ((rayTraceResult.typeOfHit != Type.MISS) && doAbility) {
				if ((rayTraceResult.typeOfHit == Type.ENTITY)) {
					hit = true;
					doDamage = true;
				} else {
					if (useMP && critReady && serverConfig.explode) {
						world.createExplosion(arrow, arrow.posX, arrow.posY, arrow.posZ, 2F, false);
						useMP = false;
					}
				}
			}
		}
	}

	@Override
	public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if ((source.damageType.contentEquals("arrow") || (source.isProjectile() && !source.isMagicDamage())) && (damageMultiplier > 0)) {

			final float baseMultiplier = serverConfig.baseMultiplier;
			float adjustedValue = dmg * baseMultiplier;
			if (adjustedValue < serverConfig.minimumDamageMultiplier) {
				adjustedValue = serverConfig.minimumDamageMultiplier;
			}
			dmg += adjustedValue * damageMultiplier;
			try {
				if (useMP && critReady && serverConfig.explode) {
					final Entity attacker = source.getTrueSource();
					NetworkHandler.sendToTracking(
							new EffectsRenderPacket(
									attacker,
									source.getImmediateSource().posX,
									source.getImmediateSource().posY,
									source.getImmediateSource().posZ, 0, 0, 0, 0, 5, 0, 0
							), source.getImmediateSource()
					);
					final List<Entity> splash = attacker.world.getEntitiesWithinAABBExcludingEntity(target, target.getEntityBoundingBox().grow(1));
					final DamageSource source1 = DamageSource.causeIndirectDamage(source.getImmediateSource(), (EntityLivingBase) attacker).setProjectile().setMagicDamage();
					for (final Entity e : splash) {
						if ((e instanceof EntityLivingBase)) {
							if (e != attacker) {
								e.attackEntityFrom(source1, dmg * 0.75F);
							}
						}
					}
				}
			} catch (final Exception e) {
			}
			this.cancel();
		}
		return dmg;
	}

	private void reset() {
		this.cancel();
		pullingBow = false;
	}

	private void cancel() {
		doAbility = false;
		release = false;
		hit = false;
		doDamage = false;
		critReady = false;
		doExplosion = false;
		useMP = false;
		heldDuration = 0;
		damageMultiplier = 0;
		//		cancel = true;
	}

	@Override
	public void onAbilityRemoved(EntityLivingBase entity) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		magic.syncToManaCostToHud(0);
	}
}
=======
package xzeroair.trinkets.traits.abilities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IBowAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;

public class AbilityChargedShot extends Ability implements ITickableAbility, IBowAbility, IAttackAbility {

	public static final ElfConfig serverConfig = TrinketsConfig.SERVER.races.elf;

	public AbilityChargedShot() {
		super(Abilities.chargedShot);
	}

	protected boolean critReady, hit, doDamage, useMP, doAbility, pullingBow, release, cancel, doExplosion = false;
	protected int v1, v2, v3, poundage, tension, resistance = 0;
	protected float chargeMultiplier, damageMultiplier, pounds, f1, f2, f3 = 0;
	protected int heldDuration = 0;
	//	protected int maxChargeTime = serverConfig.ChargeTime;//1200;

	//REskillable Check skill levels, do stuff based on them?
	@Override
	public void tickAbility(EntityLivingBase entity) {
		final float ManaCost = serverConfig.CS_Cost;
		boolean blacklistedBow = false;
		if ((entity.getActiveItemStack() != null) && !entity.getActiveItemStack().isEmpty()) {
			try {
				final String[] configList = serverConfig.bowBlacklist;
				for (final String s : configList) {
					//					if (ConfigHelper.checkItemInConfig(entity.getActiveItemStack(), s)) {
					ConfigObject object = new ConfigObject(s);
					if (object.doesItemMatchEntry(entity.getActiveItemStack())) {
						blacklistedBow = true;
						break;
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		final boolean bow = entity.isHandActive() && (entity.getActiveItemStack().getItem() instanceof ItemBow)
				&&
				!blacklistedBow
		//				!entity.getActiveItemStack().getItem().getRegistryName().getPath().toLowerCase().replace("_", "").contains("crossbow")
		;
		final MagicStats magic = Capabilities.getMagicStats(entity);
		final boolean sneaking = entity.isSneaking();

		if (magic == null)
			return;
		if (sneaking) {
			if (bow) {
				if (!pullingBow) {
					this.cancel();
					heldDuration = serverConfig.ChargeTime / 10;
				} else {
					heldDuration++;
				}
				doAbility = true;
				pullingBow = true;
			} else {
				if (pullingBow) {
					magic.syncToManaCostToHud(0);
					this.cancel();
					pullingBow = false;
				}
				return;
			}
		} else {
			if (doAbility || pullingBow) {
				magic.syncToManaCostToHud(0);
				this.reset();
			}
			return;
		}

		if (serverConfig.ChargeTime < 12) {
			serverConfig.ChargeTime = 12;
		}
		if (heldDuration > serverConfig.ChargeTime) {
			heldDuration = serverConfig.ChargeTime;
		}
		// 1200 ticks is 60 seconds, Max Use Time is 72000 which is an Hour

		final float mana = magic.getMana();
		float scale = (float) (((heldDuration * 100) / serverConfig.ChargeTime) * 0.01);//(f) / configDuration;
		final float testCost = (ManaCost * (scale * 10));
		if ((testCost > 0) && (testCost > mana)) {
			scale = mana / testCost;
			cancel = true;
		} else {
			cancel = false;
		}

		if (scale <= 0F) {

		} else if ((scale > 0.0F) && (scale <= 0.25F)) {
			//			System.out.println("Small Game");
		} else if ((scale > 0.25F) && (scale <= 0.41F)) {
			//			System.out.println("Medium Game");
		} else if ((scale > 0.41F) && (scale <= 0.65F)) {
			//			System.out.println("Large Game");
		} else if ((scale > 0.65F) && (scale <= 1F)) {
			//			System.out.println("Monster Game");
		} else if ((scale > 1F)) {
			//			System.out.println("Wimp Sauce");
		}

		if (scale > 0) {
			if (!cancel) {
				if ((scale < 1F)) {
					if (entity.world.isRemote && (entity instanceof EntityPlayer)) {
						entity.world.playSound(
								(EntityPlayer) entity,
								entity.getPosition(),
								SoundEvents.BLOCK_NOTE_GUITAR,
								SoundCategory.PLAYERS,
								0.1F,
								scale + 0.5F
						);
					}
					critReady = false;
				} else {
					if (!critReady) {
						if (entity.world.isRemote && (entity instanceof EntityPlayer)) {
							entity.world.playSound(
									(EntityPlayer) entity,
									entity.getPosition(),
									SoundEvents.EVOCATION_ILLAGER_CAST_SPELL,
									SoundCategory.PLAYERS,
									1F,
									0.5F
							);
						}
						critReady = true;
					}
				}
			} else {
			}
			final float Cost = MathHelper.clamp(ManaCost * (scale * 10), 0, magic.getMana());
			magic.syncToManaCostToHud(Cost);
		}
	}

	// Default Action Result is null
	@Override
	public void knockArrow(ArrowNockEvent event) {
		if (!event.getWorld().isRemote) {
			//			pullingBow = false;
			doAbility = false;
			release = false;
			hit = false;
			doDamage = false;
			critReady = false;
			doExplosion = false;
			useMP = false;
			//			heldDuration = 0;
			//			damageMultiplier = 0;
		}
	}

	// Client and Server
	@Override
	public int onItemUseTick(EntityLivingBase entity, ItemStack stack, int duration) {
		//		if (!entity.world.isRemote) {
		//			pullingBow = true;
		//		}
		return duration;
	}

	// Client and Server
	// Run Before Loose Arrow
	@Override
	public void onItemUseStop(EntityLivingBase entity, ItemStack stack, int duration) {
		//		if (!entity.world.isRemote) {
		//			pullingBow = false;
		//		}
		if (!entity.world.isRemote && doAbility) {
			if (!release) {
				final float ManaCost = serverConfig.CS_Cost;
				final MagicStats magic = Capabilities.getMagicStats(entity);
				//		final float test = ((((mana - cost) * 100) / maxMana) * 0.01F);
				final float mana = magic.getMana();
				float scale = (float) (((heldDuration * 100) / serverConfig.ChargeTime) * 0.01);//(f) / configDuration;
				final float testCost = (ManaCost * (scale * 10));
				if ((testCost > 0) && (testCost > mana)) {
					scale = mana / testCost;
				}
				final float Cost = MathHelper.clamp(ManaCost * (scale * 10), 0, magic.getMana());
				if (magic.spendMana(Cost)) {
					damageMultiplier = scale;
					useMP = true;
				} else {
					damageMultiplier = 0;
					this.cancel();
				}
				magic.syncToManaCostToHud(0);
			}
			pullingBow = false;
			release = true;
		}
	}

	// Client and Server
	@Override
	public void looseArrow(ArrowLooseEvent event) {

	}

	@Override
	public void arrowImpact(ProjectileImpactEvent.Arrow event) {
		final World world = event.getEntity().getEntityWorld();
		RayTraceResult rayTraceResult = event.getRayTraceResult();
		EntityArrow arrow = event.getArrow();
		if (!world.isRemote) {
			if ((rayTraceResult.typeOfHit != Type.MISS) && doAbility) {
				if ((rayTraceResult.typeOfHit == Type.ENTITY)) {
					hit = true;
					doDamage = true;
				} else {
					if (useMP && critReady && serverConfig.explode) {
						world.createExplosion(arrow, arrow.posX, arrow.posY, arrow.posZ, 2F, false);
						useMP = false;
					}
				}
			}
		}
	}

	@Override
	public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if ((source.damageType.contentEquals("arrow") || (source.isProjectile() && !source.isMagicDamage())) && (damageMultiplier > 0)) {

			final float baseMultiplier = serverConfig.baseMultiplier;
			float adjustedValue = dmg * baseMultiplier;
			if (adjustedValue < serverConfig.minimumDamageMultiplier) {
				adjustedValue = serverConfig.minimumDamageMultiplier;
			}
			dmg += adjustedValue * damageMultiplier;
			try {
				if (useMP && critReady && serverConfig.explode) {
					final Entity attacker = source.getTrueSource();
					NetworkHandler.sendToTracking(
							new EffectsRenderPacket(
									attacker,
									source.getImmediateSource().posX,
									source.getImmediateSource().posY,
									source.getImmediateSource().posZ, 0, 0, 0, 0, 5, 0, 0
							), source.getImmediateSource()
					);
					final List<Entity> splash = attacker.world.getEntitiesWithinAABBExcludingEntity(target, target.getEntityBoundingBox().grow(1));
					final DamageSource source1 = DamageSource.causeIndirectDamage(source.getImmediateSource(), (EntityLivingBase) attacker).setProjectile().setMagicDamage();
					for (final Entity e : splash) {
						if ((e instanceof EntityLivingBase)) {
							if (e != attacker) {
								e.attackEntityFrom(source1, dmg * 0.75F);
							}
						}
					}
				}
			} catch (final Exception e) {
			}
			this.cancel();
		}
		return dmg;
	}

	private void reset() {
		this.cancel();
		pullingBow = false;
	}

	private void cancel() {
		doAbility = false;
		release = false;
		hit = false;
		doDamage = false;
		critReady = false;
		doExplosion = false;
		useMP = false;
		heldDuration = 0;
		damageMultiplier = 0;
		//		cancel = true;
	}

	@Override
	public void onAbilityRemoved(EntityLivingBase entity) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		magic.syncToManaCostToHud(0);
	}
}
>>>>>>> Stashed changes
