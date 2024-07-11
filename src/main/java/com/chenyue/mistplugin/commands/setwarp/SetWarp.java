package com.chenyue.mistplugin.commands.setwarp;

import com.chenyue.mistplugin.managers.WarpManager;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetWarp extends AbstractCommand {
    public static final String NAME = "setwarp";
    public static final String DESCRIPTION = "Set a warp";
    public static final String PERMISSION = "mist.command.setwarp";
    public static final String USAGE = "/setwarp <name>";
    private final WarpManager warpManager;

    public SetWarp(WarpManager warpManager) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.warpManager = warpManager;
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
        if (args.length != 1) {
            StringUtils.sendConfigMessage(sender, "messages.setwarp.usage");
            return true;
        }
        String warpName = args[0];
        Location location = player.getLocation();

        try {
            this.warpManager.saveWarp(warpName, location);
            StringUtils.sendConfigMessage(sender, "messages.setwarp.successful", ImmutableMap.of(
                    "%name%", warpName,
                    "%X%", String.valueOf(location.getX()),
                    "%Y%", String.valueOf(location.getY()),
                    "%Z%", String.valueOf(location.getZ())
            ));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            StringUtils.sendConfigMessage(sender, "messages.setwarp.failed");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
