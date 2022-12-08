package xzeroair.trinkets.capabilities.TileEntityCap;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;

public class TileEntityProperties extends CapabilityBase<TileEntityProperties, TileEntity> {

	protected boolean hasEssence = false;
	protected int essence = -1;

	public TileEntityProperties(TileEntity te) {
		super(te);
	}

	@Override
	public NBTTagCompound getTag() {
		NBTTagCompound teTag = object.getTileData();
		if (teTag != null) {
			final String capTag = Reference.MODID + ".TrinketTE";
			if (!teTag.hasKey(capTag)) {
				teTag.setTag(capTag, new NBTTagCompound());
			}
			tag = teTag.getCompoundTag(capTag);
		}
		return super.getTag();
	}

	public TileEntityProperties setHasEssence(boolean has) {
		return this.setHasEssence(has, TrinketsConfig.SERVER.mana.essence_amount);
	}

	public TileEntityProperties setHasEssence(boolean has, int essence) {
		hasEssence = has;
		this.essence = essence;
		return this;
	}

	@Override
	public void onUpdate() {
		if (hasEssence) {
			this.updateEssence();
		}
	}

	private void updateEssence() {
		final World world = object.getWorld();
		final BlockPos tePos = object.getPos();
		if (world == null) {
			return;
		}

		if (this.hasEssence()) {
			if ((this.getEssence() > 0)) {
				final int teEssence = this.getEssence();
				final BlockPos pos1 = object.getPos().add(-10, -10, -10);
				final BlockPos pos2 = object.getPos().add(10, 10, 10);

				final boolean skip = BlockHelperUtil.isBlockNearby(world, new AxisAlignedBB(pos1, pos2), (state, pos) -> {
					final Block block = state.getBlock();
					final TileEntity te = world.getTileEntity(pos);
					final boolean isSelf = tePos.equals(pos);
					if ((te == null) || isSelf || (te == object)) {
						return false;
					}
					return Capabilities.getTEProperties(te, false, (prop, matches) -> prop.hasEssence());
				});
				if (skip) {
					return;
				}
				final List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(object.getPos()).grow(4));
				if (!entities.isEmpty()) {
					for (final EntityLivingBase e : entities) {
						Capabilities.getMagicStats(
								e, prop -> {
									if (e.isSneaking()) {
										final Counter counter = this.getTickHandler().getCounter("absorb.cooldown", TrinketsConfig.SERVER.mana.essence_cooldown, false, true, true, false);
										if (counter.Tick()) {
											final int currentBonus = prop.getBonusMana();
											prop.setBonusMana(currentBonus + 1);
											this.setEssence(teEssence - 1);
										}
									}
								}
						);
					}
				}
				if (teEssence != this.getEssence()) {
					this.saveToNBT(this.getTag());
					object.markDirty();
				}
			}
			if (this.getEssence() <= 0) {
				object.getWorld().setBlockToAir(object.getPos());
			}
		}
	}

	public boolean hasEssence() {
		return hasEssence;
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		if (this.essence != essence) {
			this.essence = essence;
		}
	}

	@Override
	public NBTTagCompound saveToNBT(NBTTagCompound compound) {
		compound.setBoolean("provides", hasEssence);
		compound.setInteger("essence", essence);
		return compound;
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("provides")) {
			hasEssence = compound.getBoolean("provides");
		}
		if (compound.hasKey("essence")) {
			essence = compound.getInteger("essence");
		}
	}

}
