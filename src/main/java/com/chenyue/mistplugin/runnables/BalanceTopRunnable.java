package com.chenyue.mistplugin.runnables;

import com.chenyue.mistplugin.economy.PlayerBalance;
import com.chenyue.mistplugin.MistPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BalanceTopRunnable extends BukkitRunnable {
    private List<PlayerBalance> balanceTop = new ArrayList<PlayerBalance>();

    @Override
    public void run() {
        List<PlayerBalance> btop = new ArrayList<PlayerBalance>(MistPlugin.getEco().getPlayers());
        btop.sort(Comparator.comparingDouble(PlayerBalance::getBalance).reversed());
        this.balanceTop = btop;
    }

    public void start(int interval) {
        this.runTaskTimer(MistPlugin.getInstance(), 1, interval);
    }

    public List<PlayerBalance> getBalanceTop() {
        return this.balanceTop;
    }
}
