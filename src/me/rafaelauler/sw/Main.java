package me.rafaelauler.sw;
 
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*     */ import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;






/*     */ 
/*     */ 
/*     */ public class Main
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {

	 private EventManager eventmanager;

	 private EventManager2 eventmanager2;

	    private final int chestItemMinAmount = 2;
	    private final int chestItemMaxAmount = 6;
	 private EventManager3 eventmanager3;
	 /*     */ /*     */   public static Plugin plugin;
/*     */   public static Main instance;

/*     */   private File cf1;
/*  77 */   public static String pluginName = "AulerSkywars";
/*     */   
/*  98 */   public static File file_x1 = new File("plugins/AulerSkywars", "1v1.yml");
/*     */ 
/* 364 */   public static FileConfiguration cfg_x1 = YamlConfiguration.loadConfiguration(file_x1);  
/*     */   public static Main getInstance()
/*     */   {
/*  82 */     return instance;
/*     */   }
/*     */   
/*     */   public static Main getInstace()
/*     */   {
/*  87 */     return instance;
/*     */   }
public static Main getInstace23()
/*     */   {
/*  87 */     return instance;
/*     */   }
public EventManager getEventManager() {
    return this.eventmanager;
  }
public EventManager2 getEventManager2() {
    return this.eventmanager2;
  }
public EventManager3 getEventManager3() {
    return this.eventmanager3;
  }
public void CarregarBaus22() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw1copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	          }
	  }
public void CarregarBaus222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw2copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	          }
	  }
public void CarregarBaus2222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw3copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	          }
	  }
public void CarregarBaus22222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw4copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	          }
	  }
public void CarregarBaus222222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw5copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	          }
	  }
public void CarregarBaus2() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw2").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	  }
	   
}
public void CarregarBaus4() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw4").getLoadedChunks()){
	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	  }
	   
}
public void CarregarBaus5() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw5").getLoadedChunks()){
	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              
	              Chest chest = (Chest) b2;
	              chest.getInventory().clear();
	              for (int i2 = 0; i2 < itemsAmount; i2++) {
	                  int slot = random.nextInt(inventory.getSize());

	                  int randomItemIndex = random.nextInt(Jaulas.items.size());
	                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
	                  

	chest.getInventory().setItem( slot, randomItem);
	}                                           
	              }
	          }
	  }
	   
}
public void CarregarBaus3() {

	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
    for(Chunk c3 : Bukkit.getWorld("sw3").getLoadedChunks()){
        for(BlockState b3 : c3.getTileEntities()){
            if(b3 instanceof Chest){
         	   Inventory inventory = ((Chest)b3.getBlock().getState()).getInventory();
              
              
              Chest chest = (Chest) b3;

              chest.getInventory().clear();
              for (int i2 = 0; i2 < itemsAmount; i2++) {
                  int slot = random.nextInt(inventory.getSize());

                  int randomItemIndex = random.nextInt(Jaulas.items.size());
                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
                  

chest.getInventory().setItem( slot, randomItem);
}

            }
}
    }

}
public void CarregarBaus() {
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
    
	 for(Chunk c : Bukkit.getWorld("sw1").getLoadedChunks()){
      for(BlockState b : c.getTileEntities()){
          if(b instanceof Chest){
         	   Inventory inventory = ((Chest)b.getBlock().getState()).getInventory();
                
          
                                    Chest chest = (Chest) b;

                  	              chest.getInventory().clear();
                                    for (int i2 = 0; i2 < itemsAmount; i2++) {
                                        int slot = random.nextInt(inventory.getSize());

                                        int randomItemIndex = random.nextInt(Jaulas.items.size());
                                        ItemStack randomItem = Jaulas.items.get(randomItemIndex);
                                        
                
    chest.getInventory().setItem( slot, randomItem);
          }
      }   
      }}


                  
}
public void onDisable()
/*     */   {

    for(Chunk c2 : Bukkit.getWorld("sw1").getLoadedChunks()){
            for(BlockState b2 : c2.getTileEntities()){
                if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
                inventory.clear();
                }
            }
    }

Bukkit.getConsoleSender().sendMessage("BAUS DA SALA #1 DESCARREGADOS");
    for(Chunk c2 : Bukkit.getWorld("sw2").getLoadedChunks()){
        for(BlockState b2 : c2.getTileEntities()){
            if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
            inventory.clear();
            }
        }
        
}

Bukkit.getConsoleSender().sendMessage("BAUS DA SALA #2 DESCARREGADOS");
    for(Chunk c2 : Bukkit.getWorld("sw3").getLoadedChunks()){
        for(BlockState b2 : c2.getTileEntities()){
            if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
            inventory.clear();
            }
        }
}  

Bukkit.getConsoleSender().sendMessage("BAUS DA SALA #3 DESCARREGADOS");
for(Chunk c2 : Bukkit.getWorld("sw4").getLoadedChunks()){
    for(BlockState b2 : c2.getTileEntities()){
        if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
        inventory.clear();
        }
    }
}  

Bukkit.getConsoleSender().sendMessage("BAUS DA SALA #4 DESCARREGADOS");
for(Chunk c2 : Bukkit.getWorld("sw5").getLoadedChunks()){
    for(BlockState b2 : c2.getTileEntities()){
        if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
        inventory.clear();
        }
    }
}
for(Chunk c2 : Bukkit.getWorld("sw5copy").getLoadedChunks()){
    for(BlockState b2 : c2.getTileEntities()){
        if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
        inventory.clear();
        }
    }
}
for(Chunk c2 : Bukkit.getWorld("sw4copy").getLoadedChunks()){
    for(BlockState b2 : c2.getTileEntities()){
        if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
        inventory.clear();
        }
    }
}
for(Chunk c2 : Bukkit.getWorld("sw3copy").getLoadedChunks()){
    for(BlockState b2 : c2.getTileEntities()){
        if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
        inventory.clear();
        }
    }
}
for(Chunk c2 : Bukkit.getWorld("sw2copy").getLoadedChunks()){
    for(BlockState b2 : c2.getTileEntities()){
        if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
        inventory.clear();
        }
    }
}
for(Chunk c2 : Bukkit.getWorld("sw1copy").getLoadedChunks()){
    for(BlockState b2 : c2.getTileEntities()){
        if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
        inventory.clear();
        }
    }
}

