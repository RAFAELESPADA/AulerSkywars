package me.rafaelauler.sw;




import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import org.bukkit.inventory.ItemStack;
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
  
  public static List<Player> players = new ArrayList<Player>();;
  
  private int maxPlayers;
  public static boolean iniciou;
  public static boolean star = false;
  private boolean full;
  private boolean rodou = false;
  private boolean started = false;;
  private boolean pvp;

  private boolean run = false;
  private List<Player> playersInPvp = new ArrayList<Player>();;
  
  private List<Player> specs;
  public static final List<String> playersIN = new ArrayList<>();
  public Automatic3() {
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
    if (players == null || players.size() == 0  && star) {
    	destroy();
    }
    for (Player w : Bukkit.getWorld("sw3").getPlayers()) {
        if (!players.contains(w)) {
        if (MainCommand.game.contains(w.getName())) {	
        
        	players.add(w);
        }
        }
        }
    if (this.gameType == GameType.STOPPED && players.size() >= 1) {
    	this.gameType = GameType.STARTING;
    }
    for (Player p : Bukkit.getOnlinePlayers()) {
 	   if (!players.contains(p)) {
 		   players.forEach(p1 -> p1.hidePlayer(p));
 		   new BukkitRunnable() {
				    public void run() {
				    	for (Player b : Bukkit.getOnlinePlayers()) {
				    		if (!b.canSee(p)) {
				    			if (!b.getWorld().equals(Bukkit.getWorld("sw3"))) {
				    			b.showPlayer(p);
				    		}
				    		}
				    	}
				    } }.runTaskLater(Main.plugin, 200l);
 		   }}
    }
  
          @EventHandler
          public void onUpdate(UpdateEvent e) {
            if (e.getType() != UpdateEvent.UpdateType.SEGUNDO) {
              return; 
            }
            if (this.gameType == GameType.STOPPED) {
            	return;
            }
            if (players.size() >= 2 && !iniciou) {
            	iniciou = true;
            }

            VerificarWin();
            if (players.size() == 1 && !iniciou) {
            	iniciou = false;
            	time = 30;
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
              if (time == 30 && !star  && iniciou) {
            	  broadcast(Main.getInstance().getConfig().getString("TournamentStart").replaceAll("&", "§").replace("%time%", "30"));
            	  TextComponent textComponent4 = new TextComponent(Main.getInstance().getConfig().getString("TournamentStartGlobal").replaceAll("&", "§").replace("%time%", "30"));
                  textComponent4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getInstance().getConfig().getString("ClickToJoin").replaceAll("&", "§")).create()));
                  textComponent4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sw3 join"));
                  broadcast2(textComponent4, Bukkit.getWorld(Main.cfg_x1.getString("x1.coords.quit.world")));
                  
              }
              for (Player p : new ArrayList<>(players)) {
            	  if (p.getWorld() != Bukkit.getServer().getWorld("swlobby") && p.getWorld() != Bukkit.getServer().getWorld("sw3")) {

            		  Bukkit.getConsoleSender().sendMessage("QUITTING " + p.getName() + " FROM SKYWARS! Mundo: " + p.getWorld().getName());
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
               
               queuedPlayers();
               time = 32;
              } 
              if (!star) {
            	  if (time > 0  && players.size() > 1) {
                      time = time - 1;
                     	  }
                      else if (players.size() == 1) {
                     	 time = 32;
                      }
             if (!started && star && !run) {
             queuedPlayers();
             run = true;
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
            if (MainCommand.game.contains(e.getPlayer().getName())) {

            	  e.getPlayer().chat("/sw leave");
            }
            if (players.contains(e.getPlayer())) {
              players.remove(e.getPlayer());
          	  broadcast(Main.getInstance().getConfig().getString("PlayerLeaveServer").replaceAll("&", "§").replace("%player%", e.getPlayer().getName()));
        	  
            }
              if (Automatic3.this.getGameType() == Automatic3.GameType.GAMIMG && playersInPvp.contains(e.getPlayer())) {
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
              p1.spigot().respawn();
      
              p1.sendMessage(Main.getInstance().getConfig().getString("PlayerKilledMessage").replaceAll("&", "§").replace("%player%", p1.getName()));
              Automatic3.this.broadcast(Main.getInstance().getConfig().getString("PlayersLeft").replaceAll("&", "§").replace("%left%", String.valueOf(players.size()))); 	  
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
              playersInPvp.remove(p);
              players.remove(p);
              p.spigot().respawn();

        	  int currentKills = Main.getInstace().getConfig().getInt("players." + d.getUniqueId() + ".kills", 0);
              Main.getInstance().getConfig().set("players." + d.getUniqueId() + ".kills", currentKills + 1);
              Main.getInstace().saveConfig();
        	  int currentDeaths = Main.getInstace().getConfig().getInt("players." + p.getUniqueId() + ".deaths", 0);
              Main.getInstance().getConfig().set("players." + p.getUniqueId() + ".deaths", currentDeaths + 1);
              Main.getInstace().saveConfig();
              p.chat("/sw leave");
              p.sendMessage(Main.getInstance().getConfig().getString("PlayerKilledMessage").replaceAll("&", "§").replace("%player%", p.getName()));
              Automatic3.this.broadcast(Main.getInstance().getConfig().getString("PlayerKilledBroadcast").replaceAll("&", "§").replace("%player%", p.getName()).replace("%killer%", d.getName()));
              Automatic3.this.broadcast(Main.getInstance().getConfig().getString("PlayersLeft").replaceAll("&", "§").replace("%left%", String.valueOf(players.size())));  
org.bukkit.World w = Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.quit.world"));
/*  98 */     p.teleport(new Location(w, Main.cfg_x1.getDouble("x1.coords.quit.x"), 
/*  99 */       Main.cfg_x1.getDouble("x1.coords.quit.y"), Main.cfg_x1.getDouble("x1.coords.quit.z")));
			 	 p.getInventory().clear();
			 	 p.getInventory().setArmorContents(null);
			 	   Bukkit.getConsoleSender().sendMessage(d.getName() + " killed " + p.getName() + " in the skywars match");
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
            VerificarWin();
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
          public void onPlayerCommandgPreProcess(PlayerCommandPreprocessEvent e) {
            Player p = e.getPlayer();
            if (e.getMessage().toLowerCase().startsWith("/") && (e.getMessage().toLowerCase().contains("/lobby") || e.getMessage().toLowerCase().contains("/sw leave")) && star && players.contains(p)) {
              players.remove(p);
              queuedPlayers();

              broadcast2(new TextComponent(ChatColor.RED + p.getName() + " desistiu da partida"), Bukkit.getWorld("sw3"));
          }
          }

          @EventHandler
          public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
            Player p = e.getPlayer();
            if (!Automatic3.this.isInEvent(p))
              return; 
            if (e.getMessage().toLowerCase().startsWith("/") && !e.getMessage().toLowerCase().contains("/lobby") && !e.getMessage().toLowerCase().contains("/sw") && !p.hasPermission("skywars.bypass") && !star && players.contains(p)) {
                e.setCancelled(true);
  		 	  p.sendMessage(Main.getInstance().getConfig().getString("CommandBlocked").replaceAll("&", "§"));
                return;
              }
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
  @EventHandler
  public void onUpdate(EntityDamageEvent e) {
	  if (!(e.getEntity() instanceof Player)) {
		  return;
	  }
	  if (e.getCause() == DamageCause.FALL) {
		  if (!started) {
			  e.setCancelled(true);
		  }
	  }
  }
  @EventHandler
  public void onUpdate(PlayerMoveEvent e) {
	  
	  if (players.contains(e.getPlayer())) {
		  if (!started && star) {

  			e.getPlayer().teleport(e.getTo());
		  }
	  }
  }
  @EventHandler
  public void onUpdate(BlockBreakEvent e) {
	  
	  if (players.contains(e.getPlayer())) {
		  if (!started) {
			  e.setCancelled(true);
		  }
	  }
  }
  public void VerificarWin() {
	  if (players == null || players.size() == 0) {
		  return;
	  }
	  Player firstPlayer = players.get(0);
	  if (rodou) {
		  Automatic.throwRandomFirework(firstPlayer);
		  return;
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

				  Bukkit.broadcastMessage(ChatColor.GREEN + "Parabéns ao jogador " + firstPlayer.getName() + " por ganhar no mapa de skywars Grego");
				  rodou = true;	
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

				  }}}
	  
  public void queuedPlayers() {
	    final Player firstPlayer = players.get(0);
	  new BukkitRunnable() {
		    public void run() {

 
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

  new BukkitRunnable() {
	    public void run() {
        Main.getInstace().CarregarBaus3();
        for (Player p : players) {
      	  TitleAPI.sendTitle(p, 40, 70, 40, ChatColor.GREEN + "Os báus foram reabastecidos!");
        }
	    }}.runTaskTimer(Main.plugin, 20 * 60 * 10l, 20l * 60 * 5);
  	if (players.size() > 1 && started == false) {
  	players.forEach(p-> playersInPvp.add(p));
	players.forEach(p-> p.getInventory().addItem(new ItemStack(Material.WOOD_SWORD)));

 	 players.forEach(p-> p.getInventory().addItem(new ItemStack(Material.STONE , 64)));
  	 players.forEach(p-> p.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET)));
  	 players.forEach(p-> p.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS)));
  	 players.forEach(p-> p.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE)));
  	 players.forEach(p-> p.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS)));
  	 List<Player> ordered = new ArrayList<>(players);

  	 Jaulas.SW3.teleportByQueueOrder(ordered);
   	 for (Player p : ordered) {
		    CageManager.createCage(p.getLocation());
		    p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getBlockY() - 1, p.getLocation().getZ())).setType(Material.GLASS);
		    TitleAPI.sendTitle(p, 40, 40, 40, ChatColor.GREEN + "A partida irá começar em 15 segundos");
		    p.playSound(p.getLocation(), Sound.valueOf("CLICK"), 2f, 2f);
		}
	 new BukkitRunnable() {
		    public void run() {
CageManager.removeAllCages();
List<Player> ordered = new ArrayList<>(players);
for (Player p : ordered) {
p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getBlockY() - 1, p.getLocation().getZ())).setType(Material.AIR);
}
new BukkitRunnable() {
    public void run() {
started = true;
    }}.runTaskLater(Main.plugin, 20 * 2l);
  		    }
	    }.runTaskLater(Main.plugin, 20 * 15l);
	    }


      Bukkit.getConsoleSender().sendMessage("[EVENT] Players in SKYWARS ROOM #1: " + getPlayers());
