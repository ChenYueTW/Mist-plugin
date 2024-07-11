package com.chenyue.mistplugin.economy;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.data.ConfigHandler;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class VaultImpl implements Economy {
    @Override
    public boolean isEnabled() {
        return MistPlugin.getInstance() != null;
    }

    @Override
    public String currencyNamePlural() {
        return ConfigHandler.getCurrencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return ConfigHandler.getCurrencyNameSingular();
    }

    @Override
    public String format(double v) {
        BigDecimal bd = new BigDecimal(v).setScale(2, RoundingMode.HALF_EVEN);
        return String.valueOf(bd.doubleValue());

    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public boolean createPlayerAccount(String name) {
        return this.createAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return this.createAccount(player.getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(String name, String world) {
        return this.createAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        return this.createAccount(player.getUniqueId());
    }

    private boolean createAccount(UUID uuid) {
        return MistPlugin.getInstance().getEco().createAccount(uuid);
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        return this.deposit(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return this.deposit(player.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return this.deposit(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return this.deposit(player.getUniqueId(), amount);
    }

    private EconomyResponse deposit(UUID uuid, double amount) {
        if (!MistPlugin.getInstance().getEco().deposit(uuid, amount)) {
            return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "Failed to deposit funds.");
        }
        return new EconomyResponse(amount, this.getBalance(uuid), ResponseType.SUCCESS, "");
    }

    @Override
    public double getBalance(String name) {
        return this.getBalance(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return this.getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(String name, String world) {
        return this.getBalance(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return this.getBalance(player.getUniqueId());
    }

    private double getBalance(UUID uuid) {
        return MistPlugin.getInstance().getEco().getBalance(uuid).getBalance();
    }

    @Override
    public String getName() {
        return "Economy";
    }

    @Override
    public boolean has(String name, double amount) {
        return this.has(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return this.has(player.getUniqueId(), amount);
    }

    @Override
    public boolean has(String name, String world, double amount) {
        return this.has(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String world, double amount) {
        return this.has(player.getUniqueId(), amount);
    }

    private boolean has(UUID uuid, double amount) {
        return MistPlugin.getInstance().getEco().has(uuid, amount);
    }

    @Override
    public boolean hasAccount(String name) {
        return this.hasAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return this.hasAccount(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String name, String world) {
        return this.hasAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String world) {
        return this.hasAccount(player.getUniqueId());
    }

    private boolean hasAccount(UUID uuid) {
        return MistPlugin.getInstance().getEco().hasAccount(uuid);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        return this.withdraw(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return this.withdraw(player.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return this.withdraw(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return this.withdraw(player.getUniqueId(), amount);
    }

    private EconomyResponse withdraw(UUID uuid, double amount) {
        if (!MistPlugin.getInstance().getEco().withdraw(uuid, amount)) {
            return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "Failed to withdraw funds.");
        }
        return new EconomyResponse(amount, this.getBalance(uuid), ResponseType.SUCCESS, "");
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String arg0) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {
        return null;
    }
}
