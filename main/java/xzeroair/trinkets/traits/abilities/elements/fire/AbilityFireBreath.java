package xzeroair.trinkets.traits.abilities.elements.fire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.EntityRangedAttack;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;

public class AbilityFireBreath extends Ability implements IKeyBindInterface {

    protected final ConfigAbilityBreath CONFIG;

    protected int BREATH_STAGE, BREATH_FREQUENCY;
    protected float COST, DAMAGE;
    protected String[] EFFECTS;
    protected boolean INTERACT_WITH_TERRAIN;

    public AbilityFireBreath() {
        this(TrinketsConfig.SERVER.ABILITIES.FIRE_BREATH);
    }

    public AbilityFireBreath(ConfigAbilityBreath config) {
        super(TrinketsRegistryNames.ModAbilities.BREATH_FIRE);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.COST = config.COST;
        this.DAMAGE = config.DAMAGE;
        this.EFFECTS = config.EFFECTS;
        this.BREATH_FREQUENCY = config.FREQUENCY;
        this.INTERACT_WITH_TERRAIN = config.TERRAIN;
        this.BREATH_STAGE = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("fbcost", true, COST);
        final TranslationHelper.KeyEntry keybind1 = new TranslationHelper.KeyBindEntry("breathkb", ModKeyBindings.RACE_ABILITY.getDisplayName());
        return helper.formatAddVariables(key, renderID, key1, keybind1);
    }

    protected boolean DragonBreath(Entity entity) {
        if (this.isSpectator(entity)) {
            return false;
        }
        if (this.BREATH_STAGE > BREATH_FREQUENCY) {
            this.BREATH_STAGE = 0;
        }
        if (this.BREATH_STAGE == 0) {
            final MagicStats magic = Capabilities.getMagicStats(entity);
            if (magic != null) {
                if (!magic.spendMana(this.COST)) {
                    return false;
                }
            }
            int bcolor = Capabilities.getEntityProperties(entity, 16711680, (prop, color) -> prop.getRaceHandler().getSecondaryTraitColor());
            final World world = entity.getEntityWorld();
            final float headPosX = (float) (entity.posX + (1.8F * 1 * 0.3F * Math.cos(((entity.rotationYaw + 90) * Math.PI) / 180)));
            final float headPosZ = (float) (entity.posZ + (1.8F * 1 * 0.3F * Math.sin(((entity.rotationYaw + 90) * Math.PI) / 180)));
            final float headPosY = (float) ((entity.posY + (entity.getEyeHeight() * 0.8)));
            final double d2 = entity.getLookVec().x;
            final double d3 = entity.getLookVec().y;
            final double d4 = entity.getLookVec().z;
            world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((Reference.random.nextFloat() * 0.4F) + 0.8F));
            if (!world.isRemote) {
                //TODO Have a max life, tick it down, then kill the projectile, use the life to show decide on the look
                final EntityRangedAttack breath = new EntityRangedAttack(entity.getEntityWorld(), (EntityLivingBase) entity, d2, d3, d4, bcolor).setElement(this.getRequiredElement()).setEffects(this.EFFECTS).setAllowTerrainInteraction(this.INTERACT_WITH_TERRAIN);
                breath.setDamage(this.DAMAGE);
                breath.setPosition(headPosX, headPosY, headPosZ);
                breath.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 1.5F, 0.0F);
                world.spawnEntity(breath.setColor(bcolor));
            }
        }
        BREATH_STAGE += 1;
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.RACE_ABILITY.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        final MagicStats magic = Capabilities.getMagicStats(entity);
        if (magic != null) {
            return magic.getMana() >= this.COST;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(Entity entity, boolean Aux) {
        if (this.DragonBreath(entity)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyRelease(Entity entity, boolean Aux) {
        if (this.BREATH_STAGE != 0) {
            this.BREATH_STAGE = 0;
        }
        return true;
    }

    @Override
    public void loadStorage(NBTTagCompound compound) {
        if (compound.hasKey("COST")) {
            this.COST = compound.getFloat("COST");
        }
        if (compound.hasKey("DAMAGE")) {
            this.DAMAGE = compound.getFloat("DAMAGE");
        }
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        if (tag.hasKey("COST")) {
            this.COST = tag.getFloat("COST");
        }
        if (tag.hasKey("DAMAGE")) {
            this.DAMAGE = tag.getFloat("DAMAGE");
        }
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat("COST", this.COST);
        tag.setFloat("DAMAGE", this.DAMAGE);
        return tag;
    }
}
