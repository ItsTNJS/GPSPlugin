package org.tnjs.GPSPlugin;

import org.bukkit.*;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class RerouteCommand implements CommandExecutor {
    private final GPSPlugin plugin;

    public RerouteCommand(GPSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("gps.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /reroute <x> <z>");
            return true;
        }

        int x, z;
        try {
            x = Integer.parseInt(args[0]);
            z = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid coordinates. Please enter valid numbers.");
            return true;
        }

        ItemStack gpsCompass = findGPSCompass(player);
        if (gpsCompass == null) {
            player.sendMessage(ChatColor.RED + "You do not have a valid GPS compass.");
            return true;
        }

        if (!updateGPSCompass(gpsCompass, player, x, z)) {
            player.getInventory().remove(gpsCompass);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            player.sendMessage(ChatColor.RED + "Your GPS compass has broken!");
        } else {
            player.sendMessage(ChatColor.GREEN + "Your GPS compass has been updated to (" + x + ", " + z + ")!");
        }

        return true;
    }

    private ItemStack findGPSCompass(Player player) {
        NamespacedKey key = new NamespacedKey(plugin, "gps-compass");

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.COMPASS && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                if (data.has(key, PersistentDataType.BYTE)) {
                    return item;
                }
            }
        }
        return null;
    }

    private boolean updateGPSCompass(ItemStack compass, Player player, int x, int z) {
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey usesKey = new NamespacedKey(plugin, "gps-uses");

            int usesLeft = data.getOrDefault(usesKey, PersistentDataType.INTEGER, 3);
            usesLeft--;

            if (usesLeft <= 0) {
                return false;  // Compass breaks
            }

            Location targetLocation = new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z), z);
            meta.setLodestone(targetLocation);
            meta.setLodestoneTracked(false);
            meta.setLore(List.of(
                    ChatColor.GREEN + "Heading to " + x + ", " + z,
                    ChatColor.AQUA + "Reroutes left: " + usesLeft
            ));

            data.set(usesKey, PersistentDataType.INTEGER, usesLeft);
            compass.setItemMeta(meta);
        }
        return true;
    }
}
