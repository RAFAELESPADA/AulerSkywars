package me.rafaelauler.sw;

/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;

import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
import org.bukkit.Sound;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;



 
/*     */ public class MCMD1
/*     */   implements CommandExecutor, Listener
/*     */ {
/*  43 */   public static HashMap<String, ItemStack[]> saveinv = new HashMap();
/*  44 */   public static HashMap<String, ItemStack[]> savearmor = new HashMap();
/*  45 */   public static HashMap<String, Location> saveworld = new HashMap();
/*  46 */   public static HashMap<String, GameMode> savegamemode = new HashMap();
public static HashMap<String, Scoreboard> savescore = new HashMap();
public static HashMap<String, Integer> savelevel = new HashMap();
public static HashMap<String, Integer> savehunger = new HashMap();
public static HashMap<String, PotionEffect> saveeffect = new HashMap();
public static HashMap<String, Integer> saveair = new HashMap();

/*     */   static Main plugin;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MCMD1(Main BukkitMain)
/*     */   {
/*  62 */     plugin = BukkitMain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   HashMap<String, Location> maps = new HashMap();
/*  77 */   public static ArrayList<String> game = new ArrayList();
public static ArrayList<Player> player = new ArrayList();
/*  78 */   List<String> commands = Arrays.asList(new String[] { "admin", "list", "create", "delete", "1v1", "score", "setspawn", "spawn", "join", "leave", "reset", "coins", "setchallenge", "kit", "kitunlocker", "shop", "resetkit", "stats", "reload", "update" });
/*     */   
/*     */   public MCMD1() {}
/*     */   

/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
/*  91 */     if (commandLabel.equalsIgnoreCase("sw1"))
/*     */     {
/*  93 */       if (args.length == 0)
/*     */       {

/* 106 */         sender.sendMessage(ChatColor.DARK_AQUA + "§m-----------" + ChatColor.AQUA + " AULERSKYWARS COMMANDS " + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "-------------");
/* 107 */         sender.sendMessage(ChatColor.DARK_AQUA + "§eCreated by Rafael Auler");
/* 108 */         sender.sendMessage("");
/* 109 */         sender.sendMessage(ChatColor.DARK_AQUA + " - " + ChatColor.AQUA + "/" + commandLabel + ChatColor.DARK_AQUA + " join - " + ChatColor.GRAY + " JOIN ROOM #1");
/* 102 */         return true;
/*     */       }
/* 167 */       if (args[0].equalsIgnoreCase("join"))
/*     */       {
/* 169 */         if ((sender instanceof Player))
/*     */         {
/*     */           if (Main.cfg_x1.getString("x1.coords.spawn.world") == null) {
	sender.sendMessage(ChatColor.YELLOW + "Skywars spawn is not seted yet!");
	return true;
}
if (Automatic.players.size() >= 12) {
	sender.sendMessage(ChatColor.RED + "Essa partida está lotada! Escolha outra!");
	return true;
}
if (Automatic.star) {
	sender.sendMessage(ChatColor.RED + "Essa partida já foi iniciada! Escolha outra!");
	return true;
}
/*     */ Player p = (Player)sender;
/*     */ Automatic a1 = new Automatic();
a1.setGameType(Automatic.GameType.STARTING);
/* 179 */           p.sendMessage(Main.getInstance().getConfig().getString("Joined").replaceAll("&", "§"));
p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 10f, 10f);
/*     */ TitleAPI.sendTitle(p, 80, 80, 80, "§b§lSKYWARS", "§fVocê entrou em uma sala!");
          Automatic.players.add(p);
          p.getInventory().clear();
          p.teleport(new Location(Bukkit.getWorld("swlobby"), -16.629, 97.1347, -11.604));
/*     */ 
/*     */ 
/*     */ 
}}}
return false;
}
}

