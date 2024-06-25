package com.chenyue.mistplugin.commands.balance;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.StringUtils;
import com.chenyue.mistplugin.utils.SubCommand;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Balance implements TabExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.balance")) {
            if (args.length == 0) {

                if (!(sender instanceof Player)) {
                    StringUtils.sendConfigMessage(sender, "messages.playersOnly");
                    return true;
                }

                Player player = (Player) sender;

                if (!MistPlugin.getEco().hasAccount(player.getUniqueId())) {
                    StringUtils.sendConfigMessage(player, "messages.balance.noAccount");
                    return true;
                }

                double balance = MistPlugin.getEco().getBalance(player.getUniqueId()).getBalance();
                StringUtils.sendConfigMessage(player, "messages.balance.balance", ImmutableMap.of(
                        "%balance%", MistPlugin.format(balance) + ""
                ));

                return true;

            } else if (args.length == 1) {
                @NotNull
                OfflinePlayer other = Bukkit.getOfflinePlayer(args[0]);

                if (!MistPlugin.getEco().hasAccount(other.getUniqueId())) {
                    StringUtils.sendConfigMessage(sender, "messages.balance.otherNoAccount", ImmutableMap.of(
                            "%player%", other.getName()
                    ));
                    return true;
                }

                double balance = MistPlugin.getEco().getBalance(other.getUniqueId()).getBalance();
                StringUtils.sendConfigMessage(sender, "messages.balance.otherBalance", ImmutableMap.of(
                        "%player%", other.getName(),
                        "%balance%", MistPlugin.format(balance) + ""
                ));
                return true;
            } else {
                StringUtils.sendConfigMessage(sender, "messages.balance.usage");
                return true;
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
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
