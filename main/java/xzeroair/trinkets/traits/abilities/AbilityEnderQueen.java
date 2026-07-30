package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.ai.EnderQueensKnightAI;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.TrinketsDamageSource;
import xzeroair.trinkets.traits.abilities.interfaces.*;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityEnderQueen;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class AbilityEnderQueen extends Ability implements ITickableAbility, IPotionAbility, IAttackAbility, IKeyBindInterface, IToggleAbility {

    protected final ConfigAbilityEnderQueen CONFIG;

    protected boolean TOGGLED;
    protected int MODE;
    protected float ACTIVE_COST, HURT_COST, CHEST_COST;

    public AbilityEnderQueen() {
        this(TrinketsConfig.SERVER.ABILITIES.ENDER_QUEEN);
    }

    public AbilityEnderQueen(@Nonnull ConfigAbilityEnderQueen config) {
        super(TrinketsRegistryNames.ModAbilities.ENDER_QUEEN);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.ACTIVE_COST = 0.5F;
        this.HURT_COST = 0.25F;
        this.CHEST_COST = 1.0F;
        this.TOGGLED = true;
        this.MODE = -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String langOutput, int rendMod, int renderID, int compatID) {
//        String langKey = this.getTranslationKey();
        final KeyEntry key1 = new OptionEntry("dmgchance", this.CONFIG.IGNORE_CHANCE > 0, MathHelper.clamp((1F / this.CONFIG.IGNORE_CHANCE) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
        final KeyEntry key2 = new OptionEntry("spawnchance", this.CONFIG.SPAWN_CHANCE > 0, MathHelper.clamp((1F / this.CONFIG.SPAWN_CHANCE) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
        final KeyEntry key3 = new OptionEntry("follow", this.CONFIG.ENDERMAN_FOLLOW, "");
        final KeyEntry key4 = new OptionEntry("waterhurts", this.CONFIG.WATER_HURTS, "");
        final KeyEntry key5 = new OptionEntry("tpchance", this.CONFIG.TELEPORT_CHANCE > 0, MathHelper.clamp((1F / this.CONFIG.TELEPORT_CHANCE) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
        final KeyEntry key6 = new OptionEntry("hurtcost", this.CONFIG.TELEPORT_CHANCE > 0, (this.HURT_COST) * 100 + "%");
        final KeyEntry key7 = new OptionEntry("tpcost", this.CONFIG.TELEPORT_CHANCE > 0, (this.ACTIVE_COST) * 100 + "%");
        final KeyEntry key8 = new OptionEntry("chest", this.CONFIG.ENDER_CHEST, "");
        final KeyEntry key9 = new OptionEntry("chestcost", this.CONFIG.ENDER_CHEST, (this.CHEST_COST) * 100 + "%");
        final KeyEntry key10 = new KeyBindEntry("tpkey", ModKeyBindings.ENDER_CROWN.getDisplayName());
        final KeyEntry key11 = new KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
        return helper.formatAddVariables(langOutput, renderID, key1, key2, key3, key4, key5, key6, key7, key8, key9, key10, key11);
    }

    @Override
    public void tickAbility(@Nonnull EntityLivingBase entity) {
        if (entity.world.isRemote) {
            return;
        }
        if (true) {
            ItemStack stack = this.getAbilityHolder().getInfo().getStackFromHandler(entity);
            Capabilities.getTrinketProperties(stack, prop -> {
                if (this.isAbilityToggled() != prop.mainAbility()) {
                    this.toggleAbility(prop.mainAbility());
                    this.sendMessageToPlayer(entity);
                    this.setChanged(false);
                }
            });
        }
        if (this.CONFIG.WATER_HURTS) {
            final Counter counter = this.tickHandler.getCounter("water_hurt", 20, true, true, true, true);
            if (counter.Tick()) {
                if ((entity.isInWater() || entity.isWet())) {
                    final MagicStats magic = Capabilities.getMagicStats(entity);
                    if ((magic != null) && magic.spendMana(5F)) {
                        magic.setManaRegenTimeout(TrinketsConfig.SERVER.MAGIC.mana_regen_timeout * 2);
                    } else {
                        if (TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketDragonsEye)) {
                            entity.attackEntityFrom(TrinketsDamageSource.water.setDamageBypassesArmor().setMagicDamage(), 4);
                        } else {
                            entity.attackEntityFrom(TrinketsDamageSource.water.setDamageBypassesArmor().setMagicDamage(), 2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAbilityAdded(EntityLivingBase entity) {
        super.onAbilityAdded(entity);
        if (true) {
            ItemStack stack = this.getAbilityHolder().getInfo().getStackFromHandler(entity);
            Capabilities.getTrinketProperties(stack, prop -> {
                if (this.isAbilityToggled() != prop.mainAbility()) {
                    this.toggleAbility(prop.mainAbility());
                    this.sendMessageToPlayer(entity);
                    this.setChanged(false);
                }
            });
        }
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        super.onAbilityRemoved(entity);
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, @Nonnull PotionEffect effect, boolean cancel) {
        final ResourceLocation e = effect.getPotion().getRegistryName();
        final Potion instability = LycanitesCompat.getPotionByName("instability");
        if ((instability != null) && (e.compareTo(instability.getRegistryName()) == 0)) {
            return true;
        }
        return cancel;
    }

    @Override
    public boolean attacked(@Nonnull EntityLivingBase attacked, @Nonnull DamageSource source, float dmg, boolean cancel) {
        final boolean client = attacked.getEntityWorld().isRemote;
        final Entity attacker = source.getTrueSource();
        if (!client) {
            if ((attacker instanceof EntityLivingBase) && (attacker != attacked) && (dmg > 0)) {
                this.spawnEnderman(attacked, (EntityLivingBase) attacker);
                this.teleportOnHurt(attacked, source, dmg);
                return this.blockDamage(attacked, source, dmg, cancel);
            }
        }
        return cancel;
    }

    private void spawnEnderman(EntityLivingBase attacked, EntityLivingBase attacker) {
        final int chanceNum = this.CONFIG.SPAWN_CHANCE;
        if (chanceNum > 0) {
            final int chance = this.random.nextInt(chanceNum);
            if ((chance == 0)) {
                try {
                    final EntityEnderman knight = new EntityEnderman(attacked.getEntityWorld());
                    final double x = attacked.getPosition().getX();
                    final double y = attacked.getPosition().getY();
                    final double z = attacked.getPosition().getZ();
                    knight.setPosition(x, y, z);
                    knight.getEntityData().setBoolean(EnderQueensKnightAI.SUMMONED_TAG, true);
                    knight.getEntityData().setString(EnderQueensKnightAI.QUEEN_UUID_TAG, attacked.getCachedUniqueIdString());
                    knight.setCanPickUpLoot(false);
                    attacked.getEntityWorld().spawnEntity(knight);
                    knight.setAttackTarget(attacker);
                    final String summonEnderman = "Go, my loyal subject!";
                    StringUtils.sendMessageToPlayer(attacked, TextFormatting.BOLD + "" + TextFormatting.GOLD + summonEnderman, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean blockDamage(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        final int chanceNum = this.CONFIG.IGNORE_CHANCE;
        if (chanceNum > 0) {
            final int chance = this.random.nextInt(chanceNum);
            if ((chance == 0)) {
                String blockDamage = "The void protects me!";
                if (TrinketsConfig.SERVER.MISC.VIPS) {
                    final VipStatus vip = Capabilities.getVipStatus(attacked);
                    if (vip != null) {
                        final String quote = vip.getRandomQuote();
                        if (!quote.isEmpty()) {
                            blockDamage = quote;
                        }
                    }
                }
                StringUtils.sendStatusMessageToPlayer(attacked, TextFormatting.BOLD + "" + TextFormatting.GOLD + blockDamage, false);
                return true;
            }
        }
        return cancel;
    }

    private boolean teleportOnHurt(EntityLivingBase attacked, DamageSource source, float dmg) {
        if ((source instanceof EntityDamageSourceIndirect) && this.isAbilityToggled()) {
            final int chanceNum = this.CONFIG.TELEPORT_CHANCE;
            if (chanceNum > 0) {
                final int chance = this.random.nextInt(chanceNum);
                if ((chance == 0)) {
                    final boolean isBoss = TrinketHelper.isEntityBoss(source.getTrueSource());
                    final MagicStats magic = Capabilities.getMagicStats(attacked);
                    if (!isBoss && !attacked.isActiveItemStackBlocking()) {
                        if ((magic != null) && (magic.getMana() >= (magic.getMaxMana() * this.HURT_COST))) {
                            for (int i = 0; i < 32; ++i) {
                                if (this.teleportRandomly(attacked, (magic.getMaxMana() * this.HURT_COST))) {
                                    return true;
                                }
                            }
                            this.sendFailMessage(attacked);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void targetedByEnemy(EntityLivingBase enemy) {
        if (!this.CONFIG.ENDERMAN_RETALIATE && (enemy instanceof EntityEnderman)) {
            if (enemy instanceof EntityLiving) {
                EntityLivingBase target = ((EntityLiving) enemy).getAttackTarget();
                if (target != null) {
                    ((EntityLiving) enemy).setAttackTarget(null);
                }
            }
        }
    }

    @Override
    public int killedEntityExpDrop(EntityLivingBase target, int originalExp, int droppedExp) {
        if (!this.CONFIG.ENDERMAN_DROP_EXP && (target instanceof EntityEnderman)) {
            return 0;
        }
        return droppedExp;
    }

    @Override
    public void killedEntityItemDrops(EntityLivingBase target, DamageSource source, int lootingLevel, List<EntityItem> drops) {
        if (!this.CONFIG.ENDERMAN_DROP_ITEMS && (target instanceof EntityEnderman)) {
            if (!drops.isEmpty()) {
                drops.clear();
            }
        }
    }

    @Override
    public float damageEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if ((target instanceof EntityEnderman) && (dmg > 0)) {
            try {
                final EntityEnderman enderman = (EntityEnderman) target;
                NBTTagCompound data = enderman.getEntityData();
                data.setBoolean("xat:summoned", true);
            } catch (Exception e) {
            }
        }
        return dmg;
    }

    protected boolean teleportRandomly(@Nonnull EntityLivingBase entity, float cost) {
        final double d0 = entity.posX + ((Reference.random.nextDouble() - 0.5D) * 32.0D);
        final double d1 = entity.posY + (Reference.random.nextInt(16) - 8);
        final double d2 = entity.posZ + ((Reference.random.nextDouble() - 0.5D) * 32.0D);
        return this.teleportTo(entity, d0, d1, d2, cost);
    }

    private boolean teleportTo(EntityLivingBase entity, double x, double y, double z, float cost) {
        final net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entity, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            if (entity instanceof EntityPlayerMP) {
                if (entity.isRiding()) {
                    entity.dismountRidingEntity();
                }
                if (entity.attemptTeleport(x, y, z)) {
                    MagicStats magic = Capabilities.getMagicStats(entity);
                    if (magic != null && magic.spendMana(cost)) {
                        if (entity.fallDistance > 0.0F) {
                            entity.fallDistance = 0.0F;
                        }
                        entity.lastTickPosX = entity.posX;
                        entity.lastTickPosY = entity.posY;
                        entity.lastTickPosZ = entity.posZ;
                        entity.world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
                        return true;
                    }
                    return false;
                }
            }
            //			} else {
            return false;
        } else {
            if (this.attemptTeleport(entity, x, y, z)) {
                MagicStats magic = Capabilities.getMagicStats(entity);
                if (magic != null && magic.spendMana(cost)) {
                    entity.world.playSound(null, entity.prevPosX, entity.prevPosY, entity.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F);
                    entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
                    return true;
                }
            }
            return false;
        }
    }

    protected boolean teleportToEntity(@Nonnull EntityLivingBase entity, @Nonnull Entity target, float cost) {
        Vec3d vec3d = new Vec3d(entity.posX - target.posX, ((entity.getEntityBoundingBox().minY + (entity.height / 2.0F)) - target.posY) + target.getEyeHeight(), entity.posZ - target.posZ);
        vec3d = vec3d.normalize();
        final double d0 = 16.0D;
        final double d1 = (entity.posX + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.x * d0);
        final double d2 = (entity.posY + (Reference.random.nextInt(16) - 8)) - (vec3d.y * 16.0D);
        final double d3 = (entity.posZ + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.z * d0);
        return this.teleportTo(entity, d1, d2, d3, cost);
    }

    protected boolean attemptTeleport(@Nonnull EntityLivingBase entity, double x, double y, double z) {
        final double d0 = entity.posX;
        final double d1 = entity.posY;
        final double d2 = entity.posZ;
        entity.posX = x;
        entity.posY = y;
        entity.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entity);
        final World world = entity.world;
        final Random random = this.random;

        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && (blockpos.getY() > 0)) {
                final BlockPos blockpos1 = blockpos.down();
                final IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --entity.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                //				entity.setPositionAndUpdate(d0, d1, d2);
                entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

                if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {
            final int i = 128;

            for (int j = 0; j < 128; ++j) {
                final double d6 = j / 127.0D;
                final float f = (random.nextFloat() - 0.5F) * 0.2F;
                final float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                final float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                final double d3 = d0 + ((entity.posX - d0) * d6) + ((random.nextDouble() - 0.5D) * entity.width * 2.0D);
                final double d4 = d1 + ((entity.posY - d1) * d6) + (random.nextDouble() * entity.height);
                final double d5 = d2 + ((entity.posZ - d2) * d6) + ((random.nextDouble() - 0.5D) * entity.width * 2.0D);
                world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
            }

            if (entity instanceof EntityCreature) {
                ((EntityCreature) entity).getNavigator().clearPath();
            }

            return true;
        }
    }

    public void openEnderChest(@Nonnull Entity entity) {
        final World world = entity.getEntityWorld();
        if ((entity instanceof EntityPlayer) && !world.isRemote && this.CONFIG.ENDER_CHEST) {
            EntityPlayer player = (EntityPlayer) entity;
            InventoryEnderChest inventoryenderchest = player.getInventoryEnderChest();
            player.displayGUIChest(inventoryenderchest);
            player.addStat(StatList.ENDERCHEST_OPENED);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.ENDER_CROWN.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }

    @Override
    public boolean sendMessageToPlayer(Entity entity) {
        if ((entity instanceof EntityPlayer)) {
            final boolean client = entity.world.isRemote;
            if (!client) {
                final TranslationHelper helper = TranslationHelper.INSTANCE;
                final String magnetMode = new TextComponentTranslation(this.getTranslationKey() + ".teleport").getFormattedText();
                final KeyEntry key = new OptionEntry("toggle", this.CONFIG.TELEPORT_CHANCE > 0, helper.toggleCheckTranslation(this.isAbilityToggled()));
                StringUtils.sendStatusMessageToPlayer(entity, helper.formatAddVariables(magnetMode, key), true);
                return true;
            }
        }
        return false;
    }

    public void sendFailMessage(@Nonnull Entity entity) {
        final boolean client = entity.world.isRemote;
        if (!client) {
            final TranslationHelper helper = TranslationHelper.INSTANCE;
            final String magnetMode = new TextComponentTranslation(this.getTranslationKey() + ".teleport.failed").getFormattedText();
            final KeyEntry key = new OptionEntry("toggle", this.CONFIG.TELEPORT_CHANCE > 0, helper.toggleCheckTranslation(this.isAbilityToggled()));
            StringUtils.sendStatusMessageToPlayer(entity, helper.formatAddVariables(magnetMode, key), true);
        }
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        if (!Aux) {
            return Capabilities.getMagicStats(entity, this.CONFIG.TELEPORT_CHANCE > 0, (magic, allow) -> {
                boolean isRemote = magic.getEntity().world.isRemote;
                if (!isRemote && allow && (magic.getMana() >= (magic.getMaxMana() * this.ACTIVE_COST))) {
                    for (int i = 0; i < 32; ++i) {
                        if (this.teleportRandomly(magic.getEntity(), magic.getMaxMana() * this.ACTIVE_COST)) {
                            return true;
                        }
                    }
                    this.sendFailMessage(entity);
                }
                return allow;
            });
        } else {
            return Capabilities.getMagicStats(entity, this.CONFIG.ENDER_CHEST, (magic, allow) -> {
                boolean isRemote = magic.getEntity().world.isRemote;
                if (!isRemote && allow && magic.spendMana(magic.getMaxMana())) {
                    this.openEnderChest(magic.getEntity());
                    return true;
                }
                return isRemote;
            });
        }
    }

    @Override
    public boolean isAbilityToggled() {
        return this.TOGGLED;
    }

    @Override
    public int getToggleMode() {
        return this.MODE;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        if (this.TOGGLED != enabled) {
            this.TOGGLED = enabled;
            this.setChanged(true);
        }
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        return this.toggleAbility(value > 0);
    }

}
