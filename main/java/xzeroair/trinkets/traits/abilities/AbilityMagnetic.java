package xzeroair.trinkets.traits.abilities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.keybinds.IKeyBindInterface;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.handlers.ItemEffectHandler;

public class AbilityMagnetic extends AbilityBase implements ITickableAbility, IToggleAbility, IKeyBindInterface {

	private static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	private final float playerDistance = 0;
	private float otherDistance = 0;
	private final float otherID = 0;
	private final float dropDistance = 0;
	private final List<String> drops = new ArrayList<>();

	/**
	 * This Ability is Unfinished, Please do not use it. TODO Finish this
	 */
	public AbilityMagnetic() {
	}

	private List<String> getDrops() {
		return drops;
	}

	private void clearDrops() {
		if (!drops.isEmpty()) {
			drops.clear();
		}
	}

	private float getOtherDistance() {
		return otherDistance;
	}

	private void setOtherDistance(float distance) {
		otherDistance = distance;
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		//		if ((entity instanceof EntityPlayer)) {
		//			EntityPlayer player = (EntityPlayer) entity;
		if (enabled) {
			final AxisAlignedBB bBox = entity.getEntityBoundingBox();
			final List<Entity> List = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(serverConfig.PR.HD, serverConfig.PR.VD, serverConfig.PR.HD));
			//			TrinketProperties cap = Capabilities.getTrinketProperties(stack);
			//			if ((cap != null)) {// && !(player.inventory.getFirstEmptyStack() < 0)) {
			for (final Entity targets : List) {
				if (targets instanceof EntityItem) {
					if (!this.getDrops().contains(String.valueOf(targets.getEntityId()))) {
						this.getDrops().add(String.valueOf(targets.getEntityId()));
					}
				}
				if (serverConfig.collectXP && (targets instanceof EntityXPOrb)) {
					if (!this.getDrops().contains(String.valueOf(targets.getEntityId()))) {
						this.getDrops().add(String.valueOf(targets.getEntityId()));
					}
				}
			}
			if (!this.getDrops().isEmpty()) {
				for (int i = 0; i < this.getDrops().size(); i++) {
					if ((this.getDrops().size() > 0) && (i < this.getDrops().size())) {
						final String dropID = this.getDrops().get(i);
						if ((dropID != null) && !dropID.isEmpty()) {
							final Entity drop = entity.world.getEntityByID(Integer.parseInt(dropID));
							if (drop != null) {
								for (final Entity entities : List) {
									if (entities instanceof EntityLivingBase) {
										final EntityProperties prop = Capabilities.getEntityRace(entities);
										if (prop != null) {
											final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(Abilities.magnetic);
											if ((handler != null) && (handler instanceof IToggleAbility)) {
												final boolean bool = ((IToggleAbility) handler).abilityEnabled();
												if (bool) {
													if ((this.getOtherDistance() == 0) || (entities.getDistance(drop) < this.getOtherDistance())) {
														this.setOtherDistance(entities.getDistance(drop));
													}
												}
											}
										}
									}
								}
								if ((entity.getDistance(drop) < this.getOtherDistance()) || (this.getOtherDistance() == 0)) {
									if (!(entity instanceof EntityPlayer) || !serverConfig.instant_pickup) {
										if ((drop instanceof EntityItem) || (serverConfig.collectXP && (drop instanceof EntityXPOrb))) {
											ItemEffectHandler.pull(drop, entity.posX, entity.posY, entity.posZ);
										}
									} else {
										if ((entity instanceof EntityPlayer) && !entity.world.isRemote) {
											final EntityPlayer player = (EntityPlayer) entity;
											if (drop instanceof EntityItem) {
												final EntityItem item = (EntityItem) drop;
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
											} else {
												if (serverConfig.collectXP && (drop instanceof EntityXPOrb)) {
													final EntityXPOrb xp = (EntityXPOrb) drop;
													final ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);

													if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
														final float ratio = itemstack.getItem().getXpRepairRatio(itemstack);
														final int iE = Math.min(roundAverage(xp.xpValue * ratio), itemstack.getItemDamage());
														xp.xpValue -= roundAverage(i / ratio);
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
										}
									}
								}
								this.setOtherDistance(0);
							}
						}
					}
				}
				this.clearDrops();
			}
		} else {
			this.clearDrops();
		}
		//		}
		//		}
	}

	private static int roundAverage(float value) {
		final double floor = Math.floor(value);
		return (int) floor + (Math.random() < (value - floor) ? 1 : 0);
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
			enabled = !enabled;
		}
		return false;
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

	}

	@Override
	public void loadStorage(NBTTagCompound nbt) {
	}

}
