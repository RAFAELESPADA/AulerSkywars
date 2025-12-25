package me.rafaelauler.sw;



import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;


@Getter
public enum Jaulas {

    ONE(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    TWO(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    THREE(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    FOUR(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    FIVE(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    SIX(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    SEVEN(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    EIGHT(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    NINE(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    TEN(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    ELEVEN(new Location(Bukkit.getWorld("sw1"), 151, 64, 54)),

    TWELVE(new Location(Bukkit.getWorld("sw1"), 151, 64, 54));
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
}



// mdr 801.5 100 519.5
// lava 641.5 118 518.5
// pvp 732.5 80 521.5
// 1v1 868.5 95 457.5
