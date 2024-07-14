package com.chenyue.mistplugin.commands.enderchest;

import com.chenyue.mistplugin.utils.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderChest extends AbstractCommand {
    public static final String NAME = "enderchest";
    public static final String DESCRIPTION = "Open a ender chest";
    public static final String PERMISSION = "mist.command.vip";
    public static final String USAGE = "/enderchest";

    public EnderChest() {
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
        Inventory inventory = player.getEnderChest();
        player.openInventory(inventory);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
