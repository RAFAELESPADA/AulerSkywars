package me.rafaelauler.sw;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Eventos implements Listener {

    private final SkywarsManager manager;

    public Eventos(SkywarsManager manager) {
        this.manager = manager;
    }

    // Impede que jogadores construam dentro do lobby
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (isInLobby(player)) {
            e.setCancelled(true);
        }
    }

    // Impede que jogadores destruam blocos dentro do lobby
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (isInLobby(player)) {
            e.setCancelled(true);
        }
    }

    // Impede que jogadores percam fome no lobby
    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player player && isInLobby(player)) {
            e.setCancelled(true);
        }
    }

    // Cria perfil do jogador ao entrar no servidor
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.getInventory().setArmorContents(null);

        String path = "players." + player.getUniqueId();
        if (Main.getInstace().getConfig().getString(path) == null) {
            Main.getInstace().getConfig().set(path + ".kills", 0);
            Main.getInstace().getConfig().set(path + ".deaths", 0);
            Main.getInstace().getConfig().set(path + ".wins", 0);
            Main.getInstance().saveConfig();
            Bukkit.getConsoleSender().sendMessage("CRIADO COM SUCESSO PERFIL DO SKYWARS DE: " + player.getName());
        }
    }

    // Verifica se o jogador está no lobby
    private boolean isInLobby(Player player) {
        String spawnWorld = Main.cfg_x1.getString("x1.coords.spawn.world");
        return player.getWorld().getName().equals(spawnWorld);
    }
}
