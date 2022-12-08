package xzeroair.trinkets.capabilities.race;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;

public class KeybindHandler {

	public static enum keyEnum {

		LEFT("Left"), RIGHT("Right"), FORWARD("Forward"), BACK("Backward"), JUMP("Jump"), SNEAK("Sneak"), NONE("None");

		private String name;

		private keyEnum(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		private static final keyEnum[] ID = new keyEnum[values().length];

		public static keyEnum byName(String name) {
			for (int i = 0; i < values().length; i++) {
				if (byID(i).getName().contentEquals(name))
					return byID(i);
			}
			return NONE;
		}

		public static keyEnum byID(int value) {
			if ((value < 0) || (value >= values().length)) {
				value = 0;
			}
			return values()[value];
		}

	}

	private Map<String, KeyHandler> storage;

	public KeybindHandler() {
		storage = new HashMap<>();
		this.addKeyBind(keyEnum.LEFT.getName());
		this.addKeyBind(keyEnum.RIGHT.getName());
		this.addKeyBind(keyEnum.FORWARD.getName());
		this.addKeyBind(keyEnum.BACK.getName());
		this.addKeyBind(keyEnum.JUMP.getName());
		this.addKeyBind(keyEnum.SNEAK.getName());
	}

	public void addKeyBind(String key) {
		if (!storage.containsKey(key)) {
			storage.put(key, new KeyHandler());
		}
	}

	public KeyHandler getKeyHandler(String key) {
		if (storage.containsKey(key))
			return storage.get(key);
		else {
			final KeyHandler kh = new KeyHandler();
			return storage.put(key, kh);
		}
	}

	public boolean pressKey(Entity entity, String key, int state) {
		keyEnum KEY = keyEnum.byName(key);
		switch (KEY) {
		case LEFT:
			return this.left(entity, state);
		case RIGHT:
			return this.right(entity, state);
		case FORWARD:
			return this.forward(entity, state);
		case BACK:
			return this.back(entity, state);
		case JUMP:
			return this.jump(entity, state);
		case SNEAK:
			return this.sneak(entity, state);
		default:
			return false;
		}
	}

	public boolean left(Entity entity, int state) {
		return Capabilities.getEntityProperties(entity, true, (properties, bool) -> {
			Map<String, AbilityHolder> abilities = properties.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					IAbilityInterface ability = holder.getAbility();
					if (ability instanceof IMovementAbility) {
						boolean cancel = ((IMovementAbility) ability).left(entity, state);
						if (!cancel)
							return false;
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			return bool;
		});
	}

	public boolean right(Entity entity, int state) {
		return Capabilities.getEntityProperties(entity, true, (properties, bool) -> {
			Map<String, AbilityHolder> abilities = properties.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					IAbilityInterface ability = holder.getAbility();
					if (ability instanceof IMovementAbility) {
						boolean cancel = ((IMovementAbility) ability).right(entity, state);
						if (!cancel)
							return false;
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			return bool;
		});
	}

	public boolean forward(Entity entity, int state) {
		return Capabilities.getEntityProperties(entity, true, (properties, bool) -> {
			Map<String, AbilityHolder> abilities = properties.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					IAbilityInterface ability = holder.getAbility();
					if (ability instanceof IMovementAbility) {
						boolean cancel = ((IMovementAbility) ability).forward(entity, state);
						if (!cancel)
							return false;
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			return bool;
		});
	}

	public boolean back(Entity entity, int state) {
		return Capabilities.getEntityProperties(entity, true, (properties, bool) -> {
			Map<String, AbilityHolder> abilities = properties.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					IAbilityInterface ability = holder.getAbility();
					if (ability instanceof IMovementAbility) {
						boolean cancel = ((IMovementAbility) ability).back(entity, state);
						if (!cancel)
							return false;
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			return bool;
		});
	}

	public boolean jump(Entity entity, int state) {
		return Capabilities.getEntityProperties(entity, true, (properties, bool) -> {
			Map<String, AbilityHolder> abilities = properties.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					IAbilityInterface ability = holder.getAbility();
					if (ability instanceof IMovementAbility) {
						boolean cancel = ((IMovementAbility) ability).jump(entity, state);
						if (!cancel)
							return false;
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			return bool;
		});
	}

	public boolean sneak(Entity entity, int state) {
		return Capabilities.getEntityProperties(entity, true, (properties, bool) -> {
			Map<String, AbilityHolder> abilities = properties.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					IAbilityInterface ability = holder.getAbility();
					if (ability instanceof IMovementAbility) {
						boolean cancel = ((IMovementAbility) ability).sneak(entity, state);
						if (!cancel)
							return false;
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			return bool;
		});
	}

}