Bukkit.getConsoleSender().sendMessage("BAUS DA SALA #5 DESCARREGADOS");
    Bukkit.getConsoleSender().sendMessage("TODOS OS BAUS DAS PARTIDAS LIMPADOS!");
}
/*     */   public void onEnable()
/*     */   {
	  
	getCommand("sw").setExecutor(new MainCommand());
	getCommand("sw1").setExecutor(new MCMD1());
	getCommand("sw2").setExecutor(new MCMD2());
	getCommand("sw3").setExecutor(new MCMD3());
	getCommand("sw4").setExecutor(new MCMD4());
	getCommand("sw5").setExecutor(new MCMD5());
	if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
		/* 151 */       Bukkit.getConsoleSender().sendMessage("§e[AulerSkywars] §aPlaceHolderAPI is found!");
		/* 151 */       Bukkit.getConsoleSender().sendMessage("§e[AulerSkywars] §aHooking into it!");
	    new PvPRounds(this).register();
		/* 151 */       Bukkit.getConsoleSender().sendMessage("§e[AulerSkywars] §aPlaceHolderAPI has hooked sucefully!");
	}
	getCommand("setswlobby").setExecutor(new MainCommand());
	 Bukkit.getPluginManager().registerEvents(new Eventos(), this);
	 /* 109 */     Metrics metrics = new Metrics(this);
	 metrics.addCustomChart(new Metrics.DrilldownPie("serverAddress", () -> {
			Map<String, Map<String, Integer>> map = new HashMap<>();
			Map<String, Integer> entry = new HashMap<>();
			if (getConfig().getBoolean("SendIPAddressData")) entry.put(Bukkit.getServer().getIp(), 1);
			else entry.put("Hidden", 1);
			
			map.put("Port " + Bukkit.getServer().getPort(), entry);
			
			return map;
		}));
	 Bukkit.getPluginManager().registerEvents(new Automatic(), this);

	 Bukkit.getPluginManager().registerEvents(new Automatic2(), this);

	 Bukkit.getPluginManager().registerEvents(new Automatic3(), this);

	 Bukkit.getPluginManager().registerEvents(new Automatic4(), this);

	 Bukkit.getPluginManager().registerEvents(new Automatic5(), this);
	 ;


                 
                 
         
	getCommand("setswlobby").setExecutor(new SetRounds());
	/*     */     
/* 121 */     instance = this;
/* 122 */     plugin = this;
(getInstance()).eventmanager = new EventManager();

(getInstance()).eventmanager2 = new EventManager2();

(getInstance()).eventmanager3 = new EventManager3();
  instance = this;
   plugin = this;
	ConsoleCommandSender cmd = Bukkit.getConsoleSender();
	if (!Coins.setupPermissions()) {
		cmd.sendMessage("AulerSkywars - Disabled due to no Vault dependency found! AulerSkywars VERSION" + getDescription().getVersion());
        cmd.sendMessage("Install vault to PvPRounds work!");
        getServer().getPluginManager().disablePlugin(this);
        return;
    }
	if (!Coins.setupEconomy()) {
		cmd.sendMessage("AulerSkywars - Disabled due to no Vault dependency found! AulerSkywars VERSION" + getDescription().getVersion());
        cmd.sendMessage("Install vault to PvPRounds work!");
        getServer().getPluginManager().disablePlugin(this);
        return;
    }
	saveDefaultConfig();
	File cf = new File(getDataFolder(), "config.yml");
	/* 127 */     if (!cf.exists()) {
	/* 128 */       saveResource("config.yml", false);
	/*     */     }
	/* 130 */     this.cf1 = new File(getDataFolder(), "config.yml");
	/* 131 */     if (!file_x1.exists()) {
	/* 132 */       saveResource("1v1.yml", false);
	/*     */     }

	try {
		YamlConfiguration.loadConfiguration(file_x1).load(file_x1);
	} catch (IOException | InvalidConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Automatic.getMVWorldManager().deleteWorld("sw1");
	Automatic.getMVWorldManager().cloneWorld("sw1copy", "sw1", "VoidGen");

	Automatic.getMVWorldManager().deleteWorld("sw2");
	Automatic.getMVWorldManager().cloneWorld("sw2copy", "sw2", "VoidGen");

	Automatic.getMVWorldManager().deleteWorld("sw3");
	Automatic.getMVWorldManager().cloneWorld("sw3copy", "sw3", "VoidGen");
	Automatic.getMVWorldManager().deleteWorld("sw4");
	Automatic.getMVWorldManager().cloneWorld("sw4copy", "sw4", "VoidGen");
	Automatic.getMVWorldManager().deleteWorld("sw5");
	Automatic.getMVWorldManager().cloneWorld("sw5copy", "sw5", "VoidGen");
	CarregarBaus();
	CarregarBaus2();
	CarregarBaus3();
	CarregarBaus4();
	CarregarBaus5();
	CarregarBaus222222();
	CarregarBaus22();
	CarregarBaus222();
	CarregarBaus2222();
	CarregarBaus22222();
 	Bukkit.getConsoleSender().sendMessage("AULERSKYWARS HAS BEEN ENABLED!");

}

              }





	 

