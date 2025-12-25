package me.rafaelauler.sw;




import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wavemc.core.bukkit.api.HelixActionBar;
import net.wavemc.core.util.UpdateEvent;

public class Automatic3 implements Listener {
  private Main main;
  
  private int time;
  
  private GameType gameType;
  
  private Listener listener;
  
  public static List<Player> players;
  
  private int maxPlayers;
  public static boolean iniciou;
  public static boolean star = false;
  private boolean full;
  
  private boolean pvp;
  
  private List<Player> playersInPvp;
  
  private List<Player> specs;
  public static final List<String> playersIN = new ArrayList<>();
  public Automatic3() {
    this.main = Main.getInstance();
    time = 32;
    players = new ArrayList<>();
    this.gameType = GameType.STARTING;
    this.maxPlayers = 60;
    this.full = false;
    this.pvp = false;
    this.specs = new ArrayList<>();
    playersInPvp = new ArrayList<>();
  }
          @EventHandler
          public void onUpdate(UpdateEvent e) {
            if (e.getType() != UpdateEvent.UpdateType.SEGUNDO) {
              return; 
            }
            
            if (players.size() >= 2 && !iniciou) {
            	iniciou = true;
            }
            else if (!iniciou) {
            	time = 30;
            	return;
            }
              if (MainCommand.game.isEmpty() && iniciou) {
            	  destroy();
              }

for (Player p2 : players) {

if (!playersInPvp.contains(p2)) {
	HelixActionBar.send(p2,  Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", String.valueOf(time)));
	}
}
              if (time == 30 && !star) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "30"));
            	  TextComponent textComponent4 = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "30"));
                  textComponent4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw3 join"));
                  broadcast2(textComponent4, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
                  
              }
              for (Player p : new ArrayList<>(players)) {
            	  if (p.getWorld() != Bukkit.getServer().getWorld("swlobby") && p.getWorld() != Bukkit.getServer().getWorld("sw3")) {
            		  p.performCommand("sw leave");
            	  }
              }
              if (time == 15 && !star) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "15"));
            	  TextComponent textComponent = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "15"));
                  textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw3 join"));
                  broadcast2(textComponent, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
                  
                  }
              if (time == 10 && !star) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "10"));
            	  TextComponent textComponent2 = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "10"));
                  textComponent2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw3 join"));
                  broadcast2(textComponent2, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
              
                 
              }
              if (time == 5 && !star) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "5"));
            	  TextComponent textComponent3 = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "5"));
                  textComponent3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw3 join"));
                 broadcast2(textComponent3, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
              
              }
              if (players.size() == 9 && time >= 20 && !this.full && !star) {
                time = 30;

          	  broadcast(Main.getInstance().getConfig().getString("TournamentTimeChanged").replaceAll("&", "§"));
                this.full = true;
              } 
              if (time <= 0 && !star) {
                this.gameType = GameType.GAMIMG;
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStarted").replaceAll("&", "§"));
               star = true;
               pvp = true;
               queuedPlayers();
               time = 32;
              } 
              if (!star) {
            	  if (time > 0) {
             time = time - 1;
              }
             if (!pvp && star) {
             queuedPlayers();
             }
            } 
          }
          public void putInEvent2(Player player) {
        	  if (players.contains(player)) {
        		  return;
        	  }
        	    players.add(player);
        	}
          public void putInEvent(Player player) {
        	  if (players.contains(player)) {
        		  return;
        	  }
        	    players.add(player);
        	    player.getInventory().clear();

          	  broadcast(Main.getInstance().getConfig().getString("PlayerJoinedMatch").replaceAll("&", "§").replace("%player%", player.getName()));
        	    player.getInventory().setArmorContents(null);
        	    for (PotionEffect pot : player.getActivePotionEffects())
        	      player.removePotionEffect(pot.getType()); 
        	  }
          
          
          @EventHandler
          public void onPlayerQuit(PlayerQuitEvent e) {
            if (players.contains(e.getPlayer())) {
              players.remove(e.getPlayer());
          	  broadcast(Main.getInstance().getConfig().getString("PlayerLeaveServer").replaceAll("&", "§").replace("%player%", e.getPlayer().getName()));
        	  
          	  e.getPlayer().chat("/sw leave");
            }
              if (Automatic3.this.getGameType() == Automatic3.GameType.GAMIMG && playersInPvp.contains(e.getPlayer())) {
              	  broadcast(Main.getInstance().getConfig().getString("PlayerLeaveServerDeath").replaceAll("&", "§").replace("%player%", e.getPlayer().getName())); 
                  e.getPlayer().damage(9999.0D);
                  playersInPvp.remove(e.getPlayer());
                  pvp = false;
        	  Bukkit.dispatchCommand(e.getPlayer(), "sw leave");
              queuedPlayers();
            } 
            }
          
          public static void broadcast2(TextComponent text, World w){
        	    for(Player p: w.getPlayers()){
        	        p.spigot().sendMessage(text);
        	    }
        	    }
          @EventHandler
          public void onPlayerDeath(PlayerDeathEvent e) {
            if (!(e.getEntity() instanceof Player))
              return; 
            if (e.getEntity().getKiller() == null)
              return; 
            Player p = e.getEntity();
            Player d = e.getEntity().getKiller();
            if ((players.contains(d) || players.contains(p)) && 
              playersInPvp.contains(d) && playersInPvp.contains(p) && (MainCommand.game.contains(d.getName()) && MainCommand.game.contains(p.getName()))) {
            	if (!iniciou) {
            		return;
            	}
              playersInPvp.remove(p);
              players.remove(p);
              p.spigot().respawn();
              e.getDrops().clear();

              p.chat("/pvr leave");
              pvp = false;
              p.sendMessage(Main.getInstance().getConfig().getString("PlayerKilledMessage").replaceAll("&", "§").replace("%player%", p.getName()));
              Automatic3.this.broadcast(Main.getInstance().getConfig().getString("PlayerKilledBroadcast").replaceAll("&", "§").replace("%player%", p.getName()).replace("%killer%", d.getName()));
              Automatic3.this.broadcast(Main.getInstance().getConfig().getString("PlayersLeft").replaceAll("&", "§").replace("%left%", String.valueOf(players.size())));
        	  
        	  if (players.size() > 1) {
org.bukkit.World w = Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.quit.world"));
/*  98 */     p.teleport(new Location(w, Main.cfg_x1.getDouble("x1.coords.quit.x"), 
/*  99 */       Main.cfg_x1.getDouble("x1.coords.quit.y"), Main.cfg_x1.getDouble("x1.coords.quit.z")));
			 	 p.getInventory().clear();
			 	 p.getInventory().setArmorContents(null);
			 	   Bukkit.getConsoleSender().sendMessage(d.getName() + " killed " + p.getName() + " in the event 1v1");
			 	  Automatic3.this.broadcast(Main.getInstance().getConfig().getString("Searching").replaceAll("&", "§"));
		              }
        	  org.bukkit.World w = Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.quit.world"));
        	  /*  98 */     p.teleport(new Location(w, Main.cfg_x1.getDouble("x1.coords.quit.x"), 
        	  /*  99 */       Main.cfg_x1.getDouble("x1.coords.quit.y"), Main.cfg_x1.getDouble("x1.coords.quit.z")));
        	  p.getInventory().setArmorContents(null);
        	  ItemJoinAPI itemAPI = new ItemJoinAPI();
        	  new BukkitRunnable() {
        	                  
        	                  public void run() {

        	               	   if (Bukkit.getPluginManager().getPlugin("ItemJoin") != null) {
        	               	   p.getInventory().clear();
        	                   itemAPI.getItems(p);
        	               	   }}}.runTaskLater(Main.plugin, 25l);
            }
            queuedPlayers();
            } 
          
          
          @EventHandler(priority = EventPriority.MONITOR)
          public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        	  
            if (!(e.getDamager() instanceof Player))
              return;
            if (!(e.getEntity() instanceof Player)) {
            	return;
            }
            if (!iniciou) {
            	return;
            }
            Player p = (Player)e.getEntity();
            if (!star && MainCommand.game.contains(p.getName())) {
                e.setCancelled(true);
              }
            if (!playersInPvp.contains(p) && MainCommand.game.contains(p.getName())) {
                e.setCancelled(true);
              }
            if (!Automatic3.this.isSpec((Player)e.getDamager()))
              return; 
           
            
            e.setCancelled(true);
          }
          
          @EventHandler
          public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
            Player p = e.getPlayer();
            if (!Automatic3.this.isInEvent(p))
              return; 
            if (e.getMessage().toLowerCase().startsWith("/") && Automatic3.this.isInPvP(p) && !e.getMessage().toLowerCase().contains("/lobby") && !e.getMessage().toLowerCase().contains("/sw") && !p.hasPermission("skywars.bypass") && iniciou) {
              e.setCancelled(true);
		 	  p.sendMessage(Main.getInstance().getConfig().getString("CommandBlocked").replaceAll("&", "§"));
              return;
            } 
          }
  
  
  public boolean isInEvent(Player player) {
    return getPlayers().contains(player);
  }
  public void removeFromEvent(Player player) {
	  if (isInEvent(player)) {
	    getPlayers().remove(player);

      	 Bukkit.getConsoleSender().sendMessage("[DEBUG] " + player.getName() + " left the event!");
	    Bukkit.getConsoleSender().sendMessage(player + " got removed from event!");
	  }
  }

  
  public void queuedPlayers() {
	  new BukkitRunnable() {
		    public void run() {
    final Player firstPlayer = players.get(0);
    
    pvp = true;
    for (Player players12 : players) {

        Bukkit.getConsoleSender().sendMessage("[EVENT] Players in SKYWARS ROOM #1: " + players12.getName());
      if (!MainCommand.game.contains(players12.getName())) {

    	    players.remove(players12);
      }
    }
    
      
  
  
    	if (!players.isEmpty()) {
players.forEach(p -> p.teleport(Jaulas.getLocations2()));
    		playersInPvp.clear();
    	    players.forEach(p-> p.sendMessage(ChatColor.GREEN + Main.getInstace().getConfig().getString("MatchStart")));

    	}
    	else {
    		
    	     destroy(); 	
    	}
    {
    	  {
    		  
    		  if (players.size() == 1 && iniciou) {
                    
      			    TitleAPI.sendTitle(firstPlayer, 50, 50, 50, "§6§lVITÓRIA!");
      			    for (Player oo : Bukkit.getOnlinePlayers()) {
      			    	oo.playSound(oo.getLocation(), Sound.valueOf("NOTE_PLING"), 10f, 10f);
      			    }
    			  new BukkitRunnable() {
    				    public void run() {

  		    	destroy();
  			  firstPlayer.chat("/sw leave");
  			Bukkit.broadcastMessage(Main.getInstance().getConfig().getString("EventWinner").replaceAll("&", "§").replace("%player%", firstPlayer.getName()));
    		Bukkit.getServer().unloadWorld("sw1", false);
    		Bukkit.getWorld("sw1").getWorldFolder().delete();
    		Bukkit.getServer().createWorld(new WorldCreator(Main.getInstance().getDataFolder().getPath() + "\\Maps\\sw1"));
    				  	   }}.runTaskLater(Main.plugin, 100l);
      	
    			  return;
    		  }
    	  }
  	   }}}.runTaskLater(Main.plugin, 40l);
      
    	}
  
  public void broadcast(String message) {
    for (Player players2 : players) {
      players2.sendMessage(String.valueOf(Main.getInstance().getConfig().getString("Prefix").replaceAll("&", "§")) + message);

     TitleAPI.sendTitle(players2, 40, 40, 40, Main.getInstance().getConfig().getString("Prefix").replaceAll("&", "§"), message);
    }
    for (Player players2 : this.specs) {
      players2.sendMessage(String.valueOf((Main.getInstance().getConfig().getString("Prefix").replaceAll("&", "§")) + message));
    TitleAPI.sendTitle(players2, 40, 40, 40, Main.getInstance().getConfig().getString("Prefix").replaceAll("&", "§"), message);
  }
  }
  
  
  public void setGameType(GameType newtr) {
	  this.gameType = newtr;
  }
  public GameType getGameType() {
	    return this.gameType;
	  }
  public List<Player> getPlayers() {
    return players;
  }
  
  public List<Player> getPlayersInPvp() {
    return playersInPvp;
  }
  
  public int getMaxPlayers() {
    return this.maxPlayers;
  }
  
  
  
  public boolean isInPvP(Player player) {
    return (playersInPvp.contains(player) && getGameType() == GameType.GAMIMG);
  }
  
  public void destroy() {
      setGameType(GameType.STARTING);
      iniciou = false;
      star = false;
      for (String s : new ArrayList<>(MainCommand.game)) {
    	  Player p = Bukkit.getPlayer(s);
    	  Bukkit.dispatchCommand(p, "sw leave");
    	  org.bukkit.World w = Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.quit.world"));
    	  /*  98 */     p.teleport(new Location(w, Main.cfg_x1.getDouble("x1.coords.quit.x"), 
    	  /*  99 */       Main.cfg_x1.getDouble("x1.coords.quit.y"), Main.cfg_x1.getDouble("x1.coords.quit.z")));
      }
      players.clear();
      time = 32;
      pvp = false;
      playersInPvp.clear();
      getPlayers().clear();
		Bukkit.getServer().unloadWorld("sw3", false);
		Bukkit.getWorld("sw3").getWorldFolder().delete();
		Bukkit.getServer().createWorld(new WorldCreator(Main.getInstance().getDataFolder().getPath() + "\\Maps\\sw3"));
    HandlerList.unregisterAll(this.listener);
   Main.getInstance().getEventManager3().setRdmAutomatic(null);
  }

  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  public void desmakeVanish(Player p) {
	    if (p == null) {
	      return; 
	    }
	    for (Player player : Bukkit.getOnlinePlayers()) {
	      if (!player.getName().equals(p.getName()))
	        player.showPlayer(p); 
	    } 
  }
  
  public List<Player> getSpecs() {
    return this.specs;
  }
  
  public boolean isSpec(Player p) {
    return this.specs.contains(p);
  }
  
  
  public enum GameType {
    STARTING, GAMIMG , STOPPED;
  }
}
