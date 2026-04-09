package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.network.AbilityCacheSyncPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigTreasureObject;
import xzeroair.trinkets.util.config.ConfigHelper.EntryType;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityClimbing;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.TreeMap;

public class AbilityClimbing extends Ability implements ITickableAbility {

    //    private float entityHeight;
//    private BlockPos entityPos, bodyPos, headPos, headSpacePos, frontBodyPos, frontHeadPos;
//    private IBlockState body, head, headSpace, frontBody, frontHead;
    protected boolean useWhitelist, canClimb = false;
    protected TreeMap<Integer, ConfigObject> climbBlocks = new TreeMap<>();

    protected ConfigAbilityClimbing CONFIG;

    public AbilityClimbing() {
        this(TrinketsConfig.SERVER.ABILITIES.CLIMBING);
    }

    public AbilityClimbing(@Nonnull ConfigAbilityClimbing config) {
        super(TrinketsRegistryNames.ModAbilities.CLIMBING);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.canClimb = config.ENABLED;
        this.useWhitelist = config.USE_WHITELIST;
        if (config.ENABLED) {
            this.initClimbBlocks();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

    @Override
    public void onAbilityAdded(EntityLivingBase entity) {
        super.onAbilityAdded(entity);
        if (this.isFirstUpdate()) {
            this.sendCacheToPlayer(entity, this.generateTag());
        }
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final boolean flag = this.isCreativePlayer(entity);
        if (!flag && !entity.onGround && entity.collidedHorizontally) {
            if (this.canClimb(entity)) {
                if (!entity.isSneaking()) {
                    entity.motionY = 0.1f;
                }
                if (entity.isSneaking()) {
                    entity.motionY = 0f;
                }
                entity.fallDistance = 0;
            }
        }
    }

    protected boolean movingForward(@Nonnull EntityLivingBase entity, @Nonnull EnumFacing facing) {
        return ((facing.getDirectionVec().getX() * entity.motionX) > 0) || ((facing.getDirectionVec().getZ() * entity.motionZ) > 0);
    }

    protected boolean canClimb(EntityLivingBase entity) {
        if (!this.canClimb) {
            return false;
        }
        final World world = entity.getEntityWorld();
        BlockPos entityPos = new BlockPos(entity.getPositionVector());
        BlockPos bodyPos = entityPos;
        BlockPos frontBodyPos = entityPos.offset(entity.getAdjustedHorizontalFacing());
        IBlockState body = world.getBlockState(bodyPos);
        IBlockState frontBody = world.getBlockState(frontBodyPos);
        //		final boolean flag1 = BlockHelper.isBlockInList(world, body, bodyPos, climbList);
        //		final boolean flag2 = BlockHelper.isBlockInList(world, frontBody, frontBodyPos, climbList);
        RayTraceResult result = world.rayTraceBlocks(entity.getPositionVector(), entity.getPositionVector().add(0, entity.height + 0.1, 0), false, true, false);
        boolean headClear = (result != null) && (result.typeOfHit == Type.BLOCK);
        final boolean whitelist = this.useWhitelist;
        TreeMap<Integer, ConfigObject> climbList = this.climbBlocks;
        for (Map.Entry<Integer, ConfigObject> s : climbList.entrySet()) {
//            boolean flag1 = object.doesBlockMatchEntry(body);
//            boolean flag2 = object.doesBlockMatchEntry(frontBody);
//            if (flag1 || flag2) {
            if (s.getValue().doesBlockMatchEntry(body) || s.getValue().doesBlockMatchEntry(frontBody)) {
                return whitelist && !headClear;
            }
        }
        //		if (flag1 || flag2)
        return !whitelist && !headClear;
    }

    protected void sendCacheToPlayer(@Nonnull Entity entity, NBTTagCompound tag) {
        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP && tag != null && !tag.isEmpty()) {
            NetworkHandler.sendTo(new AbilityCacheSyncPacket(((EntityPlayerMP) entity), tag), (EntityPlayerMP) entity);
        }
    }

    protected NBTTagCompound generateTag() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Ability", this.getRegistryName().toString());
        tag.setBoolean("canClimb", this.canClimb);
        tag.setBoolean("useWhitelist", this.useWhitelist);
        if (this.canClimb) {
            this.initClimbBlocks();
            NBTTagCompound blocks = new NBTTagCompound();
            if (this.climbBlocks != null && !this.climbBlocks.isEmpty()) {
                int index = 0;
                for (Map.Entry<Integer, ConfigObject> e : this.climbBlocks.entrySet()) {
                    NBTTagCompound blockTag = new NBTTagCompound();
                    blockTag.setString("Target", e.getValue().getOriginalEntry());
                    blocks.setTag(index + "", blockTag);
                    index++;
                }
            }
            if (!blocks.isEmpty()) {
                tag.setTag("Blocks", blocks);
            }
        }
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        if (!this.climbBlocks.isEmpty()) {
            this.climbBlocks.clear();
        }
        if (tag != null && !tag.isEmpty()) {
            NBTHelper.hasBoolean(tag, "canClimb", (bool) -> {
                this.canClimb = bool;
            });
            NBTHelper.hasBoolean(tag, "useWhitelist", (bool) -> {
                this.useWhitelist = bool;
            });
            NBTHelper.hasTag(tag, "Blocks", (blocks) -> {
                for (int i = 0; i < blocks.getSize(); i++) {
                    final int index = i;
                    NBTHelper.hasTag(blocks, index + "", (block) -> {
                        NBTHelper.hasString(block, "Target", (string) -> {
                            if (!string.isEmpty()) {
                                ConfigObject climbBlock = new ConfigObject(string);
                                this.climbBlocks.put(index, climbBlock);
                            }
                        });
                    });
                }
            });
        }
    }

    protected void initClimbBlocks() {
        if (!this.climbBlocks.isEmpty()) {
            this.climbBlocks.clear();
        }
        final String[] climb = this.CONFIG.BLOCKS;
        int index = 0;
        for (String entry : climb) {
            ConfigTreasureObject climbBlock = new ConfigTreasureObject(entry);
            boolean existsCheck = climbBlock.getObjectType().compareTo(EntryType.OREDICTIONARY) == 0 || Block.getBlockFromName(climbBlock.getObjectRegistryName()) != null;
            if (!climbBlock.isEmpty() && existsCheck) {
                this.climbBlocks.put(index, climbBlock);
                index++;
            }
        }
    }

}
