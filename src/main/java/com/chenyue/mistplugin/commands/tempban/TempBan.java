package com.chenyue.mistplugin.commands.tempban;

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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TempBan implements TabExecutor, ColorUtils {
    private final BanManager banManager;

    public TempBan(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.tempban") | sender.isOp()) {
            if (args.length < 2) {
                StringUtils.sendConfigMessage(sender, "messages.tempban.usage");
                return true;
            }
            long time = this.parseTime(args[0]);
            if (time <= 0) {
                StringUtils.sendConfigMessage(sender, "messages.tempban.invalid");
                return true;
            }
            OfflinePlayer tempbanPlayer = Bukkit.getOfflinePlayer(args[1]);
            if (tempbanPlayer == null) {
                StringUtils.sendConfigMessage(sender, "messages.tempban.playerNotFound");
                return true;
            }
            String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "No Reason";
            Date expiryDate = new Date(System.currentTimeMillis() + time);
            this.banManager.tempBanPlayer(tempbanPlayer.getUniqueId(), reason, expiryDate);
            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(tempbanPlayer.getName(), reason, expiryDate, sender.getName());
            if (tempbanPlayer.isOnline()) ((Player) tempbanPlayer).kickPlayer(color("你被TEMPBAN了!\n原因: " + reason));
            StringUtils.sendConfigMessage(sender, "messages.tempban.success", ImmutableMap.of(
                    "%player%", tempbanPlayer.getName(),
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
          return Arrays.asList("1s", "1m", "1h", "1d");
        } else if (args.length == 2) {
            return null;
        }
        return Collections.emptyList();
    }

    private long parseTime(String time) {
        long duration = 0;
        char unit = time.charAt(time.length() - 1);
        long value = Long.parseLong(time.substring(0, time.length() - 1));

        switch (unit) {
            case 's':
                duration = TimeUnit.SECONDS.toMillis(value);
                break;
            case 'm':
                duration = TimeUnit.MINUTES.toMillis(value);
                break;
            case 'h':
                duration = TimeUnit.HOURS.toMillis(value);
                break;
            case 'd':
                duration = TimeUnit.DAYS.toMillis(value);
                break;
        }
        return duration;
    }
}
