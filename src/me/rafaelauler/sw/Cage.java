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
 
    public static void createCage(Player player, Material material) {

        // BASE DA JAULA (PISO)
        Location base = player.getLocation().getBlock().getLocation();
        World world = base.getWorld();

        int x = base.getBlockX();
        int y = base.getBlockY();
        int z = base.getBlockZ();

        // TELEPORTA PLAYER 1 BLOCO ACIMA DO PISO
        Location center = base.clone().add(0.5, 1, 0.5);
        player.teleport(center);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 3; dy++) {
                for (int dz = -1; dz <= 1; dz++) {

                    boolean isCenter = dx == 0 && dz == 0;

                    // espaço interno
                    if (isCenter && dy >= 1 && dy <= 2) continue;

                    // piso e teto
                    if (dy == 0 || dy == 3) {
                        world.getBlockAt(x + dx, y + dy, z + dz)
                             .setType(material);
                        continue;
                    }

                    // paredes
                    if (dx == -1 || dx == 1 || dz == -1 || dz == 1) {
                        world.getBlockAt(x + dx, y + dy, z + dz)
                             .setType(material);
                    }
                }
            }
        }
    }



        // Method to remove the cage
    public static void removeCage(Player player) {
        Location loc = player.getLocation().getBlock().getLocation();
        World w = loc.getWorld();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 3; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    w.getBlockAt(
                        loc.getBlockX() + dx,
                        loc.getBlockY() + dy,
                        loc.getBlockZ() + dz
                    ).setType(Material.AIR);
                }
            }
        }
    }}

    
        
    
