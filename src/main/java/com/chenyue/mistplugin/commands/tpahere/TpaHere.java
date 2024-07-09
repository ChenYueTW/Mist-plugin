package com.chenyue.mistplugin.commands.tpahere;

import com.chenyue.mistplugin.data.TpManager;
import com.chenyue.mistplugin.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TpaHere implements TabExecutor {
    private final TpManager tpManager;

    public TpaHere(TpManager tpManager) {
        this.tpManager = tpManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("mist.command.tpahere")) {
            if (!(sender instanceof Player)) {
                StringUtils.sendConfigMessage(sender, "messages.playerOnly");
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 1) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);

                try {
                    this.tpManager.addTpaHereRequest(targetPlayer.getUniqueId(), player.getUniqueId());
                } catch (Exception e) {
                    StringUtils.sendConfigMessage(sender, "messages.tp.playerNotOnline");
                    return true;
                }

                TextComponent accept = new TextComponent("【同意】");
                TextComponent deny = new TextComponent("【拒絕】");

                // "已發送請求"
                StringUtils.sendConfigMessage(player, "messages.tp.tpaHereSendRequest", ImmutableMap.of(
                        "%player%", targetPlayer.getName()
                ));
                // "%player% 傳送了請求 是否同意"
                StringUtils.sendConfigMessage(targetPlayer, "messages.tp.tpaHereReceiveRequest", ImmutableMap.of(
                        "%player%", player.getName()
                ), accept, "/tpahereaccept", deny, "/tpaherecancel");
                return true;
            } else {
                StringUtils.sendConfigMessage(player, "messages.tp.tpaHereUsage");
                return true;
            }
        } else {
            StringUtils.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
