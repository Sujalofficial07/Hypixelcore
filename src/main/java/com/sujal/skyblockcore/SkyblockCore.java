package com.sujal.skyblockcore;

import com.sujal.skyblockcore.commands.*;
import com.sujal.skyblockcore.listeners.*;
import com.sujal.skyblockcore.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyblockCore extends JavaPlugin {

    private StatManager statManager;
    private ZoneManager zoneManager;
    private IndicatorManager indicatorManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getServer().getPluginManager().getPlugin("HypixelConnect") == null) {
            getLogger().severe("HypixelConnect not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize Managers
        this.statManager = new StatManager(this);
        this.zoneManager = new ZoneManager(this);
        this.indicatorManager = new IndicatorManager(this); // Assuming this is retained from previous iteration

        // Register Events
        getServer().getPluginManager().registerEvents(new MechanicsListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);

        // Register Commands
        getCommand("sb").setExecutor(new SbCommand());
        getCommand("hub").setExecutor(new HubCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));

        // Start Scoreboard/Actionbar Task (runs every 20 ticks / 1 sec)
        new ScoreboardManager(this).runTaskTimer(this, 20L, 20L);

        getLogger().info("SkyblockCore Enhanced Enabled!");
    }

    public StatManager getStatManager() { return statManager; }
    public ZoneManager getZoneManager() { return zoneManager; }
    public IndicatorManager getIndicatorManager() { return indicatorManager; }
}
