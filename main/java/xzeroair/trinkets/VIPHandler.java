package xzeroair.trinkets;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VIPHandler {

	private static boolean check, check2, check3 = false;
	private static int value, value2, value3;
	private static List<String> list, list2, list3 = new ArrayList<>();
	private static String[] array, array2, array3;

	protected static void popVIPList() {
		try {
			System.out.println("Running?");
			URL bros = new URL("https://raw.githubusercontent.com/XzeroAir/AuxFiles/master/bros.txt");
			Scanner in = new Scanner(bros.openStream());
			//				BufferedReader in = new BufferedReader(new InputStreamReader(bros.openStream()));
			//				String line = in.readLine();
			while (in.hasNext()) {
				//				while(in.readLine() != null) {
				String string = in.nextLine();
				if (!list.contains(string)) {
					list.add(string);
				}

				//				for (int k = 0; line != null; k++) {
				//					if (!this.list.contains(line)) {
				//						System.out.println("added " + line);
				//						this.list.add(line);
				//					}
				//				}
			}
			in.close();
		} catch (IOException e) {
			//
		}
	}

	protected static void buildVIPList() {

	}

}
