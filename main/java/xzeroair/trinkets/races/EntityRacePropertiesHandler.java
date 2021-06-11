package xzeroair.trinkets.races;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.capabilities.race.ElementalAttributes;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.MagicStats;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.handlers.RaceAttributeHandler;
import xzeroair.trinkets.util.handlers.SizeHandler;

public abstract class EntityRacePropertiesHandler implements IRaceHandler {

	protected boolean firstUpdate;

	protected EntityLivingBase entity;
	protected EntityProperties properties;
	protected MagicStats magic;
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
		magic = this.properties.getMagic();
		attributes = new RaceAttributeHandler(e, race);
		artemisSupport = this.getArtemisAttributeSize();
		firstUpdate = true;
	}

	/*
	 * Use @Method startTransformation() instead
	 */
	public void onTransform() {
		this.startTransformation();
	}

	/*
	 * Use @Method endTransformation() instead
	 */
	public void onTransformEnd() {
		this.endTransformation();
		attributes.removeAttibutes();
		artemisSupport.removeModifiers();
	}

	public void onTick() {
		SizeHandler.setSize(entity, properties);
		//		if (properties.isTransforming()) {
		//			entity.moveForward = 0;
		//			entity.moveStrafing = 0;
		//			entity.motionX = 0;
		//			entity.motionZ = 0;
		//			entity.posX = entity.prevPosX;
		//			entity.posY = entity.prevPosY;
		//			entity.posZ = entity.prevPosZ;
		//		}
		if (properties.isTransformed()) {
			attributes.addAttributes();
			artemisSupport.addModifiers();
			this.whileTransformed();
		}
	}

	private SizeAttribute getArtemisAttributeSize() {
		double size = (race.getRaceSize() - 100) * 0.01D;
		return new SizeAttribute(entity, size, size, 0);
	}

	public void copyFrom(EntityRacePropertiesHandler properties) {
	}

	/*------------------------------------------Race Handlers--------------------------------------------*/

}
