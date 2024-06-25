package com.chenyue.mistplugin.commands.back;

import com.chenyue.mistplugin.data.LocationManager;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Back implements TabExecutor {
    private final LocationManager locationManager;

    public Back(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.back")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            Player player = (Player) sender;

            try {
                Location location = this.locationManager.getLocation(player.getUniqueId());
                if (location == null) {
                    StringUtils.sendConfigMessage(player, "messages.back.notFound");
                    return true;
                }
                player.teleport(location);
                player.setFallDistance(0);
                StringUtils.sendConfigMessage(player, "messages.back.successful");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                StringUtils.sendConfigMessage(player, "messages.back.failed");
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
