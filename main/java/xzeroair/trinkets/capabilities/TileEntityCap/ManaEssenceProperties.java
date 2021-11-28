package xzeroair.trinkets.capabilities.TileEntityCap;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.util.TrinketsConfig;

public class ManaEssenceProperties extends CapabilityBase<ManaEssenceProperties, TileEntity> {

	World world;
	int essence = TrinketsConfig.SERVER.mana.essence_amount;
	int counter = 0;
	boolean skip = false;

	public ManaEssenceProperties(TileEntity te, World world) {
		super(te);
		this.world = world;
	}

	@Override
	public void onUpdate() {
		final BlockPos pos1 = object.getPos().add(-10, -10, -10);
		final BlockPos pos2 = object.getPos().add(10, 10, 10);

		final Iterable<BlockPos> blockList = BlockPos.getAllInBox(pos1, pos2);
		blockList.forEach(block -> {
			if (!block.equals(object.getPos())) {
				if ((object.getWorld().getTileEntity(block) != null)) {
					if ((object.getWorld().getTileEntity(block) instanceof TileEntityMoonRose)) {
						skip = true;
					}
				}
			}
		});
		if (skip) {
			skip = false;
			return;
		}
		if (!object.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(object.getPos()).grow(10)).isEmpty()) {
			final List<EntityLivingBase> entities = object.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(object.getPos()).grow(4));
			for (final EntityLivingBase e : entities) {
				final MagicStats prop = Capabilities.getMagicStats(e);
				if (prop != null) {
					if (e.isSneaking()) {
						counter++;
						if (counter >= TrinketsConfig.SERVER.mana.essence_cooldown) {
							final int currentBonus = prop.getBonusMana();
							prop.setBonusMana(currentBonus + 1);
							essence--;
							counter = 0;
							object.markDirty();
						}
					}
				}
			}
		}
		if (essence <= 0) {
			object.markDirty();
			object.getWorld().setBlockToAir(object.getPos());
		}
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		this.essence = essence;
	}

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		compound.setInteger("essence", essence);
		compound.setInteger("counter", counter);
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("essence")) {
			essence = compound.getInteger("essence");
		}
		if (compound.hasKey("counter")) {
			counter = compound.getInteger("counter");
		}
	}

}
