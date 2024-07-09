package com.chenyue.mistplugin.commands.hat;

import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Hat implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.vip") | sender.isOp()) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getType() == Material.AIR) {
                StringUtils.sendConfigMessage(player, "messages.hat.airHand");
                return true;
            }

            String itemName;
            ItemMeta meta = item.getItemMeta();

            if (meta != null && meta.hasDisplayName()) itemName = meta.getDisplayName();
            else itemName = item.getType().name().toLowerCase().replace('_', ' ');

            ItemStack helmet = player.getInventory().getHelmet();
            player.getInventory().setHelmet(item.clone());

            if (helmet != null && helmet.getType() != Material.AIR) player.getInventory().setItemInMainHand(helmet);
            else player.getInventory().setItemInMainHand(null);
            StringUtils.sendConfigMessage(player, "messages.hat.success", ImmutableMap.of(
                    "%material%", itemName
            ));
            return true;
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
