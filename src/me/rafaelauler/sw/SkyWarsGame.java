package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wavemc.core.bukkit.api.HelixActionBar;

public class SkyWarsGame implements Listener {

    private final int id;
    private final Set<UUID> players = new HashSet<>();
    private final List<UUID> playersInPvp = new ArrayList<>();
    private final Map<UUID, UUID> specTarget = new HashMap<>();

    private final Map<Chest, Boolean> abriu = new HashMap<>();
    private World world;
    private Location spawn;
    private SkyWarsMap map;
    private final List<UUID> spectators = new ArrayList<>();
    private GameState state = GameState.WAITING;
    private int countdown = 30;
    private boolean cagesClosed = false;

    private boolean damage = false;
    private final List<UUID> spectatorsWithGUI = new ArrayList<>();
    private boolean guiBlink = false;
    private int gameTask = -1;
    private int spectatorGUITask = -1;
    private int compassTask = -1;
    private int victoryTask = -1;
    private boolean worldLoading = false;
    private int startRetries = 0;


    public SkyWarsGame(int id, SkyWarsMap map) {
        this.id = id;
        this.map = map;
    }

    // ================== GETTERS ==================
    public int getId() { return id; }
    public GameState getState() { return state; }
    public Location getSpawnLocation() { return Configs.LOBBY_SPAWN; }
    public void setSpawnLocation(Location spawn) { this.spawn = spawn; }
    public void setState(GameState state) {
        this.state = state;
    }
    private int taskId = -1;

    public void startTask() {
        if (gameTask != -1) return;

        gameTask = Bukkit.getScheduler().runTaskTimer(
            Main.plugin,
            this::tickRunning,

            20L,
            20L
        ).getTaskId();
    }
    public void stopGameTask() {
        if (gameTask != -1) {
            Bukkit.getScheduler().cancelTask(gameTask);
            gameTask = -1;
        }
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
    	 if (worldLoading) {
    		    player.sendMessage("§cA arena está reiniciando.");
    		    return;
    		}
    	 if (state == GameState.RUNNING || state == GameState.ENDING) {
    		    player.sendMessage("§cEssa partida já começou.");
    		 ItemJoinAPI api = new ItemJoinAPI();
    		 if (player.getInventory().getContents() == null) {
    			 api.getItems(player);
    		 }
    		 return;
    	 }
    	 players.add(player.getUniqueId());

    	 if (players.size() == 2 && state == GameState.WAITING) {
    	     startTask();
    	 }
    	    
           player.setAllowFlight(false);
           player.setFlying(false);
           player.setGameMode(GameMode.SURVIVAL);
        Bukkit.getLogger().info("[SkyWars] Player entrou. Sala " + id + " | Players=" + players.size());
        player.teleport(spawn);
    }

