package xzeroair.trinkets.traits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IItemHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.helpers.CallHelper;

public class AbilityHandler {

	protected EntityLivingBase entity;
	protected Map<IAbilityInterface, Storage> abilities;
	// Maybe Add Ability Storage, Like nbt or something?

	public static class Storage {
		String source;
		IAbilityHandler abilityHandler;

		public Storage(String source, IAbilityHandler handler) {
			this.source = source;
			abilityHandler = handler;
		}

		public Storage(AbilitySource Type, String source, IAbilityHandler handler) {
			this(Type.getName() + ";" + source, handler);
		}

		public IAbilityHandler handler() {
			return abilityHandler;
		}

		public String getSource() {
			return source;
		}
	}

	public static enum AbilitySource {

		OTHER(0, "Other"),
		RACE(1, "Race"),
		EQUIPPED(2, "Equipped");

		private static final AbilitySource[] ID = new AbilitySource[values().length];

		private int id;
		private String name;

		private AbilitySource(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getType() {
			return id;
		}

		public String getName() {
			return name;
		}

		public static AbilitySource Type(int value) {
			if ((value < 0) || (value >= ID.length)) {
				value = 0;
			}

			return ID[value];
		}

		public static int ID(String name) {
			for (final AbilitySource ore : AbilitySource.values()) {
				if (ore.getName().contentEquals(name)) {
					return ore.getType();
				}
			}
			return OTHER.getType();
		}

	}

	public AbilityHandler(EntityLivingBase entity) {
		abilities = new HashMap<>();
		this.entity = entity;
	}

