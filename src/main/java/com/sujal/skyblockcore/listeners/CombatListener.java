package com.sujal.skyblockcore.listeners;

import com.sujal.skyblockcore.SkyblockCore;
import com.sujal.skyblockcore.managers.StatManager;
import com.yournetwork.hypixelconnect.api.HypixelConnectAPI;
import com.yournetwork.hypixelconnect.models.StatType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class CombatListener implements Listener {
    private final SkyblockCore plugin;
    private final Random random = new Random();

    public CombatListener(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                // Controlled fall damage logic
                double maxHp = plugin.getStatManager().getFinalStat(player, StatType.HEALTH);
                double damage = maxHp * 0.2; // 20% of max HP on fall
                applyCustomDamage(player, damage, false);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        event.setCancelled(true); // Cancel vanilla damage

        if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player) {
            // PVP Check
            String zoneName = plugin.getZoneManager().getPlayerZoneName(victim.getLocation());
            if (!plugin.getZoneManager().getZone(zoneName).isPvpEnabled()) {
                return;
            }
        }

        if (event.getDamager() instanceof Player player && event.getEntity() instanceof LivingEntity target) {
            StatManager stats = plugin.getStatManager();
            
            double strength = stats.getFinalStat(player, StatType.STRENGTH);
            double critChance = stats.getFinalStat(player, StatType.CRIT_CHANCE);
            double critDamageStat = stats.getFinalStat(player, StatType.CRIT_DAMAGE);
            
            double weaponDamage = 0; // Fetch from custom NBT in future
            
            // Formula Engine
            double baseDamage = (5 + weaponDamage) * (1 + (strength / 100));
            boolean isCrit = random.nextDouble() * 100 <= critChance;
            double finalDamage = isCrit ? baseDamage * (1 + (critDamageStat / 100)) : baseDamage;

            // Defense Mitigation
            double defense = 0; 
            if (target instanceof Player tPlayer) {
                defense = stats.getFinalStat(tPlayer, StatType.DEFENSE);
            }
            double damageMitigation = 1 - (defense / (defense + 100));
            finalDamage = finalDamage * damageMitigation;

            applyCustomDamage(target, finalDamage, isCrit);
        }
    }

    private void applyCustomDamage(LivingEntity entity, double damage, boolean isCrit) {
        if (entity instanceof Player player) {
            StatManager stats = plugin.getStatManager();
            double ch = stats.getCurrentHealth(player);
            if (ch - damage <= 0) {
                handleCustomDeath(player);
            } else {
                stats.setCurrentHealth(player, ch - damage);
            }
        } else {
            double vanillaHp = entity.getHealth();
            if (vanillaHp - damage <= 0) {
                entity.setHealth(0); // Let vanilla handle mob death animation/drops
                // Custom drops logic will trigger here in future
            } else {
                entity.setHealth(vanillaHp - damage);
            }
        }
        plugin.getIndicatorManager().spawnDamageIndicator(entity.getLocation().add(0, 1, 0), damage, isCrit);
    }

    private void handleCustomDeath(Player player) {
        player.sendMessage("§cYou died!");
        HypixelConnectAPI.getInstance().addCoins(player.getUniqueId(), - (HypixelConnectAPI.getInstance().getActiveProfile(player.getUniqueId()).getCoins() * 0.5));
        plugin.getStatManager().setCurrentHealth(player, plugin.getStatManager().getFinalStat(player, StatType.HEALTH));
        
        if (plugin.getZoneManager().getSpawnLocation() != null) {
            player.teleportAsync(plugin.getZoneManager().getSpawnLocation());
        }
    }
}
