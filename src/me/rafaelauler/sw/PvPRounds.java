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
        if (player == null) return "";

        SkywarsManager manager = plugin.getManager(); // pega o manager do novo sistema

        // Placeholder players_room_X
        if (identifier.startsWith("players_room_")) {
            try {
                int roomNumber = Integer.parseInt(identifier.split("_")[2]); // pega o número da sala

                // Busca a partida pelo ID da sala
                for (SkyWarsGame game : manager.getGames()) {
                    if (game.getId() == roomNumber) {
                        return String.valueOf(game.getPlayers().size());
                    }
                }

                return "0"; // não encontrou a sala
            } catch (Exception e) {
                return "0"; // erro no parse
            }
        }
        if (identifier.equals("total_players")) {
            int total = 0;
            for (SkyWarsGame game : manager.getGames()) {
                total += game.getPlayers().size();
            }
            return String.valueOf(total);
        }

        

        // Estatísticas do jogador
        switch (identifier.toLowerCase()) {
            case "kills":
                return String.valueOf(plugin.getConfig().getInt("players." + player.getUniqueId() + ".kills", 0));
            case "deaths":
                return String.valueOf(plugin.getConfig().getInt("players." + player.getUniqueId() + ".deaths", 0));
            case "wins":
                return String.valueOf(plugin.getConfig().getInt("players." + player.getUniqueId() + ".wins", 0));
        }

        // Retorna o placeholder original se não for reconhecido
        return identifier;
    }
}
