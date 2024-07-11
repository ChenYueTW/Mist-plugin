package com.chenyue.mistplugin.commands.pay;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import com.chenyue.mistplugin.utils.SubCommand;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Pay extends AbstractCommand {
    public static final String NAME = "Pay";
    public static final String DESCRIPTION = "pay money";
    public static final String PERMISSION = "mist.command.pay";
    public static final String USAGE = "/pay <player> <amount>";
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public Pay() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (!(sender instanceof Player player)) {
            this.playerOnly(sender);
            return true;
        }
        if (args.length != 2) {
            StringUtils.sendConfigMessage(sender, "messages.money.pay.usage");
            return true;
        }
        if (!MistPlugin.getInstance().getEco().hasAccount(player.getUniqueId())) {
            StringUtils.sendConfigMessage(player, "messages.money.pay.noAccount");
            return true;
        }
        OfflinePlayer other = Bukkit.getOfflinePlayer(args[0]);
        if (other == null) {
            StringUtils.sendConfigMessage(player, "messages.money.pay.otherDoesntExist", ImmutableMap.of(
                    "%player%", args[1]
            ));
            return true;
        }
        if (!MistPlugin.getInstance().getEco().hasAccount(other.getUniqueId())) {
            StringUtils.sendConfigMessage(player, "messages.money.pay.otherNoAccount", ImmutableMap.of(
                    "%player%", Objects.requireNonNull(other.getName())
            ));
            return true;
        }
        if (other.getUniqueId().equals(player.getUniqueId())) {
            StringUtils.sendConfigMessage(player, "messages.money.pay.cannotPaySelf");
            return true;
        }
        double amount;
        try {
            amount = MistPlugin.getInstance().getAmountFromString(args[1]);
        } catch (NumberFormatException e) {
            StringUtils.sendConfigMessage(player, "messages.money.pay.invalidAmount", ImmutableMap.of(
                    "%amount%", args[1]
            ));
            return true;
        }
        if (amount <= 0) {
            StringUtils.sendConfigMessage(sender, "messages.money.pay.invalidAmount", ImmutableMap.of());
            return true;
        }
        if (!MistPlugin.getInstance().getEco().has(player.getUniqueId(), amount)) {
            StringUtils.sendConfigMessage(player, "messages.money.pay.insufficientFunds");
            return true;
        }
        MistPlugin.getInstance().getEco().withdraw(player.getUniqueId(), amount);
        StringUtils.sendConfigMessage(player, "messages.money.pay.paid", ImmutableMap.of(
                "%player%", Objects.requireNonNull(other.getName()),
                "%amount%", MistPlugin.getInstance().format(amount)
        ));
        MistPlugin.getInstance().getEco().deposit(other.getUniqueId(), amount);
        if (other instanceof Player) {
            StringUtils.sendConfigMessage((Player) other, "messages.money.pay.received", ImmutableMap.of(
                    "%player%", player.getName(),
                    "%amount%", MistPlugin.getInstance().format(amount)
            ));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
                    return this.getSubCommands().get(i).getSubCommandArguments(sender, args);
                }
            }
        } else if (args.length == 2) {
            return Arrays.asList("1", "10", "100", "1k");
        } else {
            return List.of();
        }
        return null;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return this.subCommands;
    }
}
