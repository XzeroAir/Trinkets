<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableInventoryAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class AbilityRepel extends Ability implements ITickableAbility, IHeldAbility, ITickableInventoryAbility, IToggleAbility, IKeyBindInterface {

	private static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	public AbilityRepel() {
		super(Abilities.repel);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (this.abilityEnabled()) {
			this.blockArrows(entity);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean inHand) {
		if (stack.getItem() instanceof TrinketPolarized) {
			Capabilities.getTrinketProperties(stack, prop -> this.toggleAbility(prop.altAbility()));
		}
	}

	public void blockArrows(EntityLivingBase entity) {
		if (serverConfig.exhaustion) {
			final Predicate<EntityLivingBase> filter = Predicates.and(
					EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE
			);
			final boolean flag = filter.apply(entity);
			if (flag) {
				final MagicStats magic = Capabilities.getMagicStats(entity);
				if (magic != null) {
					final float exhaustRate = serverConfig.exhaust_rate;
					tickHandler.addCounter("repel.ticks", serverConfig.exhaust_ticks, false, true, false);
					final Counter counter = tickHandler.getCounter("repel.ticks");
					if (exhaustRate <= magic.getMana()) {
						if ((counter != null) && counter.Tick()) {
							magic.spendMana(exhaustRate);
						}
					} else {
						return;
					}
				}
			}
		}

		try {
			final AxisAlignedBB bBox = entity.getEntityBoundingBox();
			final List<String> cfg = Arrays.asList(serverConfig.repelledEntities);
			final Predicate<Entity> Targets = Predicates.and(
					EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE,
					ent -> (ent != null) && !(ent instanceof EntityPlayer) &&
							(EntityRegistry.getEntry(ent.getClass()) != null) &&
							(EntityRegistry.getEntry(ent.getClass()).getRegistryName() != null) &&
							cfg.contains(EntityRegistry.getEntry(ent.getClass()).getRegistryName().toString())
			);
			final List<Entity> entityList = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(serverConfig.repelRange), Targets);
			for (final Entity repelledEntity : entityList) {
				final Vec3d playerVec3 = entity.getLookVec();
				repelledEntity.motionX = playerVec3.x * 0.3D;
				repelledEntity.motionY = playerVec3.y * 0.3D;
				repelledEntity.motionZ = playerVec3.z * 0.3D;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean abilityEnabled() {
		return enabled;
	}

	@Override
	public IToggleAbility toggleAbility(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public IToggleAbility toggleAbility(int value) {
		this.value = value;
		return this;
	}

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {
		if (Aux) {
			final boolean enabled = this.abilityEnabled();
			final boolean client = entity.world.isRemote;
			this.toggleAbility(!enabled);
			if (client && (entity instanceof EntityPlayer)) {
				final TranslationHelper helper = TranslationHelper.INSTANCE;
				final ItemStack s = new ItemStack(ModItems.trinkets.TrinketPolarized);
				final String repelMode = new TextComponentTranslation(s.getTranslationKey() + ".repelmode").getFormattedText();
				final KeyEntry key = new OptionEntry("repeltoggle", serverConfig.repell, helper.toggleCheckTranslation(!enabled));
				((EntityPlayer) entity).sendStatusMessage(
						new TextComponentString(
								helper.formatAddVariables(repelMode, key)
						), true
				);
			}
			Capabilities.getEntityProperties(entity, prop -> {
				final AbilityHolder holder = prop.getAbilityHandler().getAbilityHolder(this.getRegistryName().toString());
				final SlotInformation info = holder != null ? holder.getInfo() : null;
				if ((info != null) && (entity instanceof EntityLivingBase)) {
					final ItemStack stack = info.getStackFromHandler((EntityLivingBase) entity);
					if (stack.getItem() instanceof TrinketPolarized) {
						Capabilities.getTrinketProperties(stack, cap -> {
							cap.toggleAltAbility(this.abilityEnabled());
							cap.sendInformationToPlayer(((EntityPlayer) entity), ((EntityPlayer) entity));
						});
					}
				}
			});
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

	@Override
	public void saveStorage(NBTTagCompound nbt) {
		nbt.setBoolean("enabled", enabled);
	}

	@Override
	public void loadStorage(NBTTagCompound nbt) {
		if (nbt.hasKey("enabled")) {
			enabled = nbt.getBoolean("enabled");
		}
	}

}
=======
package xzeroair.trinkets.traits.abilities;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableInventoryAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class AbilityRepel extends Ability implements ITickableAbility, IHeldAbility, ITickableInventoryAbility, IToggleAbility, IKeyBindInterface {

	private static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	public AbilityRepel() {
		super(Abilities.repel);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (this.abilityEnabled()) {
			this.blockArrows(entity);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean inHand) {
		if (stack.getItem() instanceof TrinketPolarized) {
			Capabilities.getTrinketProperties(stack, prop -> this.toggleAbility(prop.altAbility()));
		}
	}

	public void blockArrows(EntityLivingBase entity) {
		if (serverConfig.exhaustion) {
			final Predicate<EntityLivingBase> filter = Predicates.and(
					EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE
			);
			final boolean flag = filter.apply(entity);
			if (flag) {
				final MagicStats magic = Capabilities.getMagicStats(entity);
				if (magic != null) {
					final float exhaustRate = serverConfig.exhaust_rate;
					tickHandler.addCounter("repel.ticks", serverConfig.exhaust_ticks, false, true, false);
					final Counter counter = tickHandler.getCounter("repel.ticks");
					if (exhaustRate <= magic.getMana()) {
						if ((counter != null) && counter.Tick()) {
							magic.spendMana(exhaustRate);
						}
					} else {
						return;
					}
				}
			}
		}

		try {
			final AxisAlignedBB bBox = entity.getEntityBoundingBox();
			final List<String> cfg = Arrays.asList(serverConfig.repelledEntities);
			final Predicate<Entity> Targets = Predicates.and(
					EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE,
					ent -> (ent != null) && !(ent instanceof EntityPlayer) &&
							(EntityRegistry.getEntry(ent.getClass()) != null) &&
							(EntityRegistry.getEntry(ent.getClass()).getRegistryName() != null) &&
							cfg.contains(EntityRegistry.getEntry(ent.getClass()).getRegistryName().toString())
			);
			final List<Entity> entityList = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(serverConfig.repelRange), Targets);
			for (final Entity repelledEntity : entityList) {
				final Vec3d playerVec3 = entity.getLookVec();
				repelledEntity.motionX = playerVec3.x * 0.3D;
				repelledEntity.motionY = playerVec3.y * 0.3D;
				repelledEntity.motionZ = playerVec3.z * 0.3D;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean abilityEnabled() {
		return enabled;
	}

	@Override
	public IToggleAbility toggleAbility(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public IToggleAbility toggleAbility(int value) {
		this.value = value;
		return this;
	}

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {
		if (Aux) {
			final boolean enabled = this.abilityEnabled();
			final boolean client = entity.world.isRemote;
			this.toggleAbility(!enabled);
			if (client && (entity instanceof EntityPlayer)) {
				final TranslationHelper helper = TranslationHelper.INSTANCE;
				final ItemStack s = new ItemStack(ModItems.trinkets.TrinketPolarized);
				final String repelMode = new TextComponentTranslation(s.getTranslationKey() + ".repelmode").getFormattedText();
				final KeyEntry key = new OptionEntry("repeltoggle", serverConfig.repell, helper.toggleCheckTranslation(!enabled));
				((EntityPlayer) entity).sendStatusMessage(
						new TextComponentString(
								helper.formatAddVariables(repelMode, key)
						), true
				);
			}
			Capabilities.getEntityProperties(entity, prop -> {
				final AbilityHolder holder = prop.getAbilityHandler().getAbilityHolder(this.getRegistryName().toString());
				final SlotInformation info = holder != null ? holder.getInfo() : null;
				if ((info != null) && (entity instanceof EntityLivingBase)) {
					final ItemStack stack = info.getStackFromHandler((EntityLivingBase) entity);
					if (stack.getItem() instanceof TrinketPolarized) {
						Capabilities.getTrinketProperties(stack, cap -> {
							cap.toggleAltAbility(this.abilityEnabled());
							cap.sendInformationToPlayer(((EntityPlayer) entity), ((EntityPlayer) entity));
						});
					}
				}
			});
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

	@Override
	public void saveStorage(NBTTagCompound nbt) {
		nbt.setBoolean("enabled", enabled);
	}

	@Override
	public void loadStorage(NBTTagCompound nbt) {
		if (nbt.hasKey("enabled")) {
			enabled = nbt.getBoolean("enabled");
		}
	}

}
>>>>>>> Stashed changes
