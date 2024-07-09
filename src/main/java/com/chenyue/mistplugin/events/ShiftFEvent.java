package com.chenyue.mistplugin.events;

import com.chenyue.mistplugin.data.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class ShiftFEvent implements Listener {
    @EventHandler
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        event.setCancelled(true);

        Bukkit.dispatchCommand(player, ConfigHandler.getShiftFCommand());
    }
}
