package com.sujal.skyblockcore.managers;

import com.sujal.skyblockcore.SkyblockCore;
import com.yournetwork.hypixelconnect.api.HypixelConnectAPI;
import com.yournetwork.hypixelconnect.models.PlayerProfile;
import com.yournetwork.hypixelconnect.models.StatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreboardManager extends BukkitRunnable {
    private final SkyblockCore plugin;

    public ScoreboardManager(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
            updateActionBar(player);
            regenStats(player);
        }
    }

    private void updateScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = player.getScoreboard();
        
        if (board.equals(manager.getMainScoreboard())) {
            board = manager.getNewScoreboard();
            player.setScoreboard(board);
        }

        Objective obj = board.getObjective("skyblock");
        if (obj == null) {
            obj = board.registerNewObjective("skyblock", Criteria.DUMMY, Component.text("SKYBLOCK").color(NamedTextColor.YELLOW));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        HypixelConnectAPI api = HypixelConnectAPI.getInstance();
        PlayerProfile profile = api.getActiveProfile(player.getUniqueId());
        if (profile == null) return;

        String dateStr = new SimpleDateFormat("MM/dd/yy").format(new Date());
        String zone = plugin.getZoneManager().getPlayerZoneName(player.getLocation());

        // Anti-flicker: Reset scores and set them (Can be optimized with FastBoard in future)
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        obj.getScore("§7" + dateStr).setScore(5);
        obj.getScore(" ").setScore(4);
        obj.getScore("§f⏣ §a" + zone).setScore(3);
        obj.getScore("§fCoins: §6" + String.format("%,.1f", profile.getCoins())).setScore(2);
        obj.getScore("  ").setScore(1);
        obj.getScore("§eYourNetwork").setScore(0);
    }

    private void updateActionBar(Player player) {
        StatManager stats = plugin.getStatManager();
        int curHp = (int) stats.getCurrentHealth(player);
        int maxHp = (int) stats.getFinalStat(player, StatType.HEALTH);
        int curMana = (int) stats.getCurrentMana(player);
        int maxMana = (int) stats.getFinalStat(player, StatType.INTELLIGENCE);
        int defense = (int) stats.getFinalStat(player, StatType.DEFENSE);

        Component action = Component.text(curHp + "/" + maxHp + "❤   ")
                .color(NamedTextColor.RED)
                .append(Component.text(defense + "❈ Defense   ").color(NamedTextColor.GREEN))
                .append(Component.text(curMana + "/" + maxMana + "✎ Mana").color(NamedTextColor.AQUA));
        
        player.sendActionBar(action);
    }

    private void regenStats(Player player) {
        // Basic regen per second logic
        StatManager stats = plugin.getStatManager();
        double maxHp = stats.getFinalStat(player, StatType.HEALTH);
        double maxMana = stats.getFinalStat(player, StatType.INTELLIGENCE);
        
        if (stats.getCurrentHealth(player) < maxHp) {
            stats.setCurrentHealth(player, stats.getCurrentHealth(player) + (maxHp * 0.01)); // 1% regen
        }
        if (stats.getCurrentMana(player) < maxMana) {
            stats.setCurrentMana(player, stats.getCurrentMana(player) + (maxMana * 0.02)); // 2% regen
        }
    }
}
