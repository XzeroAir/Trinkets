package xzeroair.trinkets.client.keybinds;

import java.util.function.Function;

public class KeyHandler {

	private boolean isDown = true;
	private boolean isPressed = false;
	private boolean isReleased = true;
	private boolean isDead, reset = false;
	private int ticks = 0;

	//
	//	public KeyHandler() {
	//	}
	//
	public int heldDuration() {
		return ticks;
	}

	//
	public <T> boolean Pressed(T target, boolean keyDown, Function<T, Boolean> onPress) {
		return this.handler(target, keyDown, onPress, null, null);
	}

	//
	//	public <T> boolean HeldDown(T target, boolean keyDown, Function<T, Boolean> onHeld) {
	//		return this.handler(target, keyDown, null, onHeld, null);
	//	}
	//
	//	public <T> boolean Released(T target, boolean keyDown, Function<T, Boolean> onRelease) {
	//		return this.handler(target, keyDown, null, null, onRelease);
	//	}
	//
	public <T> boolean handler(T target, boolean keyDown, Function<T, Boolean> onPress, Function<T, Boolean> onDown, Function<T, Boolean> onRelease) {
		if (keyDown && isDown) {
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

	public boolean handler(boolean keyDown, Function<Boolean, Boolean> onPress, Function<Boolean, Boolean> onDown, Function<Boolean, Boolean> onRelease) {
		final boolean target = true;
		if (keyDown && isDown) {
			ticks++;
			if (!isPressed && isReleased) {
				isPressed = true;
				isDead = false;
				if ((onPress != null)) {
					isReleased = !onPress.apply(target);
				}
			} else {
				if (!isReleased) {
					if (isPressed && (onDown != null)) {
						// Only Runs this if Press is Not cancel and is held
						isReleased = !onDown.apply(target);
					}
				} else {
					if (!isPressed && (onRelease != null)) {
						// Runs this if Held is Canceled but is still held
						onRelease.apply(target);
						isReleased = true;
					} else {
						if (!isDead) {
							// Runs this if Press is Canceled but still held
							isDead = onRelease.apply(target);
						} else {

						}
					}
				}
			}
			return true;
		} else {
			if (!isReleased) {
				if ((onRelease != null)) {
					if (!isDead) {
						// Runs this if Held is Not Canceled and key is let go
						isDead = onRelease.apply(target);
					}
				}
			}
			isReleased = true;
			isPressed = false;
			isDown = true;
			ticks = 0;
			return false;
		}
	}

	//	private boolean isKeyDown = false;
	//	private int state = -1;
	//
	//	public void setKeyState(int state) {
	//		this.state = state;
	//	}
	//
	//	public int getKeyState() {
	//		return state;
	//	}

	//	public int tickKey(boolean keyDown) {
	//		if (keyDown) {
	//			if ((state == 1)) {
	//				isKeyDown = true;
	//				return 1;
	//			} else {
	//				isKeyDown = true;
	//				state = 1;
	//				return 0;
	//			}
	//		} else {
	//			if (isKeyDown) {
	//				state = -1;
	//				isKeyDown = false;
	//				return 2;
	//			} else {
	//				state = -1;
	//				return -1;
	//			}
	//		}
	//		//		if (!keyDown && isKeyDown) {
	//		//			isKeyDown = false;
	//		//			return 2;
	//		//		} else if (keyDown && isKeyDown) {
	//		//			return 1;
	//		//		} else if (keyDown && !isKeyDown) {
	//		//			isKeyDown = true;
	//		//			return 0;
	//		//		} else {
	//		//			isKeyDown = false;
	//		//			return -1;
	//		//		}
	//	}
	//
	//	public boolean isDown(boolean keyDown) {
	//		return this.handler(null, keyDown, null, null, null);
	//	}

}
