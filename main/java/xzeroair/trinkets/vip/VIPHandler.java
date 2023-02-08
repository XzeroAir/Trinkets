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

public class VIPHandler {

	private static final String VIPV2 = "https://raw.githubusercontent.com/XzeroAir/AuxFiles/master/VipsV2.json";

	private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

	public static TreeMap<String, VipUser> Vips;

	public static void popVIPList() {
		//		long startTime = System.nanoTime();
		loadJsonFromUrl(VIPV2);
		//		long endTime = System.nanoTime() - startTime;
		//				Trinkets.log.info();
		//		System.out.println("Time:" + (endTime / 1000000L));
	}

	private static void loadJsonFromUrl(String url) {
		try {
			final URL link = new URL(url);
			final InputStream stream = link.openStream();
			final InputStreamReader input = new InputStreamReader(stream);
			final BufferedReader reader = new BufferedReader(input);
			try {
				final Type mapType = new TypeToken<TreeMap<String, VipUser>>() {
				}.getType();
				Vips = gson.fromJson(reader, mapType);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
