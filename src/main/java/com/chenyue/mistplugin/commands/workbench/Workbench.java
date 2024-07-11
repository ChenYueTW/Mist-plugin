package com.chenyue.mistplugin.commands.workbench;

import com.chenyue.mistplugin.utils.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Workbench extends AbstractCommand {
    public static final String NAME = "Workbench";
    public static final String DESCRIPTION = "Open a workbench";
    public static final String PERMISSION = "mist.command.vip";
    public static final String USAGE = "/workbench";

    public Workbench() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            this.playerOnly(sender);
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage("Usage: " + USAGE);
            return true;
        }
        Player player = (Player) sender;
        player.openWorkbench(null, true);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
