package me.rafaelauler.sw;




import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;


@Getter
public enum Jaulas2 {

    ONE(new Location(Bukkit.getWorld("sw2"), 93.528, 96.000000, -1.039)),

    TWO(new Location(Bukkit.getWorld("sw2"), 66.714, 96.0000000, -27.700)),

    THREE(new Location(Bukkit.getWorld("sw2"), 24.700, 96.0000000, -66.700)),

    FOUR(new Location(Bukkit.getWorld("sw2"), -2.269, 96.0000000, -91.598)),

    FIVE(new Location(Bukkit.getWorld("sw2"), -27.700, 96.000000, -65.691)),

    SIX(new Location(Bukkit.getWorld("sw2"), -65.273, 96.00000, -23.700)),

    SEVEN(new Location(Bukkit.getWorld("sw2"), -93.700, 97.00000, 4.3999)),

    EIGHT(new Location(Bukkit.getWorld("sw2"), -64.254, 97.00000, 29.700)),

    NINE(new Location(Bukkit.getWorld("sw2"), -22.970, 96.0000000, 67.075)),

    TEN(new Location(Bukkit.getWorld("sw2"), 2.577, 96.000000, 93.700)),

    ELEVEN(new Location(Bukkit.getWorld("sw2"), 28.700, 96.000000, 67.004)),

    TWELVE(new Location(Bukkit.getWorld("sw2"), 67.258, 96.00000000, 24.700));
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
    
    public Location getLocation() {
		return location;
	
}
}



// mdr 801.5 100 519.5
// lava 641.5 118 518.5
// pvp 732.5 80 521.5
// 1v1 868.5 95 457.5

