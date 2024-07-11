package com.chenyue.mistplugin.commands.delspawn;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DelSpawn extends AbstractCommand {
    public static final String NAME = "DelSpawn";
    public static final String DESCRIPTION = "Delete a spawn";
    public static final String PERMISSION = "mist.command.delspawn";
    public static final String USAGE = "/delspawn <name>";

    public DelSpawn() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            this.playerOnly(sender);
            return true;
        }
        if (args.length != 0) {
            StringUtils.sendConfigMessage(sender, "messages.delspawn.usage");
            return true;
        }
        FileConfiguration spawnConfig = MistPlugin.getInstance().getSpawnConfig();
        spawnConfig.set("main", null);

        try {
           spawnConfig.save(new File(MistPlugin.getInstance().getDataFolder(), "spawn.yml"));
            StringUtils.sendConfigMessage(sender, "messages.delspawn.successful");
        } catch (IOException e) {
            e.printStackTrace();
            StringUtils.sendConfigMessage(sender, "messages.delspawn.fail");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
