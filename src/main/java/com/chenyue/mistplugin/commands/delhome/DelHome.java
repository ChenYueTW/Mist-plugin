package com.chenyue.mistplugin.commands.delhome;

import com.chenyue.mistplugin.data.HomeManager;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DelHome implements TabExecutor {
    private final HomeManager homeManager;

    public DelHome(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.delhome")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return false;
            }
            Player player = (Player) sender;
            if (args.length == 1) {
                String homeName = args[0];
                try {
                    this.homeManager.delHome(player, homeName);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    StringUtils.sendConfigMessage(player, "messages.delhome.failed");
                    return true;
                }
            } else {
                StringUtils.sendConfigMessage(sender, "messages.delhome.usage");
                return true;
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                try {
                    Map<String, Location> homes = homeManager.getHomes(player.getUniqueId());
                    list.addAll(homes.keySet());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }
}
