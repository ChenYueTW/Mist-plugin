package com.chenyue.mistplugin.commands.delwarp;

import com.chenyue.mistplugin.data.WarpManager;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DelWarp implements TabExecutor {
    private final WarpManager warpManager;

    public DelWarp(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.delwarp") | sender.isOp()) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            if (args.length != 1) {
                StringUtils.sendConfigMessage(sender, "messages.delwarp.usage");
                return true;
            }
            Player player = (Player) sender;
            String warpName = args[0];

            try {
                this.warpManager.deleteWarp(player, warpName);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                StringUtils.sendConfigMessage(sender, "messages.delwarp.failed");
                return true;
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return this.warpManager.getWarpNames();
        return new ArrayList<>();
    }
}
