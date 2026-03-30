package com.sujal.skyblockcore.models;

import org.bukkit.Location;

public class Zone {
    private final String name;
    private Location location;
    private final int levelRequirement;
    private final boolean pvpEnabled;

    public Zone(String name, Location location, int levelRequirement, boolean pvpEnabled) {
        this.name = name;
        this.location = location;
        this.levelRequirement = levelRequirement;
        this.pvpEnabled = pvpEnabled;
    }

    public String getName() { return name; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public int getLevelRequirement() { return levelRequirement; }
    public boolean isPvpEnabled() { return pvpEnabled; }
}
