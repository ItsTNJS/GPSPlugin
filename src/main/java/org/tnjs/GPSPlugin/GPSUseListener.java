package org.tnjs.GPSPlugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.Particle;

public class GPSUseListener implements Listener {
    private final GPSPlugin plugin;

    public GPSUseListener(GPSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGPSRightClick(PlayerInteractEvent event) {
        if (!event.getAction().toString().contains("RIGHT_CLICK")) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.COMPASS || !item.hasItemMeta()) return;

        CompassMeta meta = (CompassMeta) item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "gps-compass");

        if (!data.has(key, PersistentDataType.BYTE)) return;

        if (meta.getLodestone() == null) {
            player.sendMessage(ChatColor.RED + "GPS has no destination.");
            return;
        }

        Location target = meta.getLodestone();
        if (target == null) return;

        spawnParticleTrail(player.getLocation(), target, player);
    }

    private void spawnParticleTrail(Location start, Location target, Player player) {
        Vector direction = target.toVector().subtract(start.toVector()).normalize().multiply(0.75);

        Location particleLocation = start.clone().add(0, 1, 0); // Start above the player

        for (int i = 0; i < 10; i++) {
            particleLocation.add(direction);
            player.spawnParticle(
                    Particle.FLAME,
                    particleLocation,
                    5,
                    0, 0, 0, 0
            );
        }
    }
}
