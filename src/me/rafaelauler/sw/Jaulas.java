package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public enum Jaulas {

    SW1("sw1", new double[][]{
        { 68.677, 103.0,   5.2   },
        { 46.219, 103.0, -17.627 },
        { 20.415, 103.0, -43.497 },
        { -2.578, 103.0, -66.523 },
        { -23.676,103.0, -42.066 },
        { -50.368,103.0, -19.632 },
        { -71.379,103.0,   4.4   },
        { -46.518,103.0,  24.131 },
        { -24.551,103.0,  52.149 },
        { -0.473, 103.0,  72.177 },
        { 20.479, 103.0,  49.484 },
        { 46.269, 103.0,  24.260 }
    }),

    SW2("sw2", new double[][]{
        { 92.528, 103.0,  -2.039 },
        { 65.714, 103.0, -26.700 },
        { 23.700, 103.0, -65.700 },
        { -1.269, 103.0, -90.598 },
        { -26.700,103.0, -66.691 },
        { -64.273,103.0, -22.700 },
        { -92.700,103.0,   3.3999 },
        { -63.254,103.0,  28.700 },
        { -21.970,103.0,  65.075 },
        { 2.577,  103.0,  92.700 },
        { 27.700, 103.0,  65.004 },
        { 66.258, 103.0,  23.700 }
    }),

    SW3("sw3", new double[][]{
        { 8.343,  104.0, 78.066 },
        { 78.082, 104.0,  8.199 },
        { 6.505,  104.0,-61.381 },
        { -61.351,104.0,  7.664 }
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
    public void teleportGroupedByOrder(List<? extends Player> players) {
        if (players == null || players.isEmpty()) return;

        Location base = getRandomLocation();

        double radius = 1.5; // distância entre players
        double angleStep = Math.PI * 2 / Math.max(players.size(), 1);

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (p == null) continue;

            double angle = i * angleStep;

            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            Location loc = base.clone().add(offsetX, 0, offsetZ);
            loc.setYaw(base.getYaw());
            loc.setPitch(base.getPitch());

            p.teleport(loc);
        }
    }

    /** Teleporta 1 player pra uma jaula aleatória desse mapa */
    public void randomTeleport(Player player) {
        if (player == null) return;
        player.teleport(getRandomLocation());
    }
    public static final List<ItemStack> items = Arrays.asList( new ItemStack(Material.GOLD_HELMET), new ItemStack(Material.DIRT, 16), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLDEN_APPLE), new ItemStack(Material.ARROW , 16), new ItemStack(Material.POTION, 1, (short)16418), new ItemStack(Material.POTION, 1, (short)16387), new ItemStack(Material.POTION, 1, (short)16456), new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.POTION, 1 , (short)16388), new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EGG, 8), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.EXP_BOTTLE), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_SWORD), new ItemStack(Material.GOLD_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.COOKED_BEEF, 16), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.ARROW , 10), new ItemStack(Material.ARROW , 10), new ItemStack(Material.BOW), new ItemStack(Material.BOW), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.ENDER_STONE, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.DIRT, 16),new ItemStack(Material.DIRT, 16), new ItemStack(Material.STONE, 16), new ItemStack(Material.DIRT, 16), new ItemStack(Material.GOLDEN_APPLE, 3), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.POTION, 1 , (short)373), new ItemStack(Material.POTION, 1 , (short)373), new ItemStack(Material.POTION, 1 , (short)373) ); 
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
