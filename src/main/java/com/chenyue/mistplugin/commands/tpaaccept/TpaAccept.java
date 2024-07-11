package com.chenyue.mistplugin.commands.tpaaccept;

import com.chenyue.mistplugin.managers.TpManager;
import com.chenyue.mistplugin.utils.AbstractCommand;
import com.chenyue.mistplugin.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TpaAccept extends AbstractCommand {
    public static final String NAME = "tpaaccept";
    public static final String DESCRIPTION = "Tpa Accept";
    public static final String PERMISSION = "mist.command.tpaaccept";
    public static final String USAGE = "/tpaaccept";
    private final TpManager tpManager;

    public TpaAccept(TpManager tpManager) {
        super(NAME, DESCRIPTION, PERMISSION, USAGE);
        this.tpManager = tpManager;
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
        if (args.length != 0) {
            StringUtils.sendConfigMessage(player, "messages.tp.tpaAcceptUsage");
            return true;
        }
        this.tpManager.acceptTpaRequest(player);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
