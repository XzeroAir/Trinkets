package xzeroair.trinkets.util.handlers;

public class TickHandler {

	private String name;
	private int length;
	private int tick;
	private boolean countdown;

	public TickHandler(String name, int length) {
		this.name = name;
		tick = 0;
		this.length = length;
		countdown = false;
	}

	public TickHandler(String name, int length, boolean countdown) {
		this.name = name;
		if (countdown) {
			tick = length;
		} else {
			tick = 0;
		}
		this.length = length;
		this.countdown = countdown;
	}

	public boolean Tick() {
		if (!countdown) {
			if (tick < length) {
				tick++;
				return false;
			} else {
				this.resetTick();
				return true;
			}
		} else {
			if (tick > 0) {
				tick--;
				return false;
			} else {
				this.resetTick();
				return true;
			}
		}
	}

	public int getTick() {
		return tick;
	}

	public TickHandler setTick(int tick) {
		if (this.tick != tick) {
			this.tick = tick;
		}
		return this;
	}

	public TickHandler setLength(int length) {
		if (this.length != length) {
			//			tick = length;
			this.length = length;
		}
		return this;
	}

	public TickHandler setCountdown(boolean countdown) {
		if (this.countdown != countdown) {
			this.countdown = countdown;
		}
		return this;
	}

	public void resetTick() {
		if (countdown) {
			tick = length;
		} else {
			tick = 0;
		}

	}

}
