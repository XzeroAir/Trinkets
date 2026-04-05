package xzeroair.trinkets.client.races.dwarf;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.races.IRenderModelInterface;

public class RaceDwarfBeard implements IRenderModelInterface {

    public static RaceDwarfBeard INSTANCE = new RaceDwarfBeard();

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale) {
        int variant = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitVariant());
        int colorPrimary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getColorOption() == 1 ? prop.getRaceHandler().getSecondaryTraitColor() : prop.getRaceHandler().getPrimaryTraitColor());
        int colorSecondary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getColorOption() > 0 ? prop.getRaceHandler().getPrimaryTraitColor() : prop.getRaceHandler().getSecondaryTraitColor());
        this.render(entity, renderer, isFake, isSlim, partialTicks, scale, variant, colorPrimary, colorSecondary);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary, int colorSecondary) {

    }
}