for (Player p : getPlayers()) {
	if (!MainCommand.game.contains(p.getName())) {
  	    players.remove(p);
  	    players.forEach(p2-> p2.chat("/sw leave"));
  	    players.forEach(p2 -> p2.sendMessage("Ocorreu um erro com sua conexão ao skywars"));
    }
}

  
    


  	
  	  
  		  
  		  if (players.size() == 1 && star) {
  			  if (!rodou) {
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

    			  Bukkit.broadcastMessage(ChatColor.GREEN + "Parabéns ao jogador " + firstPlayer.getName() + " por ganhar no mapa de skywars Grego");
    			
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
  	    		  		  rodou = true;	
  				    }}.runTaskLater(Main.plugin, 100l);

    			  }}
  		  }}}.runTaskLater(Main.plugin, 20 * 5l);
}
  	
  public void broadcast(String message) {
    for (Player players2 : players) {
      players2.sendMessage(String.valueOf(Main.getInstance().getConfig().getString("Prefix").replaceAll("&", "§")) + message);

      if (players.size() > 1) {
    	     TitleAPI.sendTitle(players2, 40, 40, 40, Main.getInstance().getConfig().getString("Prefix").replaceAll("&", "§"), message);
    	    }
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
      if (Bukkit.getWorld("sw3") != null) {
          for (Player p : Bukkit.getWorld("sw3").getPlayers()) {
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
run = false;
      rodou = false;
      started = false;
      playersInPvp.clear();
      getPlayers().clear();
    HandlerList.unregisterAll(this.listener);
    
   Main.getInstance().getEventManager().setRdmAutomatic(null);  
	new BukkitRunnable() {
	    public void run() {
	    	   Automatic.getMVWorldManager().deleteWorld("sw3");
	    	Automatic.getMVWorldManager().cloneWorld("sw3copy", "sw3", "VoidGen");

			Main.getInstance().CarregarBaus2();
	    }}.runTaskLater(Main.plugin, 100l);
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
