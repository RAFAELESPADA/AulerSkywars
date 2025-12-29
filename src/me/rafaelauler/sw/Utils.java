package me.rafaelauler.sw;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

public class Utils {

    // Mapas estáticos para salvar o estado do jogador
    private static final HashMap<String, ItemStack[]> saveInv = new HashMap<>();
    private static final HashMap<String, ItemStack[]> saveArmor = new HashMap<>();
    private static final HashMap<String, Location> saveLocation = new HashMap<>();
    private static final HashMap<String, GameMode> saveGamemode = new HashMap<>();
    private static final HashMap<String, Scoreboard> saveScoreboard = new HashMap<>();
    private static final HashMap<String, Integer> saveLevel = new HashMap<>();
    private static final HashMap<String, Integer> saveHunger = new HashMap<>();
    private static final HashMap<String, PotionEffect[]> saveEffects = new HashMap<>();
    private static final HashMap<String, Integer> saveAir = new HashMap<>();

    /** Salva o estado completo do jogador */
    public static void savePlayerState(Player player) {
        saveLocation.put(player.getName(), player.getLocation());
        saveGamemode.put(player.getName(), player.getGameMode());
        saveScoreboard.put(player.getName(), player.getScoreboard());
        saveInv.put(player.getName(), player.getInventory().getContents());
        saveArmor.put(player.getName(), player.getInventory().getArmorContents());
        saveLevel.put(player.getName(), player.getLevel());
        saveHunger.put(player.getName(), player.getFoodLevel());
        saveAir.put(player.getName(), player.getRemainingAir());
        saveEffects.put(player.getName(), player.getActivePotionEffects().toArray(new PotionEffect[0]));
    }


    /** Restaura o estado completo do jogador */
    public static void restorePlayerState(Player player) {
        if (saveLocation.containsKey(player.getName())) {
            player.teleport(saveLocation.get(player.getName()));
        }

        if (saveGamemode.containsKey(player.getName())) {
            player.setGameMode(saveGamemode.get(player.getName()));
        }

        if (saveScoreboard.containsKey(player.getName())) {
            player.setScoreboard(saveScoreboard.get(player.getName()));
        }

        if (saveInv.containsKey(player.getName())) {
            player.getInventory().setContents(saveInv.get(player.getName()));
        }

        if (saveArmor.containsKey(player.getName())) {
            player.getInventory().setArmorContents(saveArmor.get(player.getName()));
        }}}
