package com.chenyue.mistplugin;

import com.chenyue.mistplugin.api.BalTop;
import com.chenyue.mistplugin.commands.back.Back;
import com.chenyue.mistplugin.commands.balance.Balance;
import com.chenyue.mistplugin.commands.balanceTop.BalanceTop;
import com.chenyue.mistplugin.commands.ban.Ban;
import com.chenyue.mistplugin.commands.delhome.DelHome;
import com.chenyue.mistplugin.commands.delspawn.DelSpawn;
import com.chenyue.mistplugin.commands.delwarp.DelWarp;
import com.chenyue.mistplugin.commands.eco.Eco;
import com.chenyue.mistplugin.commands.hat.Hat;
import com.chenyue.mistplugin.commands.home.Home;
import com.chenyue.mistplugin.commands.homevip.HomeVIP;
import com.chenyue.mistplugin.commands.mist.Mist;
import com.chenyue.mistplugin.commands.pay.Pay;
import com.chenyue.mistplugin.commands.sethome.SetHome;
import com.chenyue.mistplugin.commands.setspawn.SetSpawn;
import com.chenyue.mistplugin.commands.setwarp.SetWarp;
import com.chenyue.mistplugin.commands.spawn.Spawn;
import com.chenyue.mistplugin.commands.suicide.Suicide;
import com.chenyue.mistplugin.commands.tempban.TempBan;
import com.chenyue.mistplugin.commands.tpa.Tpa;
import com.chenyue.mistplugin.commands.tpaaccept.TpaAccept;
import com.chenyue.mistplugin.commands.tpacancel.TpaCancel;
import com.chenyue.mistplugin.commands.tpahereaccept.TpaHereAccept;
import com.chenyue.mistplugin.commands.tpaherecancel.TpaHereCancel;
import com.chenyue.mistplugin.commands.unban.UnBan;
import com.chenyue.mistplugin.commands.warp.Warp;
import com.chenyue.mistplugin.commands.workbench.Workbench;
import com.chenyue.mistplugin.data.*;
import com.chenyue.mistplugin.economy.Economy;
import com.chenyue.mistplugin.economy.SQLEconomy;
import com.chenyue.mistplugin.economy.VaultImpl;
import com.chenyue.mistplugin.economy.YamlEconomy;
import com.chenyue.mistplugin.events.*;
import com.chenyue.mistplugin.managers.*;
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
    private final VaultImpl vaultImpl;
    private final BalanceTopRunnable balanceTopRunnable;
    private final Map<String, Integer> suffixes;
    private final Map<String, String> sqlColumns;
    private final HomeManager homeManager;
    private final TpManager tpManager;
    private final LocationManager locationManager;
    private final BanManager banManager;
    private final WarpManager warpManager;
    private Economy economy;
    private FileConfiguration spawnConfig;

    public MistPlugin() {
        instance = this;
        this.homeManager = new HomeManager();
        this.tpManager = new TpManager();
        this.locationManager = new LocationManager();
        this.banManager = new BanManager();
        this.warpManager = new WarpManager();
        this.balanceTopRunnable = new BalanceTopRunnable();
        this.sqlColumns = new HashMap<>();
        this.suffixes = ConfigHandler.getSuffixes();
        this.vaultImpl = new VaultImpl();
    }

    @Override
    public void onEnable() {
        // Config
        this.saveDefaultConfig();
        this.initSpawnConfig();
        this.reloadConfig();
        this.addSqlColumns();

        // Plugin Setup
        this.setupEco();

        if (ConfigHandler.getLocale() == null) {
            this.disable(ConfigHandler.getLocale().getDisplayName() + " is an invalid locale! Change it in your config.yml");
            return;
        }

        // Settings
        this.balanceTopRunnable.start(ConfigHandler.getBalanceTopInterval());
        ConfigHandler.reloadConfig();

        // Init Cmd
        Objects.requireNonNull(this.getCommand("eco")).setExecutor(new Eco());
        Objects.requireNonNull(this.getCommand("balance")).setExecutor(new Balance());
        Objects.requireNonNull(this.getCommand("balancetop")).setExecutor(new BalanceTop());
        Objects.requireNonNull(this.getCommand("pay")).setExecutor(new Pay());
        Objects.requireNonNull(this.getCommand("mist")).setExecutor(new Mist());
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new Spawn());
        Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(new SetSpawn());
        Objects.requireNonNull(this.getCommand("delspawn")).setExecutor(new DelSpawn());
        Objects.requireNonNull(this.getCommand("home")).setExecutor(new Home(this.homeManager, new HomeGUI(this.homeManager)));
        Objects.requireNonNull(this.getCommand("sethome")).setExecutor(new SetHome(this.homeManager));
        Objects.requireNonNull(this.getCommand("delhome")).setExecutor(new DelHome(this.homeManager));
        Objects.requireNonNull(this.getCommand("homevip")).setExecutor(new HomeVIP(this.homeManager));
        Objects.requireNonNull(this.getCommand("tpa")).setExecutor(new Tpa(this.tpManager));
        Objects.requireNonNull(this.getCommand("tpahere")).setExecutor(new TpaHere(this.tpManager));
        Objects.requireNonNull(this.getCommand("tpaaccept")).setExecutor(new TpaAccept(this.tpManager));
        Objects.requireNonNull(this.getCommand("tpacancel")).setExecutor(new TpaCancel(this.tpManager));
        Objects.requireNonNull(this.getCommand("tpahereaccept")).setExecutor(new TpaHereAccept(this.tpManager));
        Objects.requireNonNull(this.getCommand("tpaherecancel")).setExecutor(new TpaHereCancel(this.tpManager));
        Objects.requireNonNull(this.getCommand("back")).setExecutor(new Back(this.locationManager));
        Objects.requireNonNull(this.getCommand("warp")).setExecutor(new Warp(this.warpManager, new WarpGUI(this.warpManager)));
        Objects.requireNonNull(this.getCommand("setwarp")).setExecutor(new SetWarp(this.warpManager));
        Objects.requireNonNull(this.getCommand("delwarp")).setExecutor(new DelWarp(this.warpManager));
        Objects.requireNonNull(this.getCommand("ban")).setExecutor(new Ban(this.banManager));
        Objects.requireNonNull(this.getCommand("tempban")).setExecutor(new TempBan(this.banManager));
        Objects.requireNonNull(this.getCommand("unban")).setExecutor(new UnBan(this.banManager));
        Objects.requireNonNull(this.getCommand("hat")).setExecutor(new Hat());
        Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new Suicide());
        Objects.requireNonNull(this.getCommand("workbench")).setExecutor(new Workbench());

        // Listener
        this.getServer().getPluginManager().registerEvents(new ShiftFEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
        this.getServer().getPluginManager().registerEvents(new LocationEvent(this.locationManager), this);
        this.getServer().getPluginManager().registerEvents(new HomeGUI(this.homeManager), this);
        this.getServer().getPluginManager().registerEvents(new WarpGUI(this.warpManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLogin(this.banManager), this);

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

    public void warn(String message) {
        MistPlugin.getInstance().getLogger().warning(message);
    }

    public void disable(String message) {
        this.warn(message);
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public String getPath() {
        return this.getDataFolder().getAbsolutePath();
    }

    public static MistPlugin getInstance() {
        return instance;
    }

    // Spawn
    private void initSpawnConfig() {
        File spanwFile = new File(this.getDataFolder(), "spawn.yml");
        if (!spanwFile.exists()) {
            spanwFile.getParentFile().mkdirs();
            this.saveResource("spawn.yml", false);
        }
        this.spawnConfig = YamlConfiguration.loadConfiguration(spanwFile);
    }
    public FileConfiguration getSpawnConfig() {
        return this.spawnConfig;
    }

    // Economy
    public Economy getEco() {
        return this.economy;
    }
    private void setupEco() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            this.getLogger().warning("Vault not found!");
            return;
        } else {
            this.getLogger().warning("Vault found!");
        }
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getLogger().warning("PlaceholderAPI not found!");
            return;
        } else {
            this.getLogger().warning("PlaceholderAPI found!");
        }
        this.getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, this.vaultImpl, this, ServicePriority.Highest);
        this.economy = ConfigHandler.isSQL() ? new SQLEconomy() : new YamlEconomy();
        this.getLogger().info("Eco has been registered");
    }
    public String format(double amount) {
        Locale locale = ConfigHandler.getLocale();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String formatted = numberFormat.format(amount).replace("&nbsp", " ").replace("\u00A0", " ");
        if (ConfigHandler.isCustomSymbol())  formatted = formatted.replace(Currency.getInstance(locale).getSymbol(locale), ConfigHandler.getCustomSymbol());
        return formatted;
    }
    public ArrayList<OfflinePlayer> getPlayerByString(CommandSender sender, String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);

        if (name.equals("@a")) {
            ArrayList<OfflinePlayer> players = new ArrayList<OfflinePlayer>(new ArrayList<OfflinePlayer>(Bukkit.getOnlinePlayers()));
            if (sender instanceof OfflinePlayer) players.remove((OfflinePlayer) sender);
            return players;
        }
        return new ArrayList<OfflinePlayer>(List.of(player));
    }
    public double getAmountFromString(String string) {
        int mult = 0;
        for (Map.Entry<String, Integer> suffix : this.suffixes.entrySet()) {
            if (string.endsWith(suffix.getKey())) {
                string = string.substring(0, string.length() - 1);
                mult = suffix.getValue();
            }
        }
        double pow = Math.pow(10, mult);
        return Math.round(Double.parseDouble(string) * (100.0 * pow)) / (100.0 * pow) * pow;
    }
    public BalanceTopRunnable getBalanceTopRunnable() {
        return this.balanceTopRunnable;
    }

    // SQL
    private void addSqlColumns() {
        this.sqlColumns.put("Balance", "DECIMAL(65, 2) NOT NULL DEFAULT " + getConfig().getDouble("startingBalance"));
    }
    public Map<String, String> getSqlColumns() {
        return this.sqlColumns;
    }
}
