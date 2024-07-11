package com.chenyue.mistplugin.commands.warp;

import com.chenyue.mistplugin.managers.WarpManager;
import com.chenyue.mistplugin.events.WarpGUI;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Warp extends AbstractCommand {
    public static final String NAME = "warp";
    public static final String DESCRIPTION = "Teleport to warp";
    public static final String PERMISSION = "mist.command.warp";
    public static final String USAGE = "/warp <name>";
    private final WarpManager warpManager;
    private final WarpGUI warpGUI;

    public Warp(WarpManager warpManager, WarpGUI warpGUI) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.warpManager = warpManager;
        this.warpGUI = warpGUI;
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
        if (args.length != 0 && args.length != 1) {
            StringUtils.sendConfigMessage(player, "messages.warp.usage");
            return true;
        }
        if (args.length == 0) {
            this.warpGUI.openWarpGUI(player);
            return true;
        }
        String warpName = args[0];
        Location warpLocation = this.warpManager.getWarp(warpName);

        if (warpLocation == null) {
            StringUtils.sendConfigMessage(player, "messages.warp.notFound");
            return true;
        }

        player.teleport(warpLocation);
        player.setFallDistance(0);
        StringUtils.sendConfigMessage(player, "messages.warp.successful", ImmutableMap.of(
                "%name%", warpName
        ));
        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return this.warpManager.getWarpNames();
        return new ArrayList<>();
    }
}