    public void leave(Player player) {
    	 if (state == GameState.ENDING || worldLoading) {
    	        // Apenas limpa status do jogador
    		 UUID uuid = player.getUniqueId();
    		 players.remove(uuid);
    		 playersInPvp.remove(uuid);
    		 spectators.remove(uuid);
    		 specTarget.remove(uuid);
    		 spectatorsWithGUI.remove(uuid);
    	        return;
    	    }
        players.remove(player.getUniqueId());
        playersInPvp.remove(player.getUniqueId());
        specTarget.remove(player.getUniqueId());
        spectators.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);
        if (players.isEmpty()) {
            stopGameTask();
        }
        updateVisibility();
        player.sendMessage("§cVocê saiu da sala " + id);
    }
    public void clearPlayers() {
        players.clear();
        playersInPvp.clear();
        spectators.clear();
        specTarget.clear();
        spectatorsWithGUI.clear();
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
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo() == null) return;

        // 🔥 Ignora movimentos irrelevantes (câmera, head rotation)
        if (e.getFrom().getBlockX() == e.getTo().getBlockX()
         && e.getFrom().getBlockY() == e.getTo().getBlockY()
         && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        if (!isInGame(e.getPlayer())) return;
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!players.contains(uuid) && !spectators.contains(uuid)) return;

        boolean isPlayer = players.contains(uuid);
        boolean isSpectator = spectators.contains(uuid);

        if (!isPlayer && !isSpectator) return;

        // 🚫 Trava jogador antes do início (jaulas)
        if (isPlayer && state != GameState.RUNNING && cagesClosed) {
            e.setTo(e.getFrom());
            return;
        }

        // ☠️ Espectador caiu no void
        if (isSpectator && p.getLocation().getY() <= 0) {
            Location safe = getSpectatorSafeLocation(p);
            if (safe != null) {
                p.teleport(safe);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {

        World world = e.getWorld();

        if (!world.getName().startsWith("sw")) return;

        Chunk chunk = e.getChunk();

        for (BlockState state : chunk.getTileEntities()) {

            if (!(state instanceof Chest)) continue;

            Chest chest = (Chest) state;

            // Evita resetar baú já configurado
            if (chest.hasMetadata("SW")) continue;

            Main.getInstace().setarLoot(chest);
            chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), true));
        }
    }
    @EventHandler
    public void onChestOpen(InventoryOpenEvent e) {
        if (!(e.getInventory().getHolder() instanceof Chest)) return;

        Chest chest = (Chest) e.getInventory().getHolder();
        World world = chest.getWorld();
        if (!world.getName().startsWith("sw")) return;
        if (chest.hasMetadata("SW")) return;
        if (!isInGame((Player) e.getPlayer())) return;

        Main.getInstace().setarLoot(chest);
        chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), true));
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!isInGame(p)) return;
        if (state != GameState.RUNNING || cagesClosed) {
            e.setCancelled(true);
        } 
    }


    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!isInGame(p)) return;

        if (state != GameState.RUNNING) {
            e.setCancelled(true);
        }
        
    }
    @EventHandler
    public void onDamage2(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!isInGame(p)) return;

        if (state == GameState.RUNNING) {
        	if (e.getCause() == DamageCause.FALL) {
        		if (!damage) {
            e.setCancelled(true);
        }
        	}
        }
    }


    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;

        Player damager = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        if (!isInGame(damager) || !isInGame(victim)) return;

        if (state != GameState.RUNNING) {
            e.setCancelled(true);
            return;
        }
        if (!isAlive(damager.getUniqueId()) || !isAlive(victim.getUniqueId())) {
            e.setCancelled(true);
        
}
    }
    public static ItemStack createRoomItem(SkyWarsGame game) {
        Material glass;
        short data;
        String status;

        switch (game.getState()) {
            case WAITING:
                glass = Material.STAINED_GLASS_PANE;
                data = 5; // verde
                status = "§aDisponível";
                break;

            case STARTING:
                glass = Material.STAINED_GLASS_PANE;
                data = 4; // amarelo
                status = "§eIniciando";
                break;

            case RUNNING:
                glass = Material.STAINED_GLASS_PANE;
                data = 14; // vermelho
                status = "§cEm andamento";
                break;

            case ENDING:
                glass = Material.STAINED_GLASS_PANE;
                data = 10; // roxo
                status = "§5Finalizando";
                break;

            default:
                glass = Material.STAINED_GLASS_PANE;
                data = 15; // preto
                status = "§7Indisponível";
                break;
        }

        ItemStack item = new ItemStack(glass, 1, data);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§eSala #" + game.getId());

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Estado: " + status);
        if (game.state == GameState.RUNNING) {
            lore.add("§7Em jogo: §f" + game.getPlayingCount());
        }
        else {
        lore.add("§7Jogadores: §f" + game.getLobbyCount());
        }
        lore.add("");
        lore.add("§eClique para entrar");

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
    public int getLobbyCount() {
        if (state == GameState.WAITING || state == GameState.STARTING) {
            return players.size();
        }
        return 0;
    }
    public int getPlayingCount() {
        return (int) players.stream()
            .filter(u -> !spectators.contains(u))
            .count();
    }
    public void showEveryoneToEveryone() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                p.showPlayer(target);
            }
        }
    }
    public static void openRoomSelector(Player p) {
        Collection<SkyWarsGame> games = Main.getInstace().getManager().getGames();

        int size = ((games.size() - 1) / 9 + 1) * 9;
        Inventory inv = Bukkit.createInventory(null, size, "§8Selecionar Sala");
        if (games.isEmpty()) {
            p.sendMessage("§cNenhuma sala disponível.");
            return;
        }
        for (SkyWarsGame game : games) {
            inv.addItem(createRoomItem(game));
        }
        p.openInventory(inv);
    }
    @EventHandler
    public void onRoomClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("§8Selecionar Sala")) return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        if (!name.startsWith("Sala #")) return;

        int id = Integer.parseInt(name.replace("Sala #", ""));
        SkyWarsGame game = Main.getInstace().getManager().getGames(id);

        if (game == null) {
            p.sendMessage("§cSala não encontrada.");
            return;
        }

        if (game.getState() != GameState.WAITING) {
            p.sendMessage("§cEssa sala já começou.");
            return;
        }
        Bukkit.dispatchCommand(p, "sw joingame " + id);
        p.closeInventory();
    }



    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!isInGame(p)) return;

        e.setDeathMessage(null);

        String path = "players." + p.getUniqueId();
        if (e.getEntity().getKiller() instanceof Player) {
        Player k = e.getEntity().getKiller();
        String path2 = "players." + k.getUniqueId();
        

        int kills = Main.getInstace().getConfig().getInt("players." + k.getUniqueId() + ".kills");
        int deaths = Main.getInstace().getConfig().getInt("players." + p.getUniqueId() + ".deaths");

        
            Main.getInstace().getConfig().set(path2 + ".kills", kills + 1);

            Main.getInstace().getConfig().set(path + ".deaths", deaths + 1);
           
        }    
        p.sendMessage("§cVocê foi eliminado!");
        TitleAPI.sendTitle(p, 20, 120, 20, "§c§lDERROTADO");
        broadcast("§e" + p.getName() + " foi eliminado da partida!");
        playersInPvp.remove(p.getUniqueId());
        spectators.add(p.getUniqueId());
        updateVisibility();
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            p.spigot().respawn();

            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(true);
            p.setFlying(true);

            giveSpectatorItem(p);
            	
            if (!playersInPvp.isEmpty()) {
                Player p2 = Bukkit.getPlayer(playersInPvp.get(0));
                if (p2 != null) {
                    p.teleport(p2.getLocation().add(0, 2, 0));
                }
            }
            p.sendMessage("§7Você agora é um §fESPECTADOR§7.");
            
            }, 2L);
        checkWin();
        }
    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (spectators.contains(p.getUniqueId())) {
            if (e.getCurrentItem() != null &&
                e.getCurrentItem().getType() == Material.COMPASS) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(org.bukkit.event.player.PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        if (spectators.contains(p.getUniqueId()) &&
            e.getItemDrop().getItemStack().getType() == Material.COMPASS) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onItemDrop(org.bukkit.event.player.PlayerPickupItemEvent e) {
        Player p = e.getPlayer();

        if (spectators.contains(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSpectatorInteract(org.bukkit.event.player.PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!spectators.contains(p.getUniqueId())) return;

        if (e.getItem() == null) return;
        if (e.getItem().getType() == Material.COMPASS) {
            openSpectatorGUI(p);
            e.setCancelled(true);
        }
    }
    public void giveSpectatorItem(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName("§aAbrir seleção de jogadores");
        compass.setItemMeta(meta);

        p.getInventory().setItem(0, compass);
    }
    @EventHandler
    public void onSpectatorBreak(BlockBreakEvent e) {
        if (spectators.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpectatorDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (spectators.contains(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onSpectatorDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (spectators.contains(e.getDamager().getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSpectatorCommand(PlayerCommandPreprocessEvent e) {
        if (spectators.contains(e.getPlayer().getUniqueId())) {

            String msg = e.getMessage().toLowerCase();
        	if (!msg.startsWith("/sw") && !msg.startsWith("/lobby")) {
        	e.setCancelled(true);
                e.getPlayer().sendMessage("§cEspectadores não podem usar comandos.");
            }
        }
    }
    @EventHandler
    public void onSpecGUIClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player spec = (Player) e.getWhoClicked();

        if (!spectators.contains(spec.getUniqueId())) return;
        if (e.getView().getTitle().equals("§8Espectadores")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;

            String name = ChatColor.stripColor(
                    e.getCurrentItem().getItemMeta().getDisplayName());

            Player target = Bukkit.getPlayer(name);
            if (target == null) {
                spec.sendMessage("§cJogador não encontrado.");
                return;
            }

            // Guarda o alvo
            specTarget.put(spec.getUniqueId(), target.getUniqueId());

            spec.sendMessage("§aAgora assistindo §f" + target.getName());
            spec.teleport(target);
            spec.closeInventory();
        }
    }
    public void startCompassUpdater() {
        if (compassTask != -1) return;

        compassTask = Bukkit.getScheduler().runTaskTimer(
            Main.plugin,
            () -> {
                for (UUID u : specTarget.keySet()) {
                    Player spec = Bukkit.getPlayer(u);
                    Player target = Bukkit.getPlayer(specTarget.get(u));
                    if (spec != null && target != null) {
                        spec.setCompassTarget(target.getLocation());
                    }
                }
            },
            0L, 10L
        ).getTaskId();
    }
    public void stopCompassUpdater() {
        if (compassTask != -1) {
            Bukkit.getScheduler().cancelTask(compassTask);
            compassTask = -1;
        }
    }
    @EventHandler
    public void onSpecGUIDrag(org.bukkit.event.inventory.InventoryDragEvent e) {
        if (e.getView().getTitle().equals("§8Espectadores")) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage().toLowerCase();
       
        if (!isInGame(p)) return;
        if (msg.startsWith("/spec")) {
            e.setCancelled(true);

            String[] args = msg.split(" ");
            String[] realArgs = new String[args.length - 1];
            System.arraycopy(args, 1, realArgs, 0, realArgs.length);

            handleSpecCommand(p, realArgs);
            return;
        }
        if (msg.startsWith("/specgui")) {
            e.setCancelled(true);
            openSpectatorGUI(p);
            return;
        }
        if (state != GameState.RUNNING && 
        	    !msg.startsWith("/sw") && 
        	    !msg.startsWith("/lobby")) {

        	    e.setCancelled(true);
        	    p.sendMessage("§cComando bloqueado durante a partida");
        	}
        if (msg.startsWith("/lobby") && state == GameState.RUNNING) {

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
        if (state != GameState.WAITING) return;

        state = GameState.STARTING;
        countdown = 30;

        broadcast("§eA partida vai começar!");
    }
    @EventHandler
    public void onSpecTeleport(org.bukkit.event.player.PlayerTeleportEvent e) {
        if (spectators.contains(e.getPlayer().getUniqueId())) {
            if (e.getCause() == org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.SPECTATE) {
                e.setCancelled(true);
            }
        }
    }
    public void openSpectatorGUI(Player spectator) {
        if (!spectators.contains(spectator.getUniqueId())) {
            spectator.sendMessage("§cApenas espectadores podem usar este menu.");
            return;
        }

        List<Player> vivos = new ArrayList<>();
        for (UUID u : players) {
            if (!spectators.contains(u)) {
                Player p = Bukkit.getPlayer(u);
                if (p != null) vivos.add(p);
            }
        }

        if (vivos.isEmpty()) {
            spectator.sendMessage("§cNenhum jogador vivo no momento.");
            return;
        }

        int size = ((vivos.size() - 1) / 9 + 1) * 9;
        org.bukkit.inventory.Inventory inv =
                Bukkit.createInventory(null, size, "§8Espectadores");

        for (Player p : vivos) {
            org.bukkit.inventory.ItemStack head =
                    new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short) 3);

            org.bukkit.inventory.meta.SkullMeta meta =
                    (org.bukkit.inventory.meta.SkullMeta) head.getItemMeta();

            meta.setOwner(p.getName());
            String color = guiBlink ? "§a" : "§e";
            meta.setDisplayName(color + p.getName());

            List<String> lore = new ArrayList<>();
            lore.add(guiBlink ? "§a✔ Vivo" : "§e⚔ Em jogo");
            lore.add("§7Vida: §c" + Math.ceil(p.getHealth()) + " ❤");

            meta.setLore(lore);
            meta.setLore(Collections.singletonList("§7Clique para assistir"));

            head.setItemMeta(meta);
            inv.addItem(head);
        }

        spectator.openInventory(inv);
        spectator.playSound(spectator.getLocation(),
                Sound.valueOf("CLICK"), 1.0f, 1.0f);
        spectatorsWithGUI.add(spectator.getUniqueId());
    }
    @EventHandler
    public void onGUIClose(org.bukkit.event.inventory.InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("§8Espectadores")) {
            spectatorsWithGUI.remove(e.getPlayer().getUniqueId());
        }
    }
    public void startSpectatorGUITask() {
        if (spectatorGUITask != -1) return;

        spectatorGUITask = Bukkit.getScheduler().runTaskTimer(
            Main.plugin,
            () -> {
                if (spectatorsWithGUI.isEmpty()) return;

                for (UUID u : new ArrayList<>(spectatorsWithGUI)) {
                    Player spec = Bukkit.getPlayer(u);

                    if (spec == null || !spec.isOnline()) {
                        spectatorsWithGUI.remove(u);
                        continue;
                    }

                    if (!spectators.contains(u)) {
                        spec.closeInventory();
                        spectatorsWithGUI.remove(u);
                        continue;
                    }

                    updateSpectatorGUI(spec);
                }
            },
            0L, 20L
        ).getTaskId();
    }
    public void stopSpectatorGUITask() {
        if (spectatorGUITask != -1) {
            Bukkit.getScheduler().cancelTask(spectatorGUITask);
            spectatorGUITask = -1;
        }
    }
    public void updateSpectatorGUI(Player spectator) {
        if (!spectator.getOpenInventory().getTitle().equals("§8Espectadores")) {
            spectatorsWithGUI.remove(spectator.getUniqueId());
            return;
        }
        guiBlink = !guiBlink;
        org.bukkit.inventory.Inventory inv = spectator.getOpenInventory().getTopInventory();
        inv.clear();

        List<Player> vivos = new ArrayList<>();
        for (UUID u : players) {
            if (!spectators.contains(u)) {
                Player p = Bukkit.getPlayer(u);
                if (p != null) vivos.add(p);
            }
        }

        if (vivos.isEmpty()) {
        	spectator.playSound(spectator.getLocation(),
                    Sound.valueOf("VILLAGER_NO"), 1.0f, 1.0f);
            spectator.sendMessage("§cNão há mais jogadores vivos.");
            spectator.closeInventory();
            spectatorsWithGUI.remove(spectator.getUniqueId());
            return;
        }

        for (Player p : vivos) {
            org.bukkit.inventory.ItemStack head =
                    new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short) 3);

            org.bukkit.inventory.meta.SkullMeta meta =
                    (org.bukkit.inventory.meta.SkullMeta) head.getItemMeta();

            meta.setOwner(p.getName());
            meta.setDisplayName("§a" + p.getName());
            meta.setLore(Collections.singletonList(
                    "§7Vida: §c" + Math.ceil(p.getHealth()) + " ❤"
            ));
            head.setItemMeta(meta);
            inv.addItem(head);
        }

        spectator.updateInventory();
    }
    private void tickRunning() {

        // Segurança: não roda nada enquanto o mundo está reiniciando
        if (worldLoading) return;

        switch (state) {

        case WAITING:
            if (!worldLoading && players.size() >= 2 && state == GameState.WAITING) {
                startCountdown();
            }
            break;

            case STARTING:
            	if (players.size() < 2) {
            	    countdown = 30;
            	    state = GameState.WAITING;
            	    return; // NÃO muda state aqui
            	}
                tickStarting();
                break;

            case RUNNING:
                checkWin();
                break;

            case ENDING:
                stopGameTask();
                break;
            case STOPPED:
            default:
                // Não faz nada
                break;
        }
    }


    private void tickStarting() {
if (players.size() < 2) {
	 countdown = 30;
	return;
}
if (worldLoading) {
    countdown = 30;

    return;
}
        if (countdown <= 0) {
            startGame();
            return;
        }
        if (countdown % 5 == 0) {
            announceStarting();
            broadcast("§eIniciando em §6" + countdown + "s");
        }
        
        countdown--;
    }
    private void announceStarting() {
        // Pega o mundo do lobby
        World lobbyWorld = Bukkit.getWorld("spawn");
        if (lobbyWorld == null) return;

        String msg = "§eA partida da Sala §a#" + id + " §evai começar em " + countdown + " segundos! Clique aqui para entrar nela!";
TextComponent txt = new TextComponent(msg);
txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw joingame " + id));

        // Envia para todos os jogadores do lobby
        for (Player p : lobbyWorld.getPlayers()) {
            p.spigot().sendMessage(txt);
        }

        // Opcional: também pode enviar ActionBar
        for (Player p : lobbyWorld.getPlayers()) {
            HelixActionBar.send(p, msg);
            p.playSound(p.getLocation(), Sound.valueOf("ARROW_HIT"), 10f, 1f);
        }
    }
  
    public Location getSpectatorSafeLocation(Player spec) {
        // prioridade: alvo espectador
        UUID targetUUID = specTarget.get(spec.getUniqueId());
        if (targetUUID != null) {
            Player alvo = Bukkit.getPlayer(targetUUID);
            if (alvo != null && alvo.isOnline()) {
                // teleporta pra cima dele
                return alvo.getLocation().add(0, 2, 0);
            }
        }
        
        // fallback: respawn da arena
            return new Location(world, 10, 82, 10);
}
    private boolean teleportPlayersToCagesSafe() {
        World w = Bukkit.getWorld(map.getWorldName());
        if (w == null) return false;

        List<Location> cages = map.getCages(w); // 🔥 aqui
        if (cages.isEmpty()) return false;

        for (Location loc : cages) {
            loc.getChunk().load(true);
        }
        return true;
    }
    public void teleportPlayersToCages() {
        if (world == null) return;

        List<Location> cages = map.getCages(world);
        if (cages.isEmpty()) return;

        List<Player> vivos = new ArrayList<>(getPlayers());
        Collections.shuffle(vivos);

        // ⚠️ LOGA UMA ÚNICA VEZ
        if (vivos.size() > cages.size()) {
            Bukkit.getLogger().warning(
                "[SkyWars] Jogadores (" + vivos.size() +
                ") > Jaulas (" + cages.size() +
                ") no mapa " + map.getWorldName()
            );
        }

        for (int i = 0; i < vivos.size(); i++) {
            Player p = vivos.get(i);

            // 🟢 Jogadores extras viram espectador
            if (i >= cages.size()) {
                spectators.add(p.getUniqueId());
                playersInPvp.remove(p.getUniqueId());

                p.setGameMode(GameMode.SURVIVAL);
                p.setAllowFlight(true);
                p.setFlying(true);
                giveSpectatorItem(p);
                p.sendMessage("§cVocê virou espectador: não havia jaulas suficientes.");
                continue;
            }

            Location cage = cages.get(i);
            p.teleport(cage);
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    }


    private void startGame() {

	    if (state != GameState.STARTING) return;
    	  if (worldLoading) {
    	        Bukkit.getLogger().warning(
    	            "[SkyWars] Tentativa de start com mundo carregando"
    	        );
    	        return;
    	    }
    	  World w = Bukkit.getWorld(map.getWorldName());

    	    if (w == null) {
    	        Bukkit.getLogger().warning(
    	            "[SkyWars] Mundo ainda não pronto, adiando start..."
    	        );
    	        if (++startRetries > 5) {
    	            Bukkit.getLogger().severe("[SkyWars] Falha crítica ao iniciar sala " + id);
    	            return;
    	        }
    	        Bukkit.getScheduler().runTaskLater(
    	            Main.plugin,
    	            this::startGame,
    	            20L // tenta de novo em 1 segundo
    	        );
    	        return;
    	    }
    	    state = GameState.RUNNING;
    	    this.world = w;
        cagesClosed = true;
        List<UUID> ordered = new ArrayList<>(players);
        Collections.shuffle(ordered);

        // Usa Cage + Jaulas
        
List<Player> vivos = new ArrayList<>(getPlayers());
        Collections.shuffle(vivos);
        if (world == null) {
            Bukkit.getLogger().warning("[SkyWars] Mundo ainda não disponível");
            return;
        }
        if (!teleportPlayersToCagesSafe()) {
            Bukkit.getLogger().warning("[SkyWars] Start abortado: jaulas indisponíveis");
            return;
        }

        playersInPvp.clear();
        playersInPvp.addAll(players);
        teleportPlayersToCages();
        playersInPvp.removeIf(uuid -> spectators.contains(uuid));
        for (UUID u : ordered) {
            Player p = Bukkit.getPlayer(u);
            if (p != null) {
                Cage.createCage(p, Material.GLASS);
                p.playSound(p.getLocation(), Sound.valueOf("CLICK"), 10f, 10f);
            }
        }

        broadcast("§aA partida vai começar em 15 segundos!");

        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            cagesClosed = false;
            
            for (UUID u : ordered) {
            	Player u2 = Bukkit.getPlayer(u);
            	
Cage.removeCage(u2);
            	 u2.playSound(u2.getLocation(), Sound.valueOf("CLICK"), 10f, 10f);
                 
            }
            updateVisibility();

            Main.getInstance().CarregarTodos();
            broadcast("§aA partida começou!");
            
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            	damage = true;
startCompassUpdater();
startSpectatorGUITask();
            }, 20L * 19);

        }, 20L * 15);
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        if (isInGame(sender)) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            handleGameChat(sender, e.getMessage());
        });

        e.setCancelled(true);
    }
    }
    
    
    public void handleGameChat(Player sender, String message) {

        // Não está nessa partida
        if (!isInGame(sender) && !spectators.contains(sender.getUniqueId())) {
            return;
        }
 // cancelamos o chat padrão

        boolean isSpectator = spectators.contains(sender.getUniqueId());

        String prefix = isSpectator
                ? "§8[ESPECTADOR] §7"
                : "§a[JOGO] §f";

        String msg = prefix + sender.getDisplayName() + "§7: §f" + message;

        if (isSpectator) {
            // espectador fala só com espectador
            for (UUID u : spectators) {
                Player p = Bukkit.getPlayer(u);
                if (p != null) p.sendMessage(msg);
            }
        } else {
            // vivo fala só com vivos
            for (UUID u : players) {
                Player p = Bukkit.getPlayer(u);
                if (p != null && !spectators.contains(u)) {
                    p.sendMessage(msg);
                }
            }
        }
    }
    public int getAliveCount() {
        if (state != GameState.RUNNING) {
            return players.size(); // antes de começar
        }
        return (int) players.stream()
                .filter(u -> !spectators.contains(u))
                .count();
    }
    public void checkWin() {
        if (state != GameState.RUNNING) return;

        List<UUID> alive = getAliveUUIDs();

        // Ainda tem mais de um vivo → continua o jogo
        if (alive.size() > 1) return;

        state = GameState.ENDING;

        if (alive.size() == 1) {
            Player winner = Bukkit.getPlayer(alive.get(0));
            if (winner != null) {
                Bukkit.broadcastMessage("§6" + winner.getName() + " venceu a Sala #" + id);
                playVictoryAnimation(winner);
            }
        } else {
            // 0 vivos (empate, void kill, bug, etc)
            Bukkit.broadcastMessage("§cA partida terminou sem vencedor.");
        }

        showEveryoneToEveryone();

        Bukkit.getScheduler().runTaskLater(
            Main.getInstace(),
            () -> Main.getInstace().getManager().endGame(this),
            20L * 8
        );
    }


    public int getPlayerCountReal() {
    	return players.size();
    }
    public void resetAfterWorldRestart() {
        playersInPvp.clear();
        spectators.clear();
        specTarget.clear();
        spectatorsWithGUI.clear();
        players.clear();
        cagesClosed = false;
        damage = false;
        countdown = 30;
        state = GameState.WAITING;

        stopCompassUpdater();
        stopSpectatorGUITask();
        stopGameTask();
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
        TitleAPI.sendTitle(winner, 20, 120, 20, "§6§lVITÓRIA");
        victoryTask = Bukkit.getScheduler().runTaskTimer(
        	    Main.plugin,
        	    new Runnable() {
        	        int ticks = 0;

        	        @Override
        	        public void run() {
        	            if (ticks++ >= 80) {
        	                Bukkit.getScheduler().cancelTask(victoryTask);
        	                victoryTask = -1;
        	                return;
        	            }
        	            throwRandomFirework(winner);
        	            
        	            winner.getWorld().playSound(winner.getLocation(), Sound.valueOf("NOTE_PLING"), 1.0f, 1.0f);

        	        }
        	    },
        	    0L, 10L
        	).getTaskId();
    }
    
    // ================== UTILS ==================
    public void broadcast(String msg) {
        for (Player p : getPlayers()) {
        	p.sendMessage(msg); 
        	HelixActionBar.send(p, msg); 
        }
    }

    

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            if (spectators.contains(event.getPlayer().getUniqueId())) {
            if(block.getType().equals(Material.CHEST))  {
                event.setCancelled(true);
            }
        }
        }
    }
    public void resetGame() {
        cagesClosed = false;
        countdown = 30;
        state = GameState.WAITING;

        // Limpa apenas o estado da partida, não a lista de players
        playersInPvp.clear();
        spectators.clear();
        specTarget.clear();
        spectatorsWithGUI.clear();

        stopCompassUpdater();
        stopSpectatorGUITask();
        stopGameTask();
        if (victoryTask != -1) {
            Bukkit.getScheduler().cancelTask(victoryTask);
            victoryTask = -1;
        }

        // Resetar inventário e status dos jogadores
        for (UUID uuid : new ArrayList<>(players)) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) continue;

            // Limpa inventário, armadura e status
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setFlying(false);
            p.setAllowFlight(false);
            p.setGameMode(GameMode.SURVIVAL);
            p.setFireTicks(0);

            // Reaplica itens de lobby
            ItemJoinAPI ij = new ItemJoinAPI();
            ij.getItems(p);

            // Teleporta de volta para o spawn da sala
            p.teleport(Configs.MAIN_SPAWN);
        }
        showEveryoneToEveryone();
    }

    public void resetWorldAndRestart() {
        worldLoading = true;

        String worldName = map.getWorldName();
        String backupWorld = worldName + "copy";
        World oldWorld = this.world;


            // Teleporta players para o lobby, mas mantém na lista
            if (oldWorld != null) {
                for (Player p : oldWorld.getPlayers()) {
                    p.teleport(Configs.MAIN_SPAWN);
                }
            }

            abriu.clear();
            // Deleta e clona o mundo
            Main.getMVWorldManager().deleteWorld(worldName);
            Main.getMVWorldManager().cloneWorld(backupWorld, worldName, "VoidGen");

            // Espera o mundo carregar totalmente
            new BukkitRunnable() {
                int contador = 10;
                int attempts = 0;
                @Override
                public void run() {
                    World w = Bukkit.getWorld(worldName);
        

                    if (++attempts >= 15) {
                        Bukkit.getLogger().severe("[SkyWars] Mundo não carregou: " + worldName);
                        cancel();
                        worldLoading = false;
                    }
                    if (w == null) return; // Ainda não carregou

                    // Cancela o loop
                   

                    // Mundo pronto
                    w.getChunkAt(0, 0).load(true);
                    world = w;
                    spawn = Configs.LOBBY_SPAWN;
                    worldLoading = false;

                    
                    
                    List<UUID> snapshot = new ArrayList<>(players);
                    

                    for (UUID uuid : snapshot) {
                        Player p = Bukkit.getPlayer(uuid);
                        if (p != null) {
                            p.teleport(Configs.MAIN_SPAWN);
                            p.setFlying(false);
                            p.setAllowFlight(false);
                        }
                    }
                    resetAfterWorldRestart();
                    showEveryoneToEveryone();
                    cancel(); 
                
                } }.runTaskTimer(Main.getInstance(), 0L, 20L); // Checa a cada segundo
        
    }
    public List<UUID> getAliveUUIDs() {
        List<UUID> alive = new ArrayList<>();
        for (UUID u : players) {
            Player p = Bukkit.getPlayer(u);
            if (p != null && p.isOnline() && !spectators.contains(u)) {
                alive.add(u);
            }
        }
        return alive;
    }
    public boolean handleSpecCommand(Player sender, String[] args) {
        if (!spectators.contains(sender.getUniqueId())) {
            sender.sendMessage("§cApenas espectadores podem usar este comando.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§eUso correto: §f/spec <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cJogador não encontrado.");
            return true;
        }

        if (!players.contains(target.getUniqueId()) || spectators.contains(target.getUniqueId())) {
            sender.sendMessage("§cEste jogador não está vivo na partida.");
            return true;
        }

        sender.teleport(target.getLocation().add(0, 2, 0));
        sender.sendMessage("§aAgora assistindo §f" + target.getName());

        return true;
    }
    public void updateVisibility() {

        // 🔥 FORA DE PARTIDA: TODO MUNDO VÊ TODO MUNDO
        if (state != GameState.RUNNING) {
            showEveryoneToEveryone();
            return;
        }

        List<Player> vivos = new ArrayList<>();
        List<Player> specs = new ArrayList<>();

        for (UUID u : players) {
            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;

            if (spectators.contains(u)) {
                specs.add(p);
            } else {
                vivos.add(p);
            }
        }

        // Jogadores vivos veem apenas vivos
        for (Player vivo : vivos) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (vivos.contains(target)) {
                    vivo.showPlayer(target);
                } else {
                    vivo.hidePlayer(target);
                }
            }
        }

        // Espectadores veem todo mundo
        for (Player spec : specs) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                spec.showPlayer(target);
            }
        }
    }

boolean isAlive(UUID u) {
    return players.contains(u) && !spectators.contains(u);
}
}