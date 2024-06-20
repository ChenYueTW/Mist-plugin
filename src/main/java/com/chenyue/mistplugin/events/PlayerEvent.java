package com.chenyue.mistplugin.events;

import com.chenyue.mistplugin.Config;
import com.chenyue.mistplugin.MistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Eco
        if (!MistPlugin.getEco().hasAccount(player.getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    MistPlugin.getEco().createAccount(player.getUniqueId());
                }
            }.runTaskAsynchronously(MistPlugin.getInstance());
        }

        // Welcome MSG
        event.getPlayer().sendMessage(ChatColor.AQUA + Config.getWelcomeMSG());
    }
}
