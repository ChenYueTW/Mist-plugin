package com.chenyue.mistplugin.commands.flyspeed;

import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlySpeed extends AbstractCommand {
    public static final String NAME = "flyspeed";
    public static final String DESCRIPTION = "Change fly speed";
    public static final String PERMISSION = "mist.command.flyspeed";
    public static final String USAGE = "/flyspeed <speed>";

    public FlySpeed() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
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
            player.sendMessage("Usage: " + USAGE);
        }
        try {
            float speed = Float.parseFloat(args[0]);

            if (speed <= 0) {
                StringUtils.sendConfigMessage(player, "messages.flyspeed.cantZero");
                return true;
            }

            player.setFlySpeed(speed);
            StringUtils.sendConfigMessage(player, "messages.flyspeed.success", ImmutableMap.of(
                    "%speed%", String.valueOf(speed)
            ));
            return true;
        } catch (NumberFormatException e) {
            StringUtils.sendConfigMessage(player, "messages.flyspeed.failed");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
