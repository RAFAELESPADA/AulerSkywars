package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CageManager {

    private final List<Location> cages;

    public CageManager(List<Location> cages) {
        this.cages = new ArrayList<>(cages);
        Collections.shuffle(this.cages);
    }

    public void teleport(Player player, int index) {
        if (index >= cages.size()) return;
        player.teleport(cages.get(index));
    }
}