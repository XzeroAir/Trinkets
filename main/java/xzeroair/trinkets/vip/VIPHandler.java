package xzeroair.trinkets.vip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import xzeroair.trinkets.util.Reference;

public class VIPHandler {

	private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

	private static TreeMap<String, VipUser> Vips = new TreeMap<>();

	private boolean VIPLoaded = false;

	public static VIPHandler instance = new VIPHandler();

	public VIPHandler() {
		//		loadJsonFromUrl(VIPV2);
	}

	public void popVIPList() {
		if (!this.IsVipLoaded()) {
			this.loadJsonFromUrl(Reference.VIP_LIST);
		}
	}

	private void loadJsonFromUrl(String url) {
		try {
			//		long startTime = System.nanoTime();
			final URL link = new URL(url);
			final InputStream stream = link.openStream();
			final InputStreamReader input = new InputStreamReader(stream);
			final BufferedReader reader = new BufferedReader(input);
			try {
				final Type mapType = new TypeToken<TreeMap<String, VipUser>>() {
				}.getType();
				Vips = gson.fromJson(reader, mapType);
				this.setVIPLoaded(true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
					input.close();
					stream.close();
				} catch (IOException e) {
				}
			}
			//		long endTime = System.nanoTime() - startTime;
			//				Trinkets.log.info();
			//		System.out.println("Time:" + (endTime / 1000000L));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TreeMap<String, VipUser> getVips() {
		if (Vips == null) {
			Vips = new TreeMap<>();
		}
		return Vips;
	}

	private void setVIPLoaded(boolean bool) {
		VIPLoaded = bool;
	}

	public boolean IsVipLoaded() {
		return VIPLoaded;
	}
}
