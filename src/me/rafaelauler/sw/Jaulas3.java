package me.rafaelauler.sw;





import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;


@Getter
public enum Jaulas3 {

    ONE(new Location(Bukkit.getWorld("sw3"), 8.343, 98.000000, 78.066)),

    TWO(new Location(Bukkit.getWorld("sw3"), 78.082, 98.000000, 8.199)),

    THREE(new Location(Bukkit.getWorld("sw3"), 6.505, 98.000000, -61.381)),

    FOUR(new Location(Bukkit.getWorld("sw3"), -61.351, 98.000000, 7.664));

    private final Location location;
    Jaulas3(Location location) {
        this.location = location;
    }
    public static List<Jaulas3> getUniqueShuffledAnimals()
    {
        // Get values into a modifiable list
        List<Jaulas3> animalsList = Arrays.asList(Jaulas3.values());
        
        // Shuffle the list to randomize the order
        Collections.shuffle(animalsList);
        
        return animalsList;
    }

    public static Location getRandomLocation() {   	
    	assert (Jaulas.values().length <= 4); {
    	Random r = new Random();
Jaulas3[] allAnimals = Jaulas3.values();
        
        // Generate a random index
        int randomIndex = r.nextInt(allAnimals.length);
        
        List<Jaulas3> uniqueShuffled = getUniqueShuffledAnimals();
        for (Jaulas3 animal : uniqueShuffled)
        
    	return animal.getLocation();
    	}
		return null;
	
}
    public static Location getLocations2() {
        for (Jaulas3 evento : Jaulas3.values()) {
        	 
                 return evento.getLocation();
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


