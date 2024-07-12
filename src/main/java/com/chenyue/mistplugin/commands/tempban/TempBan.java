package com.chenyue.mistplugin.commands.tempban;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.managers.BanManager;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.ColorUtils;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TempBan extends AbstractCommand implements ColorUtils {
    public static final String NAME = "tempban";
    public static final String DESCRIPTION = "Temp Ban Player";
    public static final String PERMISSION = "mist.command.tempban";
    public static final String USAGE = "/tempban <time> <player> <reason>";
    private final BanManager banManager;

    public TempBan(BanManager banManager) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (args.length < 2) {
            StringUtils.sendConfigMessage(sender, "messages.tempban.usage");
            return true;
        }
        long time = this.parseTime(args[0]);
        if (time <= 0) {
            StringUtils.sendConfigMessage(sender, "messages.tempban.invalid");
            return true;
        }
        OfflinePlayer tempBanPlayer = Bukkit.getOfflinePlayer(args[1]);
        if (tempBanPlayer == null) {
            StringUtils.sendConfigMessage(sender, "messages.tempban.playerNotFound");
            return true;
        }
        String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "No Reason";
        Date expiryDate = new Date(System.currentTimeMillis() + time);
        this.banManager.tempBanPlayer(tempBanPlayer.getUniqueId(), reason, expiryDate);
        if (tempBanPlayer.isOnline()) ((Player) tempBanPlayer).kickPlayer(color("你被TEMPBAN了!\n原因: " + reason));
        StringUtils.sendConfigMessage((Player) sender, "messages.tempban.success", ImmutableMap.of(
                "%player%", Objects.requireNonNull(tempBanPlayer.getName()),
                "%reason%", reason
        ));
        return true;
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

        duration = switch (unit) {
            case 's' -> TimeUnit.SECONDS.toMillis(value);
            case 'm' -> TimeUnit.MINUTES.toMillis(value);
            case 'h' -> TimeUnit.HOURS.toMillis(value);
            case 'd' -> TimeUnit.DAYS.toMillis(value);
            default -> duration;
        };
        return duration;
    }
}
