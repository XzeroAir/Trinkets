package xzeroair.trinkets.traits.elements;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import xzeroair.trinkets.Registries;

public class Element extends IForgeRegistryEntry.Impl<Element> {// implements INBTSerializable<NBTTagCompound> {

	public static final ForgeRegistry<Element> Registry = Registries.getElementRegistry();
	/*----------------------------------Constructor----------------------------------------*/

	protected final String name;

	public Element(String name) {
		this.name = name;
		this.setRegistryName(name);
	}

	public String getName() {
		return name;
	}

	public Element[] getStrengths() {
		return new Element[0];
	}

	public Element[] getWeaknesses() {
		return new Element[0];
	}

	//	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
	//		ResourceLocation resourcelocation = this.getRegistryName();
	//		nbt.setString("id", resourcelocation == null ? Reference.MODID + ":neutral" : resourcelocation.toString());
	//		return nbt;
	//	}
	//
	//	@Override
	//	public NBTTagCompound serializeNBT() {
	//		NBTTagCompound ret = new NBTTagCompound();
	//		writeToNBT(ret);
	//		return ret;
	//	}
	//
	//	@Override
	//	public void deserializeNBT(NBTTagCompound nbt) {
	//	}

	/*----------------------------------Registry----------------------------------------*/

	public int getID() {
		return Registry.getID(this);
	}

	public static int getIdFromElement(Element element) {
		return element == null ? 0 : Registry.getID(element);
	}

	public static Element getItemById(int id) {
		return Registry.getValue(id);
	}

	@Nullable
	public static Element getByNameOrId(String id) {
		Element item = Registry.getValue(new ResourceLocation(id));
		if (item == null) {
			try {
				return getItemById(Integer.parseInt(id));
			} catch (NumberFormatException var3) {

			}
		}
		return item;
	}

	public static void registerElements() {
		Registry.register(new NeutralElement());
		Registry.register(new AirElement());
		Registry.register(new DarkElement());
		Registry.register(new EarthElement());
		Registry.register(new FireElement());
		Registry.register(new IceElement());
		Registry.register(new LightElement());
		Registry.register(new LightningElement());
		Registry.register(new PoisonElement());
		Registry.register(new VoidElement());
		Registry.register(new WaterElement());
	}


	public void damageResistances(){
		NBTTagCompound elements = new NBTTagCompound();

	}

	public NBTTagCompound addElement(NBTTagCompound elements){
		NBTTagCompound damageResistanceTypes = new NBTTagCompound();
		damageResistanceTypes = addDamageResist(damageResistanceTypes, "Fire", 10, false);
		damageResistanceTypes = addDamageResist(damageResistanceTypes, "Ice", 5, false);
		damageResistanceTypes = addDamageResist(damageResistanceTypes, "Lightning", 2, false);
		elements.setTag("damageResistanceTypes", damageResistanceTypes);
		return elements;
	}

	public NBTTagCompound addDamageResist(NBTTagCompound elementTag, String resistType, int amount, boolean IsFlat) {
		elementTag.setInteger(resistType, amount);
		elementTag.setBoolean("isFlatDamage", IsFlat);
		return elementTag;
	}

}