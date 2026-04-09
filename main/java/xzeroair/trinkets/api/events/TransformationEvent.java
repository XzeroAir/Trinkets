package xzeroair.trinkets.api.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;

public abstract class TransformationEvent extends Event {

    protected EntityLivingBase entity;
    protected RaceCache currentRace;
    protected EntityProperties properties;

    public TransformationEvent(EntityLivingBase entity, EntityProperties properties, RaceCache cache) {
        this.entity = entity;
        this.currentRace = cache;
        this.properties = properties;
    }

    public EntityProperties getEntityProperties() {
        return this.properties;
    }

    public EntityLivingBase getEntityLiving() {
        return this.entity;
    }

    public RaceCache getCurrentRaceCache() {
        return this.currentRace;
    }

    @Cancelable
    public static class RaceUpdateEvent extends TransformationEvent {

        protected boolean changed = false;
        protected RaceCache newRaceCache;
        protected EntityRace newRace;
        protected Element newElement;

//		public RaceUpdateEvent(EntityLivingBase entity, EntityProperties properties, ){
//
//		}

        public RaceUpdateEvent(EntityLivingBase entity, EntityProperties properties, RaceCache cache) {
            super(entity, properties, properties.getCurrentRaceCache());
            this.setChanged(!this.getCurrentRaceCache().compare(cache));
            this.setNewRaceCache(cache);
            this.setNewElement(cache.getPrimaryElement());
            this.setNewRace(cache.getRace());
        }

        private void setNewElement(Element element) {
            this.newElement = element;
        }

        public Element getNewElement() {
            return this.newElement;
        }

        private void setNewRace(EntityRace race) {
            this.newRace = race;
        }

        public EntityRace getNewRace() {
            return this.newRace;
        }

        public void setNewRaceCache(RaceCache cache) {
            this.newRaceCache = cache;
        }

        public RaceCache getNewRaceCache() {
            return this.newRaceCache;
        }

        public boolean raceChanged() {
            return this.changed;
        }

        public void setChanged(boolean changed) {
            this.changed = changed;
        }
    }

    public static class StartTransformation extends TransformationEvent {

        public StartTransformation(EntityLivingBase entity, EntityProperties properties, RaceCache current) {
            super(entity, properties, current);
        }
    }

    public static class EndTransformation extends TransformationEvent {

        protected RaceCache prevRace;

        public EndTransformation(EntityLivingBase entity, EntityProperties properties, RaceCache current) {
            super(entity, properties, current);
            this.setPreviousRace(this.getCurrentRaceCache());
        }

        public RaceCache getPreviousRace() {
            return this.prevRace;
        }

        private void setPreviousRace(RaceCache race) {
            this.prevRace = race;
        }

    }

}
