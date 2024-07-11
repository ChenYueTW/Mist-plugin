package com.chenyue.mistplugin.events;

import com.chenyue.mistplugin.managers.HomeManager;
import com.chenyue.mistplugin.utils.ColorUtils;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

public class HomeGUI implements Listener, ColorUtils {
    private final HomeManager homeManager;

    public HomeGUI(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    public void openHomeGUI(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, Location> homes;
        Map<String, Material> homeBlock;

        try {
            homes = this.homeManager.getHomes(uuid);
            homeBlock = this.homeManager.getHomeBlocks(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Inventory gui = Bukkit.createInventory(null, 9, color("&l&8>>>&r&2Home&l&8<<<"));

        for (Map.Entry<String, Location> entry : homes.entrySet()) {
            String homeName = entry.getKey();
            Location location = entry.getValue();
            Material material = homeBlock != null ? homeBlock.getOrDefault(homeName, this.getDefaultMaterial(location)) : this.getDefaultMaterial(location);
            ChatColor chatColor;
            String worldName;

            switch (location.getWorld().getName()) {
                case "world":
                    chatColor = ChatColor.GREEN;
                    worldName = "主世界";
                    break;
                case "world_nether":
                    chatColor = ChatColor.RED;
                    worldName = "地獄";
                    break;
                case "world_the_end":
                    chatColor = ChatColor.GOLD;
                    worldName = "終界";
                    break;
                default:
                    chatColor = ChatColor.AQUA;
                    worldName = "未知";
                    break;
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(chatColor + homeName);
                meta.setLore(Arrays.asList(
                        color( "&e世界&b: &f" + worldName),
                        color("&9X&b: &f" + location.getX()),
                        color("&9Y&b: &f" + location.getY()),
                        color("&9Z&b: &f" + location.getZ())
                ));
                item.setItemMeta(meta);
            }

            gui.addItem(item);
        }
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(color("&l&8>>>&r&2Home&l&8<<<"))) return;
        event.setCancelled(true);

        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) return;

        Player player = (Player) event.getWhoClicked();
        String homeName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

        UUID uuid = player.getUniqueId();
        Map<String, Location> homes;
        try {
            homes = homeManager.getHomes(uuid);
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + "Failed to load homes.");
            e.printStackTrace();
            return;
        }

        if (homes.containsKey(homeName)) {
            Location loc = homes.get(homeName);
            player.teleport(loc);
            player.setFallDistance(0);
            StringUtils.sendConfigMessage(player, "messages.home.successful", ImmutableMap.of(
                    "%home%", homeName
            ));
        } else {
            StringUtils.sendConfigMessage(player, "messages.home.notFound");
        }

        player.closeInventory();
    }

    private Material getDefaultMaterial(Location location) {
        switch (location.getWorld().getName()) {
            case "world":
                return Material.GRASS_BLOCK;
            case "world_nether":
                return Material.NETHERRACK;
            case "world_the_end":
                return Material.END_STONE;
            default:
                return Material.BEDROCK;
        }
    }
}
