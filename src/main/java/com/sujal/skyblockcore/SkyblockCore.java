package com.sujal.skyblockcore;

import com.sujal.skyblockcore.commands.AdminCoreCommand;
import com.sujal.skyblockcore.commands.StatsCommand;
import com.sujal.skyblockcore.listeners.CustomDamageListener;
import com.sujal.skyblockcore.listeners.VanillaOverrideListener;
import com.sujal.skyblockcore.managers.IndicatorManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyblockCore extends JavaPlugin {

    private IndicatorManager indicatorManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Check for HypixelConnect dependency
        if (getServer().getPluginManager().getPlugin("HypixelConnect") == null) {
            getLogger().severe("HypixelConnect not found! SkyblockCore will disable.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize Managers
        this.indicatorManager = new IndicatorManager(this);

        // Register Listeners
        getServer().getPluginManager().registerEvents(new VanillaOverrideListener(), this);
        getServer().getPluginManager().registerEvents(new CustomDamageListener(this), this);

        // Register Commands
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("sbadmin").setExecutor(new AdminCoreCommand());

        getLogger().info("SkyblockCore has been Enabled! Custom mechanics active.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyblockCore Disabled.");
    }

    public IndicatorManager getIndicatorManager() {
        return indicatorManager;
    }
}