	public void tickAbilities(EntityLivingBase entity) {
		for (final IAbilityInterface ability : this.getAbilitiesList()) {
			try {
				final IAbilityHandler handler = this.getAbilityInstance(ability);
				if ((handler != null) && (handler instanceof IItemHeldAbility)) {
					final IItemHeldAbility a = ((IItemHeldAbility) handler);
					a.head(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
					a.chest(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
					a.legs(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
					a.feet(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET));
					a.heldMainHand(entity.getHeldItemMainhand());
					a.heldOffhand(entity.getHeldItemOffhand());
					if (entity instanceof EntityPlayer) {
						final EntityPlayer player = (EntityPlayer) entity;
						a.playerInventory(player.inventory);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			try {
				final IAbilityHandler handler = this.getAbilityInstance(ability);
				if ((handler != null) && (handler instanceof ITickableAbility)) {
					((ITickableAbility) handler).tickAbility(entity);
				}
			} catch (final Exception e) {
				Trinkets.log.error("Error with ability:" + ability.getName());
				e.printStackTrace();
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

	public Map<IAbilityInterface, Storage> getAbilities() {
		return abilities;
	}

	public Set<IAbilityInterface> getAbilitiesList() {
		return abilities.keySet();
	}

	public void cleanUp() {
		final List<IAbilityInterface> removal = new ArrayList<>();
		for (final Entry<IAbilityInterface, Storage> entry : abilities.entrySet()) {
			if (entry.getValue().handler() != null) {
				if (entry.getValue().handler().shouldRemove()) {
					removal.add(entry.getKey());
				}
			}
		}
		removal.forEach((ability) -> {
			this.removeAbility(ability, "");
		});
	}

	public void accessoriesCheck(Map<String, ItemStack> equipped) {
		if ((equipped != null) && !equipped.isEmpty()) {
			if (!abilities.isEmpty()) {
				for (final IAbilityInterface ability : this.getAbilitiesList()) {
					final Storage storage = this.getAbilityStorage(ability);
					if (storage != null) {
						final String source = storage.getSource();
						if (source.startsWith(AbilitySource.EQUIPPED.getName())) {
							final String srcObj = CallHelper.getStringFromArray(source.split(";"), 1);
							if (!srcObj.isEmpty()) {
								boolean keep = false;
								for (final Entry<String, ItemStack> stack : equipped.entrySet()) {
									if (stack.getValue().getItem().getRegistryName().toString().contentEquals(srcObj)) {
										keep = true;
										break;
									}
								}
								if (!keep && (storage.handler() != null)) {
									storage.handler().scheduleRemoval();
								}
							}
						}
					}
				}
			}
		}
	}

	public boolean hasAbility(IAbilityInterface ability) {
		return (ability != null) && !abilities.isEmpty() && abilities.containsKey(ability);
	}

	public boolean isSource(IAbilityInterface ability, String source) {
		if (this.hasAbility(ability)) {
			if (abilities.get(ability) != null) {
				if (abilities.get(ability).getSource().contentEquals(source)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isSource(IAbilityInterface ability, AbilitySource type, String source) {
		return this.isSource(ability, type.getName() + ";" + source);
	}

	public IAbilityHandler getAbilityInstance(IAbilityInterface ability) {
		if (this.hasAbility(ability)) {
			if (abilities.get(ability) != null) {
				if (abilities.get(ability).handler() != null) {
					final IAbilityHandler handler = abilities.get(ability).handler();
					return handler;
				}
			}
		}
		return null;
	}

	public String getAbilitySource(IAbilityInterface ability) {
		if (this.hasAbility(ability)) {
			if (abilities.get(ability) != null) {
				return abilities.get(ability).getSource();
			}
		}
		return null;
	}

	public Storage getAbilityStorage(IAbilityInterface ability) {
		if (this.hasAbility(ability)) {
			return abilities.get(ability);
		}
		return null;
	}

	public IAbilityInterface getAbilityByName(String name) {
		for (final IAbilityInterface ability : this.getAbilitiesList()) {
			if (ability.getName().contentEquals(name)) {
				return ability;
			}
		}
		return null;
	}

	public void addAbility(IAbilityInterface ability, String source, IAbilityHandler handler) {
		if (ability == null) {
			Trinkets.log.error("Trinkets had an Error adding an Ability from:" + source);
			return;
		}
		if (!this.hasAbility(ability)) {
			abilities.put(ability, new Storage(source, handler));
		} else {
			if (source.startsWith(AbilitySource.RACE.getName())) {
				this.replaceAbility(ability, source, handler);
			} else {
			}
		}
	}

	public void replaceAbility(IAbilityInterface ability, String source, IAbilityHandler handler) {
		if (ability == null) {
			Trinkets.log.error("Trinkets had an Error adding an Ability from:" + source);
			return;
		}
		if (this.hasAbility(ability)) {
			if (!this.isSource(ability, source)) {
				abilities.replace(ability, new Storage(source, handler));
			}
		} else {
			this.addAbility(ability, source, handler);
		}
	}

	public void removeAbility(IAbilityInterface ability, String source) {
		if (ability == null) {
			Trinkets.log.error("Trinkets had an Error adding an Ability from:" + source + " Ability can not be null");
			return;
		}
		if (entity == null) {
			return;
		}
		if (this.hasAbility(ability)) {
			if (this.isSource(ability, source) || source.isEmpty()) {
				final Storage storage = abilities.get(ability);
				if (storage != null) {
					final IAbilityHandler handler = storage.handler();
					if (handler != null) {
						handler.removeAbility(entity);
					}
				}
				abilities.remove(ability);
			}
		}
	}

	public void clearAbilities(String source) {
		if (entity == null) {
			return;
		}
		if (!abilities.isEmpty()) {
			final List<IAbilityInterface> removal = new ArrayList<>();
			for (final Entry<IAbilityInterface, Storage> entry : abilities.entrySet()) {
				if (entry.getValue() != null) {
					final String src = entry.getValue().getSource();
					if (source.isEmpty() || source.contentEquals(src)) {
						final IAbilityHandler handler = entry.getValue().handler();
						if (handler != null) {
							handler.removeAbility(entity);
						}
						removal.add(entry.getKey());
					}
				}
			}
			for (final IAbilityInterface ability : removal) {
				this.removeAbility(ability, "");
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Call Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	//	public void jump(IAbilityInterface ability) {
	//		IAbilityHandler handler = this.getAbilityInstance(ability);
	//		if (handler instanceof IJumpAbility) {
	//			((IJumpAbility) handler).jump(entity);
	//		}
	//	}
	//
	//	public boolean fall(IAbilityInterface ability, float distance, float damage, boolean cancel) {
	//		IAbilityHandler handler = this.getAbilityInstance(ability);
	//		if (handler instanceof IJumpAbility) {
	//			return ((IJumpAbility) handler).fall(distance, damage, cancel);
	//		}
	//		return cancel;
	//	}
	//
	//	public boolean attack(IAbilityInterface ability, EntityLivingBase target, DamageSource source, float damage, boolean cancel) {
	//		IAbilityHandler handler = this.getAbilityInstance(ability);
	//		if (handler instanceof IAttackAbility) {
	//			return ((IAttackAbility) handler).attackEntity(target, source, damage, cancel);
	//		}
	//		return cancel;
	//	}
	//
	//	public boolean attacked(IAbilityInterface ability, DamageSource source, float damage, boolean cancel) {
	//		IAbilityHandler handler = this.getAbilityInstance(ability);
	//		if (handler instanceof IAttackAbility) {
	//			return ((IAttackAbility) handler).attacked(entity, source, damage, cancel);
	//		}
	//		return cancel;
	//	}
	//
	//	/**
	//	 * @param ability expects an {@value IPotionAbility}
	//	 * @return will return false if the ability is not
	//	 */
	//	public boolean immuneToPotion(IAbilityInterface ability, PotionEffect effect, boolean cancel) {
	//		IAbilityHandler handler = this.getAbilityInstance(ability);
	//		if (handler instanceof IPotionAbility) {
	//			return ((IPotionAbility) handler).potionApplied(effect, cancel);
	//		}
	//		return cancel;
	//	}
}
