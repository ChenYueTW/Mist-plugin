package com.chenyue.mistplugin.events;

import com.chenyue.mistplugin.data.LocationManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;
import java.util.UUID;

public class LocationEvent implements Listener {
    private final LocationManager locationManager;

    public LocationEvent(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location location = event.getFrom();
        UUID uuid = player.getUniqueId();

        try {
            this.locationManager.saveLocation(uuid, location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        UUID playerUUID = player.getUniqueId();

        try {
            locationManager.saveLocation(playerUUID, deathLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
