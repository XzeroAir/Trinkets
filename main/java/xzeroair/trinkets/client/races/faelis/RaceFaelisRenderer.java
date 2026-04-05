package xzeroair.trinkets.client.races.faelis;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.races.faelis.RaceFaelis;

public class RaceFaelisRenderer extends RaceDefaultRenderer<RaceFaelisRenderer, RaceFaelis> {


    public RaceFaelisRenderer(EntityLivingBase entity, RaceFaelis raceFaelis) {
        super(entity, raceFaelis);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!this.getHandler().showTraits()) {
            return;
        }
        RaceFaelisEars.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitVariant(), this.getHandler().getColorOption() == 1 ? this.getHandler().getSecondaryTraitColor() : this.getHandler().getPrimaryTraitColor(), this.getHandler().getColorOption() > 0 ? this.getHandler().getPrimaryTraitColor() : this.getHandler().getSecondaryTraitColor());
        RaceFaelisClaws.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitVariant(), this.getHandler().getPrimaryTraitColor(), this.getHandler().getSecondaryTraitColor());
        if (this.getHandler().getTraitAuxVariant() == 0) {
            RaceFaelisTail.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitAuxVariant(), this.getHandler().getColorOption() == 1 ? this.getHandler().getSecondaryTraitColor() : this.getHandler().getColorOption() > 1 ? this.getHandler().getPrimaryTraitColor() : this.getHandler().getTraitAuxColor(), this.getHandler().getColorOption() > 0 ? this.getHandler().getPrimaryTraitColor() : this.getHandler().getSecondaryTraitColor());
        }
    }
}
