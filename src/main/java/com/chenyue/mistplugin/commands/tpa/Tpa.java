package com.chenyue.mistplugin.commands.tpa;

import com.chenyue.mistplugin.data.TpManager;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Tpa implements TabExecutor {
    private final TpManager tpManager;

    public Tpa(TpManager tpManager) {
        this.tpManager = tpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.tpa")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 1) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);

                try {
                    this.tpManager.addTpaRequest(targetPlayer.getUniqueId(), player.getUniqueId());
                } catch (Exception e) {
                    StringUtils.sendConfigMessage(sender, "messages.tp.playerNotOnline");
                    return true;
                }

                StringUtils.sendConfigMessage(player, "messages.tp.tpaSendRequest");
                StringUtils.sendConfigMessage(targetPlayer, "messages.tp.tpaReceiveRequest", ImmutableMap.of(
                        "%name%", player.getName()
                ));
                return true;
            } else {
                StringUtils.sendConfigMessage(player, "messages.tp.tpaUsage");
                return true;
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
