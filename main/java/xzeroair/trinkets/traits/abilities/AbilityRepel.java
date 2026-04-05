package xzeroair.trinkets.traits.abilities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.*;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityRepel;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.Kinetics;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class AbilityRepel extends Ability implements ITickableAbility, IHeldAbility, ITickableInventoryAbility, IToggleAbility, IKeyBindInterface {

    protected final ConfigAbilityRepel CONFIG;

    protected boolean toggled;
    protected int mode;
    protected float COST, CONSUMPTION_COOLDOWN;
    protected double FORCE;

    public AbilityRepel() {
        this(TrinketsConfig.SERVER.ABILITIES.REPEL);
    }

    public AbilityRepel(ConfigAbilityRepel config) {
        super(TrinketsRegistryNames.ModAbilities.REPEL);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.COST = config.COST;
        this.FORCE = config.FORCE;
        this.CONSUMPTION_COOLDOWN = config.FREQUENCY;
        this.toggled = false;
        this.mode = -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = this.getTranslationKey();
        final KeyEntry key3 = new LangEntry(langKey, "repel", this.CONFIG.ENABLED);
        final KeyEntry key5 = new OptionEntry("repeltoggle", this.CONFIG.ENABLED, helper.toggleCheckTranslation(this.isAbilityToggled()));
        final KeyEntry key6 = new KeyBindEntry("magnetkb", ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName());
        final KeyEntry key7 = new KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
        return helper.formatAddVariables(key, renderID, key3, key5, key6, key7);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (this.isAbilityToggled()) {
            this.blockArrows(entity);
        }
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean inHand) {
        if (stack.getItem() instanceof TrinketPolarized) {
            Capabilities.getTrinketProperties(stack, prop -> {
                if (this.isAbilityToggled() != prop.altAbility()) {
                    this.toggleAbility(prop.altAbility());
                    this.sendMessageToPlayer(entity);
                }
            });
        }
    }


    @Override
    public void onAbilityAdded(EntityLivingBase entity) {
        super.onAbilityAdded(entity);
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        super.onAbilityRemoved(entity);
    }

    public void blockArrows(EntityLivingBase entity) {
        final Predicate<EntityLivingBase> filter = Predicates.and(EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE);
        final boolean flag = filter.apply(entity);
        if (flag) {
            if (this.COST > 0F) {
                final MagicStats magic = Capabilities.getMagicStats(entity);
                if (magic != null) {
                    if (magic.getMana() < this.COST) {
                        this.toggleAbility(false);
                        return;
                    }
                    this.tickHandler.addCounter("repel.ticks", this.CONFIG.FREQUENCY, false, true, false);
                    final Counter counter = this.tickHandler.getCounter("repel.ticks");
                    if ((counter != null) && counter.Tick()) {
                        if (!magic.spendMana(this.COST)) {
                            this.toggleAbility(false);
                            return;
                        }
                    }
                }
            }
            try {
                final AxisAlignedBB bBox = entity.getEntityBoundingBox();
                final List<String> cfg = Arrays.asList(this.CONFIG.WHITELIST);
                final Predicate<Entity> Targets = Predicates.and(EntitySelectors.CAN_AI_TARGET, EntitySelectors.IS_ALIVE, ent -> (ent != null) && !(ent instanceof EntityPlayer) && (EntityRegistry.getEntry(ent.getClass()) != null) && (EntityRegistry.getEntry(ent.getClass()).getRegistryName() != null) && cfg.contains(EntityRegistry.getEntry(ent.getClass()).getRegistryName().toString()));
                final List<Entity> entityList = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(this.CONFIG.RANGE.RANGE_HORIZONTAL, this.CONFIG.RANGE.RANGE_VERTICAL, this.CONFIG.RANGE.RANGE_HORIZONTAL), Targets);
                for (final Entity repelledEntity : entityList) {
                    Kinetics.applyForce(entity, repelledEntity, false, this.FORCE, 0.8D, 0.85D);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isAbilityToggled() {
        return this.toggled;
    }

    @Override
    public int getToggleMode() {
        return this.mode;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        if (this.toggled != enabled) {
            this.toggled = enabled;
            this.setChanged(true);
        }
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        if (this.mode != value) {
            this.mode = value;
            this.setChanged(true);
        }
        return this.toggleAbility(value > 0);
    }

    @Override
    public boolean sendMessageToPlayer(@Nonnull Entity entity) {
        final boolean client = entity.world.isRemote;
        if (!client && (entity instanceof EntityPlayer)) {
            final TranslationHelper helper = TranslationHelper.INSTANCE;
            final String repelMode = new TextComponentTranslation(this.getTranslationKey() + ".repelmode").getFormattedText();
            final KeyEntry key = new OptionEntry("repeltoggle", this.CONFIG.ENABLED, helper.toggleCheckTranslation(this.isAbilityToggled()));
            StringUtils.sendStatusMessageToPlayer(entity, helper.formatAddVariables(repelMode, key), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        if (Aux) {
            final boolean enabled = this.isAbilityToggled();
            this.toggleAbility(!enabled);
            this.sendMessageToPlayer(entity);
            AbilityHolder holder = this.getAbilityHolder();
            if ((entity instanceof EntityLivingBase)) {
                final ItemStack stack = holder.getInfo().getStackFromHandler((EntityLivingBase) entity);
                if (!stack.isEmpty() && stack.getItem() instanceof TrinketPolarized) {
                    Capabilities.getTrinketProperties(stack, cap -> {
                        cap.toggleAltAbility(this.isAbilityToggled());
                        cap.sendInformationToPlayer(((EntityPlayer) entity));
                    });
                }
            }
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }

}
