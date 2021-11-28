package xzeroair.trinkets.races;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.capabilities.race.ElementalAttributes;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.AbilityHandler.AbilitySource;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.handlers.RaceAttributeHandler;
import xzeroair.trinkets.util.handlers.SizeHandler;

public abstract class EntityRacePropertiesHandler implements IRaceHandler {

	protected boolean firstUpdate;

	protected EntityLivingBase entity;
	protected EntityProperties properties;
	protected ElementalAttributes element;

	protected EntityRace race;

	protected RaceAttributeHandler attributes;
	protected SizeAttribute artemisSupport;

	protected boolean MainAbility = false;
	protected boolean SecondaryAbility = false;

	public EntityRacePropertiesHandler(@Nonnull EntityLivingBase e, EntityProperties properties, @Nonnull EntityRace race) {
		entity = e;
		this.race = race;
		this.properties = properties;
		attributes = new RaceAttributeHandler(e, race);
		artemisSupport = this.getArtemisAttributeSize();
		firstUpdate = true;
	}

	public RaceAttributeHandler getAttributes() {
		return attributes;
	}

	protected void addAbility(IAbilityInterface ability, IAbilityHandler handler) {
		properties.getAbilityHandler().addAbility(ability, AbilitySource.RACE.getName() + ";" + race.getName(), handler);
	}

	/**
	 * Use {@link #startTransformation()} instead
	 */
	public void onTransform() {
		this.startTransformation();
	}

	/**
	 * Use {@link #endTransformation()} instead
	 */
	public void onTransformEnd() {
		this.endTransformation();
		attributes.removeAttibutes();
		artemisSupport.removeModifiers();
		properties.getAbilityHandler().clearAbilities(AbilitySource.RACE.getName() + ";" + race.getName());
	}

	public void onTick() {
		SizeHandler.setSize(entity, properties);
		if (properties.isTransformed()) {
			attributes.addAttributes();
			artemisSupport.addModifiers();
			this.whileTransformed();
		}
	}

	private SizeAttribute getArtemisAttributeSize() {
		final double size = (race.getRaceSize() - 100) * 0.01D;
		return new SizeAttribute(entity, size, size, 0);
	}

	public void copyFrom(EntityRacePropertiesHandler properties, boolean wasDeath, boolean keepInv) {
	}

	public boolean isCreativePlayer() {
		final boolean flag = (entity instanceof EntityPlayer) && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator());
		return flag;
	}

	/*------------------------------------------Race Handlers--------------------------------------------*/

}
