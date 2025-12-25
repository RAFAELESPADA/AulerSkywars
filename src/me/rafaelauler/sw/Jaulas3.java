package me.rafaelauler.sw;





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


