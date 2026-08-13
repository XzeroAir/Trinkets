package xzeroair.trinkets.traits.abilities.elements.ice;

import com.google.common.base.Objects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityFrostWalker;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;

import javax.annotation.Nonnull;

public class AbilityFrostWalker extends Ability implements ITickableAbility, IAttackAbility {

    protected static final String MINECRAFT_HOTFLOOR = DamageSource.HOT_FLOOR.damageType;
    protected final ConfigAbilityFrostWalker CONFIG;

    public AbilityFrostWalker() {
        this(TrinketsConfig.SERVER.ABILITIES.FROST_WALKER);
    }

    public AbilityFrostWalker(ConfigAbilityFrostWalker config) {
        super(TrinketsRegistryNames.ModAbilities.FROST_WALKER);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, @Nonnull DamageSource source, float dmg, boolean cancel) {
        if (source.damageType.contentEquals(MINECRAFT_HOTFLOOR)) {
            return true;
        }
        return cancel;
    }

    @Override
    public void tickAbility(@Nonnull EntityLivingBase entity) {
        final World world = entity.getEntityWorld();
        if (!world.isRemote && !this.isSpectator(entity)) {
            if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.DEPTH_STRIDER, entity) > 0) {
                return;
            }
            if (entity instanceof EntityPlayer) {
                final BlockPos prev = Capabilities.getEntityProperties(entity, entity.getPosition(), (prop, pos) -> prop.getPrevBlockpos());
                if (!Objects.equal(prev, entity.getPosition())) {
                    int lvl = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, entity);
//                EnchantmentFrostWalker.freezeNearby(entity, world, entity.getPosition(), lvl + 1);
                    if (this.getAbilityHolder().getHandler().getParentProperties().isGrounded()) {
                        BlockHelperUtil.freezeWater(world, entity.posX, entity.posY, entity.posZ, lvl + 1, 1.0D);
                        BlockHelperUtil.freezeLava(world, entity.posX, entity.posY, entity.posZ, lvl + 1, 1.0D);
                    }
                }
            }
        }
    }

}
