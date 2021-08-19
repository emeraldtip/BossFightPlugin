package com.bfg.veo.managers;

import com.bfg.veo.Main;

public class ConfigManager {
	
	public static double[] getFloor1() {
		
		return new double[]{Main.getMain().getConfig().getDouble("FloorX1"), Main.getMain().getConfig().getDouble("FloorY1"), Main.getMain().getConfig().getDouble("FloorZ1")};
		
	}
	
	public static double[] getFloor2() {
		
		return new double[]{Main.getMain().getConfig().getDouble("FloorX2"), Main.getMain().getConfig().getDouble("FloorY2"), Main.getMain().getConfig().getDouble("FloorZ2")};
		
	}
	
	public static double[] getStartLocs() {
		
		return new double[]{Main.getMain().getConfig().getDouble("StartX"), Main.getMain().getConfig().getDouble("StartY"), Main.getMain().getConfig().getDouble("StartZ")};
		
	}
	
	public static long getDelay() {
		
		long delay = Main.getMain().getConfig().getLong("Delay");
		return delay;
		
	}
	
	public static String getWorld() { return Main.getMain().getConfig().getString("world"); }
	
	public static void editLocs(String name, String value) {
		
		Main.getMain().getConfig().set(name, Double.parseDouble(value));
		Main.getMain().saveConfig();
		
	}
	
	public static void editWorld(String name, String value) {
		
		Main.getMain().getConfig().set(name, value);
		Main.getMain().saveConfig();
		
	}

}
