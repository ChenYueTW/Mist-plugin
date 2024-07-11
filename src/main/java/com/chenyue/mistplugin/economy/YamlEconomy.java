package com.chenyue.mistplugin.economy;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.data.ConfigHandler;
import com.chenyue.mistplugin.data.YamlData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class YamlEconomy implements Economy {
    public YamlEconomy() {
        Path dataDir = Paths.get(MistPlugin.getInstance().getPath() + "/data/");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                MistPlugin.getInstance().warn("There was an error creating the data directory.");
                return;
            }
        }
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) return false;

        YamlData data = new YamlData(uuid.toString() + ".yml", MistPlugin.getInstance().getPath() + "/data");
        data.getConfig().set("UUID", uuid.toString());
        data.getConfig().set("Balance", amount);
        data.saveConfig();
        return true;
    }

    @Override
    public PlayerBalance getBalance(UUID uuid) {
        try {
            YamlData data = new YamlData(uuid.toString() + ".yml", MistPlugin.getInstance().getPath() + "/data");
            double balance = data.getConfig().getDouble("Balance");
            return new PlayerBalance(uuid, balance);
        } catch (Exception e) {
            return new PlayerBalance(uuid, 0);
        }
    }

    @Override
    public boolean createAccount(UUID uuid) {
        this.set(uuid, ConfigHandler.getStringBalance());
        return true;
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        for (PlayerBalance pb : getPlayers()) {
            if (pb.getUuid().equals(uuid)) return true;
        }
        return false;
    }

    @Override
    public boolean delete(UUID uuid) {
        File isLandFile = new File(MistPlugin.getInstance().getPath() + "/data/" + uuid.toString() + ".yml");
        isLandFile.delete();
        return true;
    }

    @Override
    public boolean withdraw(UUID uuid, double amount) {
        return this.set(uuid, this.getBalance(uuid).getBalance() - amount);
    }

    @Override
    public boolean deposit(UUID uuid, double amount) {
        return this.set(uuid, this.getBalance(uuid).getBalance() + amount);
    }

    @Override
    public boolean has(UUID uuid, double amount) {
        return this.getBalance(uuid).getBalance() >= amount;
    }

    @Override
    public List<PlayerBalance> getPlayers() {
        List<PlayerBalance> playerData = new ArrayList<PlayerBalance>();
        File[] files = new File(MistPlugin.getInstance().getPath() + "/data").listFiles();
        for (File file : files) {
            playerData.add(this.getBalance(UUID.fromString(file.getName().replace(".yml", ""))));
        }
        return playerData;
    }
}
