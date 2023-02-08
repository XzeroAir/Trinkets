package xzeroair.trinkets.capabilities.race;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.traits.elements.Element;

public class ElementalAttributes {

	protected Element primary;
	protected Element secondary;
	protected Map<ResourceLocation, Element> subElements;

	public ElementalAttributes() {
		primary = Elements.NEUTRAL;
		secondary = Elements.NEUTRAL;
		subElements = new TreeMap<>();
	}

	public ElementalAttributes setPrimaryElement(Element element) {
		if ((element != null) && (element != primary)) {
			primary = element;
		}
		return this;
	}

	public ElementalAttributes setSecendaryElement(Element element) {
		if ((element != null) && (element != secondary)) {
			secondary = element;
		}
		return this;
	}

	public ElementalAttributes addSubElements(Element... elements) {
		for (Element element : elements) {
			if ((this.getPrimaryElement() != element) && (this.getSecondaryElement() != element)) {
				subElements.put(element.getRegistryName(), element);
			}
		}
		return this;
	}

	@Nullable
	public Element removeSubElement(Element element) {
		if (element != null) {
			return subElements.remove(element.getRegistryName());
		}
		return null;
	}

	public Element getPrimaryElement() {
		return primary;
	}

	public Element getSecondaryElement() {
		return secondary;
	}

	public Map<ResourceLocation, Element> getSubElements() {
		return subElements;
	}

	public NBTTagCompound saveToNBT(NBTTagCompound compound) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString("primary", this.getPrimaryElement().getRegistryName().toString());
		tag.setString("secondary", this.getSecondaryElement().getRegistryName().toString());
		final NBTTagCompound subs = new NBTTagCompound();
		for (Element subEle : this.getSubElements().values()) {
			subs.setString(subEle.getID() + "", subEle.getRegistryName().toString());
		}
		tag.setTag("sub", subs);
		compound.setTag("Elements", tag);
		return compound;
	}

	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("Elements")) {
			final NBTTagCompound tag = compound.getCompoundTag("Elements");
			if (tag.hasKey("primary")) {
				primary = Element.getByNameOrId(tag.getString("primary"));
			}
			if (tag.hasKey("secondary")) {
				secondary = Element.getByNameOrId(tag.getString("secondary"));
			}
			if (tag.hasKey("sub")) {
				final NBTTagCompound subs = tag.getCompoundTag("sub");
				for (String entry : subs.getKeySet()) {
					final String ele = subs.getString(entry);
					this.addSubElements(Element.getByNameOrId(ele));
				}
			}

		}
	}

	//TODO Finish this

}
