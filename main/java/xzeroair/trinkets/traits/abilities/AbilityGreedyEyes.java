package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.enums.ActivationMethod;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.Utils.TempCache;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigTreasureObject;
import xzeroair.trinkets.util.config.ConfigHelper.EntryType;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityGreedyEyes;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class AbilityGreedyEyes extends Ability implements ITickableAbility, IToggleAbility, IKeyBindInterface {

    protected ConfigAbilityGreedyEyes CONFIG;

    protected TreeMap<Integer, ConfigTreasureObject> TreasureBlocks = new TreeMap<>();
    protected TreeMap<Double, TempCache<Vec3d, ConfigTreasureObject>> cache = new TreeMap<>();
    protected ConfigTreasureObject targetTreasure;

    protected boolean wasEmptyLastSync;

    protected float COST;

    public AbilityGreedyEyes() {
        this(TrinketsConfig.SERVER.ABILITIES.GREEDY_EYES);
    }

    public AbilityGreedyEyes(ConfigAbilityGreedyEyes config) {
        super(TrinketsRegistryNames.ModAbilities.GREEDY_EYES);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.COST = config.COST;
        this.wasEmptyLastSync = true;
        this.targetTreasure = new ConfigTreasureObject("");
        this.initTreasureBlocks();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = this.getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.LangEntry(langKey, "treasurefinder", true);
        String oreTarget = this.getTreasure().isEmpty() ? "NONE" : this.getTreasure().parseTargetName();
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("target", true, oreTarget);
        final TranslationHelper.KeyEntry keybind1 = new TranslationHelper.KeyBindEntry("denvkb", ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName());
        final TranslationHelper.KeyEntry keybind2 = new TranslationHelper.KeyBindEntry("deofkb", ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName());
        final TranslationHelper.KeyEntry keybind3 = new TranslationHelper.KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
        return helper.formatAddVariables(key, renderID, key1, key2, keybind1, keybind2, keybind3);
    }

    private int targetValue = -1;

    @Override
    public int getToggleMode() {
        return this.targetValue;
    }

    @Override
    public boolean isAbilityToggled() {
        return this.targetValue > -1;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        int size = this.TreasureBlocks.size();
        final int off = -1;
        final int max = size - 1;
        if (enabled) {
            this.targetValue++;
        } else {
            this.targetValue--;
        }
        if (this.targetValue >= size) {
            this.targetValue = off;
        } else if (this.targetValue < off) {
            this.targetValue = max;
        }
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        int size = this.TreasureBlocks.size();
        final int off = -1;
        final int max = size - 1;
        if (this.targetValue != value) {
            this.targetValue = value;
        }
        if (this.targetValue >= size) {
            this.targetValue = off;
        } else if (this.targetValue < off) {
            this.targetValue = max;
        }
        return this;
    }

    protected void IterateBlocks(EntityLivingBase entity, final Vec3d origin, @Nonnull final World world, AxisAlignedBB aabb) {
        final boolean closest = this.CONFIG.CLOSEST;
        if (!world.isRemote) {
            if (!this.cache.isEmpty()) {
                this.cache.clear();
            }
            final ConfigTreasureObject treasure = this.getTreasure(this.getToggleMode());
            if (treasure == null || treasure.isEmpty() || treasure.getObjectRegistryName().contentEquals("*:*")) {
                return;
            }
//            System.out.println("" + treasure.parseTargetName() + "|" + treasure.getObjectRegistryName() + "|" + treasure.getObjectType());
            if (treasure.getObjectType().compareTo(EntryType.NORMAL) == 0 || treasure.getObjectType().compareTo(EntryType.BLOCK) == 0 || treasure.getObjectType().compareTo(EntryType.OREDICTIONARY) == 0) {
                final int i = MathHelper.floor(aabb.minX);
                final int j = MathHelper.floor(aabb.maxX + 1.0D);
                final int k = MathHelper.floor(aabb.minY);
                final int l = MathHelper.floor(aabb.maxY + 1.0D);
                final int i1 = MathHelper.floor(aabb.minZ);
                final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
                boolean foundIt = false;
                for (int k1 = i; k1 < j; ++k1) {
                    for (int l1 = k; l1 < l; ++l1) {
                        for (int i2 = i1; i2 < j1; ++i2) {
                            final Vec3d pos = new Vec3d(k1, l1, i2);
                            final BlockPos bPos = new BlockPos(pos);
                            final IBlockState state = world.getBlockState(bPos);
                            final Block block = state.getBlock();
                            if (!(block.isAir(state, world, bPos)) && treasure.doesBlockMatchEntry(state)) {
                                foundIt = true;
                                double blockDist = pos.distanceTo(origin);
                                this.cache.put(blockDist, new TempCache<>(pos, treasure));
                            }
                        }
                    }
                }
            }
            if (treasure.getObjectType().compareTo(EntryType.ENTITY) == 0) {
                try {
                    final ResourceLocation isEntityRegName = new ResourceLocation(treasure.getObjectRegistryName());
                    if (EntityList.isRegistered(isEntityRegName)) {
                        final Class<? extends Entity> e = EntityList.getClass(isEntityRegName);
                        if (e != null) {
                            final List<? extends Entity> entList = world.getEntitiesWithinAABB(e, aabb);
                            if (!entList.isEmpty()) {
                                for (final Entity targetEntity : entList) {
                                    final Vec3d pos = targetEntity.getPositionVector();
                                    final double entDist = pos.distanceTo(origin);
                                    this.cache.put(entDist, new TempCache<>(pos, treasure));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            boolean isEmpty = !this.wasEmptyLastSync && this.cache.isEmpty();
            boolean wasEmpty = this.wasEmptyLastSync && !this.cache.isEmpty();
            boolean bothEmpty = this.wasEmptyLastSync && this.cache.isEmpty();
            if (!bothEmpty) {
                this.wasEmptyLastSync = isEmpty;
                this.setChanged(true);
            }
        } else {
            if (!this.cache.isEmpty()) {
                final Entry<Double, TempCache<Vec3d, ConfigTreasureObject>> first = this.cache.firstEntry();
                final double distance = first.getKey();
                if (first.getKey() > 1.8) {
                    this.playSound(entity, new BlockPos(first.getValue().getFirst()), distance);
                }
                if (closest) {
                    for (int p1 = 0; p1 < 3; p1++) {
                        this.SpawnParticle(entity.getEntityWorld(), first.getValue().getFirst(), first.getValue().getSecond().getColor());
                    }
                } else {
                    int particleCount = 0;
                    for (Entry<Double, TempCache<Vec3d, ConfigTreasureObject>> e : this.cache.entrySet()) {
                        if (particleCount >= this.CONFIG.CLIENT.PARTICLES) {
                            break;
                        }
                        for (int p1 = 0; p1 < 3; p1++) {
                            this.SpawnParticle(entity.getEntityWorld(), e.getValue().getFirst(), e.getValue().getSecond().getColor());
                            particleCount++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final World world = entity.getEntityWorld();
        if (!world.isRemote) {
            if (!this.isAbilityToggled()) {
                return;
            }
        }
        final int vd = this.CONFIG.RANGE.RANGE_VERTICAL;
        final int hd = this.CONFIG.RANGE.RANGE_HORIZONTAL;
        if (vd <= 0 || hd <= 0) {
            return;
        }
        final AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(hd, vd, hd);
        final Counter counter = this.tickHandler.getCounter("refresh_rate", this.CONFIG.FREQUENCY, true, true, true, false);
        if (this.isFirstUpdate() || counter.Tick()) {
            if (this.COST > 0F) {
                if (!world.isRemote) {
                    if (Capabilities.getMagicStats(entity, false, (magic, rtn) -> magic.spendMana(this.COST))) {
                        this.IterateBlocks(entity, entity.getPositionVector(), world, aabb);
                    } else {
                        TranslationHelper helper = TranslationHelper.INSTANCE;
                        this.targetTreasure = ConfigTreasureObject.EMPTY;
                        final String message = helper.formatAddVariables(new TextComponentTranslation(this.getTranslationKey() + ".treasurefinder.off").getFormattedText(), new TranslationHelper.OptionEntry("looking", true, helper.toggleCheckTranslation(false)));
                        StringUtils.sendStatusMessageToPlayer(entity, message, true);
                        this.toggleAbility(-1);
                    }
                } else {
                    this.IterateBlocks(entity, entity.getPositionVector(), world, aabb);
                }
            } else {
                this.IterateBlocks(entity, entity.getPositionVector(), world, aabb);
            }
        }
    }

    public ConfigTreasureObject getTreasure() {
        return this.targetTreasure;
    }

    public ConfigTreasureObject getTreasure(int mapID) {
        return this.TreasureBlocks.get(mapID);
    }

//    @SideOnly(Side.CLIENT)
//    protected void drawPath(EntityLivingBase player, Vec3d target, int color) {
//        final double d = player.getDistance(target.x, target.y, target.z);
//        if (d > 2) {
//            final RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, d, 1D, false);
//            GlStateManager.pushMatrix();
//            RayTraceHelper.drawPath(player.getPositionVector().add(0, player.getEyeHeight() * 0.8, 0), target, player.world, beam, color, 2);
//            GlStateManager.popMatrix();
//        }
//    }

    @SideOnly(Side.CLIENT)
    protected void SpawnParticle(World world, Vec3d pos, int color) {
        final double X = Reference.random.nextDouble() + pos.x;
        final double Y = Reference.random.nextDouble() + pos.y;
        final double Z = Reference.random.nextDouble() + pos.z;
        GlStateManager.pushMatrix();
        final ParticleGreed effect = new ParticleGreed(world, new Vec3d(X, Y, Z), color, 1F, false);
        Minecraft.getMinecraft().effectRenderer.addEffect(effect);
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    protected void playSound(EntityLivingBase entity, BlockPos pos, double distance) {
        final ActivationMethod method = this.CONFIG.CLIENT.GROWL_ACTIVATION;
        if (method != ActivationMethod.NEVER) {
            final EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
            final boolean sneaking = method == ActivationMethod.SNEAK && entity.isSneaking();
            final boolean standing = method == ActivationMethod.STAND && !entity.isSneaking();
            if ((method == ActivationMethod.ALWAYS || sneaking || standing)) {
                final float configVolume = this.CONFIG.CLIENT.VOLUME;
                final float volume = ((configVolume / 100));
                float v = (float) (1F - (distance / 10D)) * volume;
                if (v > 1F) {
                    v = 1F;
                } else if (v < 0F) {
                    v = 0F;
                }
                if (pos != null) {
                    entity.world.playSound(player, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.PLAYERS, v, 1.0F);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        final Counter counter = this.tickHandler.getCounter("refresh_rate", this.CONFIG.FREQUENCY, true, true, true, false);
        counter.setTick(Math.max(this.CONFIG.FREQUENCY / 5, 30));

        if (!entity.world.isRemote) {
            this.toggleAbility(!Aux);
            TranslationHelper helper = TranslationHelper.INSTANCE;
            if (this.isAbilityToggled()) {
                final ConfigTreasureObject entry = this.getTreasure(this.getToggleMode());
                this.targetTreasure = entry;
                final String target = entry.parseTargetName().trim();
                final String entryRegName = entry.getObjectRegistryName();
                final String NotFound = helper.formatAddVariables(new TextComponentTranslation(this.getTranslationKey() + ".treasurefinder.notfound").getFormattedText(), new TranslationHelper.OptionEntry("target", true, entryRegName), new TranslationHelper.OptionEntry("looking", true, helper.toggleCheckTranslation(true)));
                final String FoundTarget = helper.formatAddVariables(new TextComponentTranslation(this.getTranslationKey() + ".treasurefinder.on").getFormattedText(), new TranslationHelper.OptionEntry("target", true, target), new TranslationHelper.OptionEntry("looking", true, helper.toggleCheckTranslation(true)));
                final String message = target.isEmpty() ? NotFound : FoundTarget;
                StringUtils.sendStatusMessageToPlayer(entity, message, true);
            } else { // Is On
                this.targetTreasure = ConfigTreasureObject.EMPTY;
                final String message = helper.formatAddVariables(new TextComponentTranslation(this.getTranslationKey() + ".treasurefinder.off").getFormattedText(), new TranslationHelper.OptionEntry("looking", true, helper.toggleCheckTranslation(false)));
                StringUtils.sendStatusMessageToPlayer(entity, message, true);
            }
        } else {
            if (!this.cache.isEmpty()) {
                this.cache.clear();
                this.setChanged(true);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        return this.collectBlocks(this.cache);
    }

    protected NBTTagCompound collectBlocks(TreeMap<Double, TempCache<Vec3d, ConfigTreasureObject>> collection) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Empty", this.wasEmptyLastSync);
        if (collection != null && !collection.isEmpty()) {
            tag.setString("Target", this.targetTreasure.getOriginalEntry());
            int index = 0;
            for (Entry<Double, TempCache<Vec3d, ConfigTreasureObject>> e : collection.entrySet()) {
                double distance = e.getKey();
                Vec3d vec = e.getValue().getFirst();
                NBTTagCompound blockTag = new NBTTagCompound();
                blockTag.setDouble("Distance", distance);
                blockTag.setDouble("x", vec.x);
                blockTag.setDouble("y", vec.y);
                blockTag.setDouble("z", vec.z);
                tag.setTag(index + "", blockTag);
                index++;
            }
        }
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        if (!this.cache.isEmpty()) {
            this.cache.clear();
        }
        if (tag != null && !tag.isEmpty()) {
            if (tag.hasKey("Empty")) {
                this.wasEmptyLastSync = tag.getBoolean("Empty");
            }
            ConfigTreasureObject entry = ConfigTreasureObject.EMPTY;
            boolean hasTarget = false;
            if (tag.hasKey("Target")) {
                String t = tag.getString("Target");
                if (!t.isEmpty()) {
                    entry = new ConfigTreasureObject(t);
                    hasTarget = true;
                }
            }
            this.targetTreasure = hasTarget ? entry : ConfigTreasureObject.EMPTY;
            if (hasTarget) {
                for (int i = 0; i < tag.getSize(); i++) {
                    NBTTagCompound blockTag = tag.getCompoundTag(i + "");
                    if (!blockTag.isEmpty()) {
                        if (blockTag.hasKey("Distance") && blockTag.hasKey("x") && blockTag.hasKey("y") && blockTag.hasKey("z")) {
                            double distance = blockTag.getDouble("Distance");
                            Vec3d vec = new Vec3d(blockTag.getDouble("x"), blockTag.getDouble("y"), blockTag.getDouble("z"));
                            this.cache.put(distance, new TempCache<>(vec, entry));
                        }
                    }
                }
            }
        }
    }

    public void initTreasureBlocks() {
        if (!this.TreasureBlocks.isEmpty()) {
            this.TreasureBlocks.clear();
        }
        final String[] treasures = this.CONFIG.BLOCKS;
        int index = 0;
        for (String entry : treasures) {
            ConfigTreasureObject treasure = new ConfigTreasureObject(entry);
            boolean oreDictEntry = treasure.getObjectType().compareTo(EntryType.OREDICTIONARY) == 0;
            boolean blockEntry = Block.getBlockFromName(treasure.getObjectRegistryName()) != null;
            boolean entityEntry = false;
            if (treasure.getObjectType().compareTo(EntryType.ENTITY) == 0) {
                try {
                    entityEntry = EntityList.isRegistered(new ResourceLocation(treasure.getObjectRegistryName()));
                } catch (Exception ignored) {
                }
            }
            boolean existsCheck = oreDictEntry || blockEntry || entityEntry;
            if (!treasure.isEmpty() && existsCheck) {
                this.TreasureBlocks.put(index, treasure);
                index++;
            }
        }
    }

}
