package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Cage {

    private static final Map<UUID, List<Block>> cages = new HashMap<>();

    // =================== TELEPORT ===================

    public static void teleportByQueueOrder(List<UUID> players, Jaulas map) {
        if (players == null || players.isEmpty() || map == null) return;

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

    public static void randomTeleport(Player player, Jaulas map) {
        if (player == null || map == null) return;

        player.teleport(map.getRandomLocation());
        createCage(player, Material.GLASS);
    }

    // =================== JAULAS ===================

    public static void createCage(Player player, Material material) {
        UUID uuid = player.getUniqueId();

        // remove jaula antiga, se existir
        removeCage(player);

        Location base = player.getLocation().getBlock().getLocation();
        World world = base.getWorld();

        int x = base.getBlockX();
        int y = base.getBlockY();
        int z = base.getBlockZ();

        player.teleport(base.clone().add(0.5, 1, 0.5));

        List<Block> blocks = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 3; dy++) {
                for (int dz = -1; dz <= 1; dz++) {

                    boolean isCenter = dx == 0 && dz == 0;
                    if (isCenter && dy >= 1 && dy <= 2) continue;

                    if (dy == 0 || dy == 3 ||
                        dx == -1 || dx == 1 ||
                        dz == -1 || dz == 1) {

                        Block block = world.getBlockAt(x + dx, y + dy, z + dz);
                        block.setType(material);
                        blocks.add(block);
                    }
                }
            }
        }

        cages.put(uuid, blocks);
    }

    // =================== REMOVE ===================

    public static void removeCage(Player player) {
        if (player == null) return;

        List<Block> blocks = cages.remove(player.getUniqueId());
        if (blocks == null) return;

        for (Block b : blocks) {
            b.setType(Material.AIR);
        }
    }

    public static void removeAllCages() {
        for (List<Block> blocks : cages.values()) {
            for (Block b : blocks) {
                b.setType(Material.AIR);
            }
        }
        cages.clear();
    }
}
