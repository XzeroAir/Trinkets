package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.util.TrinketsRegistryNames;

public class AbilitySteal extends Ability {

    public AbilitySteal() {
        super(TrinketsRegistryNames.ModAbilities.STEAL);
    }

    public boolean stealWeapon(EntityLivingBase stealer, EntityLivingBase target, EntityEquipmentSlot slot) {
        if (slot.getSlotType() != EntityEquipmentSlot.Type.HAND) {
            return false;
        }
        return this.stealFromSlot(stealer, target, slot);
    }

    public boolean stealArmor(EntityLivingBase stealer, EntityLivingBase target, EntityEquipmentSlot slot) {
        if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) {
            return false;
        }
        return this.stealFromSlot(stealer, target, slot);
    }

    protected boolean stealFromSlot(EntityLivingBase stealer, EntityLivingBase target, EntityEquipmentSlot slot) {
        if ((stealer == target) || stealer.world.isRemote) {
            return false;
        }
        final ItemStack stack = target.getItemStackFromSlot(slot);
        if (stack.isEmpty()) {
            return false;
        }
        if (stealer instanceof EntityPlayer) {
            return this.giveToPlayer((EntityPlayer) stealer, target, slot, stack);
        }
        return this.equipStolenItem(stealer, target, slot, stack);
    }

    protected boolean giveToPlayer(EntityPlayer player, EntityLivingBase target, EntityEquipmentSlot slot, ItemStack stack) {
        if (!this.canReceive(player, stack)) {
            return false;
        }
        if (player.inventory.addItemStackToInventory(stack.copy())) {
            target.setItemStackToSlot(slot, ItemStack.EMPTY);
            return true;
        }
        return false;
    }

    protected boolean equipStolenItem(EntityLivingBase stealer, EntityLivingBase target, EntityEquipmentSlot slot, ItemStack stack) {
        final ItemStack equipped = stealer.getItemStackFromSlot(slot);
        if (!this.shouldEquip(slot, stack, equipped)) {
            return false;
        }
        if (!equipped.isEmpty() && (stealer.entityDropItem(equipped.copy(), 0F) == null)) {
            return false;
        }
        stealer.setItemStackToSlot(slot, stack.copy());
        target.setItemStackToSlot(slot, ItemStack.EMPTY);
        return true;
    }

    protected boolean shouldEquip(EntityEquipmentSlot slot, ItemStack candidate, ItemStack equipped) {
        if (equipped.isEmpty()) {
            return true;
        }
        if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            return this.getAttributeValue(candidate, slot, SharedMonsterAttributes.ARMOR.getName()) > this.getAttributeValue(equipped, slot, SharedMonsterAttributes.ARMOR.getName());
        }
        if (slot == EntityEquipmentSlot.MAINHAND) {
            return this.getAttributeValue(candidate, slot, SharedMonsterAttributes.ATTACK_DAMAGE.getName()) > this.getAttributeValue(equipped, slot, SharedMonsterAttributes.ATTACK_DAMAGE.getName());
        }
        return false;
    }

    protected double getAttributeValue(ItemStack stack, EntityEquipmentSlot slot, String attributeName) {
        double value = 0D;
        for (AttributeModifier modifier : stack.getAttributeModifiers(slot).get(attributeName)) {
            if (modifier.getOperation() == 0) {
                value += modifier.getAmount();
            }
        }
        return value;
    }

    protected boolean canReceive(EntityPlayer player, ItemStack stack) {
        if (player.inventory.getFirstEmptyStack() >= 0) {
            return true;
        }
        for (ItemStack inventoryStack : player.inventory.mainInventory) {
            if (!inventoryStack.isEmpty() && ItemStack.areItemsEqual(inventoryStack, stack) && ItemStack.areItemStackTagsEqual(inventoryStack, stack) && (inventoryStack.getCount() + stack.getCount() <= inventoryStack.getMaxStackSize())) {
                return true;
            }
        }
        return false;
    }
}
