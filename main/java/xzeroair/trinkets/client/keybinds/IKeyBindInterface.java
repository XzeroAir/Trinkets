package xzeroair.trinkets.client.keybinds;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IKeyBindInterface extends IAbilityHandler {

	@SideOnly(Side.CLIENT)
	public String getKey();

	@SideOnly(Side.CLIENT)
	public default String getAuxKey() {
		return null;
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
