package com.chenyue.mistplugin.Commands.ChenyuePlugin;

import com.chenyue.mistplugin.Commands.ChenyuePlugin.SubCommands.Reload;
import com.chenyue.mistplugin.utils.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class ChenyuePlugin implements TabExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public ChenyuePlugin() {
        // Add SubCommands
        this.subCommands.add(new Reload());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
                    this.getSubCommands().get(i).perform(sender, args);
                }
            }
        } else if (args.length == 0) {
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                sender.sendMessage(this.getSubCommands().get(i).getName());
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subCommandsArguments = new ArrayList<>();

            for (int i = 0; i < this.getSubCommands().size(); i++) {
                subCommandsArguments.add(this.getSubCommands().get(i).getName());
            }

            return subCommandsArguments;
        } else if (args.length >= 2) {
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
                    return this.getSubCommands().get(i).getSubCommandArguments(sender, args);
                }
            }
        }
        return null;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return this.subCommands;
    }
}
