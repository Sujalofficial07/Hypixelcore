package com.sujal.skyblockcore.managers;

import com.sujal.skyblockcore.SkyblockCore;
import com.sujal.skyblockcore.models.StatModifier;
import com.yournetwork.hypixelconnect.api.HypixelConnectAPI;
import com.yournetwork.hypixelconnect.models.StatType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StatManager {
    private final SkyblockCore plugin;
    // UUID -> StatType -> List of Modifiers
    private final Map<UUID, Map<StatType, List<StatModifier>>> activeModifiers;
    private final Map<UUID, Double> currentHealth;
    private final Map<UUID, Double> currentMana;

    public StatManager(SkyblockCore plugin) {
        this.plugin = plugin;
        this.activeModifiers = new ConcurrentHashMap<>();
        this.currentHealth = new ConcurrentHashMap<>();
        this.currentMana = new ConcurrentHashMap<>();
    }

    public double getFinalStat(Player player, StatType type) {
        HypixelConnectAPI api = HypixelConnectAPI.getInstance();
        double base = api.getStat(player.getUniqueId(), type);
        
        double additive = 0;
        double multiplicative = 1.0;

        Map<StatType, List<StatModifier>> playerMods = activeModifiers.getOrDefault(player.getUniqueId(), new HashMap<>());
        List<StatModifier> mods = playerMods.getOrDefault(type, new ArrayList<>());

        for (StatModifier mod : mods) {
            if (mod.isMultiplicative()) {
                multiplicative += mod.getAmount(); // e.g. 0.1 for 10% boost
            } else {
                additive += mod.getAmount();
            }
        }
        return (base + additive) * multiplicative;
    }

    public void addModifier(Player player, StatModifier modifier) {
        activeModifiers.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                       .computeIfAbsent(modifier.getStat(), k -> new ArrayList<>())
                       .add(modifier);
        updatePlayerMaxHealth(player);
    }

    public void clearModifiers(Player player, String source) {
        if (!activeModifiers.containsKey(player.getUniqueId())) return;
        activeModifiers.get(player.getUniqueId()).values().forEach(list -> list.removeIf(mod -> mod.getSource().equals(source)));
        updatePlayerMaxHealth(player);
    }

    public void updatePlayerMaxHealth(Player player) {
        double maxHp = getFinalStat(player, StatType.HEALTH);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0); // Vanilla UI limit
        
        // Ensure current health doesn't exceed new max
        double ch = getCurrentHealth(player);
        if (ch > maxHp) setCurrentHealth(player, maxHp);
    }

    public double getCurrentHealth(Player player) {
        return currentHealth.getOrDefault(player.getUniqueId(), getFinalStat(player, StatType.HEALTH));
    }

    public void setCurrentHealth(Player player, double hp) {
        double max = getFinalStat(player, StatType.HEALTH);
        double finalHp = Math.max(0, Math.min(hp, max));
        currentHealth.put(player.getUniqueId(), finalHp);
        
        // Update vanilla health bar proportionally
        double vanillaHealth = (finalHp / max) * 40.0;
        player.setHealth(Math.max(1.0, vanillaHealth)); // Avoid vanilla death unless our system kills
    }

    public double getCurrentMana(Player player) {
        return currentMana.getOrDefault(player.getUniqueId(), getFinalStat(player, StatType.INTELLIGENCE));
    }

    public void setCurrentMana(Player player, double mana) {
        double max = getFinalStat(player, StatType.INTELLIGENCE);
        currentMana.put(player.getUniqueId(), Math.max(0, Math.min(mana, max)));
    }
}
