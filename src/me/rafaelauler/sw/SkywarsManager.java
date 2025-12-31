package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkywarsManager {

	private final Map<Integer, SkyWarsGame> games = new LinkedHashMap<>();
    private int nextId = 1;
    private final int maxPlayersPerGame = 16;
    private int lastIndex = 0;
    // ================== CREATE / GET ==================

    /**
     * Cria uma nova partida com ID automático e um mapa específico
     */
    public SkyWarsGame createGame(SkyWarsMap map) {
        int id = nextId++;
        SkyWarsGame game = new SkyWarsGame(id, map);

        
        // Coloca o jogo no mapa imediatamente
        games.put(id, game);

        // Log claro
        Bukkit.getLogger().info("Sala " + id + " criada para o mundo: " + map);

        // Registra eventos e inicia task
        Bukkit.getPluginManager().registerEvents(game, Main.getInstace());
        game.startTask();

        return game;
    }

    /** Retorna todas as partidas ativas */
    public List<SkyWarsGame> getGames() {
        return new ArrayList<>(games.values());
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
    public void endGame(SkyWarsGame game) {
        for (Player p : new ArrayList<>(game.getPlayers())) {
            sendToLobby(p);
        }

        game.resetInternal();
    }
    private void sendToLobby(Player p) {
        // implementar depois
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
