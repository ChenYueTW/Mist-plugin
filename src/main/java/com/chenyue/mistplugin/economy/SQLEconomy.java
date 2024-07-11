package com.chenyue.mistplugin.economy;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.data.ConfigHandler;
import com.chenyue.mistplugin.data.MySQL;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SQLEconomy implements Economy {
    private final MySQL sql;

    public SQLEconomy() {
        this.sql = new MySQL(
                ConfigHandler.getHost(),
                ConfigHandler.getPort(),
                ConfigHandler.getDatabase(),
                ConfigHandler.getUsername(),
                ConfigHandler.getPassword()
        );
        this.connectSQL();

        if (this.sql.isConnected()) {
            try {
                Statement statement = this.sql.getConnection().createStatement();
                DatabaseMetaData md = this.sql.getConnection().getMetaData();
                statement.execute("CREATE TABLE IF NOT EXISTS Economy (UUID VARCHAR(36) NOT NULL);");
                for (Map.Entry<String, String> column : MistPlugin.getInstance().getSqlColumns().entrySet()) {
                    if (!md.getColumns(null, null, "Economy", column.getKey()).next()) {
                        statement.execute("ALTER TABLE Economy ADD " + column.getKey() + " " + column.getValue() + ";");
                    }
                }
                statement.close();
            } catch (SQLException e) {
                MistPlugin.getInstance().disable("There was an error with creating the database table.");
                return;
            }

            try {
                PreparedStatement statement = this.sql.getConnection().prepareStatement("ALTER TABLE Economy "
                        + "MODIFY COLUMN Balance " + MistPlugin.getInstance().getSqlColumns().get("Balance"));
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                MistPlugin.getInstance().disable("There was an error updating the sql balance from 1dp to 2dp.");
                return;
            }
        }
    }

    private void connectSQL() {
        try {
            this.sql.connect();
            MistPlugin.getInstance().warn("Successfully connected to MySQL!");
        } catch (SQLException e) {
            MistPlugin.getInstance().warn("There was a problem connecting to MySQL! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(MistPlugin.getInstance());
            return;
        } catch (ClassNotFoundException e) {
            MistPlugin.getInstance().warn("MySQL driver class not found.");
            Bukkit.getPluginManager().disablePlugin(MistPlugin.getInstance());
            return;
        }
    }

    @Override
    public boolean createAccount(UUID uuid) {
        PlayerBalance playerBalance = new PlayerBalance(uuid, ConfigHandler.getStringBalance());
        try {
            PreparedStatement statement = this.sql.getConnection().prepareStatement("INSERT INTO Economy " + "(UUID, Balance) VALUES (?, ?);");
            statement.setString(1, playerBalance.getUuid().toString());
            statement.setDouble(2, playerBalance.getBalance());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            MistPlugin.getInstance().warn(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        for (PlayerBalance pb : getPlayers()) {
            if (pb.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(UUID uuid) {
        try {
            PreparedStatement statement = this.sql.getConnection().prepareStatement("DELETE FROM Economy " + "WHERE UUID=?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            MistPlugin.getInstance().warn(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean withdraw(UUID uuid, double amount) {
        return this.set(uuid, getBalance(uuid).getBalance() - amount);
    }

    @Override
    public boolean deposit(UUID uuid, double amount) {
        return this.set(uuid, getBalance(uuid).getBalance() + amount);
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) return false;
        try {
            PreparedStatement statement = this.sql.getConnection().prepareStatement("UPDATE Economy SET " + "UUID=?, Balance=? WHERE UUID=?");
            statement.setString(1, uuid.toString());
            statement.setDouble(2, amount);
            statement.setString(3, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            MistPlugin.getInstance().warn(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean has(UUID uuid, double amount) {
        return this.getBalance(uuid).getBalance() >= amount;
    }

    @Override
    public PlayerBalance getBalance(UUID uuid) {
        try {
            PreparedStatement statement = this.sql.getConnection().prepareStatement("SELECT * FROM Economy " + "WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            result.next();
            double balance = result.getDouble("Balance");
            return new PlayerBalance(uuid, balance);
        } catch (SQLException e) {
            return new PlayerBalance(uuid, 0);
        }
    }

    @Override
    public List<PlayerBalance> getPlayers() {
        try {
            List<PlayerBalance> playerData = new ArrayList<PlayerBalance>();
            Statement statement = this.sql.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Economy;");
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("UUID"));
                double balance = result.getDouble("Balance");
                playerData.add(new PlayerBalance(uuid, balance));
            }
            return playerData;
        } catch (SQLException e) {
            MistPlugin.getInstance().warn(e.getMessage());
            return null;
        }
    }
}
