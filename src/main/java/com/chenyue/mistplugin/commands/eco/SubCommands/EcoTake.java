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
        } else {
            List<OfflinePlayer> others = MistPlugin.getPlayerByString(sender, args[1]);
            double amount = 0;
            int total = 0;
            boolean failed = false;

            if (others.isEmpty() && !args[1].equals("@a")) {
                StringUtils.sendConfigMessage(sender, "messages.money.take.otherDoesntExist", ImmutableMap.of(
                        "%player%", args[1]
                ));
                return;
            }
            try {
                amount = MistPlugin.getAmountFromString(args[2]);
            }
            catch (NumberFormatException e){
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
                if (!MistPlugin.getEco().hasAccount(other.getUniqueId())) {
                    StringUtils.sendConfigMessage(sender, "messages.money.take.otherNoAccount", ImmutableMap.of());
                    failed = true;
                    continue;
                }
                if (!MistPlugin.getEco().has(other.getUniqueId(), amount)) {
                    StringUtils.sendConfigMessage(sender, "messages.money.take.insufficientFunds", ImmutableMap.of(
                            "%player%", other.getName()
                    ));
                    failed = true;
                    continue;
                }
                MistPlugin.getEco().withdraw(other.getUniqueId(), amount);
                if (other instanceof Player) {
                    if (!(sender instanceof Player && ((Player) sender).equals((Player) other))) {
                        StringUtils.sendConfigMessage((Player) other, "messages.money.take.taken", ImmutableMap.of(
                                "%amount%", MistPlugin.format(amount)
                        ));
                    }
                }
                total += 1;
            }
            if (others.size() == 1) {
                if (!failed) {
                    StringUtils.sendConfigMessage(sender, "messages.money.take.take", ImmutableMap.of(
                            "%amount%", MistPlugin.format(amount),
                            "%player%", others.get(0).getName()
                    ));
                }
            } else {
                StringUtils.sendConfigMessage(sender, "messages.money.take.takeMultiple", ImmutableMap.of(
                        "%total%", total + "",
                        "%amount%", MistPlugin.format(amount)
                ));
            }
            return;
        }
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }
}
