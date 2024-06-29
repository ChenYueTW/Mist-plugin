package com.chenyue.mistplugin.utils;

import com.chenyue.mistplugin.MistPlugin;
import com.google.common.collect.ImmutableMap;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.N;
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

    public static void sendConfigMessage(@NotNull CommandSender sender, @NotNull String path, @NotNull ImmutableMap<String, String> placeholders, @NotNull TextComponent accept, @NotNull String acceptCmd, @NotNull TextComponent deny, @NotNull String denyCmd) {
        accept.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        deny.setColor(net.md_5.bungee.api.ChatColor.RED);
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCmd));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, denyCmd));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("點擊以同意傳送請求")));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("點擊以拒絕傳送請求")));

        for (String line : MistPlugin.getInstance().getConfig().getStringList(path)) {
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                line = line.replace(placeholder.getKey(), placeholder.getValue());
            }
            line = line.replace("%prefix%", getPrefix());
            line = color(line);
            TextComponent message = new TextComponent(line + "\n");
            message.addExtra(accept);
            message.addExtra(deny);

            sender.sendMessage(message);
        }
    }
}
