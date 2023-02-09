package xzeroair.trinkets.blocks.tileentities;

import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.blocks.BlockTeddyBear;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.TileEntityCap.TileEntityProperties;

public class TileEntityTeddyBear extends TileEntity implements ITickable {

	protected int facingRotation;
	protected ItemStack stack = ItemStack.EMPTY;

	public TileEntityTeddyBear() {
	}

	@Override
	public void update() {
	}

	public void setTeddyBear(ItemStack stack) {
		if ((stack != null) && !stack.isEmpty()) {
			this.stack = stack;
			Capabilities.getTEProperties(this, prop -> {
				NBTTagCompound tag = prop.getTag();
				NBTTagCompound stackTag = new NBTTagCompound();
				stack.writeToNBT(stackTag);
				tag.setTag("TeddyBear", stackTag);
			});
		}
	}

	public ItemStack getTeddyBear() {
		return Capabilities.getTEProperties(this, stack, (prop, rtn) -> {
			if (rtn.isEmpty()) {
				NBTTagCompound tag = prop.getTag();
				if (tag.hasKey("TeddyBear")) {
					NBTTagCompound stackTag = tag.getCompoundTag("TeddyBear");
					ItemStack stack = new ItemStack(stackTag);
					return stack;
				}
			}
			return rtn;
		});
	}

	public TileEntityProperties getProperties() {
		return Capabilities.getTEProperties(this);
	}

	@SideOnly(Side.CLIENT)
	public int getRotation() {
		return facingRotation;
	}

	public void setRotation(int rotation) {
		facingRotation = rotation;
	}

	@Override
	public void mirror(Mirror mirrorIn) {
		if ((world != null) && (world.getBlockState(this.getPos()).getValue(BlockTeddyBear.FACING) == EnumFacing.UP)) {
			facingRotation = mirrorIn.mirrorRotation(facingRotation, 16);
		}
	}

	@Override
	public void rotate(Rotation rotationIn) {
		if ((world != null) && (world.getBlockState(this.getPos()).getValue(BlockTeddyBear.FACING) == EnumFacing.UP)) {
			facingRotation = rotationIn.rotate(facingRotation, 16);
		}
	}

	/**
	 * Retrieves packet to send to the client whenever this Tile Entity is resynced
	 * via World.notifyBlockUpdate. For modded TE's, this packet comes back to you
	 * clientside in {@link #onDataPacket}
	 */
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 4, this.getUpdateTag());
	}

	/**
	 * Get an NBT compound to sync to the client with SPacketChunkData, used for
	 * initial loading of the chunk or when many blocks change at once. This
	 * compound comes back to you clientside in {@link handleUpdateTag}
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setByte("Rot", (byte) (facingRotation & 255));
		if (!stack.isEmpty()) {
			NBTTagCompound stackTag = new NBTTagCompound();
			stack.writeToNBT(stackTag);
			compound.setTag("TeddyBear", stackTag);
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		facingRotation = compound.getByte("Rot");
		if (compound.hasKey("TeddyBear")) {
			NBTTagCompound stackTag = compound.getCompoundTag("TeddyBear");
			stack = new ItemStack(stackTag);
		}
	}
}
