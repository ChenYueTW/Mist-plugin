package com.chenyue.mistplugin;

import com.chenyue.mistplugin.Commands.ChenyuePlugin.ChenyuePlugin;
import com.chenyue.mistplugin.Commands.Eco.Eco;
import com.chenyue.mistplugin.Events.PlayerEvent;
import com.chenyue.mistplugin.Events.ShiftF;
import org.bukkit.plugin.java.JavaPlugin;

public final class MistPlugin extends JavaPlugin {
    private static MistPlugin instance;

    public MistPlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Config
        this.saveDefaultConfig();
        this.reloadConfig();
        // Init Cmd
        this.getCommand("eco").setExecutor(new Eco());
        this.getCommand("chenyueplugin").setExecutor(new ChenyuePlugin());
        // Listener
        this.getServer().getPluginManager().registerEvents(new ShiftF(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
        // Log
        this.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled");
    }

    public static MistPlugin getInstance() {
        return instance;
    }
}
