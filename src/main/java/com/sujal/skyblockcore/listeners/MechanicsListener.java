package com.sujal.skyblockcore.listeners;

import com.sujal.skyblockcore.SkyblockCore;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class MechanicsListener implements Listener {
    private final SkyblockCore plugin;

    public MechanicsListener(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.getEntity().setFoodLevel(20); // Lock to full
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        // Disable vanilla natural regen, handled by our ScoreboardManager runnable
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || 
            event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("skyblock.admin") || event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("skyblock.admin") || event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.setCancelled(true); // Disable vanilla crafting
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        // Prevent accidental dropping of valuable items without confirmation (simplified to cancel for now)
        if (!event.getPlayer().hasPermission("skyblock.admin")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cItem dropping is temporarily disabled to prevent abuse.");
        }
    }
}
