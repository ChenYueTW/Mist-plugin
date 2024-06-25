package com.chenyue.mistplugin.data;

import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpManager {
    private final Map<UUID, UUID> tpaRequest = new HashMap<>();

    public void addTpaRequest(UUID player, UUID target) {
        this.tpaRequest.put(player, target);
    }

    public void addTpaHereRequest(UUID player, UUID target) {
        this.tpaRequest.put(player, target);
    }

    public void acceptRequest(Player player) {
        UUID targetUuid = this.tpaRequest.remove(player.getUniqueId());

        if (targetUuid == null) {
            StringUtils.sendConfigMessage(player, "messages.tp.requestNotFound");
        } else {
            Player targetPlayer = Bukkit.getPlayer(targetUuid);

            if (targetPlayer == null | !targetPlayer.isOnline()) {
                StringUtils.sendConfigMessage(player, "messages.tp.playerNotOnline");
            } else {
                player.teleport(targetPlayer.getLocation());
                StringUtils.sendConfigMessage(player, "messages.tp.successfulTeleport");
            }
        }
    }

    public void cancelRequest(Player player) {
        UUID targetUuid = null;

        for (Map.Entry<UUID, UUID> entry : this.tpaRequest.entrySet()) {
            if (entry.getValue().equals(player.getUniqueId())) {
                targetUuid = entry.getKey();
                break;
            }
        }

        if (targetUuid == null) {
            StringUtils.sendConfigMessage(player, "messages.tp.requestNotFound");
        } else {
            this.tpaRequest.remove(targetUuid);
            StringUtils.sendConfigMessage(player, "messages.tp.cancelRequest");
        }
    }
}
