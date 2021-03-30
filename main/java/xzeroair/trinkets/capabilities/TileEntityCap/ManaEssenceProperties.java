package xzeroair.trinkets.capabilities.TileEntityCap;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.util.TrinketsConfig;

public class ManaEssenceProperties {

	TileEntity te;
	World world;
	int essence = TrinketsConfig.SERVER.mana.essence_amount;
	int counter = 0;
	boolean skip = false;

	public ManaEssenceProperties(TileEntity te, World world) {
		this.te = te;
		this.world = world;
	}

	public void onUpdate() {
		BlockPos pos1 = te.getPos().add(-10, -10, -10);
		BlockPos pos2 = te.getPos().add(10, 10, 10);

		Iterable<BlockPos> blockList = BlockPos.getAllInBox(pos1, pos2);
		blockList.forEach(block -> {
			if (!block.equals(te.getPos())) {
				if ((te.getWorld().getTileEntity(block) != null)) {
					if ((te.getWorld().getTileEntity(block) instanceof TileEntityMoonRose)) {
						skip = true;
					}
				}
			}
		});
		if (skip) {
			skip = false;
			return;
		}
		if (!te.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(te.getPos()).grow(10)).isEmpty()) {
			List<EntityLivingBase> entities = te.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(te.getPos()).grow(4));
			for (EntityLivingBase e : entities) {
				if (e.hasCapability(Capabilities.ENTITY_RACE, null)) {
					EntityProperties prop = Capabilities.getEntityRace(e);
					if (prop != null) {
						if (e.isSneaking()) {
							counter++;
							if (counter >= TrinketsConfig.SERVER.mana.essence_cooldown) {
								int currentBonus = prop.getMagic().getBonusMana();
								prop.getMagic().setBonusMana(currentBonus + 1);
								prop.getMagic().sendManaToPlayer(e);
								prop.getMagic().syncToManaHud();
								essence--;
								counter = 0;
								te.markDirty();
							}
						}
					}
				}
			}
		}
		if (essence <= 0) {
			te.markDirty();
			te.getWorld().setBlockToAir(te.getPos());
		}
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		this.essence = essence;
	}

	public void saveToNBT(NBTTagCompound compound) {
		compound.setInteger("essence", essence);
		compound.setInteger("counter", counter);
	}

	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("essence")) {
			essence = compound.getInteger("essence");
		}
		if (compound.hasKey("counter")) {
			counter = compound.getInteger("counter");
		}
	}

}
