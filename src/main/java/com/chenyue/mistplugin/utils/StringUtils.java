package com.chenyue.mistplugin.utils;

import com.chenyue.mistplugin.MistPlugin;
import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class StringUtils {
    private static String getPrefix() {
        return color(MistPlugin.getInstance().getConfig().getString("messages.prefix"));
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull List<String> message) {
        for (String line : message) {
            line = line.replace("%prefix%", getPrefix());
            sender.sendMessage(color(line));
        }
    }

    public static void sendMessage(@NotNull Player player, @NotNull List<String> message) {
        for (String line : message) {
            line = line.replace("%prefix%", getPrefix());
            player.sendMessage(color(line));
        }
    }

    public static void sendMessage(@NotNull Player player, @NotNull List<String> message, @NotNull ImmutableMap<String, String> placeholders) {
        for (String line : message) {
            line = line.replace("%prefix%", getPrefix());
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                line = line.replace(placeholder.getKey(), placeholder.getValue());
            }
            player.sendMessage(color(line));
        }
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull List<String> message, @NotNull ImmutableMap<String, String> placeholders) {
        for (String line : message) {
            line = line.replace("%prefix%", getPrefix());
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                line = line.replace(placeholder.getKey(), placeholder.getValue());
            }
            sender.sendMessage(color(line));
        }
    }

    public static void sendConfigMessage(@NotNull CommandSender sender, @NotNull String path) {
        for (String line : MistPlugin.getInstance().getConfig().getStringList(path)) {
            line = line.replace("%prefix%", getPrefix());
            sender.sendMessage(color(line));
        }
    }

    public static void sendConfigMessage(@NotNull Player player, @NotNull String path) {
        for (String line : MistPlugin.getInstance().getConfig().getStringList(path)) {
            line = line.replace("%prefix%", getPrefix());
            player.sendMessage(color(line));
        }
    }

    public static void sendConfigMessage(@NotNull Player player, @NotNull String path, @NotNull ImmutableMap<String, String> placeholders) {
        for (String line : MistPlugin.getInstance().getConfig().getStringList(path)) {
            line = line.replace("%prefix%", getPrefix());
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                line = line.replace(placeholder.getKey(), placeholder.getValue());
            }
            player.sendMessage(color(line));
        }
    }

    public static void sendConfigMessage(@NotNull CommandSender sender, @NotNull String path, @NotNull ImmutableMap<String, String> placeholders) {
        for (String line : MistPlugin.getInstance().getConfig().getStringList(path)) {
            line = line.replace("%prefix%", getPrefix());
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                line = line.replace(placeholder.getKey(), placeholder.getValue());
            }
            sender = sender instanceof Player ? (Player) sender : sender;
            sender.sendMessage(color(line));
        }
    }
}
