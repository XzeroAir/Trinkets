package xzeroair.trinkets.traits.abilities.interfaces;

import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAbilityInterface {

	String getName();

	String getTranslationKey();

	String getDisplayName();

	String getUUID();

	@SideOnly(Side.CLIENT)
	List<String> getDescription();

}
