package com.sujal.skyblockcore.commands;

import com.sujal.skyblockcore.SkyblockCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HubCommand implements CommandExecutor {
    private final SkyblockCore plugin;

    public HubCommand(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length > 0 && args[0].equalsIgnoreCase("setspawn") && player.hasPermission("skyblock.admin")) {
            plugin.getZoneManager().setSpawn(player.getLocation());
            player.sendMessage("§aHub spawn set!");
            return true;
        }

        if (plugin.getZoneManager().getSpawnLocation() != null) {
            player.teleportAsync(plugin.getZoneManager().getSpawnLocation());
            player.sendMessage("§aTeleported to Hub!");
        } else {
            player.sendMessage("§cHub spawn is not set.");
        }
        return true;
    }
}
