package me.rafaelauler.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wavemc.core.bukkit.api.HelixActionBar;

public class SkyWarsGame implements Listener {

    private final int id;
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> playersInPvp = new ArrayList<>();
    private final Map<UUID, UUID> specTarget = new HashMap<>();

    private World world;
    private Location spawn;
    private SkyWarsMap map;
    private final List<UUID> spectators = new ArrayList<>();
    private GameState state = GameState.WAITING;
    private int countdown = 30;
    private boolean started = false;
    private boolean cagesClosed = false;
    private final List<UUID> spectatorsWithGUI = new ArrayList<>();
    private boolean guiBlink = false;
    private int gameTask = -1;
    private int spectatorGUITask = -1;
    private int compassTask = -1;
    private int victoryTask = -1;


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
            this::tick,
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
    	 if (getState() != GameState.WAITING && getState() != GameState.COUNTDOWN && getState() != GameState.STARTING) {
    		 player.sendMessage(ChatColor.RED + "Essa partida está em andamento. Escolha outra!");
    		 ItemJoinAPI api = new ItemJoinAPI();
    		 if (player.getInventory().getContents() == null) {
    			 api.getItems(player);
    		 }
    		 return;
    	 }
    	    players.add(player.getUniqueId());
           player.setAllowFlight(false);
           player.setFlying(false);
           player.setGameMode(GameMode.SURVIVAL);
        if (players.size() >= 2 && state == GameState.WAITING) {
            startCountdown();
            startTask();
        }
        Bukkit.getLogger().info("[SkyWars] Player entrou. Sala " + id + " | Players=" + players.size());
        player.teleport(spawn);
    }

    public void leave(Player player) {
        players.remove(player.getUniqueId());
        playersInPvp.remove(player.getUniqueId());
        specTarget.remove(player.getUniqueId());
        spectators.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);
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
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo() == null) return;

        // 🔥 Ignora movimentos irrelevantes (câmera, head rotation)
        if (e.getFrom().getBlockX() == e.getTo().getBlockX()
         && e.getFrom().getBlockY() == e.getTo().getBlockY()
         && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        boolean isPlayer = players.contains(uuid);
        boolean isSpectator = spectators.contains(uuid);

        if (!isPlayer && !isSpectator) return;

        // 🚫 Trava jogador antes do início (jaulas)
        if (isPlayer && !started && cagesClosed) {
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
            	

            Player p2 = Bukkit.getPlayer(playersInPvp.get(0));
            p.teleport(p2.getLocation().add(0, 2, 0));
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
        if (state != GameState.WAITING) return;
        state = GameState.STARTING;
        countdown = 30;
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
    public void teleportPlayersToCages() {
        List<Location> cages = map.getCages();
        List<Player> vivos = new ArrayList<>(getPlayers());
        Collections.shuffle(vivos);
        if (vivos.size() > cages.size()) {
            Bukkit.getLogger().warning("[SkyWars] Jogadores demais para o mapa " + map.name());
            return;
        }

        for (int i = 0; i < vivos.size(); i++) {
            Player p = vivos.get(i);
            Location cage = cages.get(i);

            p.teleport(cage);
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    }
    private void startGame() {
        state = GameState.RUNNING;
        cagesClosed = true;

        List<UUID> ordered = new ArrayList<>(players);
        Collections.shuffle(ordered);

        // Usa Cage + Jaulas
        

        List<Player> vivos = new ArrayList<>(getPlayers());
        Collections.shuffle(vivos);
        teleportPlayersToCages();
       for (UUID u : ordered) {
    	   Player p = Bukkit.getPlayer(u);
    	      Cage.createCage(p, Material.GLASS);
    		
        broadcast("§aA partida vai começar em 15 segundos!");
        p.playSound(p.getLocation(), Sound.valueOf("CLICK"), 10f, 10f);
       
       }
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            cagesClosed = false;
            
            for (UUID u : ordered) {
            	Player u2 = Bukkit.getPlayer(u);
            	updateVisibility();
Cage.removeCage(u2.getLocation(), Material.GLASS);
            	 playersInPvp.add(u2.getUniqueId());
            	 u2.playSound(u2.getLocation(), Sound.valueOf("CLICK"), 10f, 10f);
                 
            }

            Main.getInstance().CarregarTodos();
            broadcast("§aA partida começou!");
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
started = true;

startCompassUpdater();
startSpectatorGUITask();
            }, 20L * 18);

        }, 20L * 15);
    }
    @EventHandler
    public void onChat(org.bukkit.event.player.AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();

        // Não está nessa partida
        if (!isInGame(sender) && !spectators.contains(sender.getUniqueId())) {
            return;
        }
        if (e.getMessage().startsWith("!")) {
            e.setCancelled(false); // deixa o Bukkit tratar
            e.setMessage(e.getMessage().substring(1));
            return;
        }
        e.setCancelled(true); // cancelamos o chat padrão

        boolean isSpectator = spectators.contains(sender.getUniqueId());

        String prefix = isSpectator
                ? "§8[ESPECTADOR] §7"
                : "§a[JOGO] §f";

        String msg = prefix + sender.getName() + "§7: §f" + e.getMessage();

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

    public void checkWin() {
        if (state != GameState.RUNNING) return;
        if (playersInPvp.size() != 1) return;

        Player winner = Bukkit.getPlayer(playersInPvp.get(0));
        if (winner == null) return;

        state = GameState.ENDING;

        Bukkit.broadcastMessage("§6" + winner.getName() + " venceu a Sala #" + id);
        playVictoryAnimation(winner);

        Bukkit.getScheduler().runTaskLater(
            Main.getInstace(),
            () -> Main.getInstace().getManager().endGame(this),
            20L * 8
        );
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

    

    public void resetInternal() {
        players.clear();
        playersInPvp.clear();
        spectators.clear();
        specTarget.clear();
        spectatorsWithGUI.clear();

        started = false;
        cagesClosed = false;
        countdown = 30;
        state = GameState.WAITING;

        stopCompassUpdater();
        stopSpectatorGUITask();
        stopGameTask();

        if (victoryTask != -1) {
            Bukkit.getScheduler().cancelTask(victoryTask);
            victoryTask = -1;
        }
    }
    public void resetGame() {
        started = false;
        cagesClosed = false;
        countdown = 30;
        state = GameState.WAITING;
        specTarget.clear();
        playersInPvp.clear();
        stopGameTask();
        if (victoryTask != -1) {
            Bukkit.getScheduler().cancelTask(victoryTask);
            victoryTask = -1;
        }
        spectatorsWithGUI.clear();
        // Limpa players
        for (UUID u : new ArrayList<>(players)) {
            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        p.showPlayer(all);
                    }
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.setFlying(false);
            p.setAllowFlight(false);
            p.setFireTicks(0);
            stopCompassUpdater();
            stopSpectatorGUITask();
            stopGameTask();
            p.setGameMode(GameMode.SURVIVAL);
            for (UUID u2 : new ArrayList<>(spectators)) {
            	Player arr = Bukkit.getPlayer(u2);
           	  ItemJoinAPI ij = new ItemJoinAPI();
           	  arr.getInventory().clear();
           	  arr.getInventory().setArmorContents(null);
           	  ij.getItems(arr);
            }
            spectators.clear();  
        	  ItemJoinAPI ij = new ItemJoinAPI();
        	  p.getInventory().clear();
        	  p.getInventory().setArmorContents(null);
        	  ij.getItems(p);
        	  players.clear();
          }
        
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
        List<Player> vivos = new ArrayList<>();
        List<Player> specs = new ArrayList<>();

        for (UUID u : new ArrayList<>(players)) {
            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;

            if (spectators.contains(u)) {
                specs.add(p);
            } else {
                vivos.add(p);
            }
        }

        // vivos NÃO veem espectadores
        for (Player vivo : vivos) {
            for (Player spec : specs) {
                vivo.hidePlayer(spec);
            }
        }

        // espectadores veem todo mundo
        for (Player spec : specs) {
            for (Player vivo : vivos) {
                spec.showPlayer(vivo);
            }
            for (Player otherSpec : specs) {
                spec.showPlayer(otherSpec);
            }
        }

        // vivos veem vivos
        for (Player p1 : vivos) {
            for (Player p2 : vivos) {
                p1.showPlayer(p2);
            }
        }
    }
    public void resetWorldAndRestart() {
        World oldWorld = this.world;

        Bukkit.getScheduler().runTask(Main.plugin, () -> {

            // Descarrega o mundo atual
            Bukkit.unloadWorld(oldWorld, false);

            String worldName = map.getWorldName();
            String backupWorld = worldName + "copy";

            // Multiverse reset
            Main.getMVWorldManager().deleteWorld(worldName);
            Main.getMVWorldManager().cloneWorld(backupWorld, worldName, "VoidGen");

            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {

                this.world = Bukkit.getWorld(worldName);

                if (this.world == null) {
                    Bukkit.getLogger().severe(
                        "[SkyWars] Falha ao recarregar mundo " + worldName
                    );
                    return;
                }

                this.spawn = new Location(world, 0.5, 100, 0.5);

                stopCompassUpdater();
                resetGame();
                startTask();

            }, 40L);
        });
    }

}

