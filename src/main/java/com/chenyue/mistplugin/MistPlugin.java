package com.chenyue.mistplugin;

import com.chenyue.mistplugin.commands.back.Back;
import com.chenyue.mistplugin.commands.balance.Balance;
import com.chenyue.mistplugin.commands.balanceTop.BalanceTop;
import com.chenyue.mistplugin.commands.delhome.DelHome;
import com.chenyue.mistplugin.commands.delspawn.DelSpawn;
import com.chenyue.mistplugin.commands.eco.Eco;
import com.chenyue.mistplugin.commands.home.Home;
import com.chenyue.mistplugin.commands.mist.Mist;
import com.chenyue.mistplugin.commands.pay.Pay;
import com.chenyue.mistplugin.commands.sethome.SetHome;
import com.chenyue.mistplugin.commands.setspawn.SetSpawn;
import com.chenyue.mistplugin.commands.spawn.Spawn;
import com.chenyue.mistplugin.commands.tpa.Tpa;
import com.chenyue.mistplugin.commands.tpaaccept.TpaAccept;
import com.chenyue.mistplugin.commands.tpacancel.TpaCancel;
import com.chenyue.mistplugin.data.*;
import com.chenyue.mistplugin.economy.Economy;
import com.chenyue.mistplugin.economy.SQLEconomy;
import com.chenyue.mistplugin.economy.VaultImpl;
import com.chenyue.mistplugin.economy.YamlEconomy;
import com.chenyue.mistplugin.events.LocationEvent;
import com.chenyue.mistplugin.events.PlayerEvent;
import com.chenyue.mistplugin.events.ShiftFEvent;
import com.chenyue.mistplugin.runnables.BalanceTopRunnable;
import com.chenyue.mistplugin.commands.tpahere.TpaHere;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

public final class MistPlugin extends JavaPlugin {
    private static MistPlugin instance;
    private static VaultImpl vaultImpl;
    private static Economy economy;
    private static BalanceTopRunnable balanceTopRunnable;
    private static Map<String, Integer> suffixes = new HashMap<>();
    private static final Map<String, String> sqlColumns = new HashMap<>();
    private static FileConfiguration spawnConfig;
    private static HomeManager homeManager;
    private static TpManager tpManager;
    private static LocationManager locationManager;

