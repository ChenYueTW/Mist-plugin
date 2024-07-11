package com.chenyue.mistplugin.commands.spawn;

import com.chenyue.mistplugin.MistPlugin;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class Spawn extends AbstractCommand {
    public static final String NAME = "spawn";
    public static final String DESCRIPTION = "Teleport to spawn";
    public static final String PERMISSION = "mist.command.spawn";
    public static final String USAGE = "/spawn";

    public Spawn() {
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
        if (this.getLocationFromConfig() == null) {
            StringUtils.sendConfigMessage(sender, "messages.spawn.noLocation");
            return false;
        }
        Location spawnLocation = this.getLocationFromConfig();
        // 傳送全部or特定玩家到Spawn
        if (args.length > 0 && player.isOp()) {
            if (args[0].equalsIgnoreCase("*")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!(onlinePlayer.equals(player))) {
                        // 傳送玩家
                        onlinePlayer.teleport(spawnLocation);
                        // 防摔落 設置摔落零位面
                        onlinePlayer.setFallDistance(0);
                        StringUtils.sendConfigMessage(onlinePlayer, "messages.spawn.teleport");
                    }
                }
                StringUtils.sendConfigMessage(player, "messages.spawn.successful");
            } else {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                // 傳送特定玩家到Spawn
                if (targetPlayer != null) {
                    targetPlayer.teleport(spawnLocation);
                    targetPlayer.setFallDistance(0);
                    StringUtils.sendConfigMessage(targetPlayer, "messages.spawn.teleport");
                    StringUtils.sendConfigMessage(player, "messages.spawn.successful");
                }
            }
            return true;
        } else if (args.length == 0) {
            player.teleport(spawnLocation);
            player.setFallDistance(0);
            StringUtils.sendConfigMessage(player, "messages.spawn.successful");
            return true;
        } else {
            StringUtils.sendConfigMessage(player, "messages.spawn.successful");
            return true;
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

    private Location getLocationFromConfig() {
        try {
            double x = MistPlugin.getInstance().getSpawnConfig().getDouble("main.x");
            double y = MistPlugin.getInstance().getSpawnConfig().getDouble("main.y");
            double z = MistPlugin.getInstance().getSpawnConfig().getDouble("main.z");
            float yaw = (float) MistPlugin.getInstance().getSpawnConfig().getDouble("main.yaw");
            float pitch = (float) MistPlugin.getInstance().getSpawnConfig().getDouble("main.pitch");
            World world = MistPlugin.getInstance().getServer().getWorld(Objects.requireNonNull(MistPlugin.getInstance().getSpawnConfig().getString("main.world")));
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            return null;
        }
    }
}
