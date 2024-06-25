package com.chenyue.mistplugin.commands.delspawn;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.StringUtils;
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

public class DelSpawn implements TabExecutor {
    private final MistPlugin plugin;

    public DelSpawn(MistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.delspawn") | sender.isOp()) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return false;
            }
            if (args.length == 0) {
                FileConfiguration spawnConfig = MistPlugin.getSpawnConfig();
                spawnConfig.set("main", null);

                try {
                    MistPlugin.getSpawnConfig().save(new File(plugin.getDataFolder(), "spawn.yml"));
                    StringUtils.sendConfigMessage(sender, "messages.delspawn.successful");
                } catch (IOException e) {
                    e.printStackTrace();
                    StringUtils.sendConfigMessage(sender, "messages.delspawn.fail");
                }
            } else {
                StringUtils.sendConfigMessage(sender, "messages.delspawn.usage");
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
