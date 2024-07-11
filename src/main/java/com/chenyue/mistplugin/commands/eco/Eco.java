package com.chenyue.mistplugin.commands.eco;

import com.chenyue.mistplugin.commands.eco.SubCommands.*;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.ColorUtils;
import com.chenyue.mistplugin.utils.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Eco extends AbstractCommand implements ColorUtils {
    public static final String NAME = "eco";
    public static final String DESCRIPTION = "Economy";
    public static final String PERMISSION = "mist.command.eco";
    public static final String USAGE = "/eco <give|take|set>";
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public Eco() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        // Add SubCommands
        this.subCommands.add(new EcoGive());
        this.subCommands.add(new EcoSet());
        this.subCommands.add(new EcoTake());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (args.length > 0) {
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
                    this.getSubCommands().get(i).perform(sender, args);
                }
            }
        } else {
            sender.sendMessage(color("&b----------&r&4[&r&eMist Plugin&r&4]&r&b----------"));
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                sender.sendMessage(this.getSubCommands().get(i).getName() + " - " + this.getSubCommands().get(i).getDescription());
            }
            sender.sendMessage(color("&b------------------------------"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> subCommandsArguments = new ArrayList<>();
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                subCommandsArguments.add(this.getSubCommands().get(i).getName());
            }
            return subCommandsArguments;
        } else if (args.length == 2) {
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
                    return this.getSubCommands().get(i).getSubCommandArguments(sender, args);
                }
            }
        } else if (args.length == 3) {
            return Arrays.asList("1", "10", "100", "1k");
        } else {
            return List.of();
        }
        return null;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return this.subCommands;
    }
}
