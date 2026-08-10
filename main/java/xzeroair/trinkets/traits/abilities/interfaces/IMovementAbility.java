package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IMovementAbility extends IKeyBindInterface {

    @Override
    @SideOnly(Side.CLIENT)
    public default String getKey() {
        return "";
    }

    @SideOnly(Side.CLIENT)
    public default NBTTagCompound createMovementPayload(Entity entity, int primaryState, boolean primaryDown, boolean auxiliaryDown, int left, int right, int forward, int back, int jump, int sneak) {
        return null;
    }

    /**
     * Receives one coherent movement-input snapshot. Override this for compound movement gestures;
     * the default preserves the individual directional callbacks for ordinary abilities.
     * On the client, false omits this movement snapshot from the packet; it never cancels key handling.
     */
    public default boolean onMovement(Entity entity, int primaryState, boolean primaryDown, boolean auxiliaryDown, int left, int right, int forward, int back, int jump, int sneak, @Nullable NBTTagCompound payload) {
        if ((left >= 0) && !this.left(entity, left, primaryState, primaryDown, auxiliaryDown, payload)) {
            return false;
        }
        if ((right >= 0) && !this.right(entity, right, primaryState, primaryDown, auxiliaryDown, payload)) {
            return false;
        }
        if ((forward >= 0) && !this.forward(entity, forward, primaryState, primaryDown, auxiliaryDown, payload)) {
            return false;
        }
        if ((back >= 0) && !this.back(entity, back, primaryState, primaryDown, auxiliaryDown, payload)) {
            return false;
        }
        if ((jump >= 0) && !this.jump(entity, jump, primaryState, primaryDown, auxiliaryDown, payload)) {
            return false;
        }
        return (sneak < 0) || this.sneak(entity, sneak, primaryState, primaryDown, auxiliaryDown, payload);
    }

    public default boolean left(Entity entity, int state, int primaryState, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        return false;
    }

    public default boolean right(Entity entity, int state, int primaryState, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        return false;
    }

    public default boolean forward(Entity entity, int state, int primaryState, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        return false;
    }

    public default boolean back(Entity entity, int state, int primaryState, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        return false;
    }

    public default boolean jump(Entity entity, int state, int primaryState, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        return false;
    }

    public default boolean sneak(Entity entity, int state, int primaryState, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        return false;
    }
}
