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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /gps <x> <z>");
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

        int compassCount = countCompasses(player);
        if (compassCount < 10) {
            player.sendMessage(ChatColor.RED + "You need at least 10 compasses to use this command.");
            return true;
        }

        removeCompasses(player, 10);
        giveGPSCompass(player, x, z);
        player.sendMessage(ChatColor.GREEN + "Your GPS compass is now pointing to (" + x + ", " + z + ")!");

        return true;
    }

    private int countCompasses(Player player) {
        return (int) player.getInventory().all(Material.COMPASS).values().stream()
                .mapToInt(ItemStack::getAmount).sum();
    }

    private void removeCompasses(Player player, int amount) {
        List<ItemStack> stacks = player.getInventory().all(Material.COMPASS).values().stream()
                .flatMap(stack -> IntStream.range(0, stack.getAmount()).mapToObj(i -> stack))
                .collect(Collectors.toList());

        for (ItemStack stack : stacks) {
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

    private void giveGPSCompass(Player player, int x, int z) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();

        if (meta != null) {
            Location targetLocation = new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z), z);
            meta.setLodestone(targetLocation);
            meta.setLodestoneTracked(false);
            meta.setDisplayName(ChatColor.GOLD + "GPS");
            meta.setLore(List.of(ChatColor.YELLOW + "Heading to " + x + ", " + z));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "gps-compass");
            data.set(key, PersistentDataType.BYTE, (byte) 1);

            compass.setItemMeta(meta);
        }

        player.getInventory().addItem(compass);
    }
}
