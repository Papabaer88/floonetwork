package de.papabaer.floonetwork;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FlooManager {
    private final FlooNetworkPlugin plugin;
    private final Map<String, Fireplace> fireplaces = new HashMap<>();

    public FlooManager(FlooNetworkPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        fireplaces.clear();
        ConfigurationSection section = config.getConfigurationSection("fireplaces");
        if (section == null) {
            return;
        }
        for (String name : section.getKeys(false)) {
            String worldName = section.getString(name + ".world");
            double x = section.getDouble(name + ".x");
            double y = section.getDouble(name + ".y");
            double z = section.getDouble(name + ".z");
            if (worldName == null) {
                continue;
            }
            fireplaces.put(name, new Fireplace(name, worldName, x, y, z));
        }
    }

    public void save() {
        FileConfiguration config = plugin.getConfig();
        config.set("fireplaces", null);
        for (Map.Entry<String, Fireplace> entry : fireplaces.entrySet()) {
            Fireplace fireplace = entry.getValue();
            config.set("fireplaces." + entry.getKey() + ".world", fireplace.worldName());
            config.set("fireplaces." + entry.getKey() + ".x", fireplace.x());
            config.set("fireplaces." + entry.getKey() + ".y", fireplace.y());
            config.set("fireplaces." + entry.getKey() + ".z", fireplace.z());
        }
        plugin.saveConfig();
    }

    public boolean addFireplace(String name, Location location) {
        if (name == null || name.isBlank()) {
            return false;
        }
        String cleaned = name.trim();
        String worldName = location.getWorld() == null ? null : location.getWorld().getName();
        if (worldName == null) {
            return false;
        }
        fireplaces.put(cleaned, new Fireplace(cleaned, worldName, location.getX(), location.getY(), location.getZ()));
        save();
        return true;
    }

    public boolean removeFireplace(String name) {
        Fireplace removed = fireplaces.remove(name);
        if (removed != null) {
            save();
            return true;
        }
        return false;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>(fireplaces.keySet());
        Collections.sort(names);
        return names;
    }

    public boolean exists(String name) {
        return fireplaces.containsKey(name);
    }

    public Fireplace get(String name) {
        return fireplaces.get(name);
    }

    public int size() {
        return fireplaces.size();
    }

    public Fireplace getRandom() {
        if (fireplaces.isEmpty()) {
            return null;
        }
        List<Fireplace> values = new ArrayList<>(fireplaces.values());
        return values.get((int) (Math.random() * values.size()));
    }

    public void teleportTo(org.bukkit.entity.Player player, String name) {
        Fireplace fireplace = fireplaces.get(name);
        if (fireplace == null) {
            player.sendMessage("§cUnknown fireplace: " + name);
            return;
        }
        World world = Bukkit.getWorld(fireplace.worldName());
        if (world == null) {
            player.sendMessage("§cThat fireplace is in an unloaded world.");
            return;
        }
        Location destination = fireplace.toLocation();
        destination.setYaw(player.getLocation().getYaw());
        destination.setPitch(player.getLocation().getPitch());
        player.teleport(destination);
        player.sendMessage("§aWhoosh! You travelled through the Floo Network to §f" + name + "§a.");
    }
}
