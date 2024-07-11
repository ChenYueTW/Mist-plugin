package com.chenyue.mistplugin.api;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.economy.PlayerBalance;
import com.chenyue.mistplugin.runnables.BalanceTopRunnable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class BalTop extends PlaceholderExpansion {
    private final MistPlugin plugin;

    public BalTop(MistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mistplugin";
    }

    @Override
    public @NotNull String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        BalanceTopRunnable balanceTopRunnable = MistPlugin.getInstance().getBalanceTopRunnable();
        if (identifier.startsWith("baltop_player_")) {
            try {
                int rank = Integer.parseInt(identifier.substring("baltop_player_".length()));
                List<PlayerBalance> balancesTop = balanceTopRunnable.getBalanceTop();
                String playerName = balancesTop.get(rank - 1).getPlayerName();
                return playerName != null ? playerName : "Unknown";
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return "";
            }
        }
        if (identifier.startsWith("baltop_balance_")) {
            try {
                int rank = Integer.parseInt(identifier.substring("baltop_balance_".length()));
                List<PlayerBalance> balancesTop = balanceTopRunnable.getBalanceTop();
                double playerBalance = balancesTop.get(rank - 1).getBalance();

                // Format
                DecimalFormat formatter = new DecimalFormat("#, ###.00");
                String formattedBalance = formatter.format(playerBalance);

                return formattedBalance;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return "";
            }
        }
        return null;
    }
}
