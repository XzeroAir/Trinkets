package xzeroair.trinkets.races.faelis;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.faelis.RaceFaelisRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ArmorEntry;
import xzeroair.trinkets.util.helpers.AttributeHelper;

import javax.annotation.Nonnull;
import java.util.UUID;

public class RaceFaelis extends EntityRacePropertiesHandler {

    public static FaelisConfig serverConfig = TrinketsConfig.SERVER.races.faelis;
    protected UpdatingAttribute movement, jump;

    public RaceFaelis(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.faelis);
        movement = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        jump = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    public RaceFaelis(@Nonnull EntityLivingBase e, Element element) {
        super(e, EntityRaces.faelis, element);
        movement = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        jump = new UpdatingAttribute(UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    @Override
    public void startTransformation() {
        if (TrinketsConfig.getClientStore().CLIMBING_ENABLED) {
            this.addAbility(new AbilityClimbing());
        }
    }

    @Override
    public void whileTransformed() {
        if (entity.world.isRemote) {
            return;
        }
        boolean hasMilkBuff = false;
        if (TrinketsConfig.SERVER.races.faelis.penalties) {
            double amount = 0;
            final StatusHandler status = Capabilities.getStatusHandler(entity);
            if (status != null) {
                if (status.getActiveEffects().containsKey(StatusEffectsEnum.Invigorated.getName())) {
                    hasMilkBuff = true;
                } else {
                    hasMilkBuff = false;
                }
            }
            if (TrinketsConfig.SERVER.races.faelis.penalties) {
                try {
                    for (final ItemStack stack : entity.getEquipmentAndArmor()) {
                        final Item item = stack.getItem();
                        final String regName = item.getRegistryName().toString();
                        final String itemType = ConfigHelper.ArmorEntry.getItemType(stack);
                        if (!itemType.isEmpty()) {
                            final String ItemMaterial = ConfigHelper.ArmorEntry.getItemMaterial(stack).toLowerCase();
                            if (item instanceof ItemArmor) {
                                final ItemArmor armor = ((ItemArmor) item);
                                final String armorType = armor.armorType.getName();
                                ArmorEntry entry = ConfigHelper.TrinketConfigStorage.getEquipmentEntry(regName + ":" + armorType, regName, "ObjectMaterial:" + ItemMaterial + ":" + armorType, "ObjectMaterial:" + ItemMaterial);
                                if (entry != null) {
                                    amount -= entry.getEquipmentWeight();
                                }
                            } else {
                                final String hand = stack.isItemEqual(entity.getHeldItemMainhand()) ? "mainhand" : stack.isItemEqual(entity.getHeldItemOffhand()) ? "offhand" : "hand";
                                String[] mS = new String[]{regName + ":" + hand, regName, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + hand, "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                                final ArmorEntry main = ConfigHelper.TrinketConfigStorage.getEquipmentEntry((k, v) -> v.doesItemMatchEntry(stack), mS);
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
                movement.addModifier(entity, amount, 2);
                jump.addModifier(entity, amount, 2);
            } else {
                movement.removeModifier(entity);
                jump.removeModifier(entity);
            }
        } else {
            movement.removeModifier(entity);
            jump.removeModifier(entity);
        }
    }

    @Override
    public void endTransformation() {
        AttributeHelper.removeAttributes(entity, UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a"));
    }

    @Override
    public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if (!((source instanceof EntityDamageSourceIndirect) || source.isExplosion() || source.isMagicDamage() || source.isProjectile())) {
            if ((dmg > 0)) {
                final ItemStack stack1 = entity.getHeldItemMainhand();
                final ItemStack stack2 = entity.getHeldItemOffhand();
                boolean mainhandCountsAsBare = stack1.isEmpty();
                boolean offhandCountsAsBare = stack2.isEmpty();
                if (!mainhandCountsAsBare) {
                    Item item = stack1.getItem();
                    String regName = item.getRegistryName().toString();
                    String itemType = item instanceof ItemBlock ? "block" : ConfigHelper.ArmorEntry.getItemType(stack1);
                    if (itemType.isEmpty()) {
                        itemType = "item";
                    }
                    if (!itemType.isEmpty()) {
                        final String ItemMaterial = ConfigHelper.ArmorEntry.getItemMaterial(stack1).toLowerCase();
                        if (!(item instanceof ItemArmor)) {
                            final String hand = "mainhand";
                            String[] mS = new String[]{regName + ":" + hand, regName, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + hand, "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                            final ArmorEntry main = ConfigHelper.TrinketConfigStorage.getListEntry(ConfigHelper.TrinketConfigStorage.BareHandedItems, (k, v) -> v.doesItemMatchEntry(stack1), mS);
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
                    String itemType = item instanceof ItemBlock ? "block" : ConfigHelper.ArmorEntry.getItemType(stack2);
                    if (itemType.isEmpty()) {
                        itemType = "item";
                    }
                    if (!itemType.isEmpty()) {
                        final String ItemMaterial = ConfigHelper.ArmorEntry.getItemMaterial(stack2).toLowerCase();
                        if (!(item instanceof ItemArmor)) {
                            final String hand = "offhand";
                            String[] mS = new String[]{regName + ":" + hand, regName, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + hand + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + hand, "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                            final ArmorEntry main = ConfigHelper.TrinketConfigStorage.getListEntry(ConfigHelper.TrinketConfigStorage.BareHandedItems, (k, v) -> v.doesItemMatchEntry(stack2), mS);
//                        double mW = main == null ? 0 : main.getEquipmentWeight();
                            if (main != null) {
                                offhandCountsAsBare = true;
                                dmg += main.getEquipmentWeight();
                            }
                        }
                    }
                }
                if (mainhandCountsAsBare && offhandCountsAsBare) {
                    dmg += serverConfig.bonus;
                }
            }
        }
        return dmg;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceFaelisRenderer(entity, this);
        }
        return RendererRace;
    }
}