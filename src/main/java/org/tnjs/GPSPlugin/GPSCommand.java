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

public class GPSCommand implements CommandExecutor {
    private final GPSPlugin plugin;

    public GPSCommand(GPSPlugin plugin) {
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

        if (args.length < 2 || args.length > 3) {
            player.sendMessage(ChatColor.RED + "Usage: /gps <x> <z> [true/false]");
            return true;
        }


        int x, z;
        boolean hidden = false;

        try {
            x = Integer.parseInt(args[0]);
            z = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid coordinates. Please enter valid numbers.");
            return true;
        }

        if (args.length == 3) {
            hidden = Boolean.parseBoolean(args[2]);
        }


        int compassCount = countCompasses(player);
        if(((Player) sender).getGameMode() == GameMode.CREATIVE){
            giveGPSCompass(player, x, z, hidden);
            player.sendMessage(ChatColor.GREEN + "Your GPS compass is now pointing to (" + x + ", " + z + ")!");

            return true;
        }else{

        if (compassCount < 10) {
            player.sendMessage(ChatColor.RED + "You need at least 10 compasses to use this command.");
            return true;
        }

        removeCompasses(player, 10);
        giveGPSCompass(player, x, z, hidden);
        player.sendMessage(ChatColor.GREEN + "Your GPS compass is now pointing to (" + x + ", " + z + ")!");

        return true;
    }
    }

    private int countCompasses(Player player) {
        return (int) player.getInventory().all(Material.COMPASS).values().stream()
                .mapToInt(ItemStack::getAmount).sum();
    }

    private void removeCompasses(Player player, int amount) {
        for (ItemStack stack : player.getInventory().all(Material.COMPASS).values()) {
            int stackSize = stack.getAmount();
            if (amount <= 0) break;

            if (stackSize > amount) {
                stack.setAmount(stackSize - amount);
                amount = 0;
            } else {
                amount -= stackSize;
                player.getInventory().removeItem(stack);
            }
        }
    }

    private void giveGPSCompass(Player player, int x, int z, boolean hidden) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();

        if (meta != null) {
            Location targetLocation = new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z), z);
            meta.setLodestone(targetLocation);
            meta.setLodestoneTracked(false);
            meta.setDisplayName(ChatColor.GOLD + "GPS");

            int maxUses;
            if (hidden) {
                 maxUses = 0;
            }else{
             maxUses = plugin.getConfig().getInt("max-reroutes", 3);
            }

            if(!hidden) {
                meta.setLore(List.of(
                        ChatColor.GREEN + "Heading to " + x + ", " + z,
                        ChatColor.AQUA + "Reroutes left: " + maxUses
                ));
            }else{
                meta.setLore(List.of(
                        ChatColor.GREEN + "Heading to "+ ChatColor.MAGIC + x + ChatColor.RESET + ChatColor.GREEN + " , " + ChatColor.MAGIC + z + ChatColor.RESET
                ));
            }
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "gps-compass");
            NamespacedKey usesKey = new NamespacedKey(plugin, "gps-uses");

            data.set(key, PersistentDataType.BYTE, (byte) 1);
            data.set(usesKey, PersistentDataType.INTEGER, maxUses);

            compass.setItemMeta(meta);
        }

        player.getInventory().addItem(compass);
    }
}
