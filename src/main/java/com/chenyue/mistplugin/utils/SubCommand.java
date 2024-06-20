package com.chenyue.mistplugin.utils;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract void perform(@NotNull CommandSender sender, @NotNull String[] args);
    public abstract List<String> getSubCommandArguments(@NotNull CommandSender sender, @NotNull String[] args);
}
