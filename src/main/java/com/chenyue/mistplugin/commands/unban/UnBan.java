package com.chenyue.mistplugin.commands.unban;

import com.chenyue.mistplugin.data.BanManager;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class UnBan implements TabExecutor {
    private final BanManager banManager;

    public UnBan(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.unban") | sender.isOp()) {
            if (args.length != 1) {
                StringUtils.sendConfigMessage(sender, "messages.unban.usage");
                return true;
            }
            Player unbanPlayer = Bukkit.getPlayer(args[0]);
            if (unbanPlayer == null) {
                StringUtils.sendConfigMessage(sender, "messages.unban.playerNotfound");
                return true;
            }
            this.banManager.unbanPlayer(unbanPlayer.getUniqueId());
            StringUtils.sendConfigMessage(sender, "messages.unban.success", ImmutableMap.of(
                    "%player%", unbanPlayer.getName()
            ));
            return true;
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}
