package xzeroair.trinkets.items.base;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

import javax.annotation.Nonnull;

public class RaceFood extends FoodBase implements IRaceProvider {

    private final int useDuration;
    private final EntityRace race;
    private final EnumAction action;

    public RaceFood(String modid, String name, int useDur, EnumAction action, EntityRace race, String uuid, int heal, float saturation) {
        super(modid, name, heal, saturation);
        this.setAlwaysEdible();
        this.action = action;
        this.useDuration = useDur;
        this.race = race;
        this.setUUID(uuid);

    }

    public RaceFood(String name, int useDur, EnumAction action, EntityRace race, String uuid) {
        this(Reference.MODID, name, useDur, action, race, uuid, 2, 4F);
        this.setCreativeTab(Trinkets.CREATIVE_TAB);
    }

    public RaceFood(String name, int useDur, EnumAction action, EntityRace race) {
        this(name, useDur, action, race, "");
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        final Element element = this.getPrimaryElement(stack);
        if (element != Elements.NEUTRAL) {
            return super.getTranslationKey() + "." + element.getName().toLowerCase();
        }
        return super.getTranslationKey();
    }


    @Override
    public EntityRace getRace() {
        return this.race;
    }

    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityLivingBase entity) {
        return super.onItemUseFinish(stack, worldIn, entity);
    }

    @Override
    protected void onFoodEaten(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityPlayer player) {
        super.onFoodEaten(stack, worldIn, player);
        if (TrinketsConfig.SERVER.FOOD.EFFECTS) {
            Capabilities.getEntityProperties(player, prop -> {
                prop.setImbuedRaceCache(new RaceCache(this.getRace(), this.getPrimaryElement(stack)));
            });
        }
        this.setCooldown(20);
    }

    @Override
    public void onCreated(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityPlayer player) {
        super.onCreated(stack, world, player);
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return this.action;
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
                Element element = this.getPrimaryElement(stack);
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
