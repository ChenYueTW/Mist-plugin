package com.chenyue.mistplugin;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private static FileConfiguration config;
    private static String welcomeMSG;
    private static String shiftFCmd;

    public static void load() {
        MistPlugin.getInstance().saveDefaultConfig();
        MistPlugin.getInstance().reloadConfig();

        welcomeMSG = MistPlugin.getInstance().getConfig().getString("Welcome_MSG");
        shiftFCmd = MistPlugin.getInstance().getConfig().getString("ShiftF_CMD");
    }

    public static String getWelcomeMSG() {
        return welcomeMSG;
    }

    public static String getShiftFCmd() {
        return shiftFCmd;
    }
}
