package xzeroair.trinkets.capabilities.race;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.keybinds.KeybindPacket;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class KeybindHandler {

    public enum keyEnum {

        LEFT("Left"), RIGHT("Right"), FORWARD("Forward"), BACK("Backward"), JUMP("Jump"), SNEAK("Sneak"), NONE("None");

        private final String name;

        private keyEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static keyEnum byName(String name) {
            for (keyEnum key : values()) {
                if (key.getName().contentEquals(name)) {
                    return key;
                }
            }
            return NONE;
        }

        public static keyEnum byID(int value) {
            if ((value < 0) || (value >= values().length)) {
                value = 0;
            }
            return values()[value];
        }
    }

    private final EntityProperties parent;
    private final Map<String, KeyHandler> storage;

    public KeybindHandler(EntityProperties parent) {
        this.parent = parent;
        this.storage = new HashMap<>();
        this.addKeyBind(keyEnum.LEFT.getName());
        this.addKeyBind(keyEnum.RIGHT.getName());
        this.addKeyBind(keyEnum.FORWARD.getName());
        this.addKeyBind(keyEnum.BACK.getName());
        this.addKeyBind(keyEnum.JUMP.getName());
        this.addKeyBind(keyEnum.SNEAK.getName());
    }

    public void addKeyBind(String key) {
        if (!this.storage.containsKey(key)) {
            this.storage.put(key, new KeyHandler());
        }
    }

    public KeyHandler getKeyHandler(String key) {
        KeyHandler handler = this.storage.get(key);
        if (handler == null) {
            handler = new KeyHandler();
            this.storage.put(key, handler);
        }
        return handler;
    }

    /**
     * The event layer calls this once per client tick. Movement transitions are sampled before
     * abilities are visited, so one ability receives its primary and movement input coherently.
     */
    @SideOnly(Side.CLIENT)
    public void handleClientInput() {
        this.initializeMovementSnapshot();
        final Entity entity = this.parent.getObject();
        for (Entry<String, AbilityHolder> entry : this.parent.getAbilityHandler().getActiveAbilities().entrySet()) {
            final String abilityKey = entry.getKey();
            final IAbilityInterface ability = entry.getValue().getAbility();
            if (!(ability instanceof IKeyBindInterface)) {
                continue;
            }
            try {
                if (!this.handleClientAbilityInput(abilityKey, (IKeyBindInterface) ability, entity)) {
                    return;
                }
            } catch (final Exception e) {
                Trinkets.LOGGER.error("Trinkets had an Error with Ability:{}", abilityKey);
                e.printStackTrace();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void initializeMovementSnapshot() {
        for (final keyEnum key : keyEnum.values()) {
            if (key != keyEnum.NONE) {
                this.getKeyHandler(key.getName()).updateKeyState(this.isMovementKeyDown(key));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected boolean handleClientAbilityInput(String abilityKey, IKeyBindInterface keybind, Entity entity) {
        final String primaryKey = keybind.getKey().replace(" ", "");
        final String auxiliaryKey = keybind.getAuxKey().replace(" ", "");
        final boolean primaryDown = !primaryKey.isEmpty() && ModKeyBindings.isKeyDownFromName(primaryKey);
        final boolean auxiliaryDown = !auxiliaryKey.isEmpty() && ModKeyBindings.isKeyDownFromName(auxiliaryKey);
        final IMovementAbility movementAbility = keybind instanceof IMovementAbility ? (IMovementAbility) keybind : null;
        final KeyHandler primaryHandler = primaryKey.isEmpty() ? null : this.getKeyHandler(abilityKey + "." + primaryKey);
        final String handlerKey = primaryKey.isEmpty() ? "" : abilityKey + "." + primaryKey;

        if (primaryHandler != null) {
            primaryHandler.updateKeyState(primaryDown);
        }
        final int primaryState = primaryHandler == null ? -1 : primaryHandler.getState();
        NBTTagCompound movementStates = null;
        NBTTagCompound payload = null;
        if (movementAbility != null) {
            final int left = this.getKeyHandler(keyEnum.LEFT.getName()).getState();
            final int right = this.getKeyHandler(keyEnum.RIGHT.getName()).getState();
            final int forward = this.getKeyHandler(keyEnum.FORWARD.getName()).getState();
            final int back = this.getKeyHandler(keyEnum.BACK.getName()).getState();
            final int jump = this.getKeyHandler(keyEnum.JUMP.getName()).getState();
            final int sneak = this.getKeyHandler(keyEnum.SNEAK.getName()).getState();
            if ((left >= 0) || (right >= 0) || (forward >= 0) || (back >= 0) || (jump >= 0) || (sneak >= 0)) {
                payload = movementAbility.createMovementPayload(entity, primaryState, primaryDown, auxiliaryDown, left, right, forward, back, jump, sneak);
                if (movementAbility.onMovement(entity, primaryState, primaryDown, auxiliaryDown, left, right, forward, back, jump, sneak, payload)) {
                    movementStates = new NBTTagCompound();
                    this.addMovementState(movementStates, keyEnum.LEFT, left);
                    this.addMovementState(movementStates, keyEnum.RIGHT, right);
                    this.addMovementState(movementStates, keyEnum.FORWARD, forward);
                    this.addMovementState(movementStates, keyEnum.BACK, back);
                    this.addMovementState(movementStates, keyEnum.JUMP, jump);
                    this.addMovementState(movementStates, keyEnum.SNEAK, sneak);
                }
            }
        }
        final boolean primaryContinues = (primaryHandler == null) || (primaryState < 0) || primaryHandler.loadState(primaryState, keyHandler -> keybind.onKeyState(entity, keyHandler, auxiliaryDown));
        if (movementStates != null) {
            NetworkHandler.sendToServer(new KeybindPacket(abilityKey, handlerKey, primaryState, primaryDown, auxiliaryDown, movementStates, payload));
        } else if (primaryState >= 0) {
            NetworkHandler.sendToServer(new KeybindPacket(abilityKey, handlerKey, primaryState, auxiliaryDown));
        }
        return primaryContinues;
    }

    protected void addMovementState(NBTTagCompound movementStates, keyEnum key, int state) {
        if (state >= 0) {
            movementStates.setInteger(key.getName(), state);
        }
    }

    @SideOnly(Side.CLIENT)
    protected boolean isMovementKeyDown(keyEnum movementKey) {
        final GameSettings settings = Minecraft.getMinecraft().gameSettings;
        final KeyBinding binding;
        switch (movementKey) {
            case LEFT:
                binding = settings.keyBindLeft;
                break;
            case RIGHT:
                binding = settings.keyBindRight;
                break;
            case FORWARD:
                binding = settings.keyBindForward;
                break;
            case BACK:
                binding = settings.keyBindBack;
                break;
            case JUMP:
                binding = settings.keyBindJump;
                break;
            case SNEAK:
                binding = settings.keyBindSneak;
                break;
            default:
                return false;
        }
        return binding.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    private boolean isKeyDown(String key) {
        return (key != null) && !key.replace(" ", "").isEmpty() && ModKeyBindings.isKeyDownFromName(key.replace(" ", ""));
    }
}
