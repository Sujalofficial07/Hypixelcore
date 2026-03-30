package com.sujal.skyblockcore.listeners;

import com.sujal.skyblockcore.SkyblockCore;
import com.yournetwork.hypixelconnect.api.HypixelConnectAPI;
import com.yournetwork.hypixelconnect.models.StatType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class CustomDamageListener implements Listener {
    private final SkyblockCore plugin;
    private final Random random;

    public CustomDamageListener(SkyblockCore plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        // Cancel vanilla damage calculations
        event.setDamage(0);

        // Fetch Stats from HypixelConnect API
        HypixelConnectAPI api = HypixelConnectAPI.getInstance();
        double strength = api.getStat(player.getUniqueId(), StatType.STRENGTH);
        double critChance = api.getStat(player.getUniqueId(), StatType.CRIT_CHANCE);
        double critDamageStat = api.getStat(player.getUniqueId(), StatType.CRIT_DAMAGE);
        
        // Custom Weapon Damage (will fetch from NBT later, using 0 for bare hands now)
        double weaponDamage = 0; 
        
        // Base Damage Formula
        double baseDamage = (5 + weaponDamage) * (1 + (strength / 100));
        
        // Crit Calculation
        boolean isCrit = random.nextDouble() * 100 <= critChance;
        double finalDamage = baseDamage;
        if (isCrit) {
            finalDamage = baseDamage * (1 + (critDamageStat / 100));
        }

        // Apply Custom Damage directly to entity health to bypass vanilla armor reductions
        double currentHealth = target.getHealth();
        double newHealth = currentHealth - finalDamage;
        if (newHealth < 0) newHealth = 0;
        target.setHealth(newHealth);

        // Display Hologram
        plugin.getIndicatorManager().spawnDamageIndicator(target.getLocation().add(0, 1, 0), finalDamage, isCrit);

        // If target dies, let vanilla handle the death animation
    }
}
