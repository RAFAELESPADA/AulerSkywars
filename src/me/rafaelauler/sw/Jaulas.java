package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public enum Jaulas {

	sw1("sw1", new double[][]{
        { -31, 64, 32 },
        { -42, 66, 23 },
        { -41, 66, -22 },
        { -31, 66, -31 },
        { -21, 66, -41 },
        { 22, 63.81, -41 },
        { 31, 63.81, -30 },
        { 42, 63.81, -22 },
        { 41.6, 63.81, 23 },
        { 32.6, 63.81, 32 },
        { 23.0, 64.55, 41.911 },
        { -22.170, 64.55, 42.443 }
    }),

    sw2("sw2", new double[][]{
        { -17, 41, 50 },
        { -35, 41, 44 },
        { -41, 41, 25 },
        { -40, 41, -28 },
        { -36, 41, -46 },
        { -17, 41, -52 },
        { 17, 41, -51 },
        { 35, 41, -47 },
        { 40, 41, -28 },
        { 41, 41, 26 },
        { 35, 41, 44 },
        { 16, 41, 49 }
    }),

    sw3("sw3", new double[][]{
        { -41, 74, -21 },
        { -1, 74, -70 },
        { 24, 74, -43 },
        { 74, 70, -22 },
        { 70, 70, 0 },
        { 42, 70, 23 },
        { 23, 70, 42 },
        { 0, 70, 0 },
        { -22, 71, 43 },
        { -42, 70, 22 },
        { -69, 70, 1 },
        { -43, 70, -21 }
    }),

    sw4("sw4", new double[][]{
        { -1, 78, 57 },
        { -14, 78, 43 },
        { -44, 78, 13 },
        { -58, 78, 0 },
        { -44, 77, -13 },
        { -14, 81, -43 },
        { -1, 81, -57 },
        { 15, 81, -44 },
        { 44, 81, -14 },
        { 59, 81, -2 },
        { 44, 81, 13 },
        { 14, 81, 44 }
    }),

    sw5("sw5", new double[][]{
        { 66, 68, 3 },
        { 49, 66, -25 },
        { 25, 66, -42 },
        { 2, 66, -65 },
        { -23, 66, -50 },
        { -43, 66, -25 },
        { -66, 66, -3 },
        { -51, 66, 25 },
        { -25, 66, 44 },
        { -3, 66, 66 },
        { 24, 66, 51 },
        { 43, 66, 23 }
    });



	
	

    private final String worldName;
    private final List<Vector> cageVectors;

    Jaulas(String worldName, double[][] coords) {
        this.worldName = worldName;

        List<Vector> temp = new ArrayList<>(coords.length);
        for (double[] c : coords) {
            temp.add(new Vector(c[0], c[1], c[2]));
        }
        this.cageVectors = Collections.unmodifiableList(temp);
    }

    private World requireWorld() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException("Mundo não carregado: " + worldName);
        }
        return world;
    }

    public Location getRandomLocation() {
        Vector v = cageVectors.get(ThreadLocalRandom.current().nextInt(cageVectors.size()));
        World world = requireWorld();
        
        return new Location(world, v.getX(), v.getY(), v.getZ());
    }

    public List<Location> getShuffledLocations() {
        World world = requireWorld();
        List<Vector> shuffled = new ArrayList<>(cageVectors);
        Collections.shuffle(shuffled, ThreadLocalRandom.current());

        List<Location> locations = new ArrayList<>(shuffled.size());
        for (Vector v : shuffled) {
            locations.add(new Location(world, v.getX(), v.getY(), v.getZ()));
        }
        return locations;
    }
    public void teleportByQueueOrder(List<? extends UUID> players) {
        if (players == null || players.isEmpty()) return;

        World world = requireWorld();

        for (int i = 0; i < players.size(); i++) {
            UUID uuid = players.get(i); // pega o UUID correto
            Player p = Bukkit.getPlayer(uuid); // busca o Player online

            if (p == null) continue;

            Location target;

            if (i < cageVectors.size()) {
                Vector v = cageVectors.get(i);
                target = new Location(world, v.getX(), v.getY(), v.getZ());
            } else {
                // Caso tenha mais players do que jaulas
                target = getRandomLocation();
            }

            p.teleport(target);
        }
    }

    /** Teleporta 1 player pra uma jaula aleatória desse mapa */
    public void randomTeleport(Player player) {
        if (player == null) return;
        player.teleport(getRandomLocation());
    }
    public static final List<ItemStack> items = Arrays.asList( new ItemStack(Material.GOLD_HELMET), new ItemStack(Material.DIRT, 16), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLDEN_APPLE), new ItemStack(Material.ARROW , 16), new ItemStack(Material.POTION, 1, (short)16418), new ItemStack(Material.POTION, 1, (short)16387), new ItemStack(Material.POTION, 1, (short)16456), new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.POTION, 1 , (short)16388), new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EXP_BOTTLE) , new ItemStack(Material.STONE_AXE) , new ItemStack(Material.STONE_AXE), new ItemStack(Material.STONE_AXE) , new ItemStack(Material.STONE_AXE) , new ItemStack(Material.STONE_AXE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_SWORD) , new ItemStack(Material.IRON_SWORD) , new ItemStack(Material.IRON_SWORD) , new ItemStack(Material.IRON_SWORD) , new ItemStack(Material.IRON_SWORD) , new ItemStack(Material.IRON_SWORD), new ItemStack(Material.GOLD_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.COOKED_BEEF, 16), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.ARROW , 10), new ItemStack(Material.ARROW , 10), new ItemStack(Material.BOW), new ItemStack(Material.BOW), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.ENDER_STONE, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16),new ItemStack(Material.DIRT, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.GOLDEN_APPLE, 3), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.POTION, 1 , (short)373), new ItemStack(Material.POTION, 1 , (short)373), new ItemStack(Material.POTION, 1 , (short)373) ); 
    /**
     * Teleporta uma lista de players em jaulas únicas (quando possível).
     * Se tiver mais players do que jaulas, o resto cai em aleatória.
     */
    public void teleportUnique(Collection<? extends Player> players) {
        if (players == null || players.isEmpty()) return;

        Iterator<Location> it = getShuffledLocations().iterator();

        for (Player p : players) {
            if (p == null) continue;

            if (it.hasNext()) {
                p.teleport(it.next());
            } else {
                p.teleport(getRandomLocation());
            }
        }
    }

    public int getCageCount() {
        return cageVectors.size();
    }
}
