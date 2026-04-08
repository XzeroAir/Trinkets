package xzeroair.trinkets.items.trinkets.cosmetic;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.races.dragon.RaceDragonHorns;
import xzeroair.trinkets.client.races.dragon.RaceDragonWings;
import xzeroair.trinkets.client.races.dwarf.RaceDwarfBeard;
import xzeroair.trinkets.client.races.elf.RaceElfEars;
import xzeroair.trinkets.client.races.faelis.RaceFaelisClaws;
import xzeroair.trinkets.client.races.faelis.RaceFaelisEars;
import xzeroair.trinkets.client.races.faelis.RaceFaelisTail;
import xzeroair.trinkets.client.races.fairy.RaceFairyWings;
import xzeroair.trinkets.client.races.generic.RaceHorns;
import xzeroair.trinkets.client.races.generic.RaceHornsInverted;
import xzeroair.trinkets.client.races.goblin.RaceGoblinEars;
import xzeroair.trinkets.client.races.succubus.RaceSuccubusHorns;
import xzeroair.trinkets.client.races.taurus.RaceTaurusBell;
import xzeroair.trinkets.client.races.taurus.RaceTaurusFemaleHorns;
import xzeroair.trinkets.client.races.taurus.RaceTaurusHorns;
import xzeroair.trinkets.enums.RenderCosmeticFeature;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.items.base.AccessoryBase;

import javax.annotation.Nonnull;

public class TrinketCosmetic extends AccessoryBase {

    public TrinketCosmetic(String name) {
        super(name);
        this.addPropertyOverride(new ResourceLocation("meta"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
                return stack.getMetadata();
            }
        });
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        super.getSubItems(tab, items);
//        for (int i = 1; i < RenderCosmeticFeature.getMaxLength(); i++) {
//            items.add(new ItemStack(this, 1, i));
//        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void playerRenderLayer(@Nonnull ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        final RenderCosmeticFeature cosmetic = RenderCosmeticFeature.cosmetic(stack.getMetadata());
        switch (cosmetic) {
//            case HUMAN:
//                break;
            case FAIRY:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.fairy))) {
                    RaceFairyWings.INSTANCE.render(player, renderer, false, isSlim, partialTicks, scale);
                }
                break;
            case DWARF:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.dwarf))) {
                    RaceDwarfBeard.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
            case ELF:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.elf))) {
                    RaceElfEars.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
            case GOBLIN:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.goblin))) {
                    RaceGoblinEars.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
            case FAELIS:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.faelis))) {
                    RaceFaelisEars.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                    RaceFaelisClaws.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
//            case TITAN:
//                break;
            case DRAGON:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.dragon))) {
                    RaceDragonWings.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
            case TAURUS:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.taurus))) {
                    RaceTaurusHorns.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
            case TAURUS_BELL:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.taurus))) {
                    RaceTaurusHorns.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                    RaceTaurusBell.INSTANCE.render(player, renderer, true, false, partialTicks, scale);
                }
                break;
            case TAURUS_F:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.taurus))) {
                    RaceTaurusFemaleHorns.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
            case TAURUS_F_BELL:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.taurus))) {
                    RaceTaurusFemaleHorns.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                    RaceTaurusBell.INSTANCE.render(player, renderer, true, true, partialTicks, scale);
                }
                break;
            case TAURIAN_BELL:
                if (Capabilities.getEntityProperties(player, true, (prop, rtn) -> !prop.getCurrentRaceCache().compareRace(EntityRaces.taurus))) {
                    RaceTaurusBell.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                }
                break;
            case SUCCUBUS:
                RaceSuccubusHorns.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                break;
            case GENERIC_HORNS:
                RaceHorns.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                break;
            case GENERIC_HORNS_INVERTED:
                RaceHornsInverted.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                break;
            case DRAGON_HORNS:
                RaceDragonHorns.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                break;
            case FAELIS_TAIL:
                RaceFaelisTail.INSTANCE.render(player, renderer, true, isSlim, partialTicks, scale);
                break;
            default:
                break;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        final ItemStack stack = player.getHeldItem(hand);
        final boolean client = player.world.isRemote;
        if (client) {
            return super.onItemRightClick(world, player, hand);
        }
        final boolean sneak = player.isSneaking();
        int meta = stack.getMetadata();
        boolean lowPitch = false;
        if (sneak) {
            meta--;
            lowPitch = true;
        } else {
            meta++;
        }
        if (meta > RenderCosmeticFeature.getMaxLength() - 1) {
            meta = 0;
        }
        if (meta < 0) {
            meta = RenderCosmeticFeature.getMaxLength() - 1;
        }
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.3F, lowPitch ? 0.6F : 0.3F);
        stack.setItemDamage(meta);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public boolean canEquipAccessory(ItemStack stack, EntityLivingBase player) {
        return !TrinketHelper.AccessoryCheck(player, check -> {
            if (!check.isEmpty()) {
                if (check.getItem().getRegistryName().toString().contentEquals(this.getRegistryName().toString())) {
                    return check.getMetadata() == stack.getMetadata();
                }
            }
            return false;
        });
    }

    @Override
    public boolean ItemEnabled() {
        return super.ItemEnabled();
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        return this.getTranslationKey() + "." + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        for (int i = 0; i < RenderCosmeticFeature.getMaxLength(); i++) {
            Trinkets.proxy.registerItemRenderer(this, i, "inventory");
        }
//        Trinkets.proxy.registerItemRenderer(this, HUMAN, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, FAIRY, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, DWARF, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, ELF, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, GOBLIN, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, FAELIS, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, TITAN, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, DRAGON, "inventory");
//        Trinkets.proxy.registerItemRenderer(this, TAURUS, "inventory");
    }


}
