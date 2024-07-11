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
import java.util.Objects;

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
            return;
        }
        List<OfflinePlayer> others = MistPlugin.getInstance().getPlayerByString(sender, args[1]);
        double amount;
        int total = 0;
        boolean failed = false;

        if (others.isEmpty() && !args[1].equals("@a")) {
            StringUtils.sendConfigMessage(sender, "messages.money.set.otherDoesntExist", ImmutableMap.of(
                    "%player%", args[1]
            ));
            return;
        }
        try {
            amount = MistPlugin.getInstance().getAmountFromString(args[2]);
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
            if (!MistPlugin.getInstance().getEco().hasAccount(other.getUniqueId())) {
                StringUtils.sendConfigMessage(sender, "messages.money.set.otherNoAccount", ImmutableMap.of());
                failed = true;
                continue;
            }
            MistPlugin.getInstance().getEco().set(other.getUniqueId(), amount);
            if (other instanceof Player) {
                if (!(sender instanceof Player && sender.equals(other))) {
                    StringUtils.sendConfigMessage((Player) other, "messages.money.set.set", ImmutableMap.of(
                            "%amount%", MistPlugin.getInstance().format(amount)));
                }
            }
            total += 1;
        }
        if (others.size() == 1) {
            if (!failed) {
                OfflinePlayer other = others.getFirst();
                StringUtils.sendConfigMessage(sender, "messages.money.set.setter", ImmutableMap.of(
                        "%balance%", MistPlugin.getInstance().format(amount),
                        "%player%", Objects.requireNonNull(other.getName())
                ));
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.money.set.setterMultiple", ImmutableMap.of(
                    "%total%", total + "",
                    "%balance%", MistPlugin.getInstance().format(amount)
            ));
        }
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }
}
