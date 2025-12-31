package me.rafaelauler.sw;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*     */ import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.onarandombox.MultiverseCore.MultiverseCore;






/*     */ 
/*     */ 
/*     */ public class Main
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {



	    private final int chestItemMinAmount = 3;
	    private final int chestItemMaxAmount = 8;
	 /*     */ /*     */   public static Plugin plugin;
/*     */   public static Main instance;
private SkywarsManager manager;
private static final double RARE_CHANCE = 0.25;       // 25%
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
public boolean isInvEmpty(Inventory inv) {
    for (ItemStack item : inv.getContents()) {
        if (item != null) {
            return false;
        }
    }
    return true;
}
public static final List<ItemStack> LEGENDARY_ITEMS = Arrays.asList(
        new ItemStack(Material.DIAMOND_SWORD),
        new ItemStack(Material.DIAMOND_CHESTPLATE),
        new ItemStack(Material.DIAMOND_HELMET),
        new ItemStack(Material.DIAMOND_BOOTS),
        new ItemStack(Material.DIAMOND_AXE),
        new ItemStack(Material.GOLDEN_APPLE, 2)
);
public static ItemStack getRandomItem(Random random) {

    double chance = random.nextDouble();


        if (chance <= RARE_CHANCE) {
            return LEGENDARY_ITEMS.get(random.nextInt(LEGENDARY_ITEMS.size())).clone();
        }
    

    return Jaulas.items.get(random.nextInt(Jaulas.items.size())).clone();
}
public void CarregarTodos() {

    Random random = new Random();

    for (World world : Bukkit.getWorlds()) {

        if (!world.getName().startsWith("sw")) continue;

        for (Chunk chunk : world.getLoadedChunks()) {

            for (BlockState state : chunk.getTileEntities()) {

                if (!(state instanceof Chest)) continue;

                Chest chest = (Chest) state;
                Inventory inv = chest.getInventory();
                inv.clear();

                chest.setMetadata(
                        "SW",
                        new FixedMetadataValue(Main.getInstance(), true)
                );

                int itemsAmount = random.nextInt(
                        chestItemMaxAmount - chestItemMinAmount + 1
                ) + chestItemMinAmount;

                List<Integer> slots = new ArrayList<>();
                for (int i = 0; i < inv.getSize(); i++) {
                    slots.add(i);
                }

                Collections.shuffle(slots, random);

                int maxItems = Math.min(itemsAmount, slots.size());
                for (int i = 0; i < maxItems; i++) {
                    ItemStack item = getRandomItem(random);
                    inv.setItem(slots.get(i), item);
                }
            }
        }
    }

    Bukkit.getLogger().info("Os baús de todos os mapas foram setados!");
}
public void setarLoot(Chest chest) {

    Random random = new Random();
    Inventory inv = chest.getInventory();
    inv.clear();

    chest.setMetadata(
            "SW",
            new FixedMetadataValue(Main.getInstance(), true)
    );

    int itemsAmount = random.nextInt(
            chestItemMaxAmount - chestItemMinAmount + 1
    ) + chestItemMinAmount;

    List<Integer> slots = new ArrayList<>();
    for (int i = 0; i < inv.getSize(); i++) {
        slots.add(i);
    }

    Collections.shuffle(slots, random);

    int maxItems = Math.min(itemsAmount, slots.size());

    for (int i = 0; i < maxItems; i++) {
        ItemStack item = getRandomItem(random);
        inv.setItem(slots.get(i), item);
    }
}

public void CarregarBaus22() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw1copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){ 
	              Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              Chest chest = (Chest) b2;
	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #1 COPY");
	  }
public void CarregarBaus222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw2copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              Chest chest = (Chest) b2;

	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #2 COPY");
	  }
public void CarregarBaus2222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw3copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              Chest chest = (Chest) b2;

	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #3 COPY");
	  }
public void CarregarBaus22222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw4copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              Chest chest = (Chest) b2;

	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #4 COPY");
	  }
public void CarregarBaus222222() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw5copy").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              Chest chest = (Chest) b2;

	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #5 COPY");
	  }
