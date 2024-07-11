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

public class EcoTake extends SubCommand {
    @Override
    public String getName() {
        return "take";
    }

    @Override
    public String getDescription() {
        return "Take money";
    }

    @Override
    public String getSyntax() {
        return "/eco take <player> <amount>";
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(args.length == 3)) {
            StringUtils.sendConfigMessage(sender, "messages.money.take.usage");
            return;
        }
        List<OfflinePlayer> others = MistPlugin.getInstance().getPlayerByString(sender, args[1]);
        double amount;
        int total = 0;
        boolean failed = false;

        if (others.isEmpty() && !args[1].equals("@a")) {
            StringUtils.sendConfigMessage(sender, "messages.money.take.otherDoesntExist", ImmutableMap.of(
                    "%player%", args[1]
            ));
            return;
        }
        try {
            amount = MistPlugin.getInstance().getAmountFromString(args[2]);
        } catch (NumberFormatException e) {
            StringUtils.sendConfigMessage(sender, "messages.money.take.invalidAmount", ImmutableMap.of(
                    "%amount%", args[2]
            ));
            return;
        }
        if (amount < 0) {
            StringUtils.sendConfigMessage(sender, "messages.money.take.invalidAmount", ImmutableMap.of(
                    "%amount%", args[2]
            ));
            return;
        }
        for (OfflinePlayer other : others) {
            if (!MistPlugin.getInstance().getEco().hasAccount(other.getUniqueId())) {
                StringUtils.sendConfigMessage(sender, "messages.money.take.otherNoAccount", ImmutableMap.of());
                failed = true;
                continue;
            }
            if (!MistPlugin.getInstance().getEco().has(other.getUniqueId(), amount)) {
                StringUtils.sendConfigMessage(sender, "messages.money.take.insufficientFunds", ImmutableMap.of(
                        "%player%", Objects.requireNonNull(other.getName())
                ));
                failed = true;
                continue;
            }
            MistPlugin.getInstance().getEco().withdraw(other.getUniqueId(), amount);
            if (other instanceof Player) {
                if (!(sender instanceof Player && sender.equals(other))) {
                    StringUtils.sendConfigMessage((Player) other, "messages.money.take.taken", ImmutableMap.of(
                            "%amount%", MistPlugin.getInstance().format(amount)
                    ));
                }
            }
            total += 1;
        }
        if (others.size() == 1) {
            if (!failed) {
                StringUtils.sendConfigMessage(sender, "messages.money.take.take", ImmutableMap.of(
                        "%amount%", MistPlugin.getInstance().format(amount),
                        "%player%", Objects.requireNonNull(others.getFirst().getName())
                ));
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.money.take.takeMultiple", ImmutableMap.of(
                    "%total%", total + "",
                    "%amount%", MistPlugin.getInstance().format(amount)
            ));
        }
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }
}
