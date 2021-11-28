package xzeroair.trinkets.capabilities.race;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;

public class KeybindHandler {

	Map<String, KeyHandler> storage;

	public KeybindHandler() {
		storage = new HashMap<>();
		this.addKeyBind("Left");
		this.addKeyBind("Right");
		this.addKeyBind("Forward");
		this.addKeyBind("Back");
		this.addKeyBind("Jump");
		this.addKeyBind("Sneak");
	}

	public void addKeyBind(String key) {
		if (!storage.containsKey(key)) {
			storage.put(key, new KeyHandler());
		}
	}

	public KeyHandler getKeyHandler(String key) {
		if (storage.containsKey(key)) {
			return storage.get(key);
		} else {
			final KeyHandler kh = new KeyHandler();
			storage.put(key, kh);
			return kh;
		}
	}

	public boolean pressKey(Entity entity, String key, int state) {
		switch (key) {
		case "Left":
			return this.left(entity, state);
		case "Right":
			return this.right(entity, state);
		case "Forward":
			return this.forward(entity, state);
		case "Backward":
			return this.back(entity, state);
		case "Jump":
			return this.jump(entity, state);
		case "Sneak":
			return this.sneak(entity, state);
		default:
			return false;
		}
	}

	//	public int tickKeyState(String key, boolean isDown) {
	//		KeyHandler keybind = this.getKeyHandler(key);
	//		if (keybind != null) {
	//			return keybind.tickKey(isDown);
	//		} else {
	//			return -1;
	//		}
	//	}

	public boolean left(Entity entity, String ability, int state) {
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			final IAbilityInterface kbAbility = cap.getAbilityHandler().getAbilityByName(ability);
			if (kbAbility != null) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(kbAbility);
					if ((handler != null) && (handler instanceof IMovementAbility)) {
						final IMovementAbility keybind = ((IMovementAbility) handler);
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + kbAbility.getName());
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public boolean left(Entity entity, int state) {
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null)) {
						if ((handler instanceof IMovementAbility)) {
							final IMovementAbility keybind = (IMovementAbility) handler;
							if (!keybind.left(entity, state)) {
								return false;
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean right(Entity entity, int state) {
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null)) {
						if ((handler instanceof IMovementAbility)) {
							final IMovementAbility keybind = (IMovementAbility) handler;
							if (!keybind.right(entity, state)) {
								return false;
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean forward(Entity entity, int state) {
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null)) {
						if ((handler instanceof IMovementAbility)) {
							final IMovementAbility keybind = (IMovementAbility) handler;
							if (!keybind.forward(entity, state)) {
								return false;
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean back(Entity entity, int state) {
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null)) {
						if ((handler instanceof IMovementAbility)) {
							final IMovementAbility keybind = (IMovementAbility) handler;
							if (!keybind.back(entity, state)) {
								return false;
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean jump(Entity entity, int state) {
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null)) {
						if ((handler instanceof IMovementAbility)) {
							final IMovementAbility keybind = (IMovementAbility) handler;
							if (!keybind.jump(entity, state)) {
								return false;
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean sneak(Entity entity, int state) {
		final EntityProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null)) {
						if ((handler instanceof IMovementAbility)) {
							final IMovementAbility keybind = (IMovementAbility) handler;
							if (!keybind.sneak(entity, state)) {
								return false;
							}
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}
