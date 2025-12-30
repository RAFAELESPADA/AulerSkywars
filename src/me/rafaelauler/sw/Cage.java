package me.rafaelauler.sw;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Cage {

    // =================== TELEPORT ===================
    public static void teleportByQueueOrder(List<UUID> players, Jaulas map) {
        if (players == null || players.isEmpty() || map == null) return;

        World world = Bukkit.getWorld(map.name());
        if (world == null) return;

        List<Location> shuffledLocations = map.getShuffledLocations();

        for (int i = 0; i < players.size(); i++) {
            Player p = Bukkit.getPlayer(players.get(i));
            if (p == null) continue;

            Location target = i < shuffledLocations.size()
                    ? shuffledLocations.get(i)
                    : map.getRandomLocation();

            p.teleport(target);

            createCage(target.getWorld(), (int)target.getX(), (int)target.getY(), (int)target.getZ());
        }
    }

    /** Teleporta um único jogador para uma jaula aleatória do mapa */
    public static void randomTeleport(Player player, Jaulas map) {
        if (player == null || map == null) return;
        Location loc = map.getRandomLocation();
        player.teleport(loc);
        createCage(loc.getWorld(), (int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
    }

    // =================== JAULAS ===================
    public static void createCage(World world, int cx, int cy, int cz) {

    	for(int x = 0; x < 12; x++){
    	    for(int y = 0; y < 4; y++){
    	        for(int z = 0; z < 12; z++){
    	             Location l = new Location(world, x, y, z);
    	             l.getBlock().setType(Material.GLASS);
    	        }
    	    }
    	}
    	for(int x = 0; x < 10; x++){
    	    for(int y = 0; y < 3; y++){
    	        for(int z = 0; z < 10; z++){
    	             Location l = new Location(world, x, y, z);
    	             l.getBlock().setType(Material.AIR);
    	        }
    	    }
    	}
                }
          
        
    
    public static void removeAll(Jaulas map) {
        if (map == null) return;
        World world = Bukkit.getWorld(map.name());
        if (world == null) return;

        for (Location loc : map.getShuffledLocations()) {
            if (loc.getBlock().getType() == Material.GLASS) {
                loc.getBlock().setType(Material.AIR);
            }
        }
    }
}
