package me.rafaelauler.sw;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.onarandombox.MultiverseCore.MultiverseCore;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wavemc.core.bukkit.api.HelixActionBar;
import net.wavemc.core.util.UpdateEvent;

public class Automatic implements Listener {
  private Main main;
  
  private int time;
  
  private GameType gameType;
  
  private Listener listener;
  
  public static List<Player> players = new ArrayList<Player>();;
  
  private int maxPlayers;
  public static boolean iniciou;
  public static boolean star = false;
  private boolean full;
  
  private boolean pvp;
  
  private List<Player> playersInPvp = new ArrayList<Player>();
  public HashMap<Player, Integer> kills = new HashMap<Player, Integer>();
  
  private List<Player> specs;
  public static final List<String> playersIN = new ArrayList<>();
  public Automatic() {
    this.main = Main.getInstance();
    time = 32;
    this.gameType = GameType.STARTING;
    this.maxPlayers = 60;
    this.full = false;
    this.pvp = false;
    this.specs = new ArrayList<>();
    playersInPvp = new ArrayList<>();
  }
  @EventHandler
  public void onUpdate2(UpdateEvent e) {
    if (e.getType() != UpdateEvent.UpdateType.SEGUNDO) {
      return; 
    }
    if (players == null || players.size() == 0 && star) {
    	destroy();
    }
    if (this.gameType == GameType.STOPPED && players.size() >= 1) {
    	this.gameType = GameType.STARTING;
    }
    if (players.size() == 1 && star) {
    	 queuedPlayers();
    }
       for (Player p : Bukkit.getOnlinePlayers()) {
    	   if (!players.contains(p)) {
    		   players.forEach(p1 -> p1.hidePlayer(p));
    		   new BukkitRunnable() {
				    public void run() {
				    	for (Player b : Bukkit.getOnlinePlayers()) {
				    		if (!b.canSee(p)) {
				    			if (!b.getWorld().equals(Bukkit.getWorld("sw1"))) {
				    			b.showPlayer(p);
				    		}
				    		}
				    	}
				    } }.runTaskLater(Main.plugin, 200l);
    		   }}
       
    }
  public static MultiverseCore getMVWorldManager() {
      return JavaPlugin.getPlugin(MultiverseCore.class);
  }
          @EventHandler
          public void onUpdate(UpdateEvent e) {
            if (e.getType() != UpdateEvent.UpdateType.SEGUNDO) {
              return; 
            }
            if (this.gameType == GameType.STOPPED) {
            	return;
            }
            new BukkitRunnable() {
			    public void run() {
                  Main.getInstace().CarregarBaus();
                  for (Player p : players) {
                	  TitleAPI.sendTitle(p, 40, 70, 40, ChatColor.GREEN + "Os báus foram reabastecidos!");
                  }
			    }}.runTaskLater(Main.plugin, 20 * 60 * 10l);
			    
            for (Player w : Bukkit.getWorld("sw1").getPlayers()) {
            if (!players.contains(w)) {
            if (MainCommand.game.contains(w.getName())) {	
            
            	players.add(w);
            }
            }
            }
            if (players.size() >= 2 && !iniciou) {
            	iniciou = true;
            }
            
            
            if (players.size() == 1 && !iniciou) {
            	iniciou = false;
            	time = 34;
            	for (Player p2 : players) {
        		HelixActionBar.send(p2, ChatColor.YELLOW + "Aguardando mais 1 jogador...");
            	}
            	return;
            }
            else if (!iniciou) {
            	time = 30;
            	return;
            }
              if (MainCommand.game.isEmpty() && iniciou) {
            	  destroy();
              }

for (Player p2 : players) {

if (!playersInPvp.contains(p2) && !star) {
	HelixActionBar.send(p2,  Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", String.valueOf(time)));
	}
}
if (time == 34 && !star) {
	for (Player p2 : players) {

		HelixActionBar.send(p2, ChatColor.YELLOW + "Aguardando mais 1 jogador...");
}
}
              if (time == 30 && !star && iniciou) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "30"));
            	  TextComponent textComponent4 = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "30"));
                  textComponent4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw1 join"));
                  broadcast2(textComponent4, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
                  
              }

              for (Player p : new ArrayList<>(players)) {
            	  if (p.getWorld() != Bukkit.getServer().getWorld("swlobby") && p.getWorld() != Bukkit.getServer().getWorld("sw1")) {

            		  Bukkit.getConsoleSender().sendMessage("QUITTING " + p.getName() + " FROM SKYWARS! Mundo: " + p.getWorld().getName());
            		  p.performCommand("sw leave");
            	  }
              }
              if (time == 15 && !star) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "15"));
            	  TextComponent textComponent = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "15"));
                  textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw1 join"));
                  broadcast2(textComponent, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
                  
                  }
              if (time == 10 && !star) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "10"));
            	  TextComponent textComponent2 = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "10"));
                  textComponent2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw1 join"));
                  broadcast2(textComponent2, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
              
                 
              }
              if (time == 5 && !star) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "5"));
            	  TextComponent textComponent3 = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "5"));
                  textComponent3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw1 join"));
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
              if (time > 0  && players.size() > 1) {
                  time = time - 1;
                 	  }
                  else if (players.size() == 1) {
                 	 time = 32;
                  }
             if (!pvp && star) {
             queuedPlayers();
            
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
            if (MainCommand.game.contains(e.getPlayer().getName())) {

            	  e.getPlayer().chat("/sw leave");
            }
            if (players.contains(e.getPlayer())) {
              players.remove(e.getPlayer());
          	  broadcast(Main.getInstance().getConfig().getString("PlayerLeaveServer").replaceAll("&", "§").replace("%player%", e.getPlayer().getName()));
        	  
            }
              if (Automatic.this.getGameType() == Automatic.GameType.GAMIMG && playersInPvp.contains(e.getPlayer())) {
              	  broadcast(Main.getInstance().getConfig().getString("PlayerLeaveServerDeath").replaceAll("&", "§").replace("%player%", e.getPlayer().getName())); 
                  e.getPlayer().damage(9999.0D);
                  playersInPvp.remove(e.getPlayer());
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

            if (players.contains(e.getEntity())) {
            	if (!iniciou) {
            		
            	}
if (e.getEntity().getKiller() == null) {
                Player p1 = e.getEntity();
              playersInPvp.remove(p1);
              players.remove(p1);
              p1.spigot().respawn();
              playersInPvp.remove(p1);
              players.remove(p1);
              
              kills.put(p1, 1);
              p1.spigot().respawn();
              e.getDrops().clear();
              p1.sendMessage(Main.getInstance().getConfig().getString("PlayerKilledMessage").replaceAll("&", "§").replace("%player%", p1.getName()));
              Automatic.this.broadcast(Main.getInstance().getConfig().getString("PlayersLeft").replaceAll("&", "§").replace("%left%", String.valueOf(players.size()))); 	  
              p1.chat("/sw leave");
            }
            }
            if (e.getEntity().getKiller() == null)
              return; 
            Player p = e.getEntity();
            Player d = e.getEntity().getKiller();
            
            if ((players.contains(d) || players.contains(p)) && 
              playersInPvp.contains(d) && playersInPvp.contains(p) && (MainCommand.game.contains(d.getName()) && MainCommand.game.contains(p.getName()))) {
            	if (!iniciou) {
            		return;
            	}
            	  int currentKills = Main.getInstace().getConfig().getInt("players." + d.getUniqueId() + ".kills", 0);
                  Main.getInstance().getConfig().set("players." + d.getUniqueId() + ".kills", currentKills + 1);
                  Main.getInstace().saveConfig();

            	  int currentDeaths = Main.getInstace().getConfig().getInt("players." + p.getUniqueId() + ".deaths", 0);
                  Main.getInstance().getConfig().set("players." + p.getUniqueId() + ".deaths", currentDeaths + 1);
                  Main.getInstace().saveConfig();
              playersInPvp.remove(p);
              players.remove(p);
              p.spigot().respawn();
              e.getDrops().clear();

              p.chat("/sw leave");
              p.sendMessage(Main.getInstance().getConfig().getString("PlayerKilledMessage").replaceAll("&", "§").replace("%player%", p.getName()));
              Automatic.this.broadcast(Main.getInstance().getConfig().getString("PlayerKilledBroadcast").replaceAll("&", "§").replace("%player%", p.getName()).replace("%killer%", d.getName()));
              Automatic.this.broadcast(Main.getInstance().getConfig().getString("PlayersLeft").replaceAll("&", "§").replace("%left%", String.valueOf(players.size())));
        	  
        	  if (players.size() > 1) {
org.bukkit.World w = Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.quit.world"));
/*  98 */     p.teleport(new Location(w, Main.cfg_x1.getDouble("x1.coords.quit.x"), 
/*  99 */       Main.cfg_x1.getDouble("x1.coords.quit.y"), Main.cfg_x1.getDouble("x1.coords.quit.z")));
			 	 p.getInventory().clear();
			 	 p.getInventory().setArmorContents(null);
			 	   Bukkit.getConsoleSender().sendMessage(d.getName() + " killed " + p.getName() + " in the event 1v1");
			 	  Automatic.this.broadcast(Main.getInstance().getConfig().getString("Searching").replaceAll("&", "§"));
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
            if (!Automatic.this.isSpec((Player)e.getDamager()))
              return; 
           
            
            e.setCancelled(true);
          }
          
          @EventHandler
          public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
            Player p = e.getPlayer();
            if (!Automatic.this.isInEvent(p))
              return; 
            if (e.getMessage().toLowerCase().startsWith("/") && Automatic.this.isInPvP(p) && !e.getMessage().toLowerCase().contains("/lobby") && !e.getMessage().toLowerCase().contains("/sw") && !p.hasPermission("skywars.bypass") && iniciou) {
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

    pvp = true;
    if (players == null) {
    	Bukkit.broadcastMessage(ChatColor.DARK_RED + "A partida SW1 foi finalizada!");
    	
    	destroy();
    	return;
    }
    if (players.size() == 0) {
    	Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "A partida SW1 foi finalizada!");    	
    	destroy();
    	return;
    }
    
    final Player firstPlayer = players.get(0);
    for (Player players12 : new ArrayList<>(players)) {
    	if (players.size() > 1) {
    	playersInPvp.add(players12);
    	players12.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));

  	  players12.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
    	players12.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
    	players12.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
players12.teleport(Jaulas.getRandomLocation());
        Bukkit.getConsoleSender().sendMessage("[EVENT] Players in SKYWARS ROOM #1: " + players12.getName());
      if (!MainCommand.game.contains(players12.getName())) {

    	    players.remove(players12);
    	    players12.chat("/sw leave");
    	    players12.sendMessage("Ocorreu um erro com sua conexão ao skywars");
      }
    }
    }
    
      
  
  
    	if (!players.isEmpty() && players.size() > 1) {
    	    players.forEach(p-> p.sendMessage(ChatColor.GREEN + Main.getInstace().getConfig().getString("MatchStart")));
    	}
    	
    	  
    		  
    		  if (players.size() == 1 && star) {
                    
      			    TitleAPI.sendTitle(firstPlayer, 50, 50, 50, "§6§lVITÓRIA!");

                	  int currentDeaths = Main.getInstace().getConfig().getInt("players." + firstPlayer.getUniqueId() + ".wins", 0);
                      Main.getInstance().getConfig().set("players." + firstPlayer.getUniqueId() + ".wins", currentDeaths + 1);
                      Main.getInstace().saveConfig();
      			  for (String ko : MainCommand.game) {
      				Player k = Bukkit.getPlayer(ko);
      				if (k != null) {
      					if (k != firstPlayer) {
      				k.chat("/sw leave");
      			}
      			}
      			  for (Player oo : Bukkit.getOnlinePlayers()) {
    			    	oo.playSound(oo.getLocation(), Sound.valueOf("NOTE_PLING"), 10f, 10f);
    			    }
    			  new BukkitRunnable() {
    				  
    				    public void run() {

  			  			  firstPlayer.chat("/sw leave");
    				    	new BukkitRunnable() {
    	    				    public void run() {
    			  			  ItemJoinAPI ij = new ItemJoinAPI();
                                ij.getItems(firstPlayer);
                                
                  		    	destroy();
                                firstPlayer.sendMessage("Parabens por vencer a partida! :)");
    	    		  		    }}.runTaskLater(Main.plugin, 180l);
    			  			
  		    }}.runTaskLater(Main.plugin, 100l);
    		  }
    	  }
		    }}.runTaskLater(Main.plugin, 40l);
      
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
      setGameType(GameType.STOPPED);
      iniciou = false;
      star = false;
      
      for (String s : new ArrayList<>(MainCommand.game)) {
    	  Player p = Bukkit.getPlayer(s);
    	  p.sendMessage(ChatColor.RED + "A partida foi finalizada!");
    	  Bukkit.dispatchCommand(p, "sw leave");
    	  org.bukkit.World w = Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.quit.world"));
    	  /*  98 */     p.teleport(new Location(w, Main.cfg_x1.getDouble("x1.coords.quit.x"), 
    	  /*  99 */       Main.cfg_x1.getDouble("x1.coords.quit.y"), Main.cfg_x1.getDouble("x1.coords.quit.z")));
      }
      if (Bukkit.getWorld("sw1") != null) {
      for (Player p : Bukkit.getWorld("sw1").getPlayers()) {
    	  p.sendMessage(ChatColor.RED + "A partida foi finalizada!");
    	  Bukkit.dispatchCommand(p, "sw leave");
    	  org.bukkit.World w = Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.quit.world"));
    	  /*  98 */     p.teleport(new Location(w, Main.cfg_x1.getDouble("x1.coords.quit.x"), 
    	  /*  99 */       Main.cfg_x1.getDouble("x1.coords.quit.y"), Main.cfg_x1.getDouble("x1.coords.quit.z")));
      }
      }
      players.clear();
      time = 32;
      pvp = false;
      playersInPvp.clear();
      getPlayers().clear();
    HandlerList.unregisterAll(this.listener);
    
   Main.getInstance().getEventManager().setRdmAutomatic(null);

	
	new BukkitRunnable() {
	    public void run() {
	    	getMVWorldManager().deleteWorld("sw1");
	    	Automatic.getMVWorldManager().cloneWorld("sw1copy", "sw1", "VoidGen");
	    	Main main2 = new Main();
			main2.CarregarBaus();
	    }}.runTaskLater(Main.plugin, 100l);
  }
  public static boolean deleteWorld(File path) {
      if(path.exists()) {
          File files[] = path.listFiles();
          for(int i=0; i<files.length; i++) {
              if(files[i].isDirectory()) {
                  deleteWorld(files[i]);
              } else {
                  files[i].delete();
              }
          }
      }
      return(path.delete());
}
  public static void copyWorld(World originalWorld, String newWorldName) {
      copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
      new WorldCreator(newWorldName).createWorld();
}
  private static void copyFileStructure(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                    if (!target.mkdirs())
	                        throw new IOException("Couldn't create world directory!");
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyFileStructure(srcFile, destFile);
	                }
	            } else {
	                FileInputStream in = new FileInputStream(source);
	                FileOutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
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