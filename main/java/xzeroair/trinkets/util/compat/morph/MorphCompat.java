package xzeroair.trinkets.util.compat.morph;

import me.ichun.mods.morph.api.MorphApi;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

public class MorphCompat {

	public static EntityLivingBase isEntityMorph(EntityLivingBase entity, Side side){
		if((Loader.isModLoaded("morph"))) {
			return MorphApi.getApiImpl().getMorphEntity(entity.world, entity.getName(), side);
		}
		return null;
	}

	public static float morphProgress(EntityLivingBase entity, Side side){
		if((Loader.isModLoaded("morph"))) {
			return MorphApi.getApiImpl().morphProgress(entity.getName(), side);
		}
		return 0;
	}

	public static boolean CanMorph(EntityPlayer player){
		if((Loader.isModLoaded("morph"))) {
			return MorphApi.getApiImpl().canPlayerMorph(player);
		}
		return false;
	}

	public static void forceMorph(EntityPlayerMP player, EntityLivingBase targetMorph){
		if((Loader.isModLoaded("morph"))) {
			MorphApi.getApiImpl().forceMorph(player, targetMorph);
		}
	}

	public static void forceDemorph(EntityPlayerMP player){
		if((Loader.isModLoaded("morph"))) {
			MorphApi.getApiImpl().forceDemorph(player);
		}
	}
}
