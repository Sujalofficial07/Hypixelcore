package com.sujal.skyblockcore.commands;

import com.yournetwork.hypixelconnect.api.HypixelConnectAPI;
import com.yournetwork.hypixelconnect.models.StatType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("skyblock.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 4 || !args[0].equalsIgnoreCase("setstat")) {
            sender.sendMessage("§cUsage: /sbadmin setstat <player> <stat> <value>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        StatType type;
        try {
            type = StatType.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid stat type.");
            return true;
        }

        double value;
        try {
            value = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cValue must be a number.");
            return true;
        }

        HypixelConnectAPI.getInstance().setStat(target.getUniqueId(), type, value);
        sender.sendMessage("§aUpdated " + type.name() + " for " + target.getName() + " to " + value);
        return true;
    }
}
