package com.chenyue.mistplugin.commands.homevip;

import com.chenyue.mistplugin.data.HomeManager;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomeVIP implements TabExecutor {
    private final HomeManager homeManager;

    public HomeVIP(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.vip")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            Player player = (Player) sender;

            if (args.length == 2) {
                String homename = args[0];
                String blockID = args[1];
                Material material = Material.getMaterial(blockID);

                if (material == null | material == Material.AIR) {
                    StringUtils.sendConfigMessage(player, "messages.homeVIP.invalid");
                    return true;
                }

                try {
                    this.homeManager.setHomeBlock(player.getUniqueId(), homename, material);
                    StringUtils.sendConfigMessage(player, "messages.homeVIP.successful", ImmutableMap.of(
                            "%homename%", homename,
                            "%blockID%", material.toString()
                    ));
                    return true;
                } catch (IOException e) {
                    StringUtils.sendConfigMessage(player, "messages.homeVIP.failed");
                    return true;
                }
            } else {
                StringUtils.sendConfigMessage(player, "messages.homeVIP.usage");
                return true;
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.notVIP");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                try {
                    return new ArrayList<>(this.homeManager.getHomes(player.getUniqueId()).keySet());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Collections.emptyList();
        } else if (args.length == 2) {
            String prefix = args[1].toUpperCase();
            List<String> completions = new ArrayList<>();

            for (Material material : Material.values()) {
                if (material.name().startsWith(prefix)) {
                    completions.add(material.name());
                }
            }

            Collections.sort(completions);
            return completions;
        }
        return Collections.emptyList();
    }
}
