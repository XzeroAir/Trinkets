package xzeroair.trinkets.client.races.taurus;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.client.races.dwarf.RaceDwarfRenderer;
import xzeroair.trinkets.races.taurus.RaceTaurus;

public class RaceTaurusRenderer extends RaceDefaultRenderer<RaceDwarfRenderer, RaceTaurus> {
    public RaceTaurusRenderer(EntityLivingBase entity, RaceTaurus raceTaurus) {
        super(entity, raceTaurus);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!this.getHandler().showTraits()) {
            return;
        }
        if (this.getHandler().getTraitVariant() == 0) {
            RaceTaurusFemaleHorns.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitVariant(), this.getHandler().getColorOption() == 1 ? this.getHandler().getSecondaryTraitColor() : this.getHandler().getPrimaryTraitColor(), this.getHandler().getColorOption() > 0 ? this.getHandler().getPrimaryTraitColor() : this.getHandler().getSecondaryTraitColor(), this.getHandler().getTraitAuxColor());
        } else if (this.getHandler().getTraitVariant() == 1) {
            RaceTaurusHorns.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitVariant(), this.getHandler().getColorOption() == 1 ? this.getHandler().getSecondaryTraitColor() : this.getHandler().getPrimaryTraitColor(), this.getHandler().getColorOption() > 0 ? this.getHandler().getPrimaryTraitColor() : this.getHandler().getSecondaryTraitColor(), this.getHandler().getTraitAuxColor());
        }
        if (this.getHandler().getTraitAuxVariant() == 0) {
            RaceTaurusBell.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitAuxVariant(), this.getHandler().getTraitAuxColor());
        }
    }

}
