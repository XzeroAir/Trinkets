package xzeroair.trinkets.vip;

import java.util.ArrayList;
import java.util.List;

public class VipPackage {

	private String group;
	private int id;
	private List<String> groupQuotes;

	public VipPackage(String group, int ID) {
		this.group = group;
		id = ID;
		groupQuotes = new ArrayList<>();
	}

	public String getGroup() {
		return group;
	}

	public int getGroupID() {
		return id;
	}

	public List<String> getGroupQuotes() {
		return groupQuotes;
	}

	public void addQuote(String... quote) {
		for (String s : quote) {
			groupQuotes.add(s);
		}
	}

	public VipPackage setGroupQuotes(List<String> groupQuotes) {
		this.groupQuotes = groupQuotes;
		return this;
	}
}
