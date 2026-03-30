package com.sujal.skyblockcore.managers;

import com.sujal.skyblockcore.SkyblockCore;
import com.sujal.skyblockcore.models.Zone;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ZoneManager {
    private final SkyblockCore plugin;
    private final Map<String, Zone> zones = new HashMap<>();
    private Location spawnLocation;
    private File zoneFile;
    private FileConfiguration zoneConfig;

    public ZoneManager(SkyblockCore plugin) {
        this.plugin = plugin;
        loadZones();
    }

    public void loadZones() {
        zoneFile = new File(plugin.getDataFolder(), "zones.yml");
        if (!zoneFile.exists()) {
            plugin.saveResource("zones.yml", false);
        }
        zoneConfig = YamlConfiguration.loadConfiguration(zoneFile);
        
        if (zoneConfig.contains("spawn")) {
            spawnLocation = zoneConfig.getLocation("spawn");
        }

        if (zoneConfig.contains("zones")) {
            for (String key : zoneConfig.getConfigurationSection("zones").getKeys(false)) {
                String path = "zones." + key;
                zones.put(key.toLowerCase(), new Zone(
                    key,
                    zoneConfig.getLocation(path + ".location"),
                    zoneConfig.getInt(path + ".level", 0),
                    zoneConfig.getBoolean(path + ".pvp", false)
                ));
            }
        }
    }

    public void setSpawn(Location loc) {
        this.spawnLocation = loc;
        zoneConfig.set("spawn", loc);
        saveFile();
    }

    public void createZone(String name, Location loc) {
        zones.put(name.toLowerCase(), new Zone(name, loc, 0, false));
        zoneConfig.set("zones." + name + ".location", loc);
        zoneConfig.set("zones." + name + ".level", 0);
        zoneConfig.set("zones." + name + ".pvp", false);
        saveFile();
    }

    public Zone getZone(String name) {
        return zones.get(name.toLowerCase());
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getPlayerZoneName(Location loc) {
        // Basic distance check logic (can be upgraded to WorldGuard regions later)
        for (Zone zone : zones.values()) {
            if (zone.getLocation().getWorld().equals(loc.getWorld()) && zone.getLocation().distance(loc) < 50) {
                return zone.getName();
            }
        }
        return "Unknown";
    }

    private void saveFile() {
        try {
            zoneConfig.save(zoneFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save zones.yml");
        }
    }
}
