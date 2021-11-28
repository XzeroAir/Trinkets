package xzeroair.trinkets.util.compat.firstaid;

import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Optional.Method;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IFirstAidAbility extends IAbilityHandler {

	@Method(modid = "firstaid")
	boolean firstAidHit(EntityLivingBase entity, DamageSource source, float undistributedDmg, boolean cancel, AbstractPlayerDamageModel before, AbstractPlayerDamageModel after);

}
