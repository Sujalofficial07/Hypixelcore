package com.sujal.skyblockcore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class VanillaOverrideListener implements Listener {

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent event) {
        // Disable vanilla hunger loss
        event.setCancelled(true);
        event.getEntity().setFoodLevel(20);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        // Disable vanilla fall damage (Can be re-enabled later based on custom boots/islands)
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        // Always sunny
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        // Prevent all natural mob spawning unless they have a specific metadata/tag
        // We will add custom spawning logic in the Bestiary/Combat plugin later.
        switch (event.getEntity().getType()) {
            case ZOMBIE:
            case SKELETON:
            case CREEPER:
            case SPIDER:
            case ENDERMAN:
                if (!event.getEntity().hasMetadata("custom_mob")) {
                    event.setCancelled(true);
                }
                break;
            default:
                break;
        }
    }
}
