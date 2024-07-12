package com.chenyue.mistplugin.commands.unban;

import com.chenyue.mistplugin.managers.BanManager;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UnBan extends AbstractCommand {
    public static final String NAME = "unban";
    public static final String DESCRIPTION = "Un Ban Player";
    public static final String PERMISSION = "mist.command.unban";
    public static final String USAGE = "/unban <player>";
    private final BanManager banManager;

    public UnBan(BanManager banManager) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (args.length != 1) {
            StringUtils.sendConfigMessage(sender, "messages.unban.usage");
            return true;
        }
        OfflinePlayer unbanPlayer = Bukkit.getOfflinePlayer(args[0]);
        if (unbanPlayer == null) {
            StringUtils.sendConfigMessage(sender, "messages.unban.playerNotfound");
            return true;
        }
        this.banManager.unbanPlayer(unbanPlayer.getUniqueId());
        StringUtils.sendConfigMessage(sender, "messages.unban.success", ImmutableMap.of(
                "%player%", Objects.requireNonNull(unbanPlayer.getName())
        ));
        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}
