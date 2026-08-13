package xzeroair.trinkets.client.races.titan;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.races.titan.RaceTitan;

public class RaceTitanRenderer extends RaceDefaultRenderer<RaceTitanRenderer, RaceTitan> {

    private static final float WALK_ANIMATION_SPEED = 0.55F;

    private float originalLimbSwing;
    private boolean modifiedLimbSwing;

    public RaceTitanRenderer(EntityLivingBase entity, RaceTitan raceTitan) {
        super(entity, raceTitan);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
        super.doRenderPlayerPre(entity, x, y, z, renderer, partialTick);
        if (entity.limbSwingAmount > 0F) {
            this.originalLimbSwing = entity.limbSwing;
            final float partialOffset = entity.limbSwingAmount * (1.0F - partialTick);
            final float renderedSwing = entity.limbSwing - partialOffset;
            entity.limbSwing = (renderedSwing * WALK_ANIMATION_SPEED) + partialOffset;
            this.modifiedLimbSwing = true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderPlayerPost(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
        if (this.modifiedLimbSwing) {
            entity.limbSwing = this.originalLimbSwing;
            this.modifiedLimbSwing = false;
        }
        super.doRenderPlayerPost(entity, x, y, z, renderer, partialTick);
    }
}
