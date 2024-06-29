package com.chenyue.mistplugin.commands.setwarp;

import com.chenyue.mistplugin.data.WarpManager;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetWarp implements TabExecutor {
    private final WarpManager warpManager;

    public SetWarp(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.setwarp") | sender.isOp()) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            if (args.length != 1) {
                StringUtils.sendConfigMessage(sender, "messages.setwarp.usage");
                return true;
            }
            Player player = (Player) sender;
            String warpName = args[0];
            Location location = player.getLocation();

            try {
                this.warpManager.saveWarp(warpName, location);
                StringUtils.sendConfigMessage(sender, "messages.setwarp.successful");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                StringUtils.sendConfigMessage(sender, "messages.setwarp.failed");
                return true;
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
