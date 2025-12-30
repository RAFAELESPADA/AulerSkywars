package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wavemc.core.bukkit.api.HelixActionBar;

public class SkyWarsGame implements Listener {

    private final int id;
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> playersInPvp = new ArrayList<>();

    private final String worldName;
    private World world;
    private Location spawn;
    private final Jaulas map;

    private GameState state = GameState.WAITING;
    private int countdown = 30;
    private boolean started = false;
    private boolean cagesClosed = false;

    public SkyWarsGame(int id, Jaulas map) {
        this.id = id;
        this.map = map;
        this.worldName = map.name();
       
        this.world = Bukkit.getWorld(worldName);
        if (this.world == null) {
            Bukkit.getServer().getLogger().warning("Mundo " + worldName + " não carregado!");
            return;
        }

        this.spawn = new Location(world, 0.5, 100, 0.5);
    }

    // ================== GETTERS ==================
    public int getId() { return id; }
    public GameState getState() { return state; }
    public String getWorldName() { return worldName; }
    public World getWorld() { return world; }
    public Location getSpawnLocation() { return Configs.LOBBY_SPAWN; }
    public void setSpawnLocation(Location spawn) { this.spawn = spawn; }
    public void setState(GameState spawn) { this.state = state; }
    private int taskId = -1;

    public void startTask() {
        if (taskId != -1) return;

        taskId = Bukkit.getScheduler().runTaskTimer(Main.getInstace(), () -> {
            if (state == GameState.STOPPED) {
                Bukkit.getScheduler().cancelTask(taskId);
                taskId = -1;
                return;
            }
            tick();
        }, 20L, 20L).getTaskId();
    }
    public List<Player> getPlayers() {
        List<Player> result = new ArrayList<>();
        for (UUID uuid : players) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) result.add(p);
        }
        return result;
    }

    public List<Player> getPlayersInPvP() {
        List<Player> result = new ArrayList<>();
        for (UUID uuid : playersInPvp) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) result.add(p);
        }
        return result;
    }

    public boolean isInGame(Player player) {
        return players.contains(player.getUniqueId());
    }

    // ================== PLAYER ACTIONS ==================
    public void join(Player player) {
    	 if (players.contains(player.getUniqueId())) return;
    	 if (getState() != GameState.WAITING && getState() != GameState.COUNTDOWN) {
    		 player.sendMessage(ChatColor.RED + "Essa partida está em andamento. Escolha outra!");
    		 return;
    	 }
    	    players.add(player.getUniqueId());
           player.setAllowFlight(false);
           player.setFlying(false);
           player.setGameMode(GameMode.SURVIVAL);
    	    // Registrar evento se ainda não registrado
    	    if (!Bukkit.getPluginManager().isPluginEnabled(Main.getInstace())) {
    	        Bukkit.getPluginManager().registerEvents(this, Main.getInstace());
    	    }
        if (players.size() >= 2 && state == GameState.WAITING) {
            startCountdown();
        }
        Bukkit.getLogger().info("[SkyWars] Player entrou. Sala " + id + " | Players=" + players.size());
        player.teleport(spawn);
    }

    public void leave(Player player) {
        players.remove(player.getUniqueId());
        playersInPvp.remove(player.getUniqueId());
        player.sendMessage("§cVocê saiu da sala " + id);
    }

    // ================== EVENTOS ==================
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!isInGame(p)) return;

        leave(p);
        broadcast("§c" + p.getName() + " saiu da partida");
        checkWin();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!isInGame(p)) return;
        if (!started && cagesClosed) e.setTo(e.getFrom());
    }
    public void updatePlayerVisibility() {
        List<Player> inGame = getPlayers();
        List<Player> allPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player p : allPlayers) {
            for (Player target : allPlayers) {
                if (inGame.contains(p) && !inGame.contains(target)) {
                    // jogador na partida não vê quem não está
                    p.hidePlayer(target);
                } else if (inGame.contains(p) && inGame.contains(target)) {
                    // jogadores da partida se veem
                    p.showPlayer(target);
                } else if (!inGame.contains(p)) {
                    // jogadores fora da partida veem todos normalmente
                    p.showPlayer(target);
                }
            }
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!isInGame(p)) return;
        if (!started) e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!isInGame(p)) return;
        if (!started && e.getCause() == DamageCause.FALL) e.setCancelled(true);
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        Player damager = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();
        if (!isInGame(damager) || !isInGame(victim)) return;
        if (!started || !playersInPvp.contains(damager.getUniqueId())) e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!isInGame(p)) return;

        e.setDeathMessage(null);
        players.remove(p.getUniqueId());
        playersInPvp.remove(p.getUniqueId());

        String path = "players." + p.getUniqueId();
        if (e.getEntity().getKiller() instanceof Player) {
        Player k = e.getEntity().getKiller();
        String path2 = "players." + k.getUniqueId();
        

        int kills = Main.getInstace().getConfig().getInt("players." + k.getUniqueId() + ".kills");
        int deaths = Main.getInstace().getConfig().getInt("players." + p.getUniqueId() + ".kills");
        
            Main.getInstace().getConfig().set(path2 + ".kills", kills + 1);

            Main.getInstace().getConfig().set(path + ".deaths", deaths + 1);
           
        }
        
        p.sendMessage("§cVocê foi eliminado!");
        broadcast("§e" + p.getName() + " foi eliminado da partida!");
        Bukkit.dispatchCommand(p, "sw leave");
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> p.performCommand("sw leave"), 5L);
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
        	p.getInventory().clear();
        	ItemJoinAPI ij = new ItemJoinAPI();
           ij.getItems(p);
        }, 20L * 2);
        checkWin();
        }
    

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (!isInGame(p)) return;

        String msg = e.getMessage().toLowerCase();
        if (!started && !msg.startsWith("/sw") && !msg.startsWith("/lobby")) {
            e.setCancelled(true);
            p.sendMessage("§cComando bloqueado durante a partida");
            return;
        }
