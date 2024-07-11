package com.chenyue.mistplugin.events;

import com.chenyue.mistplugin.managers.BanManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Date;
import java.util.UUID;

public class PlayerLogin implements Listener {
    private final BanManager banManager;

    public PlayerLogin(BanManager banManager) {
        this.banManager = banManager;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (this.banManager.isBanned(uuid)) {
            String reason = this.banManager.getBanReason(uuid);
            Date expiry = this.banManager.getBanExpiry(uuid);
            String expiryString = expiry == null ? "永久" : expiry.toString();
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            event.setKickMessage(String.format("你已經被伺服器BAN了!\n原因: %s\n時間: %s", reason, expiryString));
        }
    }
}
