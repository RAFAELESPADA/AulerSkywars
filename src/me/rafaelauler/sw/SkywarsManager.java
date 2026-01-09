package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;

public class SkywarsManager {

	private final Map<Integer, SkyWarsGame> games = new LinkedHashMap<>();
    private int nextId = 1;
    private BukkitTask victoryTask;
    private final int maxPlayersPerGame = 16;
    private int lastIndex = 0;

    // ================== CREATE / GET ==================

    /**
     * Cria uma nova partida com ID automático e um mapa específico
     */
    public SkyWarsGame createGame(SkyWarsMap map) {

        if (games.size() >= 5) {
            return null;
        }

        int id = nextId++;
        SkyWarsGame game = new SkyWarsGame(id, map);

        game.setState(GameState.WAITING);

        games.put(id, game);

        Bukkit.getLogger().info("Sala " + id + " criada para o mapa " + map.name());

        Bukkit.getPluginManager().registerEvents(game, Main.getInstance());
        game.startTask();

        Main.getInstance().CarregarTodos();

        return game;
    }

    /** Retorna todas as partidas ativas */
    public Collection<SkyWarsGame> getGames() {
        return Collections.unmodifiableCollection(games.values());
    }
    public SkyWarsGame getDefaultGame() {
        return games.values().stream().findFirst().orElse(null);
    }
    public SkyWarsGame getGames(int id) {
        return games.get(id);
    }

    /** Retorna a partida em que um jogador está, ou null */
    public SkyWarsGame getGame(Player player) {
        return games.values().stream()
                .filter(game -> game.isInGame(player))
                .findFirst()
                .orElse(null);
    }

    public SkyWarsGame findAvailableGame() {
        return games.values().stream()
            .filter(game ->
                (game.getState() == GameState.WAITING
              || game.getState() == GameState.STARTING)
              && game.getPlayers().size() < maxPlayersPerGame
            )
            .max((a, b) -> Integer.compare(
                a.getPlayers().size(),
                b.getPlayers().size()
            ))
            .orElse(null);
    }
    public void endGame(SkyWarsGame game) {
        for (Player p : new ArrayList<>(game.getPlayers())) {
            sendToLobby(p);
        }
        
        game.resetWorldAndRestart();
        game.updateVisibility();
    }
    private void sendToLobby(Player p) {
      p.getInventory().clear();
      p.getInventory().setArmorContents(null);
      ItemJoinAPI ij = new ItemJoinAPI();
      ij.getItems(p);
      victoryTask = Bukkit.getScheduler().runTaskLater(
      	    Main.plugin,
      	    new Runnable() {

      	        @Override
      	        public void run() {
      	            
      	            ij.getItems(p);
      	            p.playSound(p.getLocation(), Sound.valueOf("ARROW_HIT"), 10f, 10f);
      	        }
      	    }, 30L);
      p.teleport(Configs.MAIN_SPAWN);
    }
    public SkyWarsGame findAvailableGame2() {
        List<SkyWarsGame> list = new ArrayList<>(games.values());
        if (list.isEmpty()) return null;

        for (int i = 0; i < list.size(); i++) {
            SkyWarsGame game = list.get(lastIndex % list.size());
            lastIndex++;

            if ((game.getState() == GameState.WAITING
                || game.getState() == GameState.STARTING)
                && game.getPlayers().size() < maxPlayersPerGame) {
                return game;
            }
        }
        return null;
    }

    // ================== PLAYER MANAGEMENT ==================

    /** Remove um jogador de qualquer partida */
    public void removePlayer(Player player) {
        SkyWarsGame game = getGame(player);
        if (game != null) game.leave(player);
    }

    /** Contagem de jogadores em uma partida */
    public int getPlayerCount(int gameId) {
        SkyWarsGame game = games.get(gameId);
        return game != null ? game.getPlayers().size() : 0;
    }

    /** Lista de jogadores de uma partida */
    public List<Player> getPlayers(int gameId) {
        SkyWarsGame game = games.get(gameId);
        return game != null ? game.getPlayers() : Collections.emptyList();
    }

    /** Remove e destrói uma partida pelo ID */
    public void removeGame(int gameId) {
        SkyWarsGame game = games.remove(gameId);
        if (game != null) game.resetGame();
    }
}
