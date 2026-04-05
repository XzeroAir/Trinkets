package xzeroair.trinkets.client.races.titan;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.races.titan.RaceTitan;

public class RaceTitanRenderer extends RaceDefaultRenderer<RaceTitanRenderer, RaceTitan> {

    public RaceTitanRenderer(EntityLivingBase entity, RaceTitan raceTitan) {
        super(entity, raceTitan);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
        super.doRenderPlayerPre(entity, x, y, z, renderer, partialTick);
        if (entity.limbSwingAmount > 0) {
            entity.limbSwingAmount -= 0.04F;
        }
    }
}
