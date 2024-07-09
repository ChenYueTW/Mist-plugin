package com.chenyue.mistplugin.data;

import com.chenyue.mistplugin.MistPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BanManager {
    private final File banFolder;
    private final Map<UUID, Long> banExpiryMap = new ConcurrentHashMap<>();

    public BanManager() {
        this.banFolder = new File(MistPlugin.getInstance().getDataFolder(), "bans");
        if (!this.banFolder.exists()) this.banFolder.mkdirs();
    }

    public void banPlayer(UUID uuid, String reason) {
        File banFile = new File(this.banFolder, uuid.toString() + ".json");
        JsonObject banData = new JsonObject();
        banData.add("reason", new JsonPrimitive(reason));
        banData.add("expiry", new JsonPrimitive(-1));
        this.writeJsonToFile(banFile, banData);
    }

    public void tempBanPlayer(UUID uuid, String reason, Date expiry) {
        File banFile = new File(this.banFolder, uuid.toString() + ".json");
        JsonObject banData = new JsonObject();
        banData.add("reason", new JsonPrimitive(reason));
        banData.add("expiry", new JsonPrimitive(expiry.getTime()));
        this.writeJsonToFile(banFile, banData);
    }

    public void unbanPlayer(UUID uuid) {
        File banFile = new File(this.banFolder, uuid.toString() + ".json");
        if (banFile.exists()) banFile.delete();
    }

    public boolean isBanned(UUID uuid) {
        File banFile = new File(this.banFolder, uuid.toString() + ".json");
        if (!banFile.exists()) return false;

        try (Reader reader = new FileReader(banFile)) {
            JsonObject banData = JsonParser.parseReader(reader).getAsJsonObject();
            long expiry = banData.get("expiry").getAsLong();
            if (expiry != -1 && new Date().getTime() > expiry) {
                this.unbanPlayer(uuid);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getBanReason(UUID uuid) {
        File banFile = new File(this.banFolder, uuid.toString() + ".json");
        if (!banFile.exists()) return null;

        try (Reader reader = new FileReader(banFile)) {
            JsonObject banData = JsonParser.parseReader(reader).getAsJsonObject();
            return banData.get("reason").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getBanExpiry(UUID uuid) {
        File banFile = new File(this.banFolder, uuid.toString() + ".json");
        if (!banFile.exists()) return null;

        try (Reader reader = new FileReader(banFile)) {
            JsonObject banData = JsonParser.parseReader(reader).getAsJsonObject();
            long expiry = banData.get("expiry").getAsLong();
            return expiry == -1 ? null : new Date(expiry);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeJsonToFile(File file, JsonObject jsonObject) {
        try (Writer writer = new FileWriter(file);
             JsonWriter jsonWriter = new JsonWriter(writer)) {
            jsonWriter.setIndent("  ");
            jsonWriter.jsonValue(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
