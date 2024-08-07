package com.chenyue.mistplugin.events;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.data.ConfigHandler;
import com.chenyue.mistplugin.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEvent implements Listener, ColorUtils {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Eco
        if (!MistPlugin.getInstance().getEco().hasAccount(player.getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    MistPlugin.getInstance().getEco().createAccount(player.getUniqueId());
                }
            }.runTaskAsynchronously(MistPlugin.getInstance());
        }

        // Welcome MSG
        event.setJoinMessage(color(ChatColor.YELLOW + event.getPlayer().getName() + ConfigHandler.getWelcomeMessage()));
    }
}
