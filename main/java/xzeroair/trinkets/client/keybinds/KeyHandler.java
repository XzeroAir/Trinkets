package xzeroair.trinkets.client.keybinds;

import java.util.function.Function;

import net.minecraft.client.settings.KeyBinding;

public class KeyHandler {

	KeyBinding key;
	boolean isDown = true;
	boolean isPressed = false;
	boolean isReleased = true;
	int ticks = 0;

	public KeyHandler(KeyBinding key) {
		this.key = key;
	}

	public int heldDuration() {
		return ticks;
	}

	public <T> boolean Pressed(T target, Function<T, Boolean> onPress) {
		return this.handler(target, onPress, null, null);
	}

	public <T> boolean HeldDown(T target, Function<T, Boolean> onHeld) {
		return this.handler(target, null, onHeld, null);
	}

	public <T> boolean Released(T target, Function<T, Boolean> onRelease) {
		return this.handler(target, null, null, onRelease);
	}

	public <T> boolean handler(T target, Function<T, Boolean> onPress, Function<T, Boolean> onDown, Function<T, Boolean> onRelease) {
		if (key.isKeyDown() && isDown) {
			ticks++;
			if (!isPressed && isReleased) {
				isPressed = true;
				isReleased = false;
				if ((target != null) && (onPress != null)) {
					isDown = onPress.apply(target);
					if (!isDown) {
						isPressed = false;
						isReleased = true;
					}
				}
			} else {
				if (!isReleased) {
					if ((target != null) && (onDown != null)) {
						isReleased = !onDown.apply(target);
					}
					if (isReleased) {
						if ((target != null) && (onRelease != null)) {
							isDown = onRelease.apply(target);
						}
					}
				}
			}
			return true;
		} else {
			if (!isReleased) {
				if ((target != null) && (onRelease != null)) {
					isReleased = onRelease.apply(target);
				} else {
					isReleased = true;
				}
			}
			isPressed = false;
			isDown = true;
			ticks = 0;
			return false;
		}
	}

	public boolean isDown() {
		return this.handler(null, null, null, null);
	}

}
