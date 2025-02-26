package org.tnjs.GPSPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class GPSPlugin extends JavaPlugin {
    private static GPSPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Register commands
        getCommand("gps").setExecutor(new GPSCommand(this));
        getCommand("reroute").setExecutor(new RerouteCommand(this));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new GPSUseListener(this), this);
    }

    public static GPSPlugin getInstance() {
        return instance;
    }
}
