package me.rafaelauler.sw;



import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;


@Getter
public enum Jaulas {

    ONE(new Location(Bukkit.getWorld("sw1"), 68.677, 97, 5.2)),

    TWO(new Location(Bukkit.getWorld("sw1"), 46.219, 97.00, -17.627)),

    THREE(new Location(Bukkit.getWorld("sw1"), 20.415, 97.0000, -43.497)),

    FOUR(new Location(Bukkit.getWorld("sw1"), -2.578, 97.00000, -66.523)),

    FIVE(new Location(Bukkit.getWorld("sw1"), -23.676, 97.00000, -42.066)),

    SIX(new Location(Bukkit.getWorld("sw1"), -50.368, 97.000, -19.632)),

    SEVEN(new Location(Bukkit.getWorld("sw1"), -71.379, 97.0000, 4.4)),

    EIGHT(new Location(Bukkit.getWorld("sw1"), -46.518, 97.000, 24.131)),

    NINE(new Location(Bukkit.getWorld("sw1"), -24.551, 97.0000, 52.149)),

    TEN(new Location(Bukkit.getWorld("sw1"), -0.473, 97.000, 72.177)),

    ELEVEN(new Location(Bukkit.getWorld("sw1"), 20.479, 97.00000, 49.484)),

    TWELVE(new Location(Bukkit.getWorld("sw1"), 46.269, 97.000, 24.260));
    private final Location location;
    Jaulas(Location location) {
        this.location = location;
    }

    public static Location getLocations2() {
        for (Jaulas evento : Jaulas.values()) {
        	 
                 return evento.getLocation();
         }
        return null;
    }
    
    public Location getLocation() {
		return location;
	
}
public static final List<ItemStack> items = Arrays.asList(
    		

    		new ItemStack(Material.GOLD_HELMET),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.CHAINMAIL_CHESTPLATE),
    		new ItemStack(Material.GOLDEN_APPLE),
    		new ItemStack(Material.ARROW , 16),
    		new ItemStack(Material.POTION, 1, (short)16418),
            new ItemStack(Material.POTION, 1, (short)16387),
            new ItemStack(Material.POTION, 1, (short)16456),
    		new ItemStack(Material.CHAINMAIL_HELMET),
    		new ItemStack(Material.CHAINMAIL_BOOTS),
    		new ItemStack(Material.LEATHER_HELMET),
    		new ItemStack(Material.LEATHER_LEGGINGS),
    		new ItemStack(Material.POTION, 1 , (short)16388),
    		new ItemStack(Material.CHAINMAIL_HELMET),
    		new ItemStack(Material.CHAINMAIL_LEGGINGS),
    		new ItemStack(Material.CHAINMAIL_HELMET),
    		new ItemStack(Material.CHAINMAIL_BOOTS),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.EXP_BOTTLE),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.IRON_BOOTS),

    		new ItemStack(Material.IRON_SWORD),

    		new ItemStack(Material.GOLD_SWORD),

    		new ItemStack(Material.COOKED_BEEF, 16),
    		new ItemStack(Material.IRON_HELMET),
    		new ItemStack(Material.ARROW , 10),

    		new ItemStack(Material.ARROW , 10),

    		new ItemStack(Material.BOW),
    		new ItemStack(Material.BOW),
    		new ItemStack(Material.ENDER_PEARL),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.STONE, 16),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.DIRT, 16),new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.STONE, 16),
    		new ItemStack(Material.DIRT, 16),
    		new ItemStack(Material.GOLDEN_APPLE, 3),
    		new ItemStack(Material.CHAINMAIL_LEGGINGS),
    		new ItemStack(Material.POTION, 1 , (short)373),   
    		new ItemStack(Material.POTION, 1 , (short)373),    
    		new ItemStack(Material.POTION, 1 , (short)373)    

    );
}



// mdr 801.5 100 519.5
// lava 641.5 118 518.5
// pvp 732.5 80 521.5
// 1v1 868.5 95 457.5
