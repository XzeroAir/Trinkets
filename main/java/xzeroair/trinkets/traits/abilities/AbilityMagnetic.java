package xzeroair.trinkets.traits.abilities;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.capabilities.Capabilities;
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
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class AbilityMagnetic extends Ability implements ITickableAbility, IHeldAbility, ITickableInventoryAbility, IToggleAbility, IKeyBindInterface {

	private static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	public AbilityMagnetic() {
		super(Abilities.magnetic);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (this.abilityEnabled()) {
			this.collectDrops(entity);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean inHand) {
		Capabilities.getTrinketProperties(stack, prop -> this.toggleAbility(prop.mainAbility()));
	}

	public void collectDrops(EntityLivingBase entity) {
		final Predicate<EntityLivingBase> filter = Predicates.and(
				EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE
		);
		final boolean flag = filter.apply(entity);
		if (flag) {
			final AxisAlignedBB bBox = entity.getEntityBoundingBox();
			final Predicate<Entity> lootPredicate = Predicates.and(
					EntitySelectors.IS_ALIVE,
					ent -> (ent instanceof EntityItem) || ((ent instanceof EntityXPOrb) && serverConfig.collectXP)
			);
			final Predicate<EntityPlayer> otherPlayerPredicate = Predicates.and(
					EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE,
					ent -> {
						if ((ent == null) || (entity == ent) || (entity.getEntityId() == ent.getEntityId())) {
							return false;
						}
						if ((ent.getHeldItemMainhand().getItem() instanceof TrinketPolarized) || TrinketHelper.AccessoryCheck(ent, ModItems.trinkets.TrinketPolarized)) {
							return true;
						}
						return false;
					}
			);
			final List<Entity> Loot = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(serverConfig.PR.HD, serverConfig.PR.VD, serverConfig.PR.HD), lootPredicate);
			final List<EntityPlayer> others = entity.world.getEntitiesWithinAABB(EntityPlayer.class, bBox.grow(serverConfig.PR.HD, serverConfig.PR.VD, serverConfig.PR.HD), otherPlayerPredicate);

			for (final Entity loot : Loot) {
				final double distance = loot.getDistance(entity.posX, entity.posY, entity.posZ);
				boolean someonesCloser = false;
				for (final EntityPlayer otherP : others) {
					final float dist = loot.getDistance(otherP);
					if (dist < distance) {
						someonesCloser = true;
					}
				}
				if (!someonesCloser) {
					this.handleLoot(entity, loot);
				}
			}
		}
	}

	private void handleLoot(Entity entity, Entity drop) {
		if ((drop instanceof EntityItem) || (serverConfig.collectXP && (drop instanceof EntityXPOrb))) {
			if ((entity instanceof EntityPlayer)) {
				final EntityPlayer player = (EntityPlayer) entity;
				if (!player.world.isRemote) {
					if (drop instanceof EntityItem) {
						if (serverConfig.instant_pickup) {
							this.pickupItem(player, drop);
						} else {
							this.pull(drop, entity.posX, entity.posY, entity.posZ);
						}
					} else if (serverConfig.collectXP && (drop instanceof EntityXPOrb)) {
						if (serverConfig.instant_xp) {
							this.pickupXP(player, drop);
						} else {
							this.pull(drop, entity.posX, entity.posY, entity.posZ);
						}
					} else {
						this.pull(drop, entity.posX, entity.posY, entity.posZ);
					}
				}
			} else {
				this.pull(drop, entity.posX, entity.posY, entity.posZ);
			}
		}
	}

	private void pickupItem(EntityPlayer player, Entity itemEntity) {
		if (itemEntity instanceof EntityItem) {
			final EntityItem item = (EntityItem) itemEntity;
			if (!(item.getItem().getItem() instanceof TrinketPolarized)) {
				for (final ItemStack slotStack : player.inventory.mainInventory) {
					if (slotStack.isEmpty()) {
						player.addItemStackToInventory(item.getItem());
					} else if (slotStack.isItemEqual(item.getItem())) {
						if (slotStack.isStackable()) {
							// if (item.getItem().areItemStackShareTagsEqual(slotStack, item.getItem())) {
							if (ItemStack.areItemStackShareTagsEqual(slotStack, item.getItem())) {
								if ((slotStack.getCount() + item.getItem().getCount()) <= slotStack.getMaxStackSize()) {
									player.addItemStackToInventory(item.getItem());
								} else {
									if ((slotStack.getMaxStackSize() - slotStack.getCount()) > 0) {
										player.addItemStackToInventory(item.getItem().splitStack((slotStack.getMaxStackSize() - slotStack.getCount())));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void pickupXP(EntityPlayer player, Entity xpOrb) {
		if (xpOrb instanceof EntityXPOrb) {
			final EntityXPOrb xp = (EntityXPOrb) xpOrb;
			final ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);

			if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
				final float ratio = itemstack.getItem().getXpRepairRatio(itemstack);
				final int iE = Math.min(this.roundAverage(xp.xpValue * ratio), itemstack.getItemDamage());
				xp.xpValue -= this.roundAverage(iE / ratio);
				itemstack.setItemDamage(itemstack.getItemDamage() - iE);
			}

			if (xp.xpValue > 0) {
				player.addExperience(xp.xpValue);
			}
			if (!xp.isDead) {
				xp.setDead();
			}
		}
	}

	protected int roundAverage(float value) {
		final double floor = Math.floor(value);
		return (int) floor + (Math.random() < (value - floor) ? 1 : 0);
	}

	protected void pull(Entity ent, double x, double y, double z) {
		final double spd = TrinketsConfig.SERVER.Items.POLARIZED_STONE.Polarized_Stone_Speed;
		final double dX = (x - 0.5) - ent.getPosition().getX();
		final double dY = y - ent.getPosition().getY();
		final double dZ = (z - 0.5) - ent.getPosition().getZ();
		final double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if ((vel > 0.0D) && (vel < 0.95D)) {
			vel *= vel;
			ent.motionX += (dX / dist) * vel * (spd * MathHelper.clamp(dist - 0.5, 0, 1));
			ent.motionY += (dY / dist) * vel * ((spd * 1.25) * MathHelper.clamp(dist - 0.5, 0, 1));
			ent.motionZ += (dZ / dist) * vel * (spd * MathHelper.clamp(dist - 0.5, 0, 1));
		}
	}

	protected void push(Entity ent, double x, double y, double z) {
		final double spd = TrinketsConfig.SERVER.Items.POLARIZED_STONE.Polarized_Stone_Speed;
		final double dX = x - ent.posX;
		final double dY = y - ent.posY;
		final double dZ = z - ent.posZ;
		final double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if (vel > 0.0D) {
			vel *= vel;
			ent.motionX -= (dX / dist) * vel * spd;
			ent.motionY -= (dY / dist) * vel * spd;
			ent.motionZ -= (dZ / dist) * vel * spd;
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
		if (!Aux) {
			final boolean enabled = this.abilityEnabled();
			final boolean client = entity.world.isRemote;
			this.toggleAbility(!enabled);
			if (client && (entity instanceof EntityPlayer)) {
				final TranslationHelper helper = TranslationHelper.INSTANCE;
				final ItemStack s = new ItemStack(ModItems.trinkets.TrinketPolarized);
				final String magnetMode = new TextComponentTranslation(s.getTranslationKey() + ".magnetmode").getFormattedText();
				final KeyEntry key = new OptionEntry("collecttoggle", serverConfig.collectXP, helper.toggleCheckTranslation(!enabled));
				((EntityPlayer) entity).sendStatusMessage(
						new TextComponentString(
								helper.formatAddVariables(magnetMode, key)
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
							cap.toggleMainAbility(this.abilityEnabled());
							cap.sendInformationToPlayer((EntityLivingBase) entity, ((EntityPlayer) entity));
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
	public NBTTagCompound saveStorage(NBTTagCompound compound) {
		compound.setBoolean("enabled", enabled);
		return compound;
	}

	@Override
	public void loadStorage(NBTTagCompound compound) {
		if (compound.hasKey("enabled")) {
			enabled = compound.getBoolean("enabled");
		}
	}

}
