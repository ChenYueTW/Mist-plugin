package com.chenyue.mistplugin.Commands.Eco.SubCommands;

import com.chenyue.mistplugin.utils.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelloWorld extends SubCommand {
    @Override
    public String getName() {
        return "helloWorld";
    }

    @Override
    public String getDescription() {
        return "Hello World!";
    }

    @Override
    public String getSyntax() {
        return "/eco <helloWorld>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage("Hello World!");
    }

    @Override
    public List<String> getSubCommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
