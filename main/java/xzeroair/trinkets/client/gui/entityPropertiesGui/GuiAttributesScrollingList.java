package xzeroair.trinkets.client.gui.entityPropertiesGui;

import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

public class GuiAttributesScrollingList extends GuiScrollingList {

	private GuiAttributesScreen parent;
	private ArrayList<IAbilityInterface> abilities;

	private int id;

	public GuiAttributesScrollingList(int ID, GuiAttributesScreen parent, ArrayList<IAbilityInterface> abilities, int listWidth, int slotHeight) {
		super(parent.mc, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight, parent.width, parent.height);
		id = ID;
		this.parent = parent;
		this.abilities = abilities;
	}

	@Override
	protected int getSize() {
		return abilities.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		parent.selectAbilityIndex(index);
	}

	@Override
	protected boolean isSelected(int index) {
		return parent.abilityIndexSelected(index);
	}

	@Override
	protected void drawBackground() {
		//		parent.drawDefaultBackground();
	}

	@Override
	protected int getContentHeight() {
		return ((this.getSize()) * slotHeight) + 1;
	}

	ArrayList<IAbilityInterface> getAbilities() {
		return abilities;
	}

	@Override
	protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
		if (idx >= abilities.size()) {
			return;
		}
		final IAbilityInterface mc = abilities.get(idx);
		final String name = mc == null ? "ERROR" : mc.getDisplayName();
		final FontRenderer font = parent.getFontRenderer();
		font.drawString(font.trimStringToWidth(name, listWidth - 10), left + 3, top + 2, 0xFFFFFF);
	}

}
