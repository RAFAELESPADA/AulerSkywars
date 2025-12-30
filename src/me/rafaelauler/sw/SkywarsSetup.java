package me.rafaelauler.sw;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class SkywarsSetup {

    private SkywarsManager manager;

    public SkywarsSetup(SkywarsManager manager) {
        this.manager = manager;
    }

    public void createAllGames() {
        for (Jaulas jaula : Jaulas.values()) {
            // Tenta pegar o mundo pelo nome do enum
            World world = Bukkit.getWorld(jaula.name());
            
            if (world == null) {
                Bukkit.getLogger().warning("Mundo " + jaula.name() + " não está carregado! Pulando criação do jogo.");
                continue; // pula essa jaula
            }

            // Cria o jogo usando o enum Jaulas
            SkyWarsGame game = manager.createGame(jaula);

            // Define spawn da lobby
            game.setSpawnLocation(Configs.LOBBY_SPAWN);

            Bukkit.getLogger().info("SkyWars criado para o mundo: " + jaula.name());
        }
    }
}
