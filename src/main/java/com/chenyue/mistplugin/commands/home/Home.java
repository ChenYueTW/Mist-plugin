package com.chenyue.mistplugin.commands.home;

import com.chenyue.mistplugin.managers.HomeManager;
import com.chenyue.mistplugin.events.HomeGUI;
import com.chenyue.mistplugin.utils.AbstractCommand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Home extends AbstractCommand {
    public static final String NAME = "Home";
    public static final String DESCRIPTION = "Teleport to home";
    public static final String PERMISSION = "mist.command.home";
    public static final String USAGE = "/home <name>";
    private final HomeManager homeManager;
    private final HomeGUI homeGUI;

    public Home(HomeManager homeManager, HomeGUI homeGUI) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.homeManager = homeManager;
        this.homeGUI = homeGUI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (!(sender instanceof Player player)) {
            this.playerOnly(sender);
            return true;
        }
        if (args.length != 1) {
            this.homeGUI.openHomeGUI(player);
            return true;
        }
        String homeName = args[0];
        try {
            this.homeManager.teleportHome(player, homeName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                try {
                    Map<String, Location> homes = homeManager.getHomes(player.getUniqueId());
                    list.addAll(homes.keySet());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
