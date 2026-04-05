package xzeroair.trinkets.capabilities.Vip;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.capabilities.CapabilityEntityPlayerBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.vip.VipStatusPacket;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.vip.VIPHandler;
import xzeroair.trinkets.vip.VipPackage;
import xzeroair.trinkets.vip.VipUser;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class VipStatus extends CapabilityEntityPlayerBase<VipStatus, EntityPlayer> {

    public static final String TAG_KEY = Reference.MODID + ":vip";

    private int status = 0;
    private boolean checkStatus;
    private List<String> quotes;

    public VipStatus(EntityPlayer player) {
        super(player);
        this.quotes = new ArrayList<>();
    }

    @Override
    public NBTTagCompound getTag() {
        final NBTTagCompound tag = NBTHelper.getEntityTag(this.getPlayer());
        if (tag != null) {
            if (!tag.hasKey(TAG_KEY)) {
                tag.setTag(TAG_KEY, new NBTTagCompound());
            }
            return tag.getCompoundTag(TAG_KEY);
        } else {
            return super.getTag();
        }
    }

    @Override
    public void onUpdate() {
        if ((!this.checkStatus)) {
            final World world = this.getPlayer().getEntityWorld();
            if (world == null) {
                return;
            }
            if (!world.isRemote) {
                try {
                    final TreeMap<String, VipUser> list = VIPHandler.instance.getVips();
                    if ((list != null) && !list.isEmpty()) {
                        this.confirmedStatus();
                        this.sendInformationToPlayer();
                    }
                } catch (Exception ignored) {
                }
            }
            this.checkStatus = true;
        }
    }

    @Override
    public void onLogin() {
//        this.sendInformationToPlayer(this.getPlayer(), this.getTag());
    }

    @Override
    public void onJoinWorld() {
        final World world = this.getPlayer().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(this.getPlayer());
        }
    }

    @Override
    public void onLogoff() {

    }

    @Override
    public void onChangedDimension(int from, int to) {

    }

    private void confirmedStatus() {
        final String id = this.getPlayer().getUniqueID().toString().replaceAll("-", "");
        if (VIPHandler.instance.getVips().containsKey(id)) {
            VipUser user = VIPHandler.instance.getVips().get(id);
            if (user != null) {
                if (!user.getGroups().isEmpty()) {
                    final VipPackage group1 = user.getGroups().get(0);
                    if (group1 != null) {
                        this.status = group1.getGroupID();
                    }
                }
                this.quotes = user.getQuotes();
            }
        }
    }


    public void sendInformationToPlayer() {
        final World world = this.getPlayer().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(this.getPlayer(), this.saveToNBT(new NBTTagCompound()));
        }
    }

    public void sendInformationToPlayer(EntityPlayer receiver) {
        final World world = this.getPlayer().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(receiver, this.saveToNBT(new NBTTagCompound()));
        }
    }

    public void sendInformationToPlayer(EntityPlayer receiver, NBTTagCompound tag) {
        final World world = this.getPlayer().getEntityWorld();
        if (!world.isRemote && (receiver instanceof EntityPlayerMP)) {
            NetworkHandler.sendTo(new VipStatusPacket(this.getPlayer(), tag), (EntityPlayerMP) receiver);
        }
    }

    public void sendInformationToTracking(NBTTagCompound tag) {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote && (world instanceof WorldServer)) {
            final WorldServer w = (WorldServer) world;
            NetworkHandler.sendToClients(w, this.getEntity().getPosition(), new VipStatusPacket(this.getEntity(), tag));
        }
    }

    public List<String> getQuotes() {
        return this.quotes;
    }

    public String getRandomQuote() {
        if (!this.quotes.isEmpty()) {
            final int rand = Reference.random.nextInt(this.quotes.size());
            return this.quotes.get(rand);
        } else {
            return "";
        }
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public void copyFrom(@Nonnull VipStatus source, boolean wasDeath, boolean keepInv) {
        this.status = source.status;
    }

    @Override
    public NBTTagCompound saveToNBT(@Nonnull NBTTagCompound compound) {
        compound.setInteger("status", this.status);
        return compound;
    }

    @Override
    public void loadFromNBT(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey("status")) {
            this.status = compound.getInteger("status");
        }
    }
}