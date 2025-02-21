package org.tnjs.GPSPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class GPSPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("gps").setExecutor(new GPSCommand(this));
        this.getCommand("reroute").setExecutor(new RerouteCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("GPS Plugin disabled!");
    }
}
