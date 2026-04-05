package xzeroair.trinkets.capabilities.race;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;

import javax.annotation.Nonnull;

public class RaceCache {

    protected int duration;
    protected boolean temporary;
    protected EntityRace race;
    protected Element primary, secondary;
    protected String potion;

    public RaceCache() {
        this(EntityRaces.none, Elements.NEUTRAL);
    }

    public RaceCache(EntityRace race, Element primary) {
        this(race, primary, Elements.NEUTRAL);
    }

    public RaceCache(EntityRace race, Element primary, Element secondary) {
        this(race, primary, secondary, false, 0, "");
    }

    public RaceCache(EntityRace race, Element primary, Element secondary, boolean temporary, int duration, String potion) {
        this.race = race;
        this.primary = primary;
        this.secondary = secondary;
        this.temporary = temporary;
        this.duration = duration;
        this.potion = potion;
    }

    public RaceCache(Element element) {
        this(EntityRaces.none, element);
    }

    public RaceCache(EntityRace race) {
        this(race, Elements.NEUTRAL);
    }

    public EntityRace getRace() {
        return this.race == null ? EntityRaces.none : this.race;
    }

    public Element getPrimaryElement() {
        return this.primary == null ? Elements.NEUTRAL : this.primary;
    }

    public Element getSecondaryElement() {
        return this.primary == null ? Elements.NEUTRAL : this.secondary;
    }

    public int getPrimaryColor() {
        return this.getPrimaryElement() == Elements.NEUTRAL ? this.getRace().getPrimaryColor() : this.getPrimaryElement().getPrimaryColor();
    }

    public int getSecondaryColor() {
        return this.getSecondaryElement() == Elements.NEUTRAL ? this.getPrimaryElement() == Elements.NEUTRAL ? this.getRace().getSecondaryColor() : this.getPrimaryElement().getSecondaryColor() : this.getSecondaryElement().getPrimaryColor();
    }

    public NBTTagCompound saveToNBT(@Nonnull NBTTagCompound tag) {
        tag.setInteger("race", this.getRace().getID());
        tag.setInteger("element", this.getPrimaryElement().getID());
        tag.setInteger("secondary", this.getSecondaryElement().getID());
        tag.setBoolean("temporary", true);
        tag.setInteger("duration", this.duration);
        tag.setString("potion", this.potion);
        return tag;
    }

    public static RaceCache loadFromNBT(NBTTagCompound tag) {
        final EntityRace race = tag.hasKey("race") ? EntityRace.getRaceById(tag.getInteger("race")) : EntityRaces.none;
        final Element element = tag.hasKey("element") ? Element.getById(tag.getInteger("element")) : Elements.NEUTRAL;
        final Element secondary = tag.hasKey("secondary") ? Element.getById(tag.getInteger("secondary")) : Elements.NEUTRAL;
        final boolean temp = tag.hasKey("temporary");
        final int dur = temp && tag.hasKey("duration") ? tag.getInteger("duration") : 0;
        final String pot = temp && tag.hasKey("potion") ? tag.getString("potion") : "";
        return new RaceCache(race == null ? EntityRaces.none : race, element == null ? Elements.NEUTRAL : element, secondary == null ? Elements.NEUTRAL : secondary, temp, dur, pot);
    }

    public RaceCache setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getDuration() {
        return this.duration;
    }

    public RaceCache setTemporary(boolean temp) {
        this.temporary = temp;
        return this;
    }

    public boolean isTemporary() {
        return this.temporary;
    }

    public RaceCache setPotion(String potion) {
        this.potion = potion;
        return this;
    }

    public boolean compare(RaceCache other) {
        return this.compareRace(other) && this.comparePrimaryElement(other) && this.compareTemporary(other) && this.potion.compareTo(other.potion) == 0;
    }

    public boolean compareRace(RaceCache other) {
        return this.getRace().equals(other.getRace());
    }

    public boolean compareRace(EntityRace race) {
        return this.getRace().equals(race);
    }

    public boolean compareRace(String race) {
        return this.getRace().getRegistryName().toString().compareTo(race) == 0;
    }

    public boolean comparePrimaryElement(RaceCache other) {
        return this.getPrimaryElement().equals(other.getPrimaryElement());
    }

    public boolean comparePrimaryElement(Element element) {
        return this.getPrimaryElement().equals(element);
    }

    public boolean comparePrimaryElement(String element) {
        return this.getPrimaryElement().getRegistryName().toString().compareTo(element) == 0;
    }

    public boolean compareTemporary(RaceCache other) {
        return this.isTemporary() == other.isTemporary();
    }

    public boolean compareDuration(RaceCache other) {
        return this.getDuration() == other.getDuration();
    }
}
