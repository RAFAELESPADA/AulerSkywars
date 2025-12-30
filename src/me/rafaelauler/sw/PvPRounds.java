package me.rafaelauler.sw;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PvPRounds extends PlaceholderExpansion {

    private final Main plugin;

    public PvPRounds(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getIdentifier() {
        return "aulerskywars";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (identifier == null) {
            return "";
        }

        SkywarsManager manager = plugin.getManager();

        /*
         * %aulerskywars_players_room_1%
         * %aulerskywars_players_room_2%
         */
        if (identifier.startsWith("players_room_")) {
            try {
                int id = Integer.parseInt(identifier.substring("players_room_".length()));

                for (SkyWarsGame game : manager.getGames()) {
                    if (game.getId() == id) {
                        return String.valueOf(game.getPlayerCount());
                    }
                }
            } catch (NumberFormatException ignored) {
            }
            return "0";
        }

        /*
         * %aulerskywars_total_players%
         */
        if (identifier.equalsIgnoreCase("total_players")) {
            int total = 0;

            for (SkyWarsGame game : manager.getGames()) {
            	
                total += game.getPlayerCount();
            }

            return String.valueOf(total);
        }

        if (player == null) {
            return "0";
        }

        String basePath = "players." + player.getUniqueId() + ".";

        /*
         * %aulerskywars_kills%
         * %aulerskywars_deaths%
         * %aulerskywars_wins%
         */
        switch (identifier.toLowerCase()) {
            case "kills":
                return String.valueOf(plugin.getConfig().getInt(basePath + "kills", 0));

            case "deaths":
                return String.valueOf(plugin.getConfig().getInt(basePath + "deaths", 0));

            case "wins":
                return String.valueOf(plugin.getConfig().getInt(basePath + "wins", 0));
        }

        return "";
    }
}
