package com.chenyue.mistplugin.commands.delwarp;

import com.chenyue.mistplugin.managers.WarpManager;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DelWarp extends AbstractCommand {
    public static final String NAME = "Delwarp";
    public static final String DESCRIPTION = "Delete a warp";
    public static final String PERMISSION = "mist.command.delwarp";
    public static final String USAGE = "/delwarp <warp>";
    private final WarpManager warpManager;

    public DelWarp(WarpManager warpManager) {
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
            StringUtils.sendConfigMessage(sender, "messages.delwarp.usage");
            return true;
        }
        String warpName = args[0];

        try {
            this.warpManager.deleteWarp(player, warpName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            StringUtils.sendConfigMessage(sender, "messages.delwarp.failed");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return this.warpManager.getWarpNames();
        return new ArrayList<>();
    }
}
