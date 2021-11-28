package xzeroair.trinkets.api.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.races.EntityRace;

public abstract class TransformationEvent extends Event {

	protected EntityLivingBase entity;
	protected EntityRace currentRace;
	protected EntityProperties properties;

	public TransformationEvent(EntityLivingBase entity, EntityProperties properties, EntityRace current) {
		this.entity = entity;
		currentRace = current;
		this.properties = properties;
	}

	public EntityProperties getEntityProperties() {
		return properties;
	}

	public EntityLivingBase getEntityLiving() {
		return entity;
	}

	public EntityRace getCurrentRace() {
		return currentRace;
	}

	@Cancelable
	public static class RaceChangedEvent extends TransformationEvent {

		protected boolean changed = false;
		protected EntityRace newRace;

		public RaceChangedEvent(EntityLivingBase entity, EntityProperties properties, EntityRace current, EntityRace next) {
			super(entity, properties, current);
			this.setChanged(!currentRace.equals(next));
			this.setNewRace(next);
		}

		public EntityRace getNewRace() {
			return newRace;
		}

		public void setNewRace(EntityRace race) {
			newRace = race;
		}

		public boolean raceChanged() {
			return changed;
		}

		public void setChanged(boolean changed) {
			this.changed = changed;
		}
	}

	public static class startTransformationEvent extends TransformationEvent {

		public startTransformationEvent(EntityLivingBase entity, EntityProperties properties, EntityRace current) {
			super(entity, properties, current);
		}
	}

	public static class endTransformationEvent extends TransformationEvent {

		protected EntityRace prevRace;

		public endTransformationEvent(EntityLivingBase entity, EntityProperties properties, EntityRace current, EntityRace prev) {
			super(entity, properties, current);
			this.setPreviousRace(prev);
		}

		public EntityRace getPreviousRace() {
			return prevRace;
		}

		private void setPreviousRace(EntityRace race) {
			prevRace = race;
		}

	}

}
