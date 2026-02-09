package xzeroair.trinkets.items.base;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties.RaceCache;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFood extends FoodBase implements IRaceProvider {

    private int useDuration;
    private EntityRace race;
    private EnumAction action;

    public RaceFood(String name, int useDur, EnumAction action, EntityRace race, String uuid) {
        super(name, 2, 4F);
        this.setAlwaysEdible();
        this.action = action;
        useDuration = useDur;
        this.race = race;
        this.setUUID(uuid);
    }

    public RaceFood(String name, int useDur, EnumAction action, EntityRace race) {
        this(name, useDur, action, race, "");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.getRegistryName().toString().contentEquals("xat:dragon_gem")) {
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
    public EntityRace getRace() {
        return race;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entity) {
        return super.onItemUseFinish(stack, worldIn, entity);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        super.onFoodEaten(stack, worldIn, player);
        Capabilities.getEntityProperties(player, prop -> {
            if (TrinketsConfig.SERVER.Food.food_effects) {
                prop.setImbuedRace(new RaceCache(race, getPrimaryElement(stack)));
            }
        });
        this.setCooldown(20);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        super.onCreated(stack, world, player);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return action;
    }

    @Override
    public void registerModels() {
        if (this.getRegistryName().toString().equals("xat:dragon_gem")) {
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
            super.registerModels();
        }
    }
}
