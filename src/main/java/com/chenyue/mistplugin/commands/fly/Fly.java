package com.chenyue.mistplugin.commands.fly;

import com.chenyue.mistplugin.utils.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Fly extends AbstractCommand {
    public static final String NAME = "fly";
    public static final String DESCRIPTION = "Allow Fly";
    public static final String PERMISSION = "mist.command.fly";
    public static final String USAGE = "/fly";

    public Fly() {
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
        if (args.length != 0) {
            sender.sendMessage("Usage: " + USAGE);
            return true;
        }
        boolean isFlying = player.getAllowFlight();
        player.setAllowFlight(!isFlying);
        player.setFlying(!isFlying);

        if (isFlying) player.sendMessage("Flight mode disabled.");
        else player.sendMessage("Flight mode enabled.");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
