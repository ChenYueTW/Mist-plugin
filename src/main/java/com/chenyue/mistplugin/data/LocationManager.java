package com.chenyue.mistplugin.data;

import com.chenyue.mistplugin.MistPlugin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class LocationManager {
    private final File teleportDataDir;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public LocationManager() {
        this.teleportDataDir = new File(MistPlugin.getInstance().getDataFolder(), "teleportData");
        if (!this.teleportDataDir.exists()) this.teleportDataDir.mkdirs();
    }

    public void saveLocation(UUID playerUUID, Location location) throws IOException {
        File playerFile = new File(this.teleportDataDir, playerUUID + ".json");
        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("world", location.getWorld().getName());
        locationJson.addProperty("x", location.getX());
        locationJson.addProperty("y", location.getY());
        locationJson.addProperty("z", location.getZ());
        locationJson.addProperty("yaw", location.getYaw());
        locationJson.addProperty("pitch", location.getPitch());

        try (FileWriter writer = new FileWriter(playerFile)) {
            this.gson.toJson(locationJson, writer);
        }
    }

    public Location getLocation(UUID playerUUID) throws IOException {
        File playerFile = new File(this.teleportDataDir, playerUUID + ".json");
        if (!playerFile.exists()) return null;

        try (FileReader reader = new FileReader(playerFile)) {
            JsonObject locationJson = JsonParser.parseReader(reader).getAsJsonObject();
            return new Location(
                    Bukkit.getWorld(locationJson.get("world").getAsString()),
                    locationJson.get("x").getAsDouble(),
                    locationJson.get("y").getAsDouble(),
                    locationJson.get("z").getAsDouble(),
                    locationJson.get("yaw").getAsFloat(),
                    locationJson.get("pitch").getAsFloat()
            );
        }
    }
}
