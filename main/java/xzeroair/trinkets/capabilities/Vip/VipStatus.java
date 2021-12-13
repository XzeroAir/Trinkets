package xzeroair.trinkets.capabilities.Vip;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.vip.VipStatusPacket;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.vip.VIPHandler;
import xzeroair.trinkets.vip.VipPackage;
import xzeroair.trinkets.vip.VipUser;

public class VipStatus extends CapabilityBase<VipStatus, EntityPlayer> {

	boolean first_login = true;
	boolean login = false;
	EntityPlayer player;
	int status = 0;
	boolean checkStatus;
	List<String> Quotes;
	private VipUser user;

	public VipStatus(EntityPlayer player) {
		super(player);
		this.player = player;
		Quotes = new ArrayList<>();
	}

	@Override
	public NBTTagCompound getTag() {
		//		if (player.getEntityData() != null) {
		//			tag = player.getEntityData();
		//		}
		return super.getTag();
	}

	@Override
	public void onUpdate() {
		if ((player.world == null) || player.isDead) {
			return;
		}
		if ((checkStatus != true)) {
			if (Trinkets.proxy.getSide() == Side.CLIENT) {
				try {
					if (Minecraft.getMinecraft().getSession() != null) {
						final Session session = Minecraft.getMinecraft().getSession();
						final boolean hasPlayer = VIPHandler.Vips.containsKey(session.getPlayerID().toString().replaceAll("-", ""));
						if (hasPlayer) {
							this.confirmedStatus();
							this.sendStatusToServer(player);
						}
						//							if (session.getProfile() != null) {
						//								final GameProfile profile = session.getProfile();
						//							System.out.println(hasPlayer + " | Player Exists in VIP List");
						//							System.out.println(session.getUsername() + " with ID:" + session.getPlayerID());
						//							System.out.println(profile.getName() + " with ID:" + profile.getId());
						//							}
					}
				} catch (final Exception e) {

				}
			}
			checkStatus = true;
		}
	}

	public void confirmedStatus() {
		final String id = player.getUniqueID().toString().replaceAll("-", "");
		if (VIPHandler.Vips.containsKey(id)) {
			user = VIPHandler.Vips.get(id);
			if (user != null) {
				if (!user.getGroups().isEmpty()) {
					final VipPackage group1 = user.getGroups().get(0);
					if (group1 != null) {
						status = group1.getGroupID();
					}
				}
				Quotes = user.getQuotes();
			}
		}
	}

	public void sendStatusToServer(EntityPlayer player) {
		final World world = player.getEntityWorld();
		if (world.isRemote) {
			if (tag == null) {
				tag = new NBTTagCompound();
			}
			this.saveToNBT(tag);
			NetworkHandler.sendToServer(new VipStatusPacket(player, tag));
		}
	}

	public void sendStatusToPlayer(EntityPlayer reciever) {
		if (reciever instanceof EntityPlayerMP) {
			final World world = player.getEntityWorld();
			try {
				if (tag == null) {
					tag = new NBTTagCompound();
				}
				this.saveToNBT(tag);
				NetworkHandler.sendTo(new VipStatusPacket(player, tag), (EntityPlayerMP) reciever);
			} catch (final Exception e) {
			}
		}
	}

	public void syncStatusToTracking() {
		final World world = player.getEntityWorld();
		if ((player instanceof EntityPlayerMP) && (world instanceof WorldServer)) {
			if (tag == null) {
				tag = new NBTTagCompound();
			}
			this.saveToNBT(tag);
			NetworkHandler.sendToClients((WorldServer) world, player.getPosition(), new VipStatusPacket(player, tag));
		}
	}

	public List<String> getQuotes() {
		return Quotes;
	}

	public String getRandomQuote() {
		if (!Quotes.isEmpty()) {
			final int rand = Reference.random.nextInt(Quotes.size());
			return Quotes.get(rand);
		} else {
			return "";
		}
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public boolean isFirstLogin() {
		return first_login;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	@Override
	public void copyFrom(VipStatus source, boolean wasDeath, boolean keepInv) {
	}

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		compound.setInteger("status", status);
		compound.setBoolean("login", login);
		compound.setString("uuid", player.getCachedUniqueIdString());
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("status")) {
			status = compound.getInteger("status");
		}
		if (compound.hasKey("login")) {
			login = compound.getBoolean("login");
		}
		first_login = false;
	}
}