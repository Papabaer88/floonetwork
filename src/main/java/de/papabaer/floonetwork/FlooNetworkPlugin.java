package de.papabaer.floonetwork;

import org.bukkit.plugin.java.JavaPlugin;

public final class FlooNetworkPlugin extends JavaPlugin {
    private FlooManager manager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.manager = new FlooManager(this);

        var flooCommand = getCommand("floo");
        if (flooCommand != null) {
            flooCommand.setExecutor(new FlooCommand(this));
            flooCommand.setTabCompleter(new FlooCommand(this));
        }

        getServer().getPluginManager().registerEvents(new FlooListener(this), this);
        getLogger().info("FlooNetwork enabled on Paper 1.21.3");
    }

    public FlooManager getManager() {
        return manager;
    }
}
