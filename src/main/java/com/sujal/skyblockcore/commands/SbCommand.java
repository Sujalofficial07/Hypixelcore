package com.sujal.skyblockcore.commands;

import com.yournetwork.hypixelconnect.api.HypixelConnectAPI;
import com.yournetwork.hypixelconnect.models.StatType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SbCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§e/sb help - View commands");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                sender.sendMessage("§aUser: /sb menu, /sb stats, /sb profile");
                if (sender.hasPermission("skyblock.admin")) {
                    sender.sendMessage("§cAdmin: /sb setstat, /sb addstat, /sb wipeplayer, /sb reload");
                }
                break;
            case "setstat":
                if (!sender.hasPermission("skyblock.admin")) return true;
                if (args.length < 4) return true;
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    HypixelConnectAPI.getInstance().setStat(target.getUniqueId(), StatType.valueOf(args[2].toUpperCase()), Double.parseDouble(args[3]));
                    sender.sendMessage("§aStat updated.");
                }
                break;
            // Add other subcommands here similarly...
        }
        return true;
    }
}
