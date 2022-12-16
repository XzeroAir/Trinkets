<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities.base;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.util.Reference;

public abstract class AbilityBase {

	private AbilityHandler handler;
	private EntityLivingBase entity;
	private boolean removeAbility = false;
	protected Random random = Reference.random;
	//	protected NBTTagCompound tag = new NBTTagCompound();

	protected int value = -1;
	protected boolean enabled = false;

	public AbilityBase() {
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

	//	@Override
	//	public boolean shouldRemove() {
	//		return removeAbility;
	//	}
	//
	//	@Override
	//	public void scheduleRemoval() {
	//		removeAbility = true;
	//	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Counters~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

}
=======
package xzeroair.trinkets.traits.abilities.base;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.util.Reference;

public abstract class AbilityBase {

	private AbilityHandler handler;
	private EntityLivingBase entity;
	private boolean removeAbility = false;
	protected Random random = Reference.random;
	//	protected NBTTagCompound tag = new NBTTagCompound();

	protected int value = -1;
	protected boolean enabled = false;

	public AbilityBase() {
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

	//	@Override
	//	public boolean shouldRemove() {
	//		return removeAbility;
	//	}
	//
	//	@Override
	//	public void scheduleRemoval() {
	//		removeAbility = true;
	//	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Counters~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

}
>>>>>>> Stashed changes
