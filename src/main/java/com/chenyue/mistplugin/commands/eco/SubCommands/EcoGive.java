package com.chenyue.mistplugin.commands.eco.SubCommands;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.ColorUtils;
import com.chenyue.mistplugin.utils.StringUtils;
import com.chenyue.mistplugin.utils.SubCommand;
import com.google.common.collect.ImmutableMap;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EcoGive extends SubCommand implements ColorUtils {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Give player money";
    }

    @Override
    public String getSyntax() {
        return "/eco give <player> <money>";
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(args.length == 3)) {
            StringUtils.sendConfigMessage(sender, "messages.money.give.usage");
        } else {
            List<OfflinePlayer> others = MistPlugin.getPlayerByString(sender, args[1]);
            double amount = 0;
            int total = 0;
            boolean failed = false;

            if (others.isEmpty() && !args[1].equals("@a")) {
                StringUtils.sendConfigMessage(sender, "messages.money.give.otherDoesntExist", ImmutableMap.of(
                        "%player%", args[1]
                ));
                return;
            }
            try {
                amount = MistPlugin.getAmountFromString(args[2]);
            } catch (NumberFormatException e) {
                StringUtils.sendConfigMessage(sender, "messages.money.give.invalidAmount", ImmutableMap.of(
                        "%amount%", args[2]
                ));
                return;
            }
            if (amount < 0) {
                StringUtils.sendConfigMessage(sender, "messages.money.give.invalidAmount", ImmutableMap.of(
                        "%amount%", args[2]
                ));
                return;
            }
            for (OfflinePlayer other : others) {
                if (!MistPlugin.getEco().hasAccount(other.getUniqueId())) {
                    StringUtils.sendConfigMessage(sender, "messages.money.give.otherNoAccount", ImmutableMap.of());
                    failed = true;
                    return;
                }
                MistPlugin.getEco().deposit(other.getUniqueId(), amount);

                if (other instanceof Player) {
                    if (!(sender instanceof Player && ((Player) sender).equals((Player) other))) {
                        StringUtils.sendConfigMessage((Player) other, "messages.money.give.received", ImmutableMap.of(
                                "%amount%", MistPlugin.format(amount)
                        ));
                    }
                }
                total += 1;
            }
            if (others.size() != 1) {
                if (!failed) {
                    OfflinePlayer other = others.get(0);
                    StringUtils.sendConfigMessage(sender, "messages.money.give.sent", ImmutableMap.of(
                            "%amount%", MistPlugin.format(amount),
                            "%player%", other.getName()
                    ));
                }
            } else {
                StringUtils.sendConfigMessage(sender, "messages.money.give.sentMultiple", ImmutableMap.of(
                        "%player%", others.get(0).getName(),
                        "%amount%", MistPlugin.format(amount)
                ));
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }
}
