package xzeroair.trinkets.util;

public class Reference {
	
	public static final String MODID = "xat";
	public static final String NAME = "Trinkets and Baubles";
	public static final String VERSION = "0.1";
	public static final String CLIENT = "xzeroair.trinkets.proxy.ClientProxy";
	public static final String COMMON = "xzeroair.trinkets.proxy.CommonProxy";
	
	
	public static enum TrinketsItems {
		
		GLOWINGINGOT("glowing_ingot", "itemglowing_ingot");
		
		private String unlocalizedName;
		private String registryName;
		
		TrinketsItems(String unlocalizedName, String registryName) {
			
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
			
		}
		
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
		
		public String getRegistryName() {
			return registryName;
		}
	}

}
