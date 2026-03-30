package com.sujal.skyblockcore.commands;

import com.sujal.skyblockcore.SkyblockCore;
import com.sujal.skyblockcore.models.Zone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCommand implements CommandExecutor {
    private final SkyblockCore plugin;

    public WarpCommand(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            player.sendMessage("§cUsage: /warp <name> or /warp set <name>");
            return true;
        }

        if (args[0].equalsIgnoreCase("set") && player.hasPermission("skyblock.admin")) {
            if (args.length < 2) return true;
            plugin.getZoneManager().createZone(args[1], player.getLocation());
            player.sendMessage("§aWarp/Zone " + args[1] + " created!");
            return true;
        }

        Zone zone = plugin.getZoneManager().getZone(args[0]);
        if (zone != null) {
            player.teleportAsync(zone.getLocation());
            player.sendMessage("§aWarped to " + zone.getName());
        } else {
            player.sendMessage("§cWarp not found.");
        }
        return true;
    }
}