if (msg.startsWith("/lobby") && started) {
        leave(p);
        p.teleport(Configs.MAIN_SPAWN);
        ItemJoinAPI ij = new ItemJoinAPI();
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
           ij.getItems(p);
        }, 20L * 2);
    
        broadcast("§c" + p.getName() + " saiu da partida");
        checkWin();
    }
    }

    // ================== GAME FLOW ==================
    public void startCountdown() {
        state = GameState.STARTING;
        countdown = 30;
    }

    public void tick() {
        if (state != GameState.STARTING) return;
if (players.size() < 2) {
	 countdown = 30;
	return;
}
        if (countdown <= 0) {
            startGame();
            return;
        }
        if (countdown % 5 == 0 || countdown <= 25) {
            announceStarting();
        }
        broadcast("§eIniciando em §6" + countdown + "s");
        countdown--;
    }
    private void announceStarting() {
        // Pega o mundo do lobby
        World lobbyWorld = Bukkit.getWorld("spawn");
        if (lobbyWorld == null) return;

        String msg = "§eA partida da Sala §a#" + id + " §evai começar em " + countdown + " segundos! Clique aqui para entrar nela!";
TextComponent txt = new TextComponent(msg);
txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "sw joingame " + id));

        // Envia para todos os jogadores do lobby
        for (Player p : lobbyWorld.getPlayers()) {
            p.sendMessage(msg);
        }

        // Opcional: também pode enviar ActionBar
        for (Player p : lobbyWorld.getPlayers()) {
            HelixActionBar.send(p, msg);
            p.playSound(p.getLocation(), Sound.valueOf("ARROW_HIT"), 10f, 1f);
        }
    }

    private void startGame() {
        state = GameState.RUNNING;
        cagesClosed = true;

        List<UUID> ordered = new ArrayList<>(players);
        Collections.shuffle(ordered);

        // Usa Cage + Jaulas
        Cage.teleportByQueueOrder(ordered, map);
       for (UUID u : ordered) {
    	   Player p = Bukkit.getPlayer(u);
    	   if (p.getWorld() != Bukkit.getWorld("swlobby")) {
        Cage.createCage(p, Material.GLASS);
        broadcast("§aA partida vai começar em 15 segundos!");
        p.playSound(p.getLocation(), Sound.valueOf("CLICK"), 10f, 10f);
       }
       }
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            cagesClosed = false;
            
            for (UUID u : ordered) {
            	Player u2 = Bukkit.getPlayer(u);

Cage.removeCage(u2.getLocation(), Material.GLASS);
            	 playersInPvp.add(u2.getUniqueId());
            	 u2.playSound(u2.getLocation(), Sound.valueOf("CLICK"), 10f, 10f);
                 
            }
            
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
started = true;
            }, 20L * 18);

            broadcast("§aA partida começou!");
        }, 20L * 15);
    }

    public void checkWin() {
        if (players.size() == 1 && state == GameState.RUNNING) {
            Player winner = Bukkit.getPlayer(players.get(0));
            if (winner != null) {
                Bukkit.broadcastMessage(ChatColor.GOLD + winner.getName() + " venceu a partida da Sala #" + id);
                playVictoryAnimation(winner);
                TitleAPI.sendTitle(winner, 80, 80, 80, ChatColor.GOLD + " VITÓRIA!");
                // chama a animação de vitória
            }
 int wins = Main.getInstace().getConfig().getInt("players." + winner.getUniqueId() + ".wins");
            
            Main.getInstace().getConfig().set("players." + winner.getUniqueId() + ".wins", wins + 1);
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
               destroy();
             }, 20L * 7);
         }
        }
    /** Contagem de jogadores em uma partida */
    public int getPlayerCount() {
        return players.size();
    }
    public static void throwRandomFirework(Player p) {
        Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        //Our random generator
        Random r = new Random();

        //Get the type
        int rt = r.nextInt(5) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;

        //Get our random colours
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = Color.fromRGB(r1i);
        Color c2 = Color.fromRGB(r2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

        //Then apply the effect to the meta
        fwm.addEffect(effect);

        //Generate some random power and set it


        //Create our effect with this   int rp = r.nextInt(2) + 1;
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);

        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);
        
    }

    public void playVictoryAnimation(Player winner) {
        if (winner == null) return;

        Bukkit.getScheduler().runTaskTimer(Main.getInstace(), new Runnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 80) { // dura 2 segundos (40 ticks)
                    return;
                }

                // Spawn de partículas coloridas ao redor do jogador
                throwRandomFirework(winner);
                // Som de comemoração
                winner.getWorld().playSound(winner.getLocation(), Sound.valueOf("NOTE_PLING"), 1.0f, 1.0f);

                ticks++;
            }
        }, 0L, 10L); // executa a cada 2 ticks
    }
    // ================== UTILS ==================
    public void broadcast(String msg) {
        for (Player p : getPlayers()) {
        	p.sendMessage(msg); 
        	HelixActionBar.send(p, msg); 
        }
    }

    public void destroy() {
        state = GameState.STOPPED;
         
        for (UUID u : new ArrayList<>(players)) {
            Player p = Bukkit.getPlayer(u);
            if (p != null) Bukkit.dispatchCommand(p, "sw leave");
            p.spigot().respawn();
            ItemJoinAPI ij = new ItemJoinAPI();
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            	p.getInventory().clear();
               ij.getItems(p);
            }, 20L * 4);
        }
     // ================= MULTIVERSE RESET =================
        if (world != null) {
            String worldNameBackup = worldName + "copy"; // nome do backup
            // Descarrega o mundo
            Bukkit.unloadWorld(world, false);

            // Carrega o backup via Multiverse-Core
            try {

            	Main.getMVWorldManager().deleteWorld(world.getName());
            	Main.getMVWorldManager().cloneWorld(worldNameBackup, world.getName(), "VoidGen");
                Bukkit.getServer().getLogger().info("[SkyWars] Mundo " + worldName + " reiniciado a partir do backup.");
            } catch (Exception e) {
                Bukkit.getServer().getLogger().warning("[SkyWars] Falha ao reiniciar o mundo " + worldName + ": " + e.getMessage());
            }
        players.clear();   
        playersInPvp.clear();
        HandlerList.unregisterAll(this);
    }
}
}

