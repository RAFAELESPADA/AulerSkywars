package me.rafaelauler.sw;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Configs {
    public static Location LOBBY_SPAWN;

    public static Location MAIN_SPAWN;

    public static void loadLobbySpawn() {
        World world = Bukkit.getWorld("swlobby");
        LOBBY_SPAWN = new Location(world, -16.629, 97.1347, -11.604);
    }
    public static void loadMainSpawn() {
    	Location l = new Location(Bukkit.getWorld("spawn"), 1000.385, 112.00000, 1000.004);
		l.setPitch((float)3.8);
		l.setYaw((float)178.1);
		MAIN_SPAWN = l;
    }
}