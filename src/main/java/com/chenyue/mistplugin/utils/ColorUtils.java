package com.chenyue.mistplugin.utils;

import org.bukkit.ChatColor;

public interface ColorUtils {
    default String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
