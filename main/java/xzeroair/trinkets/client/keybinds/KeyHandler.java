package xzeroair.trinkets.client.keybinds;

import java.util.function.Function;

public class KeyHandler {

    private static final Function<Boolean, Boolean> ACCEPT = value -> true;

    private boolean isDown = true;
    private boolean isPressed = false;
    private boolean isReleased = true;
    private boolean forceRelease;
    private int ticks = 0;
    private int state = -1;
    private boolean continueInput = true;

    public int heldDuration() {
        return this.ticks;
    }

    public int getState() {
        return this.state;
    }

    public boolean continueInput() {
        return this.continueInput;
    }

    public void reset() {
        this.isDown = true;
        this.isPressed = false;
        this.isReleased = true;
        this.forceRelease = false;
        this.ticks = 0;
    }

    public void forceRelease() {
        this.forceRelease = true;
        this.isPressed = true;
        this.isReleased = true;
        this.state = -1;
        this.continueInput = false;
    }

    public boolean loadState(int state, Function<KeyHandler, Boolean> onState) {
        if ((state < 0) || (state > 2)) {
            return false;
        }
        this.state = state;
        this.continueInput = onState.apply(this);
        return this.continueInput;
    }

    public boolean handler(boolean keyDown, Function<KeyHandler, Boolean> onState) {
        return this.handler(keyDown, ignored -> onState.apply(this), ignored -> onState.apply(this), ignored -> onState.apply(this));
    }

    public boolean updateKeyState(boolean keyDown) {
        return this.handler(keyDown, ACCEPT, ACCEPT, ACCEPT);
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
        this.state = -1;
        this.continueInput = true;
        if (keyDown && this.isDown) {
            this.ticks++;
            if (!this.isPressed && this.isReleased) {
                this.isPressed = true;
                this.isReleased = false;
                if ((target != null) && (onPress != null)) {
                    this.state = 0;
                    this.continueInput = onPress.apply(target);
                    this.isDown = this.continueInput;
                    if (!this.isDown) {
                        this.isPressed = false;
                        this.isReleased = true;
                    }
                }
            } else {
                if (!this.isReleased) {
                    if ((target != null) && (onDown != null)) {
                        this.state = 1;
                        this.continueInput = onDown.apply(target);
                        this.isReleased = !this.continueInput;
                    }
                    if (this.isReleased) {
                        if ((target != null) && (onRelease != null)) {
                            this.state = 2;
                            this.continueInput = onRelease.apply(target);
                            this.isDown = this.continueInput;
                        }
                    }
                }
            }
            return true;
        } else {
            if (!this.isReleased) {
                if ((target != null) && (onRelease != null)) {
                    this.state = 2;
                    this.continueInput = onRelease.apply(target);
                    this.isReleased = this.continueInput;
                } else {
                    this.isReleased = true;
                }
            }
            this.isPressed = false;
            this.isDown = true;
            this.ticks = 0;
            return false;
        }
    }

    public boolean handler(boolean keyDown, Function<Boolean, Boolean> onPress, Function<Boolean, Boolean> onDown, Function<Boolean, Boolean> onRelease) {
        this.state = -1;
        this.continueInput = true;
        final boolean target = true;
        if (keyDown && this.isDown) {
            this.ticks++;
            if (!this.isPressed && this.isReleased) {
                this.isPressed = true;
                this.forceRelease = false;
                if ((onPress != null)) {
                    this.state = 0;
                    this.continueInput = onPress.apply(target);
                    this.isReleased = !this.continueInput;
                }
            } else {
                if (!this.isReleased) {
                    if (this.isPressed && (onDown != null)) {
                        // Only Runs this if Press is Not cancel and is held
                        this.state = 1;
                        this.continueInput = onDown.apply(target);
                        this.isReleased = !this.continueInput;
                    }
                } else {
                    if (!this.isPressed && (onRelease != null)) {
                        // Runs this if Held is Canceled but is still held
                        this.state = 2;
                        this.continueInput = onRelease.apply(target);
                        this.isReleased = true;
                    } else {
                        if (!this.forceRelease) {
                            // Runs this if Press is Canceled but still held
                            this.state = 2;
                            this.continueInput = onRelease.apply(target);
                            this.forceRelease = this.continueInput;
                        }
                    }
                }
            }
            return true;
        } else {
            if (!this.isReleased) {
                if ((onRelease != null)) {
                    if (!this.forceRelease) {
                        // Runs this if Held is Not Canceled and key is let go
                        this.state = 2;
                        this.continueInput = onRelease.apply(target);
                        this.forceRelease = this.continueInput;
                    }
                }
            }
            this.reset();
            return false;
        }
    }


}
