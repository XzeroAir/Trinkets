package xzeroair.trinkets.network.keybinds;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.KeybindHandler;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.ThreadSafePacket;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;

import javax.annotation.Nullable;

public class KeybindPacket extends ThreadSafePacket {

    public static final String HANDLER_KEY_TAG = "HandlerKey";
    public static final String FORCE_RELEASE_TAG = "ForceRelease";
    public static final String PRIMARY_STATE_TAG = "PrimaryState";
    public static final String MOVEMENT_STATES_TAG = "MovementStates";
    public static final String PRIMARY_DOWN_TAG = "PrimaryDown";
    public static final String AUX_DOWN_TAG = "AuxDown";
    public static final String PAYLOAD_TAG = "Payload";

    private String ability = "";
    private NBTTagCompound input;

    public KeybindPacket() {
    }

    public KeybindPacket(String ability, String handlerKey, int primaryState, boolean auxiliaryDown) {
        this.ability = ability == null ? "" : ability;
        this.input = new NBTTagCompound();
        this.input.setString(HANDLER_KEY_TAG, handlerKey == null ? "" : handlerKey);
        this.input.setInteger(PRIMARY_STATE_TAG, primaryState);
        this.input.setBoolean(AUX_DOWN_TAG, auxiliaryDown);
    }

    public KeybindPacket(String ability, String handlerKey, EntityLivingBase entity) {
        this.ability = ability == null ? "" : ability;
        this.input = new NBTTagCompound();
        this.entityID = entity.getEntityId();
        this.input.setString(HANDLER_KEY_TAG, handlerKey == null ? "" : handlerKey);
        this.input.setBoolean(FORCE_RELEASE_TAG, true);
    }

    public KeybindPacket(String ability, String handlerKey, int primaryState, boolean primaryDown, boolean auxiliaryDown, NBTTagCompound movementStates, @Nullable NBTTagCompound payload) {
        this.ability = ability == null ? "" : ability;
        this.input = new NBTTagCompound();
        this.input.setString(HANDLER_KEY_TAG, handlerKey == null ? "" : handlerKey);
        if (primaryState >= 0) {
            this.input.setInteger(PRIMARY_STATE_TAG, primaryState);
        }
        this.input.setBoolean(PRIMARY_DOWN_TAG, primaryDown);
        this.input.setBoolean(AUX_DOWN_TAG, auxiliaryDown);
        this.input.setTag(MOVEMENT_STATES_TAG, movementStates);
        if (payload != null) {
            this.input.setTag(PAYLOAD_TAG, payload);
        }
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, this.ability);
        buffer.writeInt(this.entityID);
        ByteBufUtils.writeTag(buffer, this.input);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.ability = ByteBufUtils.readUTF8String(buffer);
        this.entityID = buffer.readInt();
        this.input = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        if ((this.input == null) || !this.input.getBoolean(FORCE_RELEASE_TAG) || !this.input.hasKey(HANDLER_KEY_TAG, 8)) {
            return;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            return;
        }
        final World world = mc.player.getEntityWorld();
        final Entity entity = world.getEntityByID(this.entityID);
        if (entity instanceof EntityLivingBase) {
            Capabilities.getEntityProperties(entity, prop -> prop.getKeybindHandler().getKeyHandler(this.input.getString(HANDLER_KEY_TAG)).forceRelease());
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        if (!this.isValidServerInput()) {
            return;
        }
        final boolean hasPrimaryState = this.input.hasKey(PRIMARY_STATE_TAG, 99);
        final boolean hasMovementState = this.input.hasKey(MOVEMENT_STATES_TAG, 10);
        final boolean primaryDown = hasMovementState && this.input.getBoolean(PRIMARY_DOWN_TAG);
        final boolean auxiliaryDown = this.input.getBoolean(AUX_DOWN_TAG);
        final String handlerKey = this.input.getString(HANDLER_KEY_TAG);
        final @Nullable NBTTagCompound payload = this.input.hasKey(PAYLOAD_TAG, 10) ? this.input.getCompoundTag(PAYLOAD_TAG) : null;
        final EntityLivingBase entity = server.player;

        Capabilities.getEntityProperties(entity, prop -> {
            try {
                final IAbilityInterface ability = prop.getAbilityHandler().getAbility(this.ability);
                if ((hasMovementState && !(ability instanceof IMovementAbility)) || (hasPrimaryState && !(ability instanceof IKeyBindInterface))) {
                    return;
                }
                if (hasMovementState) {
                    this.handleMovementInput((IMovementAbility) ability, entity, primaryDown, auxiliaryDown, payload);
                }
                if (hasPrimaryState) {
                    this.handlePrimaryInput((IKeyBindInterface) ability, prop.getObject(), prop.getKeybindHandler().getKeyHandler(handlerKey), this.input.getInteger(PRIMARY_STATE_TAG), auxiliaryDown, server);
                }
            } catch (final Exception e) {
                Trinkets.LOGGER.error("Trinkets had an Error with Ability:{}", this.ability);
                e.printStackTrace();
            }
        });
    }

