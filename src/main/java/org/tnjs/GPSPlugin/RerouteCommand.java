package org.tnjs.GPSPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /reroute <x> <z>");
            return true;
        }

        Player player = (Player) sender;
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

        executeRerouteCommand(player);
        updateGPSCompass(gpsCompass, player, x, z);
        player.sendMessage(ChatColor.GREEN + "Your GPS compass has been updated to (" + x + ", " + z + ")!");

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

    private void executeRerouteCommand(Player player) {
        String command = plugin.getConfig().getString("reroute-command").replace("{player}", player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    private void updateGPSCompass(ItemStack compass, Player player, int x, int z) {
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        if (meta != null) {
            Location targetLocation = new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z), z);
            meta.setLodestone(targetLocation);
            meta.setLodestoneTracked(false);
            meta.setLore(List.of(ChatColor.YELLOW + "Heading to " + x + ", " + z));
            compass.setItemMeta(meta);
        }
    }
}
