package xzeroair.trinkets.capabilities.elements;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.traits.elements.Element;

import javax.annotation.Nullable;
import java.util.TreeMap;

public abstract class ElementalAttributes<T extends ElementalAttributes, E> extends CapabilityBase<T, E> {

    protected final String TAG_KEY = Capabilities.ELEMENT_CAP_TAG;

    private final String ELEMENTS_TAG = "Elements";
    private final String SUB_ELEMENTS_TAG = "SubElements";
    private final String PRIMARY_TAG = "primary";
    private final String SECONDARY_TAG = "secondary";

    @Nullable
    protected Element primary;
    @Nullable
    protected Element secondary;
    protected Element temporary;
    protected TreeMap<ResourceLocation, Element> subElements;

    public ElementalAttributes(E object) {
        super(object);
        this.primary = Elements.NEUTRAL;
        this.secondary = Elements.NEUTRAL;
        this.temporary = Elements.NEUTRAL;
        this.subElements = new TreeMap<>();
    }

    public ElementalAttributes<T, E> setPrimaryElement(@Nullable Element element) {
        if ((element != null) && (element != this.primary)) {
            this.primary = element;
        }
        return this;
    }

    public ElementalAttributes<T, E> setSecendaryElement(@Nullable Element element) {
        if ((element != null) && (element != this.secondary)) {
            this.secondary = element;
        }
        return this;
    }

    public ElementalAttributes<T, E> setTemporaryElement(Element element) {
        if ((element != null) && (element != this.temporary)) {
            this.temporary = element;
        }
        return this;
    }

    public ElementalAttributes<T, E> addSubElements(Element... elements) {
        for (Element element : elements) {
            if ((this.getPrimaryElement() != element) && (this.getSecondaryElement() != element)) {
                this.subElements.put(element.getRegistryName(), element);
            }
        }
        return this;
    }

    @Nullable
    public Element removeSubElement(Element element) {
        if (element != null) {
            return this.subElements.remove(element.getRegistryName());
        }
        return null;
    }

    public Element getPrimaryElement() {
        return this.primary;
    }

    public Element getSecondaryElement() {
        return this.secondary;
    }

    public Element getTemporaryElement() {
        return this.temporary;
    }

//    public TreeMap<ResourceLocation, Element> getSubElements() {
//        return subElements;
//    }

    public boolean comparePrimaryElement(Element element) {
        return this.getPrimaryElement().equals(element);
    }

    public boolean compareElementAttributes(T other) {
        return false;
    }

    @Override
    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        if (!compound.hasKey(this.ELEMENTS_TAG)) {
            compound.setTag(this.ELEMENTS_TAG, new NBTTagCompound());
        }
        final NBTTagCompound tag = compound.getCompoundTag(this.ELEMENTS_TAG);
        tag.setString(this.PRIMARY_TAG, this.getPrimaryElement().getRegistryName().toString());
        tag.setString(this.SECONDARY_TAG, this.getSecondaryElement().getRegistryName().toString());
//        final NBTTagCompound subs = new NBTTagCompound();
//        for (Element subEle : this.getSubElements().values()) {
//            subs.setString(subEle.getID() + "", subEle.getRegistryName().toString());
//        }
//        if (!subs.isEmpty()) {
//            tag.setTag(SUB_ELEMENTS_TAG, subs);
//        }
        return compound;
    }

    public ElementalAttributes<T, E> saveNBT() {
        this.saveToNBT(this.getTag());
        return this;
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        NBTTagCompound tag = compound;
        if (compound.hasKey(this.ELEMENTS_TAG)) {
            tag = compound.getCompoundTag(this.ELEMENTS_TAG);
        }
        if (tag.hasKey(this.PRIMARY_TAG)) {
            this.primary = Element.getByNameOrId(tag.getString(this.PRIMARY_TAG));
        }
        if (tag.hasKey(this.SECONDARY_TAG)) {
            this.secondary = Element.getByNameOrId(tag.getString(this.SECONDARY_TAG));
        }
//            if (tag.hasKey(SUB_ELEMENTS_TAG)) {
//                final NBTTagCompound subs = tag.getCompoundTag(SUB_ELEMENTS_TAG);
//                for (String entry : subs.getKeySet()) {
//                    final String ele = subs.getString(entry);
//                    this.addSubElements(Element.getByNameOrId(ele));
//                }
//            }
    }

    public void copyFrom(ElementalAttributes<T, E> elementalAttributes, boolean wasDeath, boolean keepInv) {
        if (wasDeath && !keepInv) {

        } else {
            this.setPrimaryElement(elementalAttributes.getPrimaryElement());
            this.setSecendaryElement(elementalAttributes.getSecondaryElement());
            this.setTemporaryElement(elementalAttributes.getTemporaryElement());
        }
    }

}