    protected boolean isValidServerInput() {
        if ((this.input == null) || this.ability.isEmpty() || !this.input.hasKey(AUX_DOWN_TAG, 1)) {
            return false;
        }
        final boolean hasPrimaryState = this.input.hasKey(PRIMARY_STATE_TAG, 99);
        final boolean hasMovementState = this.input.hasKey(MOVEMENT_STATES_TAG, 10);
        if ((!hasPrimaryState && !hasMovementState) || (this.input.hasKey(PRIMARY_STATE_TAG) && !hasPrimaryState) || (this.input.hasKey(MOVEMENT_STATES_TAG) && !hasMovementState)) {
            return false;
        }
        if ((hasPrimaryState && (!this.input.hasKey(HANDLER_KEY_TAG, 8) || this.input.getString(HANDLER_KEY_TAG).isEmpty())) || (hasPrimaryState && !this.isValidState(this.input.getInteger(PRIMARY_STATE_TAG)))) {
            return false;
        }
        if (hasMovementState && (!this.input.hasKey(PRIMARY_DOWN_TAG, 1) || !this.hasConsistentPrimaryState(hasPrimaryState) || !this.isValidMovementInput(this.input.getCompoundTag(MOVEMENT_STATES_TAG)))) {
            return false;
        }
        return !this.input.hasKey(PAYLOAD_TAG) || this.input.hasKey(PAYLOAD_TAG, 10);
    }

    protected boolean hasConsistentPrimaryState(boolean hasPrimaryState) {
        return !hasPrimaryState || ((this.input.getInteger(PRIMARY_STATE_TAG) < 2) == this.input.getBoolean(PRIMARY_DOWN_TAG));
    }

    protected boolean isValidMovementInput(NBTTagCompound movementStates) {
        if (movementStates.getKeySet().isEmpty()) {
            return false;
        }
        for (final String key : movementStates.getKeySet()) {
            if ((KeybindHandler.keyEnum.byName(key) == KeybindHandler.keyEnum.NONE) || !movementStates.hasKey(key, 99) || !this.isValidState(movementStates.getInteger(key))) {
                return false;
            }
        }
        return true;
    }

    protected void handleMovementInput(IMovementAbility ability, EntityLivingBase entity, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        final NBTTagCompound movementStates = this.input.getCompoundTag(MOVEMENT_STATES_TAG);
        final int primaryState = this.input.hasKey(PRIMARY_STATE_TAG, 99) ? this.input.getInteger(PRIMARY_STATE_TAG) : -1;
        ability.onMovement(entity, primaryState, primaryDown, auxiliaryDown,
                this.getMovementState(movementStates, KeybindHandler.keyEnum.LEFT),
                this.getMovementState(movementStates, KeybindHandler.keyEnum.RIGHT),
                this.getMovementState(movementStates, KeybindHandler.keyEnum.FORWARD),
                this.getMovementState(movementStates, KeybindHandler.keyEnum.BACK),
                this.getMovementState(movementStates, KeybindHandler.keyEnum.JUMP),
                this.getMovementState(movementStates, KeybindHandler.keyEnum.SNEAK), payload);
    }

    protected void handlePrimaryInput(IKeyBindInterface ability, EntityLivingBase entity, KeyHandler keyHandler, int state, boolean auxiliaryDown, NetHandlerPlayServer server) {
        if (!keyHandler.loadState(state, handler -> ability.onKeyState(entity, handler, auxiliaryDown))) {
            NetworkHandler.sendTo(new KeybindPacket(this.ability, this.input.getString(HANDLER_KEY_TAG), entity), (EntityPlayerMP) server.player);
        }
    }

    protected boolean isValidState(int state) {
        return (state >= 0) && (state <= 2);
    }

    protected int getMovementState(NBTTagCompound movementStates, KeybindHandler.keyEnum key) {
        return movementStates.hasKey(key.getName(), 99) ? movementStates.getInteger(key.getName()) : -1;
    }
}
