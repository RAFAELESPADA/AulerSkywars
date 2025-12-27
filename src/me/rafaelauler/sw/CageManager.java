package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CageManager {

    private static final List<Block> cages = new ArrayList<>();

    // Cria a jaula em volta do jogador
    public static void createCage(Location center) {
        World w = center.getWorld();

        int[][] blocks = {
            { 1, 0, 0 }, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1},
            { 0, 1, 0 }, {0, 2, 0},
            { 1, 1, 0 }, {-1, 1, 0}, {0, 1, 1}, {0, 1, -1}
        };

        for (int[] b : blocks) {
            Block block = w.getBlockAt(
                center.getBlockX() + b[0],
                center.getBlockY() + b[1],
                center.getBlockZ() + b[2]
            );

            block.setType(Material.GLASS);
            cages.add(block);
        }
    }

    // Remove todas as jaulas
    public static void removeAllCages() {
        for (Block b : cages) {
            b.setType(Material.AIR);
        }
        cages.clear();
    }
}