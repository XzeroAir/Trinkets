package xzeroair.trinkets.traits.abilities.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.TickHandler;

public abstract class AbilityBase implements IAbilityHandler {

	Map<String, TickHandler> Counters;

	private AbilityHandler handler;
	private EntityLivingBase entity;
	private boolean removeAbility = false;
	protected Random random = Reference.random;
	//	protected NBTTagCompound tag = new NBTTagCompound();

	protected int value = -1;
	protected boolean enabled = false;

	public AbilityBase() {
		Counters = new HashMap<>();
	}

	public AbilityHandler getHandler() {
		return handler;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}

	public AbilityBase setHandler(AbilityHandler handler) {
		this.handler = handler;
		return this;
	}

	public AbilityBase setEntity(EntityLivingBase entity) {
		this.entity = entity;
		return this;
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Base Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	public boolean isCreativePlayer(Entity entity) {
		final boolean flag = (entity instanceof EntityPlayer) && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator());
		return flag;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Removal~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	@Override
	public boolean shouldRemove() {
		return removeAbility;
	}

	@Override
	public void scheduleRemoval() {
		removeAbility = true;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Counters~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	public void addCounter(String key, int length) {
		if ((Counters != null) && !Counters.containsKey(key)) {
			Counters.put(key, new TickHandler(key, length));
		}
	}

	public void removeCounter(String key) {
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			Counters.remove(key);
		}
	}

	public void clearCounters() {
		if (!Counters.isEmpty()) {
			Counters.clear();
		}
	}

	public TickHandler getCounter(String key, int length, boolean isCountdown, boolean create) {
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			return Counters.get(key);//.setLength(length).setCountdown(isCountdown);
		} else if (create) {
			final TickHandler value = new TickHandler(key, length, isCountdown);
			Counters.put(key, value);
			return value;
		} else {
			return null;
		}
	}

	public TickHandler getCounter(String key, int length, boolean isCountdown) {
		return this.getCounter(key, length, isCountdown, true);
	}

	public TickHandler getCounter(String key, boolean create) {
		return this.getCounter(key, 20, false, create);
	}

	public TickHandler getCounter(String key) {
		return this.getCounter(key, false);
	}

}
