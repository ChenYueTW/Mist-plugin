package com.chenyue.mistplugin.commands.ban;

import com.chenyue.mistplugin.data.BanManager;
import com.chenyue.mistplugin.utils.ColorUtils;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ban implements TabExecutor, ColorUtils {
    private final BanManager banManager;

    public Ban(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.ban") | sender.isOp()) {
            if (args.length < 1) {
                StringUtils.sendConfigMessage(sender, "messages.ban.usage");
                return true;
            }
            OfflinePlayer banPlayer = Bukkit.getOfflinePlayer(args[0]);
            if (banPlayer == null) {
                StringUtils.sendConfigMessage(sender, "messages.ban.playerNotFound");
                return true;
            }
            String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "No Reason";
            this.banManager.banPlayer(banPlayer.getUniqueId(), reason);
            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(banPlayer.getName(), reason, null, sender.getName());
            if (banPlayer.isOnline()) ((Player) banPlayer).kickPlayer(color("&c你被伺服器BAN了!\n原因: " + reason));
            StringUtils.sendConfigMessage(sender, "messages.ban.success", ImmutableMap.of(
                    "%player%", banPlayer.getName(),
                    "%reason%", reason
            ));
            return true;
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}
