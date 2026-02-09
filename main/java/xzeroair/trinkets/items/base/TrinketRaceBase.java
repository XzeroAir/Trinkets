package xzeroair.trinkets.items.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.AbilityBlockFinder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketRaceBase extends AccessoryBase implements IRaceProvider {

    public TransformationRingConfig serverConfig;

    protected EntityRace race;

    public TrinketRaceBase(String name, EntityRace race, TransformationRingConfig config) {
        super(name);
        this.race = race;
        this.setUUID("892cfd1f-25c5-44a0-9154-f3b630538c82");
        serverConfig = config;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.getRegistryName().toString().contentEquals("xat:dragon_ring")) {
            if (tab == this.getCreativeTab()) {
                final ItemStack normal = new ItemStack(this, 1, 0);
                items.add(normal);
                if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.FIRE_VARIANT) {
                    final ItemStack fire = new ItemStack(this, 1, 0);
                    NBTTagCompound tag = new NBTTagCompound();
                    Capabilities.getTrinketProperties(fire, prop -> {
                        prop.setVariant(1);
                        prop.getElementAttributes().setPrimaryElement(Elements.FIRE);
                        prop.saveToNBT(tag);
                    });
                    fire.setTagCompound(tag);
                    items.add(fire);
                }
                if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT) {
                    final ItemStack ice = new ItemStack(this, 1, 0);
                    NBTTagCompound tag = new NBTTagCompound();
                    Capabilities.getTrinketProperties(ice, prop -> {
                        prop.setVariant(2);
                        prop.getElementAttributes().setPrimaryElement(Elements.ICE);
                        prop.saveToNBT(tag);
                    });
                    ice.setTagCompound(tag);
                    items.add(ice);
                }
                if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.LIGHTNING_VARIANT) {
                    final ItemStack lightning = new ItemStack(this, 1, 0);
                    NBTTagCompound tag = new NBTTagCompound();
                    Capabilities.getTrinketProperties(lightning, prop -> {
                        prop.setVariant(3);
                        prop.getElementAttributes().setPrimaryElement(Elements.LIGHTNING);
                        prop.saveToNBT(tag);
                    });
                    lightning.setTagCompound(tag);
                    items.add(lightning);
                }
            }
        } else {
            super.getSubItems(tab, items);
        }
    }

    @Override
    public String[] getAttributeConfig() {
        return race.getRaceAttributes().getAttributes();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
        final EntityRace r = this.getRace();
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        if (r != null) {
            final KeyEntry size = new OptionEntry("rsize", r.getRaceHeight() + "%");
            if (r.equals(EntityRaces.fairy)) {
                final FairyConfig config = TrinketsConfig.SERVER.races.fairy;
                final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "creativeflight", config.creative_flight);
                return helper.formatAddVariables(translation, size, key1);
            }
            if (r.equals(EntityRaces.dwarf)) {
                final DwarfConfig config = TrinketsConfig.SERVER.races.dwarf;
                final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "fortune", config.fortune);
                final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "skilledminer", config.skilled_miner);
                final KeyEntry key3 = new LangEntry(this.getTranslationKey(stack), "staticminer", config.static_mining);
                return helper.formatAddVariables(translation, size, key1, key2, key3);
            }
            if (r.equals(EntityRaces.elf)) {
                final ElfConfig config = TrinketsConfig.SERVER.races.elf;
                final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "chargeshot", config.charge_shot);
                return helper.formatAddVariables(translation, size, key1);
            }
            if (r.equals(EntityRaces.dragon)) {
                String tlKey = ModItems.trinkets.TrinketDragonsEye.getTranslationKey() + ".0";
                final KeyEntry key = new LangEntry(tlKey, "treasurefinder", TrinketsConfig.getClientStore().DRAGON_EYE_OF_ENABLED);
                String oreTarget = "NONE";
                try {
                    final EntityPlayer player = Minecraft.getMinecraft().player;
                    final IAbilityInterface ability = Capabilities.getEntityProperties(player, null, (prop, a) -> prop.getAbilityHandler().getAbility("xat:" + Abilities.blockDetection));
                    if (ability instanceof AbilityBlockFinder) {
                        final AbilityBlockFinder finder = (AbilityBlockFinder) ability;
                        final String target = finder.getTreasure().parseTargetName();
                        if (!target.isEmpty()) {
                            oreTarget = target;
                        }
                    }
                } catch (Exception e) {
                    oreTarget = "ERROR";
                }
                final KeyEntry key1 = new OptionEntry("target", TrinketsConfig.getClientStore().DRAGON_EYE_OF_ENABLED, oreTarget);
                final KeyEntry keybind1 = new KeyBindEntry("denvkb", ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName());
                final KeyEntry keybind2 = new KeyBindEntry("deofkb", ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName());
                final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
                final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
                final boolean tan = tanEnabled || sdEnabled;
                final Element element = this.getPrimaryElement(stack);
                final boolean isIceVariant = element == Elements.ICE;
                final boolean isLightningVariant = element == Elements.LIGHTNING;
                final KeyEntry key2 = new OptionEntry("variantresist", new TextComponentTranslation((element == Elements.FIRE) || ((element == Elements.NEUTRAL) && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.DE_FIRE_RESIST) ? "effect.fireResistance" : (element != Elements.NEUTRAL) ? "xat.effect." + element.getName().toLowerCase() + "_resistance" : "ability.block_detection.name").getFormattedText());
                final KeyEntry TANHot = new LangEntry(tlKey, "heatimmune", tan && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.tan.immuneToHeat);
                final KeyEntry TANCold = new LangEntry(tlKey, "coldimmune", tan && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.tan.immuneToHeat);
                final KeyEntry IAFParalysis = new LangEntry(tlKey, "paralysisimmune", isLightningVariant && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.LIGHTNING_VARIANT && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.PARALYSIS_IMMUNITY);
                final KeyEntry key3 = new OptionEntry("typeimmune", new TextComponentTranslation(isIceVariant ? TANCold.option() : isLightningVariant ? "" : TANHot.option()).getFormattedText());
                final KeyEntry IAFFrostWalker = new LangEntry(tlKey + ".compat.iaf.ice", "frostwalker", isIceVariant && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.FROST_WALKER);
                final String output = helper.formatAddVariables(translation, key, key1, keybind1, keybind2, key2, TANHot, TANCold, key3, IAFFrostWalker, IAFParalysis).replace("#underline:", "");
                return output;
            }
            if (r.equals(EntityRaces.titan)) {
                final TitanConfig config = TrinketsConfig.SERVER.races.titan;
                final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "heavy", config.sink);
                return helper.formatAddVariables(translation, size, key1);
            }
            return helper.formatAddVariables(translation, size);
        } else {
            return helper.formatAddVariables(translation);
        }
    }

    @Override
    public EntityRace getRace() {
        return race;
    }

    @Override
    public boolean ItemEnabled() {
        return serverConfig.enabled;
    }

    @Override
    public void registerModels() {
        if (this.getRegistryName().toString().equals("xat:dragon_ring")) {
            final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
            final ModelResourceLocation iceVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_ice", "inventory");
            final ModelResourceLocation lightningVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_lightning", "inventory");
            final ModelResourceLocation fireVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_fire", "inventory");
            ModelBakery.registerItemVariants(this, normal, fireVariant, iceVariant, lightningVariant);
            ModelLoader.setCustomMeshDefinition(this, stack -> {
                TrinketProperties prop = Capabilities.getTrinketProperties(stack, new TrinketProperties(stack), (prop1, emptyProp) -> {
                    return prop1;
                });
                prop.loadFromNBT(prop.getTag());
                Element element = prop.getElementAttributes().getPrimaryElement();
                if (element == Elements.LIGHTNING) {
                    return lightningVariant;
                } else if (element == Elements.ICE) {
                    return iceVariant;
                } else if (element == Elements.FIRE) {
                    return fireVariant;
                } else {
                    return normal;
                }
            });
        } else {
            Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
        }
    }

}
