package com.chenyue.mistplugin.commands.setspawn;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SetSpawn extends AbstractCommand {
    public static final String NAME = "setspawn";
    public static final String DESCRIPTION = "Set spawn location";
    public static final String PERMISSION = "mist.command.setspawn";
    public static final String USAGE = "/setspawn";

    public SetSpawn() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
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
        if (args.length != 0) {
            StringUtils.sendConfigMessage(sender, "messages.setspawn.usage");
            return true;
        }
        FileConfiguration spawnConfig = MistPlugin.getInstance().getSpawnConfig();
        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        String world = location.getWorld().getName();
        double yaw = location.getYaw();
        double pitch = location.getPitch();

        spawnConfig.set("main.x", x);
        spawnConfig.set("main.y", y);
        spawnConfig.set("main.z", z);
        spawnConfig.set("main.world", world);
        spawnConfig.set("main.yaw", yaw);
        spawnConfig.set("main.pitch", pitch);

        try {
            MistPlugin.getInstance().getSpawnConfig().save(new File(MistPlugin.getInstance().getDataFolder(), "spawn.yml"));
            StringUtils.sendConfigMessage(player, "messages.setspawn.successful", ImmutableMap.of(
                    "%X%", String.valueOf(location.getX()),
                    "%Y%", String.valueOf(location.getY()),
                    "%Z%", String.valueOf(location.getZ())
            ));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            StringUtils.sendConfigMessage(sender, "messages.setspawn.fail");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
