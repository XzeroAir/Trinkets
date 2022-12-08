package xzeroair.trinkets.util.handlers;

import xzeroair.trinkets.client.keybinds.KeyHandler;

public class DodgeHandler {

	int keyPresses = 0;
	String keyPressed;
	KeyHandler left, right, forward, backward, aux;
	int direction = -1;
	Long lastKeyPress = -1L;
	boolean trigger;

	//TODO Make this an Ability
	public DodgeHandler() {
		//		left = new KeyHandler(Minecraft.getMinecraft().gameSettings.keyBindLeft);
		//		right = new KeyHandler(Minecraft.getMinecraft().gameSettings.keyBindRight);
		//		forward = new KeyHandler(Minecraft.getMinecraft().gameSettings.keyBindForward);
		//		backward = new KeyHandler(Minecraft.getMinecraft().gameSettings.keyBindBack);
		//		aux = new KeyHandler(ModKeyBindings.AUX_KEY);
	}

	public void handleDodge() {
		this.handleKeys(left, trigger, 1); // West
		this.handleKeys(right, trigger, 3); // East
		this.handleKeys(forward, trigger, 2); // North
		this.handleKeys(backward, trigger, 0); // South
		//		if (ElenaiDodgeCompat.isDodgeKeyDown()) {
		//			trigger = false;
		//			keyPresses = 0;
		//			lastKeyPress = -1L;
		//			direction = -1;
		//		}
	}

	private void handleKeys(KeyHandler key, boolean target, int direction) {
		//		long time = System.currentTimeMillis();
		//		key.handler(
		//				target,
		//				press -> {
		//					if (time > (lastKeyPress + 200)) {
		//						keyPresses = 1;
		//					}
		//					if (this.direction != direction) {
		//						lastKeyPress = -1L;
		//						keyPresses = 1;
		//						this.direction = direction;
		//					}
		//					if ((lastKeyPress + 200) > time) {
		//						keyPresses++;
		//					} else {
		//						lastKeyPress = time;
		//					}
		//
		//					if (keyPresses >= 2) {
		//						trigger = true;
		//						keyPresses = 0;
		//						lastKeyPress = -1L;
		//					} else {
		//						trigger = false;
		//					}
		//					return true;
		//				},
		//				null,
		//				null
		//		);
	}

	public int getDirection() {
		return direction;
	}

	public boolean triggerDodge() {
		if (trigger) {
			trigger = false;
			return true;
		} else {
			return false;
		}
	}

}
