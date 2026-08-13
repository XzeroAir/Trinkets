package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.elements.Element;

import javax.annotation.Nullable;
import java.util.List;

public interface IAbilityInterface {

    boolean isAbilityEnabled();

    IAbilityInterface setAbilityEnabled(boolean enabled);

    boolean isFirstUpdate();

    IAbilityInterface setFirstUpdate(boolean firstUpdate);

    ResourceLocation getRegistryName();

    String getDisplayName();

    String getTranslationKey();

    String getUUID();

    @SideOnly(Side.CLIENT)
    void getDescription(List<String> tooltips, int rendModifier, int rendID);

    /**
     * called when the ability is added to an entity
     */
    default void onAbilityAdded(EntityLivingBase entity) {

    }

    /**
     * Called when the ability is removed from the entity
     */
    default void onAbilityRemoved(EntityLivingBase entity) {

    }

    IAbilityInterface setRequiredElement(Element requiredElement);

    @Nullable
    Element getRequiredElement();

    default boolean shouldRemove() {
        return false;
    }

    boolean hasChanged();

    IAbilityInterface setChanged(boolean bool);

    default IAbilityInterface scheduleRemoval() {
        return this;
    }

    default AbilityHolder getAbilityHolder() {
        return null;
    }

    default IAbilityInterface cacheAbilityHolder(AbilityHolder holder) {
        return this;
    }

    default void loadStorage(NBTTagCompound compound) {
    }

    default NBTTagCompound saveStorage(NBTTagCompound compound) {
        return compound;
    }

    /**
     * Used to send Temporary Ability Data on change
     *
     * @return
     */
    @Nullable
    NBTTagCompound sendAbilityData();

    /**
     * Used to load Temporary Ability Data on change
     *
     * @param tag
     */
    default void loadDataCache(NBTTagCompound tag) {

    }

}
