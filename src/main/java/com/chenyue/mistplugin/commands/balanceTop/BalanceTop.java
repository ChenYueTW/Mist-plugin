package com.chenyue.mistplugin.commands.balanceTop;

import com.chenyue.mistplugin.economy.PlayerBalance;
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
import java.util.List;

public class BalanceTop extends AbstractCommand {
    public static final String NAME = "Balance Top";
    public static final String DESCRIPTION = "Check Balance Top";
    public static final String PERMISSION = "mist.command.balancetop";
    public static final String USAGE = "/balancetop [player]";
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public BalanceTop() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (args.length >= 2) {
            StringUtils.sendConfigMessage(sender, "messages.money.top.usage");
            return true;
        }
        if (MistPlugin.getInstance().getBalanceTopRunnable().getBalanceTop().isEmpty()) {
            StringUtils.sendConfigMessage(sender, "messages.money.top.noAccounts");
            return true;
        }
        int top = 0;
        if (args.length == 1) {
            try {
                top = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                StringUtils.sendConfigMessage(sender, "messages.money.top.invalidTop", ImmutableMap.of(
                        "%top%", args[0]
                ));
                return true;
            }
        }
        if (top < 0) {
            StringUtils.sendConfigMessage(sender, "messages.money.top.invalidTop", ImmutableMap.of(
                    "%top%", args[0]
            ));
            return true;
        }
        List<PlayerBalance> playerBalances = MistPlugin.getInstance().getBalanceTopRunnable().getBalanceTop();
        int i = top * 10;
        int j = 0;
        StringUtils.sendConfigMessage(sender, "messages.money.top.messageHeader");
        while (i < (top + 1) * 10) {
            if (playerBalances.size() > i) {
                PlayerBalance playerBalance = playerBalances.get(i);
                OfflinePlayer player = Bukkit.getOfflinePlayer(playerBalance.getUuid());
                if (player.getName() != null) {
                    StringUtils.sendConfigMessage(sender, "messages.money.top.message", ImmutableMap.of(
                            "%rank%", i + 1 - j + "",
                            "%player%", player.getName(),
                            "%balance%", MistPlugin.getInstance().format(playerBalance.getBalance())
                    ));
                } else {
                    j++;
                }
            } else {
                if (i == top * 10) {
                    StringUtils.sendConfigMessage(sender, "messages.money.top.notEnoughPlayers");
                    return true;
                }
            }
            i++;
        }

        if (sender instanceof Player player) {
            if (MistPlugin.getInstance().getEco().hasAccount(player.getUniqueId())) {
                PlayerBalance playerBalance = null;
                int playerIndex = -1;
                for (PlayerBalance pb : playerBalances) {
                    if (pb.getUuid().equals(player.getUniqueId())) {
                        playerBalance = pb;
                        playerIndex = playerBalances.indexOf(pb);
                    }
                }
                if (playerBalance != null) {
                    if (playerIndex < top * 10 || playerIndex > (top + 1) * 10) {
                        StringUtils.sendConfigMessage(player, "messages.money.top.self", ImmutableMap.of(
                                "%rank%", playerIndex + "",
                                "%player%", player.getName(),
                                "%balance%", MistPlugin.getInstance().format(playerBalance.getBalance())
                        ));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> subCommandsArguments = new ArrayList<>();

            for (int i = 0; i < this.getSubCommands().size(); i++) {
                subCommandsArguments.add(this.getSubCommands().get(i).getName());
            }

            return subCommandsArguments;
        } else if (args.length == 2) {
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
