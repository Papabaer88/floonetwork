package de.papabaer.floonetwork;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public record Fireplace(String name, String worldName, double x, double y, double z) {
    public Location toLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException("World not loaded: " + worldName);
        }
        return new Location(world, x + 0.5, y + 0.1, z + 0.5);
    }

    public boolean matchesWorld(World world) {
        return Objects.equals(worldName, world.getName());
    }
}
