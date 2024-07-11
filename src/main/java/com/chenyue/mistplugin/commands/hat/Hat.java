package com.chenyue.mistplugin.commands.hat;

import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Hat extends AbstractCommand {
    public static final String NAME = "Hat";
    public static final String DESCRIPTION = "Hat something";
    public static final String PERMISSION = "mist.command.vip";
    public static final String USAGE = "/hat";

    public Hat() {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!this.hasPermission(sender)) {
            this.noPermission(sender);
            return true;
        }
        if (!(sender instanceof Player player)) {
            this.playerOnly(sender);
            return true;
        }
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
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

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