    @Override
    public void onEnable() {

        // Config
        this.saveDefaultConfig();
        this.initSpawnConfig();
        this.reloadConfig();
        this.addSqlColumns();
        instance = this;
        homeManager = new HomeManager();
        tpManager = new TpManager();
        locationManager = new LocationManager();
        suffixes = ConfigHandler.getSuffixes();
        vaultImpl = new VaultImpl();

        if (!this.setupEco()) {
            disable("Eco couldn't be register, Vault plugin is missing!");
            return;
        }
        this.getLogger().info("Vault found, Eco has been registered");
        if (ConfigHandler.getLocale() == null) {
            disable(ConfigHandler.getLocale().getDisplayName() + " is an invalid locale! Change it in your config.yml");
            return;
        }
        economy = ConfigHandler.isSQL() ? new SQLEconomy() : new YamlEconomy();

        // Settings
        balanceTopRunnable = new BalanceTopRunnable();
        balanceTopRunnable.start(ConfigHandler.getBalanceTopInterval());
        Config.load();

        // Init Cmd
        this.getCommand("eco").setExecutor(new Eco());
        this.getCommand("balance").setExecutor(new Balance());
        this.getCommand("balancetop").setExecutor(new BalanceTop());
        this.getCommand("pay").setExecutor(new Pay());
        this.getCommand("mist").setExecutor(new Mist());
        this.getCommand("spawn").setExecutor(new Spawn(this));
        this.getCommand("setspawn").setExecutor(new SetSpawn(this));
        this.getCommand("delspawn").setExecutor(new DelSpawn(this));
        this.getCommand("home").setExecutor(new Home(homeManager));
        this.getCommand("sethome").setExecutor(new SetHome(homeManager));
        this.getCommand("delhome").setExecutor(new DelHome(homeManager));
        this.getCommand("tpa").setExecutor(new Tpa(tpManager));
        this.getCommand("tpahere").setExecutor(new TpaHere(tpManager));
        this.getCommand("tpaaccept").setExecutor(new TpaAccept(tpManager));
        this.getCommand("tpacancel").setExecutor(new TpaCancel(tpManager));
        this.getCommand("back").setExecutor(new Back(locationManager));

        // Listener
        this.getServer().getPluginManager().registerEvents(new ShiftFEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
        this.getServer().getPluginManager().registerEvents(new LocationEvent(locationManager), this);

        // Enabled
        this.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        try {
            MySQL.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.getLogger().info("Plugin disabled");
    }

    private void addSqlColumns() {
        sqlColumns.put("Balance", "DECIMAL(65, 2) NOT NULL DEFAULT " + getConfig().getDouble("startingBalance"));
    }

    public static void warn(String message) {
        MistPlugin.getInstance().getLogger().warning(message);
    }

    public static void disable(String message) {
        warn(message);
        Bukkit.getPluginManager().disablePlugin(MistPlugin.getInstance());
    }

    public static MistPlugin getInstance() {
        return instance;
    }

    private void initSpawnConfig() {
        File spanwFile = new File(this.getDataFolder(), "spawn.yml");
        if (!spanwFile.exists()) {
            spanwFile.getParentFile().mkdirs();
            this.saveResource("spawn.yml", false);
        }
        spawnConfig = YamlConfiguration.loadConfiguration(spanwFile);
    }

    public static FileConfiguration getSpawnConfig() {
        return spawnConfig;
    }

    public static ArrayList<OfflinePlayer> getPlayerByString(CommandSender sender, String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        ArrayList<OfflinePlayer> players = new ArrayList<OfflinePlayer>();

        if (player == null && !name.equals("@a")) return players;
        if (name.equals("@a")) {
            players.addAll(new ArrayList<OfflinePlayer>(Bukkit.getOnlinePlayers()));
            if (sender instanceof OfflinePlayer) players.remove((OfflinePlayer) sender);
            return players;
        }
        return new ArrayList<OfflinePlayer>(Arrays.asList(player));
    }

    public static double getAmountFromString(String string) {
        int mult = 0;
        for (Map.Entry<String, Integer> suffix : suffixes.entrySet()) {
            if (string.endsWith(suffix.getKey())) {
                string = string.substring(0, string.length() - 1);
                mult = suffix.getValue();
            }
        }
        double pow = Math.pow(10, mult);
        return Math.round(Double.valueOf(string) * (100.0 * pow)) / (100.0 * pow) * pow;
    }

    public static Economy getEco() {
        return economy;
    }

    public static String getPath() {
        return MistPlugin.getInstance().getDataFolder().getAbsolutePath();
    }

    public static Map<String, String> getSqlColumns() {
        return sqlColumns;
    }

    public static String format(double amount) {
        Locale locale = ConfigHandler.getLocale();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String formatted = numberFormat.format(amount).replace("&nbsp", " ").replace("\u00A0", " ");
        if (ConfigHandler.isCustomSymbol())  formatted = formatted.replace(Currency.getInstance(locale).getSymbol(locale), ConfigHandler.getCustomSymbol());
        return formatted;
    }

    public static void setSuffixes(Map<String, Integer> suffixes) {
        MistPlugin.suffixes = suffixes;
    }

    public static BalanceTopRunnable getBalanceTopRunnable() {
        return balanceTopRunnable;
    }

    public static void setBalanceTopRunnable(BalanceTopRunnable balanceTopRunnable) {
        MistPlugin.balanceTopRunnable = balanceTopRunnable;
    }

    private boolean setupEco() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        this.getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, vaultImpl, this, ServicePriority.Highest);
        return true;
    }
}
