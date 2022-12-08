package xzeroair.trinkets.vip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class VIPHandler {

	private static final String VIPV2 = "https://raw.githubusercontent.com/XzeroAir/AuxFiles/master/VipsV2.json";

	private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

	public static TreeMap<String, VipUser> Vips;

	public static void popVIPList() {
		loadJsonFromUrl(VIPV2);
	}

	private static void loadJsonFromUrl(String url) {
		try {
			final URL link = new URL(url);
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(link.openStream()));
			Type mapType = new TypeToken<TreeMap<String, VipUser>>() {
			}.getType();
			Vips = gson.fromJson(reader2, mapType);
			reader2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
