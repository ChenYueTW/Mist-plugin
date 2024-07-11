package com.chenyue.mistplugin.managers;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarpManager {
    private final File warpDir = new File(MistPlugin.getInstance().getDataFolder(), "warps");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public WarpManager() {
        if (!this.warpDir.exists()) this.warpDir.mkdirs();
    }

    public void saveWarp(String name, Location location) throws IOException {
        File file = new File(this.warpDir, name + ".json");
        JsonObject json = new JsonObject();
        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("world", location.getWorld().getName());
        locationJson.addProperty("x", location.getX());
        locationJson.addProperty("y", location.getY());
        locationJson.addProperty("z", location.getZ());
        locationJson.addProperty("yaw", location.getYaw());
        locationJson.addProperty("pitch", location.getPitch());
        json.add("location", locationJson);

        try (FileWriter writer = new FileWriter(file)) {
            this.gson.toJson(json, writer);
        }
    }

    public Location getWarp(String name) {
        File file = new File(this.warpDir, name + ".json");
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject locationJson = json.getAsJsonObject("location");
            return new Location(
                    Bukkit.getWorld(locationJson.get("world").getAsString()),
                    locationJson.get("x").getAsDouble(),
                    locationJson.get("y").getAsDouble(),
                    locationJson.get("z").getAsDouble(),
                    locationJson.get("yaw").getAsFloat(),
                    locationJson.get("pitch").getAsFloat()
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteWarp(Player player, String name) {
        File file = new File(this.warpDir, name + ".json");
        if (file.exists()) {
            file.delete();
            StringUtils.sendConfigMessage(player, "messages.delwarp.successful", ImmutableMap.of(
                    "%name%", name
            ));
        } else {
            StringUtils.sendConfigMessage(player, "messages.delwarp.notFound");
        }
    }

    public List<String> getWarpNames() {
        File[] files = this.warpDir.listFiles();
        List<String> warpNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    warpNames.add(file.getName().replace(".json", ""));
                }
            }
        }
        return warpNames;
    }
}
