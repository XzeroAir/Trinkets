package xzeroair.trinkets.capabilities.TileEntityCap;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityTileEntityBase;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;

import java.util.List;
import java.util.Random;

public class TileEntityProperties extends CapabilityTileEntityBase<TileEntityProperties, TileEntity> {

    public static final String TAG_KEY = Reference.MODID + ":TrinketTE";

    protected boolean PROVIDES_ESSENCE = false;
    protected boolean HAS_ESSENCE = false;
    protected int ESSENCE = -1;

    public TileEntityProperties(TileEntity te) {
        super(te);
    }

    @Override
    public NBTTagCompound getTag() {
        NBTTagCompound tag = this.getTileEntity().getTileData();
        if (!tag.hasKey(TAG_KEY)) {
            tag.setTag(TAG_KEY, new NBTTagCompound());
        }
        return tag.getCompoundTag(TAG_KEY);
    }

    public TileEntityProperties setHasEssence(boolean provides) {
        return this.setHasEssence(provides, TrinketsConfig.SERVER.BLOCKS.MOON_ROSE.essence_amount);
    }

    public TileEntityProperties setHasEssence(boolean provides, int essence) {
        this.PROVIDES_ESSENCE = provides;
        if (provides) {
            this.ESSENCE = essence;
            if (essence > 0) {
                this.HAS_ESSENCE = true;
            }
        }
        return this;
    }

    @Override
    public void onUpdate() {
        if (this.providesEssence()) {
            if (this.hasEssence()) {
                this.updateEssence();
            } else {
                this.getTileEntity().getWorld().setBlockToAir(this.getTileEntity().getPos());
            }
        }
    }

    private void updateEssence() {
        final World world = this.getTileEntity().getWorld();
        final BlockPos tePos = this.getTileEntity().getPos();
        if ((world == null) || world.isRemote) {
            return;
        }

        final int teEssence = this.getEssence();
        final double range = 5.0;
        final double rangeY = 2.0;
        final BlockPos pos1 = this.getTileEntity().getPos().add(-range, -rangeY, -range);
        final BlockPos pos2 = this.getTileEntity().getPos().add(range, rangeY, range);

        final boolean skip = BlockHelperUtil.isBlockNearby(world, new AxisAlignedBB(pos1, pos2), (state, pos) -> {
            final Block block = state.getBlock();
            final TileEntity te = world.getTileEntity(pos);
            final boolean isSelf = tePos.equals(pos);
            if ((te == null) || isSelf || (te == this.getTileEntity())) {
                return false;
            }
            return Capabilities.getTEProperties(te, false, (prop, matches) -> prop.hasEssence());
        });
        if (skip) {
            return;
        }
        final List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getTileEntity().getPos()).grow(4));
        if (!entities.isEmpty()) {
            for (final EntityLivingBase e : entities) {
                Capabilities.getMagicStats(e, prop -> {
                    if (e.isSneaking()) {
                        final Counter counter = this.getTickHandler().getCounter("absorb.cooldown", TrinketsConfig.SERVER.BLOCKS.MOON_ROSE.essence_cooldown, false, true, true, false);
                        if (counter.Tick()) {
                            final double currentBonus = prop.getBonusMana();
                            double addedAmount = 1;
                            prop.setBonusMana(currentBonus + addedAmount);
                            this.setEssence(teEssence - 1);
                            Random rand = Reference.random;
                            world.playSound((EntityPlayer) null, tePos, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, SoundCategory.BLOCKS, 0.4F, (rand.nextFloat() * 0.6F) + 0.4F);
                        }
                    }
                });
            }
        }
        if (teEssence != this.getEssence()) {
            this.saveToNBT(this.getTag());
            this.getTileEntity().markDirty();
        }
    }

    public boolean providesEssence() {
        return this.PROVIDES_ESSENCE;
    }

    public boolean hasEssence() {
        return (this.getEssence() > 0);
    }

    public int getEssence() {
        return this.ESSENCE;
    }

    public void setEssence(int essence) {
        if (this.ESSENCE != essence) {
            this.ESSENCE = essence;
        }
    }

    @Override
    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        compound.setBoolean("provides", this.PROVIDES_ESSENCE);
        if (this.providesEssence()) {
            compound.setInteger("essence", this.ESSENCE);
        }
        return compound;
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("provides")) {
            this.PROVIDES_ESSENCE = compound.getBoolean("provides");
        }
        if (compound.hasKey("essence")) {
            this.ESSENCE = compound.getInteger("essence");
        }
    }

}
