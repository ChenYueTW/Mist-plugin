package com.chenyue.mistplugin.commands.eco.SubCommands;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.StringUtils;
import com.chenyue.mistplugin.utils.SubCommand;
import com.google.common.collect.ImmutableMap;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EcoSet extends SubCommand {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set money";
    }

    @Override
    public String getSyntax() {
        return "/eco set <player> <amount>";
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(args.length == 3)) {
            StringUtils.sendConfigMessage(sender, "messages.money.set.usage");
        } else {
            List<OfflinePlayer> others = MistPlugin.getPlayerByString(sender, args[1]);
            double amount = 0;
            int total = 0;
            boolean failed = false;

            if (others.isEmpty() && !args[1].equals("@a")) {
                StringUtils.sendConfigMessage(sender, "messages.money.set.otherDoesntExist", ImmutableMap.of(
                        "%player%", args[1]
                ));
                return;
            }
            try {
                amount = MistPlugin.getAmountFromString(args[2]);
            } catch (NumberFormatException e) {
                StringUtils.sendConfigMessage(sender, "messages.money.set.invalidAmount", ImmutableMap.of(
                        "%amount%", args[2]
                ));
                return;
            }
            if (amount < 0) {
                StringUtils.sendConfigMessage(sender, "messages.money.set.invalidAmount", ImmutableMap.of(
                        "%amount%", args[2]
                ));
                return;
            }
            for (OfflinePlayer other : others) {
                if (!MistPlugin.getEco().hasAccount(other.getUniqueId())) {
                    StringUtils.sendConfigMessage(sender, "messages.money.set.otherNoAccount", ImmutableMap.of());
                    failed = true;
                    continue;
                }
                MistPlugin.getEco().set(other.getUniqueId(), amount);
                if (other instanceof Player) {
                    if (!(sender instanceof Player && ((Player) sender).equals((Player) other))) {
                        StringUtils.sendConfigMessage((Player) other, "messages.money.set.set", ImmutableMap.of(
                                "%amount%", MistPlugin.format(amount)));
                    }
                }
                total += 1;
            }
            if (others.size() == 1) {
                if (!failed) {
                    OfflinePlayer other = others.get(0);
                    StringUtils.sendConfigMessage(sender, "messages.money.set.setter", ImmutableMap.of(
                            "%balance%", MistPlugin.format(amount),
                            "%player%", other.getName()
                    ));
                }
            } else {
                StringUtils.sendConfigMessage(sender, "messages.money.set.setterMultiple", ImmutableMap.of(
                        "%total%", total + "",
                        "%balance%", MistPlugin.format(amount)
                ));
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }
}
