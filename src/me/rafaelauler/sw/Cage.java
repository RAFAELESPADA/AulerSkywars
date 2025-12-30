package me.rafaelauler.sw;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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

            createCage(p, Material.GLASS);
        }
    }

    /** Teleporta um único jogador para uma jaula aleatória do mapa */
    public static void randomTeleport(Player player, Jaulas map) {
        if (player == null || map == null) return;
        Location loc = map.getRandomLocation();
        player.teleport(loc);
        createCage(player, Material.GLASS);
    }

    // =================== JAULAS ===================
    public static void createCage(Player player, Material cageMaterial) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        // Create a 3x3x3 cage
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 2; dy++) { // Cage height of 3
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0 && dy > 0) continue; // Keep center air
                    Block block = world.getBlockAt(x + dx, y + dy, z + dz);
                    block.setType(cageMaterial);
                }
            }}
            
        }

        // Method to remove the cage
        public static void removeCage(Location loc, Material cageMaterial) {
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 2; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Block block = loc.getWorld().getBlockAt(x + dx, y + dy, z + dz);
                        if (block.getType() == cageMaterial) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
    
        
    
