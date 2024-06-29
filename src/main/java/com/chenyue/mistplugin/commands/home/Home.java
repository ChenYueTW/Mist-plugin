package com.chenyue.mistplugin.commands.home;

import com.chenyue.mistplugin.data.HomeManager;
import com.chenyue.mistplugin.events.HomeGUI;
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

public class Home implements TabExecutor {
    private final HomeManager homeManager;
    private final HomeGUI homeGUI;

    public Home(HomeManager homeManager, HomeGUI homeGUI) {
        this.homeManager = homeManager;
        this.homeGUI = homeGUI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.home")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            Player player = (Player) sender;
            // Usage
            if (args.length == 1) {
                String home = args[0];
                try {
                    this.homeManager.teleportHome(player, home);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                this.homeGUI.openHomeGUI(player);
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
