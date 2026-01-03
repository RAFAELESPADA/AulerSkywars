package me.rafaelauler.sw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;

public class MainCommand implements CommandExecutor {
	private final Main plugin;
private final SkywarsManager manager = new SkywarsManager();
	   public MainCommand(Main plugin) {
		    this.plugin = plugin;
		}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(ChatColor.AQUA + "Use /sw join | joingame | listgames | leave | list | info");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "join" -> joinGame(player);

            case "leave" -> leaveGame(player);
            

            case "joinspawn" -> joinSpawn(player);
            case "joingame" -> joinSpecificGame(player, args);

            case "list" -> listPlayers(player);

            case "listgames" -> listGames(player);

            case "info" -> showInfo(player);

            default -> player.sendMessage(ChatColor.RED + "Comando inválido.");
        }

        return true;
    }

    private void joinSpawn(Player player) {
    	String worldName = Main.cfg_x1.getString("x1.coords.spawn.world");
        Location quitLocation = new Location(
                Bukkit.getWorld(worldName),
                Main.cfg_x1.getDouble("x1.coords.spawn.x"),
                Main.cfg_x1.getDouble("x1.coords.spawn.y"),
                Main.cfg_x1.getDouble("x1.coords.spawn.z")
        );
        player.teleport(quitLocation);
    }
    public static boolean isNumeric(String str) {
        try {
           Integer.parseInt(str);
           return true;
        } catch (NumberFormatException var2) {
           return false;
        }
     }
    
    private void joinSpecificGame(Player player, String[] args) {

        SkyWarsGame game = manager.getGame(player);
        if (game != null) {
            player.sendMessage(ChatColor.RED + "Você já está em uma sala de SkyWars!");
            return;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Utilize: /sw joingame <ID>");
            return;
        }

        if (!isNumeric(args[1])) {
            player.sendMessage(ChatColor.RED + "O argumento número dois deve ser um número de 1 ao 5!");
            return;
        }

        int gameId = Integer.parseInt(args[1]);
        SkyWarsGame targetGame = manager.getGames(gameId);

        // 4️⃣ Verifica se a sala existe
        if (targetGame == null) {
        	for (SkyWarsMap jaula : SkyWarsMap.values()) {
        	    try {
        	    	player.sendMessage("§aCriando salas...");
        	        SkyWarsGame game2 = manager.createGame(jaula);
        	        game2.setSpawnLocation(Configs.LOBBY_SPAWN);

        	        Bukkit.getLogger().info("Sala criada para o mapa " + jaula.name());
        	    } catch (Exception e) {
        	        Bukkit.getLogger().severe("Erro ao criar sala para " + jaula.name());
        	        player.sendMessage(ChatColor.RED + "Erro ao criar a sala! Contate um staff!");
        	        e.printStackTrace();
        	    }
        	}

        	player.sendMessage(ChatColor.GREEN + "Todas as salas foram criadas com sucesso! Tente entrar novamente");
        	 targetGame = manager.findAvailableGame();           	        
            	 }

        // 🔹 Agora podemos entrar no jogo com segurança
        targetGame.join(player);
        targetGame.updatePlayerVisibility();
        player.getInventory().clear();
    }
        
    private void joinGame(Player player) {

        if (manager.getGame(player) != null) {
            player.sendMessage(ChatColor.RED + "Você já está em uma sala de SkyWars!");
            return;
        }

        SkyWarsGame available = manager.findAvailableGame();

        if (available == null) {
            player.sendMessage("§aCriando salas...");

            SkyWarsGame created = null;

            for (SkyWarsMap map : SkyWarsMap.values()) {
                try {
                    SkyWarsGame game = manager.createGame(map);
                    game.setSpawnLocation(Configs.LOBBY_SPAWN);

                    if (created == null) {
                        created = game;
                    }

                    Bukkit.getLogger().info("Sala criada: " + map.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (created == null) {
                player.sendMessage(ChatColor.RED + "Não foi possível criar salas.");
                return;
            }

            available = created;
        }

        available.join(player);
        available.updatePlayerVisibility();

        player.getInventory().clear();
        player.sendMessage("§aVocê entrou na sala #" + available.getId());
    }


    private void leaveGame(Player player) {
        SkyWarsGame game = manager.getGame(player);

        if (game == null) {
            player.sendMessage(ChatColor.RED + "Você não está em nenhuma sala de SkyWars.");
            return;
        }

        manager.removePlayer(player);

        // Restaura inventário, gamemode e localização
        Utils.restorePlayerState(player);

        // Teleporta apenas se o jogador não estiver no lobby
        String worldName = Main.cfg_x1.getString("x1.coords.quit.world");
        World quitWorld = Bukkit.getWorld(worldName);
        if (quitWorld != null && player.getWorld() != quitWorld) {
        	if (player.getWorld() != Bukkit.getWorld("spawn")) {
            Location quitLocation = new Location(
                quitWorld,
                Main.cfg_x1.getDouble("x1.coords.quit.x"),
                Main.cfg_x1.getDouble("x1.coords.quit.y"),
                Main.cfg_x1.getDouble("x1.coords.quit.z")
            );
            player.teleport(quitLocation);
        }
        }
        game.updatePlayerVisibility();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getPluginManager().getPlugin("ItemJoin") != null) {
                    new ItemJoinAPI().getItems(player);
                }
            }
        }.runTaskLater(Main.getInstace(), 5L);

        player.sendMessage(ChatColor.RED + "Você saiu da sala de SkyWars.");
    }
    private void listGames(Player player) {
    	 for (SkyWarsGame game : manager.getGames()) {
         	if (manager.getGames().size() > 0 && manager.getGames() != null) {
         	player.sendMessage(ChatColor.GREEN + "Jogos ativos: " + ChatColor.YELLOW + game.getId());
         	}	else {
         		player.sendMessage(ChatColor.RED + "Não há salas ativas no momento.");
         	}
    	 }
    }
    private void listPlayers(Player player) {
    	
        boolean hasPlayers = false;

        for (SkyWarsGame game : manager.getGames()) {
        	if (manager.getGames().size() > 0) {
        	player.sendMessage(ChatColor.GREEN + "Jogos ativos: " + ChatColor.YELLOW + game.getId());
        	}	else {
        		player.sendMessage(ChatColor.RED + "Não há salas ativas no momento.");
        	}
            if (game.getPlayers().isEmpty()) continue;

            hasPlayers = true;
            player.sendMessage(ChatColor.GREEN + "Sala " + ChatColor.YELLOW + game.getId()
                    + ChatColor.GREEN + " (" + game.getPlayers().size() + " jogadores):");

            for (Player p : game.getPlayers()) {
                player.sendMessage(ChatColor.GRAY + " - " + ChatColor.YELLOW + p.getName());
            }
        }

        if (!hasPlayers) {
            player.sendMessage(ChatColor.RED + "Não há jogadores nas salas de SkyWars.");
        }
    }

    private void showInfo(Player player) {
        player.sendMessage(ChatColor.AQUA + "Plugin: AulerSkyWars");
        player.sendMessage(ChatColor.AQUA + "Versão: " + Main.getInstace().getDescription().getVersion());
        player.sendMessage(ChatColor.AQUA + "Autor: Rafael Auler");
    }
}
