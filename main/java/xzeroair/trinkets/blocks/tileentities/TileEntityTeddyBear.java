package xzeroair.trinkets.blocks.tileentities;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
                prop.getTag().setTag("TeddyBear", stack.writeToNBT(new NBTTagCompound()));
            });
        }
    }

    public ItemStack getTeddyBear() {
        return Capabilities.getTEProperties(this, this.stack, (prop, rtn) -> {
            if (rtn.isEmpty()) {
                NBTTagCompound tag = prop.getTag();
                if (tag.hasKey("TeddyBear")) {
                    NBTTagCompound stackTag = tag.getCompoundTag("TeddyBear");
                    return new ItemStack(stackTag);
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
        return this.facingRotation;
    }

    public void setRotation(int rotation) {
        this.facingRotation = rotation;
    }

    @Override
    public void mirror(@Nonnull Mirror mirrorIn) {
        if ((this.world != null) && (this.world.getBlockState(this.getPos()).getValue(BlockTeddyBear.FACING) == EnumFacing.UP)) {
            this.facingRotation = mirrorIn.mirrorRotation(this.facingRotation, 16);
        }
    }

    @Override
    public void rotate(@Nonnull Rotation rotationIn) {
        if ((this.world != null) && (this.world.getBlockState(this.getPos()).getValue(BlockTeddyBear.FACING) == EnumFacing.UP)) {
            this.facingRotation = rotationIn.rotate(this.facingRotation, 16);
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
        return new SPacketUpdateTileEntity(this.pos, 4, this.getUpdateTag());
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
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("Rot", (byte) (this.facingRotation & 255));
        if (!this.stack.isEmpty()) {
            NBTTagCompound stackTag = new NBTTagCompound();
            this.stack.writeToNBT(stackTag);
            compound.setTag("TeddyBear", stackTag);
        }
        return compound;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.facingRotation = compound.getByte("Rot");
        if (compound.hasKey("TeddyBear")) {
            NBTTagCompound stackTag = compound.getCompoundTag("TeddyBear");
            this.stack = new ItemStack(stackTag);
        }
    }
}
