package com.chenyue.mistplugin.Commands.ChenyuePlugin.SubCommands;

import com.chenyue.mistplugin.Config;
import com.chenyue.mistplugin.utils.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Reload extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload Chenyue Plugin";
    }

    @Override
    public String getSyntax() {
        return "/chenyueplugin <reload>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Config.load();
        sender.sendMessage("Reloaded Chenyue Plugin");
    }

    @Override
    public List<String> getSubCommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
