package com.sujal.skyblockcore.commands;

import com.yournetwork.hypixelconnect.api.HypixelConnectAPI;
import com.yournetwork.hypixelconnect.models.StatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can view their stats.");
            return true;
        }

        HypixelConnectAPI api = HypixelConnectAPI.getInstance();
        
        player.sendMessage(Component.text("--- Your Skyblock Stats ---").color(NamedTextColor.GOLD));
        player.sendMessage(Component.text("❤ Health: " + api.getStat(player.getUniqueId(), StatType.HEALTH)).color(NamedTextColor.RED));
        player.sendMessage(Component.text("❈ Defense: " + api.getStat(player.getUniqueId(), StatType.DEFENSE)).color(NamedTextColor.GREEN));
        player.sendMessage(Component.text("❁ Strength: " + api.getStat(player.getUniqueId(), StatType.STRENGTH)).color(NamedTextColor.DARK_RED));
        player.sendMessage(Component.text("☣ Crit Chance: " + api.getStat(player.getUniqueId(), StatType.CRIT_CHANCE) + "%").color(NamedTextColor.BLUE));
        player.sendMessage(Component.text("☠ Crit Damage: " + api.getStat(player.getUniqueId(), StatType.CRIT_DAMAGE) + "%").color(NamedTextColor.DARK_BLUE));
        player.sendMessage(Component.text("✎ Intelligence: " + api.getStat(player.getUniqueId(), StatType.INTELLIGENCE)).color(NamedTextColor.AQUA));
        player.sendMessage(Component.text("✦ Speed: " + api.getStat(player.getUniqueId(), StatType.SPEED)).color(NamedTextColor.WHITE));
        
        return true;
    }
}
