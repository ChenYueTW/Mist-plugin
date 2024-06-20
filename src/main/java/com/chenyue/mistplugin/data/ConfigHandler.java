package com.chenyue.mistplugin.data;

import com.chenyue.mistplugin.MistPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfigHandler {
    private static FileConfiguration getConfig() {
        return MistPlugin.getInstance().getConfig();
    }

    public static Map<String, Integer> getSuffixes() {
        Map<String, Integer> suffixes = new HashMap<>();
        for (String suffix : getConfig().getConfigurationSection("suffixes").getKeys(false)) {
            suffixes.put(suffix, MistPlugin.getInstance().getConfig().getInt("suffixes." + suffix));
        }
        return suffixes;
    }

    public static double getStringBalance() {
        return getConfig().getDouble("stringBalance");
    }

    public static int getBalanceTopInterval() {
        return getConfig().getInt("BalanceTopTimerInterval");
    }

    public static String getCurrencyNamePlural() {
        return getConfig().getString("currencyNamePlural");
    }

    public static String getCurrencyNameSingular() {
        return getConfig().getString("currencyNameSingular");
    }

    public static Locale getLocale() {
        return Locale.forLanguageTag(getConfig().getString("locale"));
    }

    public static boolean isCustomSymbol() {
        return getConfig().getBoolean("customSymbolEnabled");
    }

    public static String getCustomSymbol() {
        return getConfig().getString("customSymbol");
    }

    public static boolean isSQL() {
        return getConfig().getBoolean("mysql.use-mysql");
    }

    public static String getHost() {
        return getConfig().getString("mysql.host");
    }

    public static int getPort() {
        return getConfig().getInt("mysql.port");
    }

    public static String getDatabase() {
        return getConfig().getString("mysql.database");
    }

    public static String getUsername() {
        return getConfig().getString("mysql.username");
    }

    public static String getPassword() {
        return getConfig().getString("mysql.password");
    }
}
