package com.chenyue.mistplugin.managers;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpManager {
    // A, B => A-->B
    private final Map<UUID, UUID> tpaRequest = new HashMap<>();
    // A, B => B-->A
    private final Map<UUID, UUID> tpaHereRequest = new HashMap<>();

    public void addTpaRequest(UUID player, UUID target) {
        this.tpaRequest.put(target, player);
        this.scheduleRequestRemoval(player, true);
    }

    public void addTpaHereRequest(UUID player, UUID target) {
        this.tpaHereRequest.put(player, target);
        this.scheduleRequestRemoval(player, false);
    }

    private void scheduleRequestRemoval(UUID playerUuid, boolean isTpa) {
        Bukkit.getScheduler().runTaskLater(MistPlugin.getInstance(), () -> {
            if (isTpa) this.tpaRequest.remove(playerUuid);
            else this.tpaHereRequest.remove(playerUuid);

            Player player = Bukkit.getPlayer(playerUuid);
            if (player != null && player.isOnline()) StringUtils.sendConfigMessage(player, "messages.tp.requestMaturity");
        }, 20 * 60); // 60 seconds
    }

    public void acceptTpaRequest(Player player) { // B
        UUID teleporter = this.tpaRequest.get(player.getUniqueId()); // A

        if (teleporter == null) {
            StringUtils.sendConfigMessage(player, "messages.tp.requestNotFound");
        } else {
            Player teleported = Bukkit.getPlayer(teleporter);

            if (teleported == null | !teleported.isOnline()) {
                StringUtils.sendConfigMessage(player, "messages.tp.playerNotOnline");
            } else {
                teleported.teleport(player.getLocation());
                StringUtils.sendConfigMessage(player, "messages.tp.successfulTeleport");
            }
        }
        this.tpaRequest.remove(player.getUniqueId());
    }

    public void cancelTpaRequest(Player player) { // B
        UUID teleporterUuid = this.tpaRequest.get(player.getUniqueId()); // A

        if (teleporterUuid == null) {
            StringUtils.sendConfigMessage(player, "messages.tp.requestNotFound");
        } else {
            this.tpaRequest.remove(player.getUniqueId());
            StringUtils.sendConfigMessage(player, "messages.tp.cancelRequest");
        }
    }

    public void acceptTpaHereRequest(Player player) { // A
        UUID teleporter = this.tpaHereRequest.get(player.getUniqueId()); // B

        if (teleporter == null) {
            StringUtils.sendConfigMessage(player, "messages.tp.requestNotFound");
        } else {
            Player teleported = Bukkit.getPlayer(teleporter);

            if (teleported == null | !teleported.isOnline()) {
                StringUtils.sendConfigMessage(player, "messages.tp.playerNotOnline");
            } else {
                player.teleport(teleported.getLocation());
                StringUtils.sendConfigMessage(player, "messages.tp.successfulTeleport");
            }
        }
        this.tpaHereRequest.remove(player.getUniqueId());
    }

    public void cancelTpaHereRequest(Player player) { // A
        UUID teleporterUuid = this.tpaHereRequest.get(player.getUniqueId()); // B

        if (teleporterUuid == null) {
            StringUtils.sendConfigMessage(player, "messages.tp.requestNotFound");
        } else {
            this.tpaHereRequest.remove(player.getUniqueId());
            StringUtils.sendConfigMessage(player, "messages.tp.cancelRequest");
        }
    }
}
