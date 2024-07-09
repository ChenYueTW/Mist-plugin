package com.chenyue.mistplugin.commands.mist.SubCommands;

import com.chenyue.mistplugin.data.ConfigHandler;
import com.chenyue.mistplugin.utils.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        ConfigHandler.reloadConfig();
        sender.sendMessage("Reloaded Plugin!");
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }
}
