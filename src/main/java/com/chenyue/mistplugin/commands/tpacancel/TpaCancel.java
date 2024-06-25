package com.chenyue.mistplugin.commands.tpacancel;

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

public class TpaCancel implements TabExecutor {
    private final TpManager tpManager;

    public TpaCancel(TpManager tpManager) {
        this.tpManager = tpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.tpacancel")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            } else {
                Player player = (Player) sender;
                if (args.length == 0) {
                    this.tpManager.cancelRequest(player);
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
