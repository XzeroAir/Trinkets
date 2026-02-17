package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.network.AbilityCacheSyncPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.Utils.TempCache;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDragonsEye;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.EntryType;
import xzeroair.trinkets.util.config.ConfigHelper.TreasureEntry;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class AbilityBlockFinder extends Ability implements ITickableAbility, IToggleAbility, IKeyBindInterface {

    protected final ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
    protected final ClientConfigDragonsEye clientConfig = TrinketsConfig.CLIENT.items.DRAGON_EYE;

    protected boolean firstTick = false;

    protected TreeMap<Integer, TreasureEntry> TreasureBlocks = new TreeMap<>();
    protected TreeMap<Double, TempCache<Vec3d, TreasureEntry>> cache = new TreeMap<>();
    protected TreasureEntry targetTreasure;

    public AbilityBlockFinder() {
        super(Abilities.blockDetection);
        firstTick = true;
        targetTreasure = new TreasureEntry("");
        initTreasureBlocks();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.LangEntry(langKey, "treasurefinder", serverConfig.oreFinder);
        String oreTarget = getTreasure().isEmpty() ? "NONE" : getTreasure().parseTargetName();
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("target", serverConfig.oreFinder, oreTarget);
        final TranslationHelper.KeyEntry keybind1 = new TranslationHelper.KeyBindEntry("denvkb", ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName());
        final TranslationHelper.KeyEntry keybind2 = new TranslationHelper.KeyBindEntry("deofkb", ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName());
        final TranslationHelper.KeyEntry keybind3 = new TranslationHelper.KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
        return helper.formatAddVariables(key, renderID, key1, key2, keybind1, keybind2, keybind3);
    }

    private int targetValue = -1;

    public int getTargetValue() {
        return targetValue;
    }

    @Override
    public boolean abilityEnabled() {
        return targetValue > 0;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        targetValue = value;
        return this;
    }

    protected void IterateBlocks(EntityLivingBase entity, final Vec3d origin, final World world, AxisAlignedBB aabb) {
        final boolean closest = serverConfig.BLOCKS.closest;
//        final boolean closest = TrinketsConfig.getClientStore().DRAGON_EYE_OF_CLOSEST;
        if (!world.isRemote) {
            final TreasureEntry treasure = this.getTreasure(targetValue);
            if (treasure == null) {
                return;
            } else if (treasure.getObjectRegistryName().contentEquals("*:*")) {
                return;
            }
            TreeMap<Double, TempCache<Vec3d, TreasureEntry>> collection = new TreeMap<>();
            if (treasure.getObjectType().compareTo(EntryType.BLOCK) == 0 || treasure.getObjectType().compareTo(EntryType.OREDICTIONARY) == 0) {
//            System.out.println("" + treasure.parseTargetName() + "|" + treasure.getObjectRegistryName() + "|" + treasure.getObjectType());
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
                                collection.put(blockDist, new TempCache<>(pos, treasure));
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
                                    collection.put(entDist, new TempCache<>(pos, treasure));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            sendBlockCacheToPlayer(entity, collectBlocks(collection));
        } else {
            if (!cache.isEmpty()) {
                final Entry<Double, TempCache<Vec3d, TreasureEntry>> first = cache.firstEntry();
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
                    for (Entry<Double, TempCache<Vec3d, TreasureEntry>> e : cache.entrySet()) {
                        if (particleCount >= clientConfig.Particles_Max) {
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
        if (TrinketsConfig.getClientStore().DRAGON_EYE_OF_ENABLED) {
            final World world = entity.getEntityWorld();
            if (!world.isRemote) {
                final int length = TreasureBlocks.size();
                if (targetValue > length) {
                    targetValue = -1;
                }
                if (targetValue < 0) {
                    return;
                }
            }
            final int vd = serverConfig.BLOCKS.DR.C00_VD;
            final int hd = serverConfig.BLOCKS.DR.C001_HD;
            if (vd <= 0 || hd <= 0) {
                return;
            }
            final AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(hd, vd, hd);
            final int rf = clientConfig.Render_Cooldown;
            final Counter counter = tickHandler.getCounter("refresh_rate", rf, true, true, true, false);
            if (firstTick || counter.Tick()) {
                firstTick = false;
                this.IterateBlocks(entity, entity.getPositionVector(), world, aabb);
            }
        }
    }

    public TreasureEntry getTreasure() {
        return targetTreasure;
    }

    public TreasureEntry getTreasure(int mapID) {
        return TreasureBlocks.get(mapID);
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
        if (clientConfig.Dragon_Growl) {
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) entity;
                final boolean sneaking = clientConfig.Dragon_Growl_Sneak.contentEquals("SNEAK") && player.isSneaking();
                final boolean standing = clientConfig.Dragon_Growl_Sneak.contentEquals("STAND") && !player.isSneaking();
                final boolean both = clientConfig.Dragon_Growl_Sneak.contentEquals("BOTH");
                final float configVolume = clientConfig.Dragon_Growl_Volume;
                final float volume = ((configVolume / 100));
//                final int drV = TrinketsConfig.getClientStore().DRAGON_EYE_OF_VD;
//                final int drH = TrinketsConfig.getClientStore().DRAGON_EYE_OF_HD;
//                double test = MathHelper.sqrt((drH * drH) + (drV * drV));
//                if (test <= 0) {
//                    test = 1;
//                }
                if (((sneaking && !standing) == true) || ((standing && !sneaking) == true) || (both == true)) {
                    float v = (float) (1F - (distance / 10D)) * volume;
                    if (v > 1F) {
                        v = 1F;
                    } else if (v < 0F) {
                        v = 0F;
                    }
                    if (pos != null) {
                        player.world.playSound(player, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.PLAYERS, v, 1.0F);
                    }
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
        if (TrinketsConfig.getClientStore().DRAGON_EYE_OF_ENABLED) {

            final int rf = clientConfig.Render_Cooldown;
            final Counter counter = tickHandler.getCounter("refresh_rate", rf, true, true, true, false);
            counter.resetTick();

            if (!entity.world.isRemote) {
                int size = TreasureBlocks.size();
                final int off = -1;
                final int max = size - 1;
                if (!Aux) {
                    targetValue++;
                } else {
                    targetValue--;
                }
                if (targetValue >= size) {
                    targetValue = off;
                } else if (targetValue < off) {
                    targetValue = max;
                }
                firstTick = true;
                TranslationHelper helper = TranslationHelper.INSTANCE;
                if ((targetValue != -1)) {
                    final ConfigHelper.TreasureEntry entry = getTreasure(targetValue);
                    targetTreasure = entry;
                    final String target = entry.parseTargetName().trim();
                    final String entryRegName = entry == null ? "NULL" : entry.getObjectRegistryName();
                    final String NotFound = helper.formatAddVariables(new TextComponentTranslation(getTranslationKey() + ".treasurefinder.notfound").getFormattedText(), new TranslationHelper.OptionEntry("target", true, entryRegName), new TranslationHelper.OptionEntry("looking", true, helper.toggleCheckTranslation(true)));
                    final String FoundTarget = helper.formatAddVariables(new TextComponentTranslation(getTranslationKey() + ".treasurefinder.on").getFormattedText(), new TranslationHelper.OptionEntry("target", true, target), new TranslationHelper.OptionEntry("looking", true, helper.toggleCheckTranslation(true)));
                    final String message = target.isEmpty() ? NotFound : FoundTarget;
                    StringUtils.sendStatusMessageToPlayer(entity, message, true);
                } else { // Is On
                    targetTreasure = TreasureEntry.EMPTY;
                    sendBlockCacheToPlayer(entity, collectBlocks(null));
                    final String message = helper.formatAddVariables(new TextComponentTranslation(getTranslationKey() + ".treasurefinder.off").getFormattedText(), new TranslationHelper.OptionEntry("looking", true, helper.toggleCheckTranslation(false)));
                    StringUtils.sendStatusMessageToPlayer(entity, message, true);
                }
            } else {
                if (!cache.isEmpty()) {
                    cache.clear();
                }
            }
            return true;
        }
        return false;
    }

    protected void sendBlockCacheToPlayer(Entity entity, NBTTagCompound tag) {
        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP && tag != null && !tag.isEmpty()) {
            NetworkHandler.sendTo(new AbilityCacheSyncPacket(((EntityPlayerMP) entity), tag), (EntityPlayerMP) entity);
        }
    }

    protected NBTTagCompound collectBlocks(TreeMap<Double, TempCache<Vec3d, TreasureEntry>> collection) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Ability", this.getRegistryName().toString());
        tag.setString("Target", targetTreasure.getOriginalEntry());
        if (collection != null && !collection.isEmpty()) {
            int index = 0;
            for (Entry<Double, TempCache<Vec3d, TreasureEntry>> e : collection.entrySet()) {
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
    public void loadTagCacheFromNBT(NBTTagCompound tag) {
        if (!cache.isEmpty()) {
            cache.clear();
        }
        if (tag != null && !tag.isEmpty()) {
            TreasureEntry entry = TreasureEntry.EMPTY;
            if (tag.hasKey("Target")) {
                String t = tag.getString("Target");
                if (!t.isEmpty()) {
                    entry = new TreasureEntry(t);
                }
            }
            targetTreasure = entry;
            for (int i = 0; i < tag.getSize(); i++) {
                NBTTagCompound blockTag = tag.getCompoundTag(i + "");
                if (blockTag != null && !blockTag.isEmpty()) {
                    if (blockTag.hasKey("Distance") && blockTag.hasKey("x") && blockTag.hasKey("y") && blockTag.hasKey("z")) {
                        double distance = blockTag.getDouble("Distance");
                        Vec3d vec = new Vec3d(blockTag.getDouble("x"), blockTag.getDouble("y"), blockTag.getDouble("z"));
//                        TreasureEntry treasure = new TreasureEntry(blockTag.getString("Target"));
                        cache.put(distance, new TempCache<>(vec, targetTreasure));
                    }
                }
            }
        }
    }

    public void initTreasureBlocks() {
        if (!TreasureBlocks.isEmpty()) {
            TreasureBlocks.clear();
        }
        final String[] treasures = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks;
        int index = 0;
        for (String entry : treasures) {
            TreasureEntry treasure = new TreasureEntry(entry);
            boolean existsCheck = treasure.getObjectType().compareTo(EntryType.OREDICTIONARY) == 0 ? true : Block.getBlockFromName(treasure.getObjectRegistryName()) != null;
            if (!treasure.isEmpty() && existsCheck) {
                TreasureBlocks.put(index, treasure);
                index++;
            }
        }
    }

}
