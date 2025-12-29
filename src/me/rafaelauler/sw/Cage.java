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
            createCage(target);
        }
    }

    /** Teleporta um único jogador para uma jaula aleatória do mapa */
    public static void randomTeleport(Player player, Jaulas map) {
        if (player == null || map == null) return;
        Location loc = map.getRandomLocation();
        player.teleport(loc);
        createCage(loc);
    }

    // =================== JAULAS ===================
    public static void createCage(Location loc) {
        if (loc == null || loc.getWorld() == null) return;

        World world = loc.getWorld();

        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {

                    // centro fica vazio (onde o jogador fica)
                    if (x == 0 && y == 1 && z == 0) continue;

                    // só bordas (paredes, teto e chão)
                    boolean border =
                            x == -1 || x == 1 ||
                            z == -1 || z == 1 ||
                            y == 0 || y == 2;

                    if (!border) continue;

                    world.getBlockAt(cx + x, cy + y, cz + z)
                            .setType(Material.GLASS);
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
