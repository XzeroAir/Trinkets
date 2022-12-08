package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

public interface IKeyBindInterface extends IAbilityInterface {

	@SideOnly(Side.CLIENT)
	public String getKey();

	@SideOnly(Side.CLIENT)
	public default String getAuxKey() {
		return "";
	}

	public default boolean onKeyPress(Entity entity, boolean Aux) {
		return true;
	}

	public default boolean onKeyDown(Entity entity, boolean Aux) {
		return true;
	}

	public default boolean onKeyRelease(Entity entity, boolean Aux) {
		return true;
	}

}
