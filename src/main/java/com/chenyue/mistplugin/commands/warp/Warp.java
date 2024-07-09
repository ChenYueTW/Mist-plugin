package com.chenyue.mistplugin.commands.warp;

import com.chenyue.mistplugin.data.WarpManager;
import com.chenyue.mistplugin.events.WarpGUI;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Warp implements TabExecutor {
    private final WarpManager warpManager;
    private final WarpGUI warpGUI;

    public Warp(WarpManager warpManager, WarpGUI warpGUI) {
        this.warpManager = warpManager;
        this.warpGUI = warpGUI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.warp")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 0) {
                this.warpGUI.openWarpGUI(player);
                return true;
            } else if (args.length == 1) {
                String warpName = args[0];
                Location warpLocation = this.warpManager.getWarp(warpName);

                if (warpLocation == null) {
                    StringUtils.sendConfigMessage(player, "messages.warp.notFound");
                    return true;
                }

                player.teleport(warpLocation);
                player.setFallDistance(0);
                StringUtils.sendConfigMessage(player, "messages.warp.successful", ImmutableMap.of(
                        "%name%", warpName
                ));
                return true;
            } else {
                StringUtils.sendConfigMessage(player, "messages.warp.usage");
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
