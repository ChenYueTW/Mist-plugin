package com.chenyue.mistplugin.data;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HomeManager {
    private final File playerHomeDir = new File(MistPlugin.getInstance().getDataFolder(), "playerHomes");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public HomeManager() {
        if (!playerHomeDir.exists()) playerHomeDir.mkdirs();
    }

    public void teleportHome(Player player, String homeName) throws IOException {
        File homeFile = new File(playerHomeDir, player.getUniqueId() + ".json");
        JsonObject homes;

        // 檢查有沒有玩家UUID的JSON
        homes = homeFile.exists() ? JsonParser.parseReader(new FileReader(homeFile)).getAsJsonObject() : new JsonObject();

        if (homes.has(homeName)) {
            JsonObject home = homes.getAsJsonObject(homeName);
            double x = home.getAsJsonPrimitive("x").getAsDouble();
            double y = home.getAsJsonPrimitive("y").getAsDouble();
            double z = home.getAsJsonPrimitive("z").getAsDouble();
            float yaw = home.getAsJsonPrimitive("yaw").getAsFloat();
            float pitch = home.getAsJsonPrimitive("pitch").getAsFloat();
            String world = home.getAsJsonPrimitive("world").getAsString();
            Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

            player.teleport(location);
            player.setFallDistance(0);
            StringUtils.sendConfigMessage(player, "messages.home.successful", ImmutableMap.of(
                    "%home%", homeName
            ));
        } else {
            StringUtils.sendConfigMessage(player, "messages.home.notFound");
        }
    }

    public void saveHome(Player player, Location location, String name) throws IOException {
        File homeFile = new File(playerHomeDir, player.getUniqueId() + ".json");
        JsonObject homes;

        // 檢查有沒有玩家UUID的JSON
        homes = homeFile.exists() ? JsonParser.parseReader(new FileReader(homeFile)).getAsJsonObject() : new JsonObject();

        JsonObject homeData = new JsonObject();
        homeData.addProperty("x", location.getX());
        homeData.addProperty("y", location.getY());
        homeData.addProperty("z", location.getZ());
        homeData.addProperty("yaw", location.getYaw());
        homeData.addProperty("pitch", location.getPitch());
        homeData.addProperty("world", location.getWorld().getName());

        homes.add(name, homeData);

        try (FileWriter writer = new FileWriter(homeFile)) {
            this.gson.toJson(homes, writer);
        }
    }

    public void delHome(Player player, String name) throws IOException {
        File homeFile = new File(playerHomeDir, player.getUniqueId() + ".json");
        JsonObject homes;

        // 檢查有沒有玩家UUID的JSON
        homes = homeFile.exists() ? JsonParser.parseReader(new FileReader(homeFile)).getAsJsonObject() : new JsonObject();

        if (homes.has(name)) {
            homes.remove(name);
            try (FileWriter writer = new FileWriter(homeFile)) {
                this.gson.toJson(homes, writer);
                StringUtils.sendConfigMessage(player, "messages.delhome.successful", ImmutableMap.of(
                        "%home%", name
                ));
            } catch (IOException e) {
                e.printStackTrace();
                StringUtils.sendConfigMessage(player, "messages.delhome.failed");
            }
        } else {
            StringUtils.sendConfigMessage(player, "messages.delhome.notFound");
        }
    }

    public Map<String, Location> getHomes(UUID uuid) throws IOException {
        File homeFile = new File(playerHomeDir, uuid + ".json");
        Map<String, Location> homesMap = new HashMap<>();

        if (homeFile.exists()) {
            JsonObject homes = JsonParser.parseReader(new FileReader(homeFile)).getAsJsonObject();

            for (Map.Entry<String, ?> entry : homes.entrySet()) {
                JsonObject homeData = (JsonObject) entry.getValue();
                Location location = new Location(
                        Bukkit.getServer().getWorld(homeData.get("world").getAsString()),
                        homeData.get("x").getAsDouble(),
                        homeData.get("y").getAsDouble(),
                        homeData.get("z").getAsDouble(),
                        homeData.get("yaw").getAsFloat(),
                        homeData.get("pitch").getAsFloat()
                );
                homesMap.put(entry.getKey(), location);
            }
        }
        return homesMap;
    }

    public int getHomeCount(UUID uuid) throws IOException {
        return this.getHomes(uuid).size();
    }

    public Map<String, Material> getHomeBlocks(UUID uuid) throws IOException {
        File homeFile = new File(playerHomeDir, uuid + ".json");
        Map<String, Material> homeBlocks = new HashMap<>();

        if (homeFile.exists()) {
            JsonObject homes = JsonParser.parseReader(new FileReader(homeFile)).getAsJsonObject();

            for (Map.Entry<String, ?> entry : homes.entrySet()) {
                JsonObject homeData = (JsonObject) entry.getValue();
                if (homeData.has("blockID")) {
                    homeBlocks.put(entry.getKey(), Material.getMaterial(homeData.get("blockID").getAsString()));
                }
            }
        }
        return homeBlocks;
    }

    public void setHomeBlock(UUID uuid, String homeName, Material blockID) throws IOException {
        File homeFile = new File(playerHomeDir, uuid + ".json");
        JsonObject homes;

        homes = homeFile.exists() ? JsonParser.parseReader(new FileReader(homeFile)).getAsJsonObject() : new JsonObject();

        JsonObject homeData = homes.has(homeName) ? homes.getAsJsonObject(homeName) : new JsonObject();
        homeData.addProperty("blockID", blockID.name());
        homes.add(homeName, homeData);

        try (FileWriter writer = new FileWriter(homeFile)) {
//            writer.write(homes.toString());
            this.gson.toJson(homes, writer);
        }
    }
}
