package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySkilledSwimmer;
import xzeroair.trinkets.util.helpers.SwimmingMotionHelper;
import xzeroair.trinkets.util.compat.SwimmingSizeCompat;

public class AbilitySkilledSwimmer extends Ability implements ITickableAbility, IMiningAbility {

    protected ConfigAbilitySkilledSwimmer CONFIG;

    public AbilitySkilledSwimmer() {
        this(TrinketsConfig.SERVER.ABILITIES.SKILLED_SWIMMER);
    }

    public AbilitySkilledSwimmer(ConfigAbilitySkilledSwimmer config) {
        super(TrinketsRegistryNames.ModAbilities.SKILLED_SWIMMER);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (SwimmingSizeCompat.hasExternalSwimmingMod() || this.isSpectator(entity) || this.isCreativeFlying(entity)) {
            return;
        }
        final BlockPos bodyPosition = entity.getPosition();
        final IBlockState bodyBlock = entity.world.getBlockState(bodyPosition);
        final Block block = bodyBlock.getBlock();
        if ((entity.isInWater() || entity.isInLava()) && (block != Blocks.AIR)) {
            final EntityProperties properties = this.getAbilityHolder().getHandler().getParentProperties();
            final boolean globalSwimmingEnabled = entity.world.isRemote ? TrinketsConfig.getClientStore().SWIMMING : TrinketsConfig.SERVER.MISC.SWIMMING;
            if (globalSwimmingEnabled) {
                if (!properties.getRaceHandler().isSwimming()) {
                    entity.motionY += SwimmingMotionHelper.BUOYANCY;
                }
            } else {
                SwimmingMotionHelper.apply(entity, properties.getKeybindHandler(), this.CONFIG.OLD_TWEAKS);
            }
        }
    }
}
