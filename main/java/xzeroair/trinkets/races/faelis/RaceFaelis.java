package xzeroair.trinkets.races.faelis;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.faelis.RaceFaelisRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigEquipmentObject;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.EntityHelper;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RaceFaelis extends EntityRacePropertiesHandler {

    private final FaelisConfig CONFIG = TrinketsConfig.SERVER.RACES.FAELIS;
    protected UpdatingAttribute movement, jump;

    public RaceFaelis(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties) {
        super(e, properties, new RaceCache(EntityRaces.faelis));
        this.movement = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        this.jump = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    public RaceFaelis(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties, @Nonnull RaceCache raceCache) {
        super(e, properties, raceCache);
        this.movement = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        this.jump = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    @Override
    public void registerRaceAbilities() {
        this.addAbility(new AbilityNightVision(this.CONFIG.ABILITIES.NIGHT_VISION));
        this.addAbility(new AbilityClimbing(this.CONFIG.ABILITIES.CLIMBING));
        this.addSurvivalAbilities(this.CONFIG.COMPAT.SURVIVAL);
    }

    @Override
    public void whileTransformed() {
        super.whileTransformed();
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.EFFECTS_TO_REMOVE);
        PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.CONFIG.EFFECTS_TO_ADD);
        if (!this.getEntity().world.isRemote && this.getEntity().isRiding()) {
            final Entity mount = this.getEntity().getRidingEntity();
            if ((mount != null) && !this.mountEntity(mount)) {
                this.getEntity().dismountRidingEntity();
            }
        }
        if (this.getEntity().world.isRemote) {
            return;
        }
        boolean hasMilkBuff = false;
        if (ModPotionTypes.TrinketPotions.containsKey(ModPotionTypes.invigorated)) {
            hasMilkBuff = this.getEntity().isPotionActive(ModPotionTypes.TrinketPotions.get(ModPotionTypes.invigorated));
        }
        if (this.CONFIG.HEAVY_ARMOR_PENALTY) {
            double amount = 0;
            if (TrinketsConfig.SERVER.RACES.FAELIS.HEAVY_ARMOR_PENALTY) {
                try {
                    for (final ItemStack stack : this.getEntity().getEquipmentAndArmor()) {
                        final Item item = stack.getItem();
                        final String regName = item.getRegistryName().toString();
                        final String itemType = ConfigEquipmentObject.getItemType(stack);
                        if (!itemType.isEmpty()) {
                            final String ItemMaterial = ConfigEquipmentObject.getItemMaterial(stack).toLowerCase();
                            if (item instanceof ItemArmor) {
                                final ItemArmor armor = ((ItemArmor) item);
                                final String armorType = armor.armorType.getName();
                                ConfigEquipmentObject entry = ConfigHelper.TrinketConfigStorage.getEquipmentEntry(regName + ":" + armorType, regName, "ObjectMaterial:" + ItemMaterial + ":" + armorType, "ObjectMaterial:" + ItemMaterial);
                                if (entry != null) {
                                    amount -= entry.getEquipmentWeight();
                                }
                            } else {
                                final String hand = stack.isItemEqual(this.getEntity().getHeldItemMainhand()) ? "mainhand" : stack.isItemEqual(this.getEntity().getHeldItemOffhand()) ? "offhand" : "hand";
                                String[] mS = new String[]{regName + ":" + hand, regName, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + hand, "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                                final ConfigEquipmentObject main = ConfigHelper.TrinketConfigStorage.getEquipmentEntry((k, v) -> v.doesItemMatchEntry(stack), mS);
                                double mW = main == null ? 0 : main.getEquipmentWeight();
                                amount -= mW;
                            }
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            if ((amount != 0) && !hasMilkBuff) {
                this.movement.addModifier(this.getEntity(), amount, 2);
                this.jump.addModifier(this.getEntity(), amount, 2);
            } else {
                if (this.CONFIG.MILK_INVIGORATED) {
                    this.movement.removeModifier(this.getEntity());
                    this.jump.removeModifier(this.getEntity());
                }
            }
        } else {
            this.movement.removeModifier(this.getEntity());
            this.jump.removeModifier(this.getEntity());
        }
    }

    @Override
    public boolean isAttacked(DamageSource source, float dmg) {
        if (!this.getEntity().world.isRemote) {
            boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getFirst();
            return !result;
        }
        return true;
    }

    @Override
    public float isHurt(DamageSource source, float dmg) {
        return dmg * DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getSecond();
    }

    @Override
    public float isDamaged(DamageSource source, float dmg) {
        return dmg * DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getSecond();
    }

    @Override
    public void endTransformation() {
        AttributeHelper.removeAttributes(this.getEntity(), UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"));
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.EFFECTS_TO_ADD);
    }

    @Override
    public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if (!this.CONFIG.BAREHAND_COMBAT) {
            return dmg;
        }
        if (!((source instanceof EntityDamageSourceIndirect) || source.isExplosion() || source.isMagicDamage() || source.isProjectile())) {
            if ((dmg > 0)) {
                final ItemStack stack1 = this.getEntity().getHeldItemMainhand();
                final ItemStack stack2 = this.getEntity().getHeldItemOffhand();
                boolean mainhandCountsAsBare = stack1.isEmpty();
                boolean offhandCountsAsBare = stack2.isEmpty();
                if (!mainhandCountsAsBare) {
                    Item item = stack1.getItem();
                    String regName = item.getRegistryName().toString();
                    String itemType = item instanceof ItemBlock ? "block" : ConfigEquipmentObject.getItemType(stack1);
                    if (itemType.isEmpty()) {
                        itemType = "item";
                    }
                    if (!itemType.isEmpty()) {
                        final String ItemMaterial = ConfigEquipmentObject.getItemMaterial(stack1).toLowerCase();
                        if (!(item instanceof ItemArmor)) {
                            final String hand = "mainhand";
                            String[] mS = new String[]{regName + ":" + hand, regName, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + hand, "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                            final ConfigEquipmentObject main = ConfigHelper.TrinketConfigStorage.getListEntry(ConfigHelper.TrinketConfigStorage.BareHandedItems, (k, v) -> v.doesItemMatchEntry(stack1), mS);
//                        double mW = main == null ? 0 : main.getEquipmentWeight();
                            if (main != null) {
                                mainhandCountsAsBare = true;
                                dmg += main.getEquipmentWeight();
                            }
                        }
                    }
                }
                if (!offhandCountsAsBare) {
                    Item item = stack2.getItem();
                    String regName = item.getRegistryName().toString();
                    String itemType = item instanceof ItemBlock ? "block" : ConfigEquipmentObject.getItemType(stack2);
                    if (itemType.isEmpty()) {
                        itemType = "item";
                    }
                    if (!itemType.isEmpty()) {
                        final String ItemMaterial = ConfigEquipmentObject.getItemMaterial(stack2).toLowerCase();
                        if (!(item instanceof ItemArmor)) {
                            final String hand = "offhand";
                            String[] mS = new String[]{regName + ":" + hand, regName, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + hand, "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                            final ConfigEquipmentObject main = ConfigHelper.TrinketConfigStorage.getListEntry(ConfigHelper.TrinketConfigStorage.BareHandedItems, (k, v) -> v.doesItemMatchEntry(stack2), mS);
//                        double mW = main == null ? 0 : main.getEquipmentWeight();
                            if (main != null) {
                                offhandCountsAsBare = true;
                                dmg += (float) main.getEquipmentWeight();
                            }
                        }
                    }
                }
                if (mainhandCountsAsBare && offhandCountsAsBare) {
                    dmg += (float) this.CONFIG.BAREHAND_COMBAT_BONUS;
                }
            }
        }
        return dmg;
    }

    @Override
    public boolean potionBeingApplied(PotionEffect effect) {
        return PotionHelper.isPotionEffect(effect, this.CONFIG.EFFECTS_TO_REMOVE);
    }

    @Override
    public boolean mountEntity(Entity mount) {
        if (EntityHelper.isCreative(this.getEntity())) {
            return true;
        } else if (!this.CONFIG.CAN_MOUNT) {
            return false;
        } else if (this.CONFIG.MOUNT_BLACKLIST.length > 0) {
            List<String> disallowedMounts = Arrays.asList(this.CONFIG.MOUNT_BLACKLIST);
            try {
                final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
                final String modID = regName.getNamespace();
                final String entityID = regName.getPath();
                final boolean exists = disallowedMounts.contains(modID + ":*") || disallowedMounts.contains(regName.toString());
                if (exists) {
                    if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == this.getEntity())) {
                            return false;
                        }
                    }
                    return this.CONFIG.MOUNT_WHITELIST;
                } else {
                    if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == this.getEntity())) {
                            return false;
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return !this.CONFIG.MOUNT_WHITELIST;
        } else {
            if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                final EntityBoat boat = (EntityBoat) mount;
                final Entity controller = boat.getControllingPassenger();
                return (controller != null) && (controller != this.getEntity());
            }
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceFaelisRenderer(this.getEntity(), this);
        }
        return this.RendererRace;
    }
}
