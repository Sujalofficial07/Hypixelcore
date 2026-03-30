package com.sujal.skyblockcore.managers;

import com.sujal.skyblockcore.SkyblockCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class IndicatorManager {
    private final SkyblockCore plugin;
    private final Random random;

    public IndicatorManager(SkyblockCore plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    public void spawnDamageIndicator(Location loc, double damage, boolean isCrit) {
        // Offset location slightly so they don't stack perfectly
        Location spawnLoc = loc.clone().add(
                (random.nextDouble() - 0.5), 
                random.nextDouble() + 0.5, 
                (random.nextDouble() - 0.5)
        );

        TextDisplay display = (TextDisplay) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.TEXT_DISPLAY);
        
        String formattedDamage = String.format("%,d", Math.round(damage));
        Component text = isCrit 
                ? Component.text("✧" + formattedDamage + "✧").color(NamedTextColor.GOLD)
                : Component.text(formattedDamage).color(NamedTextColor.GRAY);

        display.text(text);
        display.setBillboard(Display.Billboard.CENTER);
        display.setDefaultBackground(false);
        display.setBackgroundColor(org.bukkit.Color.fromARGB(0, 0, 0, 0));
        display.setShadowed(true);

        // Remove indicator after 20 ticks (1 second)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (display.isValid()) {
                    display.remove();
                }
            }
        }.runTaskLater(plugin, 20L);
    }
}
