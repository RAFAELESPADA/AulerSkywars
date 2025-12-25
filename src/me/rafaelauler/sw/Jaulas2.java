package me.rafaelauler.sw;




import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;


@Getter
public enum Jaulas2 {

    ONE(new Location(Bukkit.getWorld("sw2"), 92.528, 96.000000, -2.039)),

    TWO(new Location(Bukkit.getWorld("sw2"), 65.714, 96.0000000, -26.700)),

    THREE(new Location(Bukkit.getWorld("sw2"), 23.700, 96.0000000, -65.700)),

    FOUR(new Location(Bukkit.getWorld("sw2"), -1.269, 96.0000000, -90.598)),

    FIVE(new Location(Bukkit.getWorld("sw2"), -26.700, 96.000000, -66.691)),

    SIX(new Location(Bukkit.getWorld("sw2"), -64.273, 96.00000, -22.700)),

    SEVEN(new Location(Bukkit.getWorld("sw2"), -92.700, 97.00000, 3.3999)),

    EIGHT(new Location(Bukkit.getWorld("sw2"), -63.254, 97.00000, 28.700)),

    NINE(new Location(Bukkit.getWorld("sw2"), -21.970, 96.0000000, 65.075)),

    TEN(new Location(Bukkit.getWorld("sw2"), 2.577, 96.000000, 92.700)),

    ELEVEN(new Location(Bukkit.getWorld("sw2"), 27.700, 96.000000, 65.004)),

    TWELVE(new Location(Bukkit.getWorld("sw2"), 66.258, 96.00000000, 23.700));
    private final Location location;
    Jaulas2(Location location) {
        this.location = location;
    }

    public static Location getLocations2() {
        for (Jaulas2 evento : Jaulas2.values()) {
        	 
                 return evento.getLocation();
         }
        return null;
    }
    public static List<Jaulas2> getUniqueShuffledAnimals()
    {
        // Get values into a modifiable list
        List<Jaulas2> animalsList = Arrays.asList(Jaulas2.values());
        
        // Shuffle the list to randomize the order
        Collections.shuffle(animalsList);
        
        return animalsList;
    }

    public static Location getRandomLocation() {   	
    	assert (Jaulas.values().length <= 12); {
    	Random r = new Random();
Jaulas2[] allAnimals = Jaulas2.values();
        
        // Generate a random index
        int randomIndex = r.nextInt(allAnimals.length);
        
        List<Jaulas2> uniqueShuffled = getUniqueShuffledAnimals();
        for (Jaulas2 animal : uniqueShuffled)
        
    	return animal.getLocation();
    	}
		return null;
	
}
    
    public Location getLocation() {
		return location;
	
}
}



// mdr 801.5 100 519.5
// lava 641.5 118 518.5
// pvp 732.5 80 521.5
// 1v1 868.5 95 457.5

