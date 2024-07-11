package com.chenyue.mistplugin.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements TabExecutor {
    private final String name;
    private final String description;
    private final String permission;
    private final String usage;

    public AbstractCommand(final String name, final String description, final String permission, final String usage) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(this.permission) || sender.isOp();
    }

    public void noPermission(CommandSender sender) {
        StringUtils.sendConfigMessage(sender, "messages.noPermission");
    }

    public void noPermission(Player player) {
        StringUtils.sendConfigMessage(player, "messages.noPermission");
    }

    public void playerOnly(CommandSender sender) {
        StringUtils.sendConfigMessage(sender, "messages.playerOnly");
    }

    public String getUsage() {
        return this.usage;
    }
}
