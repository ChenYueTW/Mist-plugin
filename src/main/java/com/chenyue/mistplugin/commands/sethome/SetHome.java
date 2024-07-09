package com.chenyue.mistplugin.commands.sethome;

import com.chenyue.mistplugin.data.ConfigHandler;
import com.chenyue.mistplugin.data.HomeManager;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SetHome implements TabExecutor {
    private final HomeManager homeManager;

    public SetHome(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.sethome")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return false;
            }
            Player player = (Player) sender;
            if (args.length != 1) {
                StringUtils.sendConfigMessage(sender, "messages.sethome.usage");
                return false;
            }

            String homeName = args[0];
            Location location = player.getLocation();
            UUID uuid = player.getUniqueId();

            try {
                if (this.homeManager.getHomeCount(uuid) >= ConfigHandler.getHomeLimit()) {
                    StringUtils.sendConfigMessage(sender, "messages.sethome.limitExceeded");
                    return true;
                } else {
                    this.homeManager.saveHome(player, location, homeName);
                    StringUtils.sendConfigMessage(sender, "messages.sethome.successful", ImmutableMap.of(
                            "%home%", homeName,
                            "%X%", String.valueOf(location.getX()),
                            "%Y%", String.valueOf(location.getY()),
                            "%Z%", String.valueOf(location.getZ())
                    ));
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                StringUtils.sendConfigMessage(sender, "messages.sethome.failed");
                return true;
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
