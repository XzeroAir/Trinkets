package xzeroair.trinkets.capabilities.Vip;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.vip.VIPHandler;
import xzeroair.trinkets.vip.VipPackage;
import xzeroair.trinkets.vip.VipUser;

public class VipStatus {

	boolean first_login = true;
	boolean login = false;
	EntityLivingBase player;
	boolean isBro;
	boolean isPanda;
	boolean isVip;
	int status = 0;
	boolean checkStatus;
	List<String> Quotes;
	private VipUser user;

	public VipStatus(EntityLivingBase player) {
		this.player = player;
		//.getVipUser(player.getUniqueID());
		Quotes = new ArrayList<>();
	}

	public void onUpdate() {
		if ((checkStatus != true)) {
			if (user == null) {
				user = VIPHandler.Vips.get(player.getUniqueID().toString().replaceAll("-", ""));
			}
			if (user != null) {
				if (!user.getGroups().isEmpty()) {
					VipPackage group1 = user.getGroups().get(0);
					if (group1 != null) {
						status = group1.getGroupID();
					}
				}
				Quotes = user.getQuotes();
			} else {
			}
			checkStatus = true;
		}
	}

	public List<String> getQuotes() {
		return Quotes;
	}

	public String getRandomQuote() {
		if (!Quotes.isEmpty()) {
			int rand = Reference.random.nextInt(Quotes.size());
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

	public void copyFrom(VipStatus source) {
	}

	public void savedNBTData(NBTTagCompound compound) {
		compound.setInteger("status", status);
		compound.setBoolean("login", login);
	}

	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey("status")) {
			status = compound.getInteger("status");
		}
		if (compound.hasKey("login")) {
			login = compound.getBoolean("login");
		}
		first_login = false;
	}
}