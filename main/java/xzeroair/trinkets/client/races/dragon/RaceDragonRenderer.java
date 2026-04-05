package xzeroair.trinkets.client.races.dragon;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.client.races.generic.RaceHorns;
import xzeroair.trinkets.client.races.generic.RaceHornsInverted;
import xzeroair.trinkets.races.dragon.RaceDragon;

public class RaceDragonRenderer extends RaceDefaultRenderer<RaceDragonRenderer, RaceDragon> {

    public RaceDragonRenderer(EntityLivingBase entity, RaceDragon raceDragon) {
        super(entity, raceDragon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!this.getHandler().showTraits()) {
            return;
        }
        RaceDragonWings.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitVariant(), this.getHandler().getColorOption() == 1 ? this.getHandler().getSecondaryTraitColor() : this.getHandler().getPrimaryTraitColor(), this.getHandler().getColorOption() > 0 ? this.getHandler().getPrimaryTraitColor() : this.getHandler().getSecondaryTraitColor(), this.getHandler().getTraitAuxColor());
        if (this.getHandler().getTraitAuxVariant() == 0) {
            RaceHornsInverted.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitAuxVariant(), this.getHandler().getTraitAuxColor());
        } else if (this.getHandler().getTraitAuxVariant() == 1) {
            RaceHorns.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitAuxVariant(), this.getHandler().getTraitAuxColor());
        } else if (this.getHandler().getTraitAuxVariant() == 2) {
            RaceDragonHorns.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitAuxVariant(), this.getHandler().getTraitAuxColor());
        }
    }
}
