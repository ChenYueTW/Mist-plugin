package com.chenyue.mistplugin.commands.back;

import com.chenyue.mistplugin.managers.LocationManager;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Back extends AbstractCommand {
    public static final String NAME = "Back";
    public static String DESCRIPTION = "Teleport Back";
    public static String PERMISSION = "mist.command.back";
    public static String USAGE = "/back";
    private final LocationManager locationManager;

    public Back(LocationManager locationManager) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.locationManager = locationManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (!(sender instanceof Player player)) {
            this.playerOnly(sender);
            return true;
        }

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
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}



