package xzeroair.trinkets.items.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.traits.abilities.base.ItemAbilityProvider;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityParasitesImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityThirstImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.Utils;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AccessoryBase extends ItemBase implements IAccessoryInterface, ItemAbilityProvider, IAttributeProvider, IResistanceProvider, IImmunityProvider, IEffectsProvider {

    protected UUID uuid;

    public AccessoryBase(String modid, String name) {
        super(modid, name);
        this.setMaxStackSize(1);
    }

    public AccessoryBase(String name) {
        super(name);
        this.setMaxStackSize(1);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    protected void setUUID(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    @Override
    public void onAccessoryEquipped(ItemStack stack, @Nonnull EntityLivingBase entity) {
        final boolean isClient = entity.world.isRemote;
        Capabilities.getTrinketProperties(stack, cap -> {
            if (!isClient) {
                cap.itemEquipped(entity);
            } else {
                entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
            }
        });
    }

    @Override
    public void onAccessoryUnequipped(ItemStack stack, @Nonnull EntityLivingBase entity) {
        final boolean isClient = entity.world.isRemote;
        Capabilities.getTrinketProperties(stack, cap -> {
            if (!isClient) {
                cap.itemUnequipped(entity);
            } else {
                entity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
            }
        });
        AttributeHelper.removeAttributesByUUID(entity, this.getUUID());
    }

    @Override
    public boolean canUnequipAccessory(ItemStack stack, EntityLivingBase player) {
        if ((player instanceof EntityPlayer) && ((EntityPlayer) player).capabilities.isCreativeMode) {
            return true;
        }
        return !EnchantmentHelper.hasBindingCurse(stack);
    }

    @Override
    public boolean canEquipAccessory(ItemStack stack, EntityLivingBase player) {
        return !TrinketHelper.AccessoryCheck(player, this);
    }

    @Override
    public void onCreated(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityPlayer player) {
        super.onCreated(stack, world, player);
        Capabilities.getTrinketProperties(stack, cap -> cap.onCrafted(world, player));
    }

    @Override
    public boolean hasCustomEntity(@Nonnull ItemStack stack) {
        return super.hasCustomEntity(stack);
    }

    @Override
    public Entity createEntity(@Nonnull World world, @Nonnull Entity location, @Nonnull ItemStack itemstack) {
        return super.createEntity(world, location, itemstack);
    }

    @Override
    public boolean isShield(@Nonnull ItemStack stack, EntityLivingBase entity) {
        return super.isShield(stack, entity);
    }

    @Override
    public boolean isValidArmor(@Nonnull ItemStack stack, @Nonnull EntityEquipmentSlot armorType, @Nonnull Entity entity) {
        return super.isValidArmor(stack, armorType, entity);
    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack item, @Nonnull EntityPlayer player) {
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public boolean onEntityItemUpdate(@Nonnull EntityItem entityItem) {
        return super.onEntityItemUpdate(entityItem);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        Capabilities.getTrinketProperties(stack, cap -> cap.onUpdate(world, entity, itemSlot, isSelected));
    }

    /*
     * use the non player specific method for entities other than the player
     */
    @Override
    public void onArmorTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        Capabilities.getTrinketProperties(stack, cap -> cap.onArmorTick(world, player));
    }

    /*
     * Not Player Specific ArmorTick
     */
    @Override
    public void onEntityArmorTick(World world, EntityLivingBase entity, ItemStack stack) {
        Capabilities.getTrinketProperties(stack, cap -> cap.onEntityArmorTick(world, entity));
    }

    @Nullable
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ActionResult<ItemStack> defaultReturn = super.onItemRightClick(world, player, hand);
        return Capabilities.getTrinketProperties(player.getHeldItem(hand), defaultReturn, (prop, result) -> prop.itemRightClicked(world, player, hand, result));
    }

    @Nullable
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        final EnumActionResult defaultReturn = super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        return Capabilities.getTrinketProperties(player.getHeldItem(hand), defaultReturn, (prop, result) -> prop.itemUsed(player, world, pos, hand, facing, hitX, hitY, hitZ, result));
    }

    @Nullable
    @Override
    public EnumActionResult onItemUseFirst(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull EnumHand hand) {
        final EnumActionResult defaultReturn = super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        return Capabilities.getTrinketProperties(player.getHeldItem(hand), defaultReturn, (prop, result) -> prop.itemLeftClicked(player, world, pos, side, hitX, hitY, hitZ, hand, result));
    }

    @Override
    public void eventLivingUpdateTick(ItemStack stack, EntityLivingBase entity) {
        Capabilities.getTrinketProperties(stack, cap -> cap.onEntityTick(entity));
    }

    @Override
    public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
        this.initAttributes(this.getAttributeConfig(stack), player);
        PotionHelper.removeAllPotionEffectsFromConfig(player, this.getEffectsToRemove(stack));
        PotionHelper.addAllPotionEffectsFromConfig(player, true, this.getEffectsToAdd(stack));
        Capabilities.getTrinketProperties(stack, cap -> cap.onPlayerTick(cap.getItemStack(), player));
    }

    @Override
    public void eventLivingAttacked(@Nonnull ItemStack stack, @Nonnull EntityLivingBase attacked, @Nonnull LivingAttackEvent event) {
        boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, event.getSource(), event.getAmount(), this.getPrimaryElement(stack), this.getDamageTypesToIgnoreConfig(stack)).getFirst();
        if (result) {
            event.setCanceled(true);
        }
    }

    @Override
    public void eventLivingHurtAttacked(@Nonnull ItemStack stack, @Nonnull EntityLivingBase attacked, @Nonnull LivingHurtEvent event) {
        Utils.TempCache<Boolean, Float> result = DamageTypeConfigParser.parseDamageTypeConfig(1, event.getSource(), event.getAmount(), this.getPrimaryElement(stack), this.getDamageTypesToIgnoreConfig(stack));
        if (result.getFirst()) {
            float dmg = event.getAmount();
            event.setAmount(dmg * result.getSecond());
        }
    }

    @Override
    public void eventLivingDamageAttacked(ItemStack stack, EntityLivingBase attacked, LivingDamageEvent event) {
        Utils.TempCache<Boolean, Float> result = DamageTypeConfigParser.parseDamageTypeConfig(2, event.getSource(), event.getAmount(), this.getPrimaryElement(stack), this.getDamageTypesToIgnoreConfig(stack));
        if (result.getFirst()) {
            float dmg = event.getAmount();
            event.setAmount(dmg * result.getSecond());
        }
    }

    @Override
    public void eventPotionApplicable(ItemStack stack, EntityLivingBase entity, PotionEvent.PotionApplicableEvent event) {
        if (PotionHelper.isPotionEffect(event.getPotionEffect(), this.getEffectsToRemove(stack))) {
            event.setResult(Event.Result.DENY);
        }
    }

    @Override
    public void eventPlayerLogin(ItemStack stack, EntityPlayer player) {
        AttributeHelper.removeAttributesByUUID(player, this.getUUID());
    }

    @Override
    public void eventPlayerLogout(ItemStack stack, EntityPlayer player) {
        AttributeHelper.removeAttributesByUUID(player, this.getUUID());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltips, @Nonnull ITooltipFlag flagIn) {
        if (world == null) {
            return;
        }
        EntityPlayer entity = null;
        try {
            entity = Minecraft.getMinecraft().player;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }
        final EntityPlayer player = entity;
        boolean showAdvEnabled = flagIn != null && flagIn.isAdvanced();
        if (TrinketsConfig.CLIENT.debug.showID) {
            final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
            if (prop != null) {
                final SlotInformation info = prop.getSlotInfo();
                tooltips.add("§6" + info.getHandler() + "§r:§f" + info.getSlot() + "§r");
            }
        }

        super.addInformation(stack, world, tooltips, flagIn);
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        //SharedMonsterAttributes.readAttributeModifierFromNBT(compound);
        //		final String attribute = new TextComponentString("attribute.name.generic.maxHealth").getFormattedText();
        //		tooltip.add("§6" + attribute);
        // Add Attributes through the Capability, and get them from that, and then render them here, can do that instead of Adding them through the constructor
        // Also allows for more dynamic attributes, can get the attributes list on equip or something
        // Make sure not to accidentally leave attributes on the player
        // User For Loops instead of Streams, create a For loop predicate getter?

//        String translationKey = stack.getTranslationKey();
//        for (int i = 1; i <= 10; i++) {
//            final int index = i;
//            final String string = helper.getLangTranslation(translationKey + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
//            if (!helper.isStringEmpty(string)) {
//                tooltip.add(string);
//            }
//        }
        EnumRenderLocation modifier = GuiScreen.isCtrlKeyDown() ? EnumRenderLocation.ITEM_CTRL : GuiScreen.isShiftKeyDown() ? EnumRenderLocation.ITEM_SHIFT : GuiScreen.isAltKeyDown() ? EnumRenderLocation.ITEM_ALT : (showAdvEnabled ? EnumRenderLocation.ITEM_ADVANCED : EnumRenderLocation.ALWAYS);
        Capabilities.getTrinketProperties(stack, (prop) -> {
            prop.initAbilitiesOnce(player);
            final String itemSource = stack.getItem().getRegistryName().toString();
            for (IAbilityInterface ability : prop.getAbilitiesProvided().values()) {
                if (ability.isAbilityEnabled()) {
                    final String abilityName = ability.getRegistryName().toString();
                    final String holderSource = ability.getAbilityHolder() != null ? ability.getAbilityHolder().getSourceID() : "";
                    boolean disabled = Capabilities.getEntityProperties(player, false, (entProp, bool) -> {
                        if (entProp.getAbilityHandler().hasKillOrder(itemSource, abilityName)) {
                            return true;
                        }
                        return entProp.getAbilityHandler().hasKillOrder(holderSource, abilityName);
                    });
                    if (!disabled) {
                        ability.getDescription(tooltips, modifier.getId(), EnumRenderLocation.ITEM.getId());
                    }
                }
            }
        });
        try {

            String[] attributeConfig = this.getAttributeConfig(stack);
            if ((attributeConfig != null) && (attributeConfig.length > 0)) {
                List<AttributeEntry> attributes = new ArrayList<>();
                for (String entry : attributeConfig) {
                    AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                    if ((attributeShell != null)) {
                        final double amount = attributeShell.getAmount();
                        if (amount != 0) {
                            attributes.add(attributeShell);
                        }
                    }
                }
                if (!attributes.isEmpty()) {
                    tooltips.add(helper.reset + "" + helper.dGray + ConstantsTextTranslations.KEY_SHIFT.getFormattedText());
                    if (GuiScreen.isShiftKeyDown()) {
                        for (AttributeEntry entry : attributes) {
                            final String name = entry.getAttribute();
                            final double amount = entry.getAmount();
                            final int operation = entry.getOperation();
                            if ((player.getAttributeMap().getAttributeInstanceByName(name) != null)) {
                                double d1;
                                boolean flag = false;
                                if ((operation != 1) && (operation != 2)) {
                                    d1 = amount;
                                } else {
                                    d1 = amount * 100.0D;
                                }
                                final TextComponentTranslation AttrName = new TextComponentTranslation("attribute.name." + name);
                                if (flag) {
                                    final TextComponentTranslation never = new TextComponentTranslation("attribute.modifier.equals." + operation, Reference.DECIMALFORMAT.format(d1), AttrName.getFormattedText());
                                    tooltips.add(" " + never.getFormattedText());
                                    //I18n.translateToLocalFormatted("attribute.modifier.equals." + operation, DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + name)));
                                } else if (amount > 0.0D) {
                                    final TextComponentTranslation addition = new TextComponentTranslation("attribute.modifier.plus." + operation, Reference.DECIMALFORMAT.format(d1), AttrName.getFormattedText());
                                    addition.getStyle().setColor(TextFormatting.BLUE);
                                    String s = addition.getFormattedText();
                                    tooltips.add(" " + s);
                                    // I18n.translateToLocalFormatted("attribute.modifier.plus." + operation, DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + name)));
                                } else if (amount < 0.0D) {
                                    d1 = d1 * -1.0D;
                                    final TextComponentTranslation subtraction = new TextComponentTranslation("attribute.modifier.take." + operation, Reference.DECIMALFORMAT.format(d1), AttrName.getFormattedText());
                                    subtraction.getStyle().setColor(TextFormatting.RED);
                                    String s = subtraction.getFormattedText();
                                    tooltips.add(" " + s);
                                    //I18n.translateToLocalFormatted("attribute.modifier.take." + operation, DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + name)));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TrinketsConfig.CLIENT.debug.showID) {
            final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
            if (prop != null) {
                tooltips.add("§6Main" + "§r: §f" + (prop.mainAbility() ? "Enabled" : "Disabled") + "§r");
                tooltips.add("§6Alt" + "§r: §f" + (prop.altAbility() ? "Enabled" : "Disabled") + "§r");
            }
        }
        if (showAdvEnabled && TrinketsConfig.CLIENT.ITEMS.RENDER_ELEMENTS) {
            tooltips.add(ConstantsTextTranslations.KEY_CTRL.getFormattedText());
            if (modifier.equals(EnumRenderLocation.ITEM_CTRL)) {
                tooltips.add(this.getPrimaryElement(stack).getDisplayName());
            }
        }
    }

    // ITrinketInterface

    @Override
    public int getSlot(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, -1, (prop, slot) -> prop.getSlot());
    }

    @Nullable
    @Override
    public String getItemHandler(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, ItemHandlerType.NONE.getName(), (prop, slot) -> prop.getSlotInfo().getHandler());
    }

    @Override
    public String[] getDamageTypesToIgnoreConfig() {
        return IImmunityProvider.super.getDamageTypesToIgnoreConfig();
    }

    @Override
    public String[] getDamageTypesToIgnoreConfig(ItemStack stack) {
        return this.getDamageTypesToIgnoreConfig();
    }

    @Override
    public String[] getEffectsToAdd() {
        return IEffectsProvider.super.getEffectsToAdd();
    }

    @Override
    public String[] getEffectsToAdd(ItemStack stack) {
        return this.getEffectsToAdd();
    }

    @Override
    public String[] getEffectsToRemove() {
        return IResistanceProvider.super.getEffectsToRemove();
    }

    @Override
    public String[] getEffectsToRemove(ItemStack stack) {
        return this.getEffectsToRemove();
    }

    @Override
    public String[] getAttributeConfig() {
        return IAttributeProvider.super.getAttributeConfig();
    }

    @Override
    public String[] getAttributeConfig(ItemStack stack) {
        return this.getAttributeConfig();
    }

    protected void addSurvivalAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities, Element element, ConfigSurvivalCompat config) {
        if (SurvivalCompat.isSurvivalModsActive()) {
            if (config.immuneToHeat) {
                abilities.add((new AbilityHeatImmunity(config.immuneToHeat).setRequiredElement(element)));
            }
            if (config.immuneToCold) {
                abilities.add((new AbilityColdImmunity(config.immuneToCold).setRequiredElement(element)));
            }
            if (config.immuneToThirst) {
                abilities.add((new AbilityThirstImmunity(config.immuneToThirst).setRequiredElement(element)));
            }
            if (config.immuneToParasites) {
                abilities.add((new AbilityParasitesImmunity(config.immuneToParasites).setRequiredElement(element)));
            }
        }
    }

    @Override
    public void initAttributes(String[] attributeConfig, EntityLivingBase entity) {
        if (attributeConfig != null && !entity.world.isRemote) {
            for (String entry : attributeConfig) {
                AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                if ((attributeShell != null) && (this.getUUID() != null)) {
                    final String name = attributeShell.getAttribute();
                    double amount = attributeShell.getAmount();
                    final int operation = attributeShell.getOperation();
                    final boolean isSaved = attributeShell.isSaved();
                    if (!(this instanceof TrinketRaceBase)) {
                        UpdatingAttribute attribute = new UpdatingAttribute(this.getTranslationKey() + "." + name, this.uuid, name).setAmount(amount).setOperation(operation).setSavedInNBT(isSaved);
                        if (name.equalsIgnoreCase(Reference.MINECRAFT_FORGE_ATTRIBUTE_SWIM_SPEED)) {
                            boolean skip = false;
                            if (!TrinketsConfig.SERVER.MISC.DEPTH_STACKS) {
                                if ((EnchantmentHelper.getDepthStriderModifier(entity) > 0)) {
                                    skip = true;
                                }
                            }
                            try {
                                if (Trinkets.MOD_COMPAT.SoManyEnchantments && !TrinketsConfig.SERVER.MISC.STRIDER_STACKS) {
                                    Enchantment e = Enchantment.getEnchantmentByLocation("somanyenchantments:underwaterstrider");
                                    boolean hasUnderwaterStrider = ((e != null) && (EnchantmentHelper.getMaxEnchantmentLevel(e, entity) > 0));
                                    if (hasUnderwaterStrider) {
                                        skip = true;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if ((entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying) {
                                skip = true;
                            }
                            if (skip) {
                                attribute.removeModifier(entity);
                                continue;
                            }
                        }
                        attribute.addModifier(entity, amount, operation);
                    }
                }
            }
        }
    }

    //

    /*
     * Don't Edit Below this
     */

    @Override
    public String getTranslationKey() {
        return super.getTranslationKey();
    }

    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        // Name + Item Damage equals the Lang File Name
        final Element element = this.getPrimaryElement(stack);
        if (element != Elements.NEUTRAL) {
            if (stack.getItem() instanceof TrinketDragonsEye || (stack.getItem() instanceof TrinketRaceBase && stack.getItem().getRegistryName().toString().compareTo("xat:dragon_ring") == 0)) {
                return this.getTranslationKey() + "." + element.getName().toLowerCase();
            }
        }
        return this.getTranslationKey();
    }

    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        //		return super.getRarity(stack);
        return EnumRarity.RARE;
    }
}