public void CarregarBaus2() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw2").getLoadedChunks()){

	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              Chest chest = (Chest) b2;

	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #2");
}
public void CarregarBaus4() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw4").getLoadedChunks()){
	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
	              Chest chest = (Chest) b2;

	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #4");
}
public void CarregarBaus5() {
	
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
	   for(Chunk c2 : Bukkit.getWorld("sw5").getLoadedChunks()){
	          for(BlockState b2 : c2.getTileEntities()){
	              if(b2 instanceof Chest){            	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();

	              Chest chest = (Chest) b2;

	    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

		 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #5");
}
public void CarregarBaus3() {

	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
    for(Chunk c3 : Bukkit.getWorld("sw3").getLoadedChunks()){
        for(BlockState b3 : c3.getTileEntities()){
            if(b3 instanceof Chest){
         	   Inventory inventory = ((Chest)b3.getBlock().getState()).getInventory();

              
              Chest chest = (Chest) b3;

    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
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

	 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #3");
}
public void CarregarBaus() {
	   Random random = new Random();
	   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;
    
	 for(Chunk c : Bukkit.getWorld("sw1").getLoadedChunks()){
      for(BlockState b : c.getTileEntities()){
          if(b instanceof Chest){
         	   Inventory inventory = ((Chest)b.getBlock().getState()).getInventory();

          
                                    Chest chest = (Chest) b;

                  	    	      chest.setMetadata("SW1", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
                  	              chest.getInventory().clear();
                  	              
                                    for (int i2 = 0; i2 < itemsAmount; i2++) {
                                        int slot = random.nextInt(inventory.getSize());

                                        int randomItemIndex = random.nextInt(Jaulas.items.size());
                                        ItemStack randomItem = Jaulas.items.get(randomItemIndex);
                                        
                
    chest.getInventory().setItem( slot, randomItem);
          }
      }   
      }}
	 Bukkit.getConsoleSender().sendMessage("SETADO ITEMS DO BAU PARA A SALA #1");


                  
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
            if(b2 instanceof Chest){
            	Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
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
	/* 121 */     instance = this;
	/* 122 */     plugin = this;

    manager = new SkywarsManager();
	this.getCommand("sw").setExecutor(new MainCommand(this));
	Configs.loadLobbySpawn();
	Configs.loadMainSpawn();
	 /* 109 */     Metrics metrics = new Metrics(this);
	 metrics.addCustomChart(new Metrics.DrilldownPie("serverAddress", () -> {
			Map<String, Map<String, Integer>> map = new HashMap<>();
			Map<String, Integer> entry = new HashMap<>();
			if (getConfig().getBoolean("SendIPAddressData")) entry.put(Bukkit.getServer().getIp(), 1);
			else entry.put("Hidden", 1);
			
			map.put("Port " + Bukkit.getServer().getPort(), entry);
			
			return map;
		}));

	 Bukkit.getPluginManager().registerEvents(new Eventos(manager), this);
	getCommand("setswlobby").setExecutor(new SetRounds());
	/*     */     
new BukkitRunnable() {
    public void run() {
    Bukkit.getConsoleSender().sendMessage("SETADO BAUS PARA TODAS AS SALAS");	
    for (World w : Bukkit.getWorlds()) {
    	for(Chunk c2 : w.getLoadedChunks()){
    		for(BlockState b2 : c2.getTileEntities()){
    			if (b2 instanceof Chest) {
    				if (w.getName().startsWith("sw")) {
    					  Chest chest = (Chest) b2;
    					   Random random = new Random();
    					   int itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount;

    		         	   Inventory inventory = ((Chest)b2.getBlock().getState()).getInventory();
    		    	      chest.setMetadata("SW", new FixedMetadataValue(Main.getInstance(), Boolean.valueOf(true)));
    		              chest.getInventory().clear();
    		              for (int i2 = 0; i2 < itemsAmount; i2++) {
    		                  int slot = random.nextInt(inventory.getSize());
    		                  int randomItemIndex = random.nextInt(Jaulas.items.size());
    		                  ItemStack randomItem = Jaulas.items.get(randomItemIndex);
    		chest.getInventory().setItem( slot, randomItem);                              		          
    }}}}}}}}.runTaskTimer(getInstace(), 20 * 10l, 100 * 20l * 5);
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
	World sw1 = Bukkit.getWorld("sw1");
	if (sw1 == null) {
	    Bukkit.getConsoleSender().sendMessage("Erro: mundo sw1 não existe!");
	    return;
	}
	World sw2 = Bukkit.getWorld("sw2");
	if (sw2 == null) {
	    Bukkit.getConsoleSender().sendMessage("Erro: mundo sw2 não existe!");
	    return;
	}
	World sw3 = Bukkit.getWorld("sw3");
	if (sw3 == null) {
	    Bukkit.getConsoleSender().sendMessage("Erro: mundo sw3 não existe!");
	    return;
	}
	getMVWorldManager().deleteWorld("sw1");
	getMVWorldManager().cloneWorld("sw1copy", "sw1", "VoidGen");

	getMVWorldManager().deleteWorld("sw2");
	getMVWorldManager().cloneWorld("sw2copy", "sw2", "VoidGen");

	getMVWorldManager().deleteWorld("sw3");
	getMVWorldManager().cloneWorld("sw3copy", "sw3", "VoidGen");
	getMVWorldManager().deleteWorld("sw4");
	getMVWorldManager().cloneWorld("sw4copy", "sw4", "VoidGen");
	getMVWorldManager().deleteWorld("sw5");
	getMVWorldManager().cloneWorld("sw5copy", "sw5", "VoidGen");
	Bukkit.getScheduler().runTaskLater(this, () -> {
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
    for (SkyWarsMap jaula : SkyWarsMap.values()) {
        try {
            SkyWarsGame game = manager.createGame(jaula);
            game.setSpawnLocation(Configs.LOBBY_SPAWN);
            Bukkit.getLogger().info("Sala criada para o mapa " + jaula.name());
        } catch (IllegalStateException e) {
            Bukkit.getLogger().warning("Não foi possível criar sala para " + jaula.name() + ": " + e.getMessage());
        }
    }
    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
		/* 151 */       Bukkit.getConsoleSender().sendMessage("§e[AulerSkywars] §aPlaceHolderAPI is found!");
		/* 151 */       Bukkit.getConsoleSender().sendMessage("§e[AulerSkywars] §aHooking into it!");
	    new PvPRounds(this).register();
		/* 151 */       Bukkit.getConsoleSender().sendMessage("§e[AulerSkywars] §aPlaceHolderAPI has hooked sucefully!");
	}
	}, 150L);
 	Bukkit.getConsoleSender().sendMessage("AULERSKYWARS HAS BEEN ENABLED!");
	 Bukkit.getScheduler().runTaskTimer(this, () -> {
         for (SkyWarsGame game : manager.getGames()) {
             game.tick();
             game.checkWin();
         }
     }, 20, 20);
}

public static MultiverseCore getMVWorldManager() {
    return JavaPlugin.getPlugin(MultiverseCore.class);
}              
public SkywarsManager getManager() {
    return manager;
}
}




	 

