package com.chenyue.mistplugin.Events;

import com.chenyue.mistplugin.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class ShiftF implements Listener {
    @EventHandler
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        event.setCancelled(true);

        Bukkit.dispatchCommand(player, Config.getShiftFCmd());
    }
}
