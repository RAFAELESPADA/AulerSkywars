package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public enum SkyWarsMap {


	MAP_1("sw1", new double[][]{
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

	MAP_2("sw2", new double[][]{
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

    MAP_3("sw3", new double[][]{
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

    MAP_4("sw4", new double[][]{
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

    MAP_5("sw5", new double[][]{
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
    private final double[][] cageCoords;

    SkyWarsMap(String worldName, double[][] cageCoords) {
        this.worldName = worldName;
        this.cageCoords = cageCoords;
    }
    public String getWorldName() {
        return worldName;
    }
    public List<Location> getCages(World world) {
        if (world == null) {
            Bukkit.getLogger().warning(
                "[SkyWars] Tentativa de pegar jaulas com mundo não carregado: " + worldName
            );
            return Collections.emptyList();
        }

        List<Location> list = new ArrayList<>();
        for (double[] c : cageCoords) {
            list.add(new Location(world, c[0], c[1], c[2]));
        }
        return list;
    }
}

