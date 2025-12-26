package me.rafaelauler.sw;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class Eventos implements Listener {


	  @EventHandler
	  public void aoconstruir(BlockPlaceEvent e)
	  {
	    if (MainCommand.game.contains(e.getPlayer().getName()) && e.getPlayer().getWorld().equals( Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.spawn.world")))) {
	      e.setCancelled(true);
	    }
	  }
	  @EventHandler
	  public void aocgonstruir(FoodLevelChangeEvent e)
	  {
	    if (MainCommand.game.contains(e.getEntity().getName()) && e.getEntity().getWorld().equals( Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.spawn.world")))) {
	      e.setCancelled(true);
	    }
	  }
	  @EventHandler
	  public void aoentrar(PlayerJoinEvent e) {
		  if (Main.getInstace().getConfig().getString("players." + e.getPlayer().getUniqueId()) == null) {
			  Main.getInstace().getConfig().set("players." + e.getPlayer().getUniqueId() + ".kills", 0);

			  Main.getInstace().getConfig().set("players." + e.getPlayer().getUniqueId() + ".deaths", 0);

			  Main.getInstace().getConfig().set("players." + e.getPlayer().getUniqueId() + ".wins", 0);
			  Bukkit.getConsoleSender().sendMessage("CRIADO COM SUCESSO PERFIL DO SKYWARS DE: " + e.getPlayer().getName());
		  }
	  
	    }

	  

	  
	  @EventHandler
	  public void aoconstruir(BlockBreakEvent e)
	  {
	    if (MainCommand.game.contains(e.getPlayer().getName()) && e.getPlayer().getWorld().equals( Bukkit.getServer().getWorld(Main.cfg_x1.getString("x1.coords.spawn.world")))) {
	      e.setCancelled(true);
	    }
	  }

	  }	  
