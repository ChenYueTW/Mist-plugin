package com.chenyue.mistplugin.commands.tpaherecancel;

import com.chenyue.mistplugin.data.TpManager;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TpaHereCancel implements TabExecutor {
    private final TpManager tpManager;

    public TpaHereCancel(TpManager tpManager) {
        this.tpManager = tpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.tpaherecancel")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            } else {
                Player player = (Player) sender;
                if (args.length == 0) {
                    this.tpManager.cancelTpaHereRequest(player);
                    return true;
                } else {
                    StringUtils.sendConfigMessage(player, "messages.tp.tpaCancelUsage");
                    return true;
                }
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return Arrays.asList();
    }
}
