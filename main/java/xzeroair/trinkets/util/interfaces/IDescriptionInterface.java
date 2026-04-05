package xzeroair.trinkets.util.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IDescriptionInterface {

    String getDisplayName();

    @SideOnly(Side.CLIENT)
    default void getDescription(List<String> tooltips, int rendMod, int rendID) {

    }

}
