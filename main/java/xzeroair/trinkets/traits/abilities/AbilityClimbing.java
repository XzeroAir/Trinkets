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
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.network.AbilityCacheSyncPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import java.util.Map;
import java.util.TreeMap;

public class AbilityClimbing extends Ability implements ITickableAbility {

    //    private float entityHeight;
//    private BlockPos entityPos, bodyPos, headPos, headSpacePos, frontBodyPos, frontHeadPos;
//    private IBlockState body, head, headSpace, frontBody, frontHead;
//	private final String[] climbList = TrinketsConfig.SERVER.races.fairy.allowedBlocks;
    private boolean useWhitelist, canClimb, sync = false;
    protected TreeMap<Integer, ConfigHelper.ConfigObject> climbBlocks = new TreeMap<>();

    public AbilityClimbing() {
        super(Abilities.blockClimbing);
        canClimb = false;
        sync = true;
        useWhitelist = false;
        initClimbBlocks();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final boolean flag = this.isCreativePlayer(entity);
        if (!flag) {
            if (sync) {
                sendCacheToPlayer(entity, generateTag());
                return;
            }
            if (!entity.onGround && entity.collidedHorizontally && this.canClimb(entity)) {
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

    protected boolean movingForward(EntityLivingBase entity, EnumFacing facing) {
        if (((facing.getDirectionVec().getX() * entity.motionX) > 0) || ((facing.getDirectionVec().getZ() * entity.motionZ) > 0)) {
            return true;
        }
        // return ((facing.getDirectionVec().getX() * player.motionX) +
        // (facing.getDirectionVec().getZ() * player.motionZ)) > 0;
        return false;
    }

    protected boolean canClimb(EntityLivingBase entity) {
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
                return whitelist ? true && !headClear : false;
            }
        }
        //		if (flag1 || flag2)
        return whitelist ? false : true && !headClear;
    }

    protected void sendCacheToPlayer(Entity entity, NBTTagCompound tag) {
        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP && tag != null && !tag.isEmpty()) {
            NetworkHandler.sendTo(new AbilityCacheSyncPacket(((EntityPlayerMP) entity), tag), (EntityPlayerMP) entity);
        }
    }

    protected NBTTagCompound generateTag() {
        sync = false;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Ability", this.getRegistryName().toString());
        tag.setBoolean("canClimb", canClimb);
        tag.setBoolean("useWhitelist", useWhitelist);
        if (climbBlocks != null && !climbBlocks.isEmpty()) {
            int index = 0;
            for (Map.Entry<Integer, ConfigObject> e : climbBlocks.entrySet()) {
                NBTTagCompound blockTag = new NBTTagCompound();
                tag.setString("Target", e.getValue().getOriginalEntry());
                tag.setTag(index + "", blockTag);
                index++;
            }
        }
        return tag;
    }

    @Override
    public void loadTagCacheFromNBT(NBTTagCompound tag) {
        sync = false;
        if (!climbBlocks.isEmpty()) {
            climbBlocks.clear();
        }
        if (tag != null && !tag.isEmpty()) {
            if (tag.hasKey("canClimb")) {
                canClimb = tag.getBoolean("canClimb");
            }
            if (tag.hasKey("useWhitelist")) {
                useWhitelist = tag.getBoolean("useWhitelist");
            }
            ConfigObject entry = ConfigObject.EMPTY_CONFIG;
            for (int i = 0; i < tag.getSize(); i++) {
                NBTTagCompound blockTag = tag.getCompoundTag(i + "");
                if (blockTag != null && !blockTag.isEmpty()) {
                    if (tag.hasKey("Target")) {
                        String t = tag.getString("Target");
                        if (!t.isEmpty()) {
                            ConfigObject climbBlock = new ConfigHelper.ConfigObject(t);
                            climbBlocks.put(i, climbBlock);
                        }
                    }
                }
            }
        }
    }

    protected void initClimbBlocks() {
        if (!climbBlocks.isEmpty()) {
            climbBlocks.clear();
        }
        final String[] climb = TrinketsConfig.SERVER.races.fairy.allowedBlocks;
        int index = 0;
        for (String entry : climb) {
            ConfigHelper.TreasureEntry climbBlock = new ConfigHelper.TreasureEntry(entry);
            boolean existsCheck = climbBlock.getObjectType().compareTo(ConfigHelper.EntryType.OREDICTIONARY) == 0 ? true : Block.getBlockFromName(climbBlock.getObjectRegistryName()) != null;
            if (!climbBlock.isEmpty() && existsCheck) {
                climbBlocks.put(index, climbBlock);
                index++;
            }
        }
    }

}
