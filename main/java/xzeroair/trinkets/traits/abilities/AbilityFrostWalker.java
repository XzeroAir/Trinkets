package xzeroair.trinkets.traits.abilities;

import com.google.common.base.Objects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityFrostWalker extends Ability implements ITickableAbility, IAttackAbility {

    public AbilityFrostWalker() {
        super(Abilities.frostWalker);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (source.damageType.contentEquals("hotFloor")) {
            return true;
        }
        return cancel;
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final World world = entity.getEntityWorld();
        if (!world.isRemote && !this.isSpectator(entity)) {
            if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.DEPTH_STRIDER, entity) > 0) {
                return;
            }
            final BlockPos prev = Capabilities.getEntityProperties(entity, entity.getPosition(), (prop, pos) -> prop.getPrevBlockpos());
            if (!Objects.equal(prev, entity.getPosition())) {
                int lvl = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, entity);
//                EnchantmentFrostWalker.freezeNearby(entity, world, entity.getPosition(), lvl + 1);
                if (entity.onGround) {
                    BlockHelperUtil.freezeWater(world, entity.posX, entity.posY, entity.posZ, lvl + 1, 1.0D);
                    BlockHelperUtil.freezeLava(world, entity.posX, entity.posY, entity.posZ, lvl + 1, 1.0D);
                }
            }
        }
    }

}
