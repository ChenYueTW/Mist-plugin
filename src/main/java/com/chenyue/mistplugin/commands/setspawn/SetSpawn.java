package com.chenyue.mistplugin.commands.setspawn;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SetSpawn implements TabExecutor {
    private final MistPlugin plugin;

    public SetSpawn(MistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.setspawn") | sender.isOp()) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return false;
            }
            Player player = (Player) sender;
            if (args.length == 0) {
                FileConfiguration spawnConfig = MistPlugin.getSpawnConfig();
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
                    MistPlugin.getSpawnConfig().save(new File(plugin.getDataFolder(), "spawn.yml"));
                    StringUtils.sendConfigMessage(sender, "messages.setspawn.successful");
                } catch (IOException e) {
                    e.printStackTrace();
                    StringUtils.sendConfigMessage(sender, "messages.setspawn.fail");
                }
            } else {
                StringUtils.sendConfigMessage(sender, "messages.setspawn.usage");
            }
            return true;
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
