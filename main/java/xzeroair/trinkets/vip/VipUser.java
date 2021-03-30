package xzeroair.trinkets.vip;

import java.util.ArrayList;
import java.util.List;

public class VipUser {

	private String ID;
	private String Username;
	private List<String> Quotes;
	private List<VipPackage> Groups;

	public VipUser(String uuid, String username) {
		ID = uuid;
		Username = username;
		Quotes = new ArrayList<>();
		Groups = new ArrayList<>();
	}

	public String getID() {
		return ID;
	}

	public String getUsername() {
		return Username;
	}

	public List<String> getQuotes() {
		return Quotes;
	}

	public List<VipPackage> getGroups() {
		return Groups;
	}

	public VipUser setQuotes(List<String> quotes) {
		Quotes = quotes;
		return this;
	}

	public VipUser setGroups(List<VipPackage> groups) {
		Groups = groups;
		return this;
	}

}
