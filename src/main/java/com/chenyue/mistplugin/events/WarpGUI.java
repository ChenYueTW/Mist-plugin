package com.chenyue.mistplugin.events;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.managers.WarpManager;
import com.chenyue.mistplugin.utils.ColorUtils;
import com.chenyue.mistplugin.utils.StringUtils;
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

import java.io.File;
import java.util.Arrays;

public class WarpGUI implements Listener, ColorUtils {
    private final WarpManager warpManager;

    public WarpGUI(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    public void openWarpGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, color("&l&8>>>&r&2Warp&l&8<<<"));

        File warpDir = new File(MistPlugin.getInstance().getDataFolder(), "warps");
        if (warpDir.exists() && warpDir.isDirectory()) {
            File[] files = warpDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    String warpName = file.getName().replace(".json", "");
                    Location location = this.warpManager.getWarp(warpName);
                    Material material;
                    ChatColor chatColor;
                    String worldName;

                    if (location != null) {
                        switch (location.getWorld().getName()) {
                            case "world":
                                material = Material.GRASS_BLOCK;
                                chatColor = ChatColor.GREEN;
                                worldName = "主世界";
                                break;
                            case "world_nether":
                                material = Material.NETHERRACK;
                                chatColor = ChatColor.RED;
                                worldName = "地獄";
                                break;
                            case "world_the_end":
                                material = Material.END_STONE;
                                chatColor = ChatColor.GOLD;
                                worldName = "終界";
                                break;
                            default:
                                material = Material.BEDROCK;
                                chatColor = ChatColor.AQUA;
                                worldName = "未知";
                                break;
                        }
                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(StringUtils.color(chatColor + warpName));
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
                }
            }
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(color("&l&8>>>&r&2Warp&l&8<<<"))) {
            event.setCancelled(true);
            if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) return;

            if (event.getCurrentItem() != null) {
                Player player = (Player) event.getWhoClicked();
                String warpName = event.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                Location warpLocation = this.warpManager.getWarp(warpName);

                if (warpLocation != null) {
                    player.teleport(warpLocation);
                    player.setFallDistance(0);
                    StringUtils.sendConfigMessage(player, "messages.warp.successful");
                    player.closeInventory();
                }
            }
        }
    }
}
