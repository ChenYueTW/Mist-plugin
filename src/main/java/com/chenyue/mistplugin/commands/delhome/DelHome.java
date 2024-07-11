package com.chenyue.mistplugin.commands.delhome;

import com.chenyue.mistplugin.managers.HomeManager;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
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

public class DelHome extends AbstractCommand {
    public static final String NAME = "DelHome";
    public static final String DESCRIPTION = "Delete a home";
    public static final String PERMISSION = "mist.command.delhome";
    public static final String USAGE = "/delhome <name>";
    private final HomeManager homeManager;

    public DelHome(HomeManager homeManager) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.homeManager = homeManager;
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
            StringUtils.sendConfigMessage(sender, "messages.delhome.usage");
            return true;
        }
        String homeName = args[0];
        try {
            this.homeManager.delHome(player, homeName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            StringUtils.sendConfigMessage(player, "messages.delhome.failed");
            return true;
        }


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
