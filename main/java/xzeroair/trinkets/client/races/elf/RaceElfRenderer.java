package xzeroair.trinkets.client.races.elf;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.races.elf.RaceElf;

public class RaceElfRenderer extends RaceDefaultRenderer<RaceElfRenderer, RaceElf> {


    public RaceElfRenderer(EntityLivingBase entity, RaceElf raceElf) {
        super(entity, raceElf);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!this.getHandler().showTraits()) {
            return;
        }
        RaceElfEars.INSTANCE.render(entity, renderer, isFake, isSlim, partialTicks, scale, this.getHandler().getTraitVariant(), this.getHandler().getColorOption() == 1 ? this.getHandler().getSecondaryTraitColor() : this.getHandler().getPrimaryTraitColor(), this.getHandler().getColorOption() > 0 ? this.getHandler().getPrimaryTraitColor() : this.getHandler().getSecondaryTraitColor());
    }
}
