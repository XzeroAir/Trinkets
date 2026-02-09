package xzeroair.trinkets.api.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;

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
    public static class RaceUpdateEvent extends TransformationEvent {

        protected boolean changed = false;
        protected boolean elementChanged = false;
        protected EntityRace newRace;
        protected Element newElement;

//		public RaceUpdateEvent(EntityLivingBase entity, EntityProperties properties, ){
//
//		}

        public RaceUpdateEvent(EntityLivingBase entity, EntityProperties properties, EntityRace next, Element nextElement) {
            super(entity, properties, properties.getCurrentRace().getRace());
            this.setChanged(!this.getCurrentRace().equals(next));
            this.setElementChanged(!properties.getCurrentRace().compareElement(nextElement));
            setNewElement(nextElement);
            this.setNewRace(next);
        }

        private void setNewElement(Element element) {
            this.newElement = element;
        }

        public Element getNewElement() {
            return newElement;
        }

        public boolean ElementChanged() {
            return elementChanged;
        }

        public void setElementChanged(boolean elementChanged) {
            this.elementChanged = elementChanged;
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

    public static class StartTransformation extends TransformationEvent {

        public StartTransformation(EntityLivingBase entity, EntityProperties properties, EntityRace current) {
            super(entity, properties, current);
        }
    }

    public static class EndTransformation extends TransformationEvent {

        protected EntityRace prevRace;

        public EndTransformation(EntityLivingBase entity, EntityProperties properties, EntityRace current) {
            super(entity, properties, current);
            this.setPreviousRace(this.getCurrentRace());
        }

        public EntityRace getPreviousRace() {
            return prevRace;
        }

        private void setPreviousRace(EntityRace race) {
            prevRace = race;
        }

    }

}
